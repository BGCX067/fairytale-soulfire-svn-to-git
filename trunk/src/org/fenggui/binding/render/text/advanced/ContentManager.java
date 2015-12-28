/*
 * FengGUI - Java GUIs in OpenGL (http://www.fenggui.org)
 * 
 * Copyright (C) 2005, 2006 FengGUI Project
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details:
 * http://www.gnu.org/copyleft/lesser.html#TOC3
 * 
 * Created on 22.11.2007
 * $Id$
 */
package org.fenggui.binding.render.text.advanced;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.fenggui.event.*;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

/**
 * Manages the content of a complex text string.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ContentManager implements IContentSelection
{
	private List<ContentUserLine> content;
	private List<ISizeChangedListener> sizeListeners = new ArrayList<ISizeChangedListener>();
	private List<IContentChangedListener> contentChangedListeners = new ArrayList<IContentChangedListener>();
	private List<IElementListener<AbstractContentPart>> elementListener = new ArrayList<IElementListener<AbstractContentPart>>();

	private String contentCache = "";
	private int requestedWidthCache = 0;
	private long atomCountCache = 0;

	private IContentFactory factory;
	private Dimension size = null;
	private boolean autoWarp = false;
	private boolean multiline = false;
	private boolean hideContent = false;

	private long activeAtom = -1;
	private ContentUserLine activeLine = null;
	private int activeLineY = 0;
	private int activeLineAtoms = 0;

	private int maxLines = -1;

	public ContentManager(IContentFactory factory)
	{
		this.factory = factory;
		this.content = new CopyOnWriteArrayList<ContentUserLine>();
		synchronized (content)
		{
			this.content.add(new ContentUserLine(factory, hideContent));
		}
		this.updateSize();
		this.updateContentCache();
		this.setActiveAtom(0);
	}

	public ContentManager(ContentManager manager)
	{
		this.factory = manager.factory;
		synchronized (manager.content)
		{
			this.content = new ArrayList<ContentUserLine>(manager.content);
		}
		this.size = manager.size;
		this.updateContentCache();
		this.setActiveAtom(0);
	}

	public int getAtomsTillActiveLine()
	{
		if (hasActiveLine())
		{
			return this.activeLineAtoms + this.activeLine.getAtomsTillActiveLine();
		}
		else
		{
			return 0;
		}
	}

	public int getAtomsTillActiveLineEnd()
	{
		if (hasActiveLine())
		{
			return this.activeLineAtoms + this.activeLine.getAtomsTillActiveLineEnd();
		}
		else
		{
			return 0;
		}
	}

	private void updateSize()
	{
		int y = 0;
		//		int x = requestedWidthCache;
		int x = 0;

		synchronized (content)
		{
			for (ContentUserLine userline : content)
			{
				x = Math.max(x, userline.getSize().getWidth());
				y += userline.getSize().getHeight();
			}
		}
		Dimension oldSize = this.size;
		this.size = new Dimension(x, y);

		if (!this.size.equals(oldSize))
			fireSizeChangedEvent(new SizeChangedEvent(null, oldSize, this.size));
	}

	public boolean addSizeChangedListener(ISizeChangedListener listener)
	{
		return sizeListeners.add(listener);
	}

	public boolean removeSizeChangedListener(ISizeChangedListener listener)
	{
		return sizeListeners.remove(listener);
	}

	protected void fireSizeChangedEvent(SizeChangedEvent event)
	{
		for (ISizeChangedListener listener : sizeListeners)
		{
			listener.sizeChanged(event);
		}
	}

	public boolean addContentChangedListener(IContentChangedListener listener)
	{
		return contentChangedListeners.add(listener);
	}

	public boolean removeContentChangedListener(IContentChangedListener listener)
	{
		return contentChangedListeners.remove(listener);
	}

	protected void fireContentChangedEvent(ContentChangedEvent event)
	{
		for (IContentChangedListener listener : contentChangedListeners)
		{
			listener.contentChanged(event);
		}
	}

	private void updateContentCache()
	{
		String result = "";
		String oldContent = contentCache;
		synchronized (content)
		{
			atomCountCache = 0;
			for (ContentUserLine line : content)
			{
				result += line.getContent() + "\n";
				atomCountCache += line.getAtomCount() + 1;
			}

			// remove last newline
			if (result.length() > 0)
			{
				result = result.substring(0, result.length() - 1);
				atomCountCache--;
			}
			contentCache = result;
		}

		fireContentChangedEvent(new ContentChangedEvent(null, oldContent, contentCache));
	}

	public String getContent()
	{
		return contentCache;
	}

	/**
	 * Returns the currently selected content or an empty string if nothing is selected.
	 * 
	 * @return
	 */
	public String getSelectedContent()
	{
		if (this.hasSelection())
		{
			this.optimizeContent();

			int startIndex = this.getContentIndexOfAtom(this.getSelectionStart());
			int endIndex = this.getContentIndexOfAtom(this.getSelectionEnd());
			this.updateContent(this.requestedWidthCache);

			return contentCache.substring(startIndex, endIndex);
		}
		else
		{
			return "";
		}
	}

	/**
	 * Updates the content as it is.
	 * 
	 * @param width
	 */
	public void updateContent(int width)
	{
		if (width <= 0)
		{
			width = 1;
		}

		requestedWidthCache = width;

		synchronized (content)
		{
			for (ContentUserLine line : content)
			{
				line.UpdateContent(width, autoWarp, factory);
			}
		}
		this.updateSize();
	}

	/**
	 * Optimizes the content objects. This will remove all auto-warped lines
	 * so that an index transformation is correct. This should be called before
	 * a call to getContentIndexOfAtom() to ensure that the index is correct.
	 */
	public void optimizeContent()
	{
		for (ContentUserLine line : content)
		{
			line.optimizeContent();
		}
	}

	/**
	 * Recreates the content form the given string.
	 * 
	 * @param text
	 * @param width
	 */
	public void updateContent(String text, int width)
	{
		if (width <= 0)
		{
			width = 1;
		}
		
		if (text == null)
			text = "";
		
		requestedWidthCache = width;
		contentCache = text;
		atomCountCache = 0;
		String[] lines = text.split("\n", -1);
		synchronized (content)
		{
			if (content != null)
				content.clear();

			content = new ArrayList<ContentUserLine>(lines.length);

			for (String line : lines)
			{
				ContentUserLine parts = createContentParts(line, width);
				atomCountCache++; // add one for newline (empty lines)
				content.add(parts);
			}

			//remove last atom that is too much here
			if (atomCountCache > 0)
				atomCountCache--;
		}

		updateSize();
		setActiveAtom(0);
	}

	/**
	 * Calculates the index of a given atom. Returns -1 if the atom is not
	 * within the Content.
	 * 
	 * @param atom
	 */
	public int getContentIndexOfAtom(int atom)
	{
		if (atom < 0 || atom > this.getAtomCount())
			return -1;

		int index = 0;
		int currentAtom = 0;
		ContentUserLine currentPart = null;

		synchronized (content)
		{
			for (ContentUserLine part : content)
			{
				index += part.getIndexCount();
				currentAtom += part.getAtomCount();
				currentPart = part;
				if (currentAtom >= atom)
				{
					break;
				}
				currentAtom++; // '\n'
				index++;
			}

			if (currentPart != null)
			{
				currentAtom -= currentPart.getAtomCount();
				index -= currentPart.getIndexCount();
				int preIndex = atom - currentAtom;
				if (preIndex != 0)
					index += currentPart.getContentIndexOfAtom(preIndex);
			}
		}
		return index;
	}

	private ContentUserLine createContentParts(String textLine, int width)
	{
		ContentUserLine result = new ContentUserLine(factory, hideContent);
		ContentLine contentLine = factory.getParts(textLine, hideContent);

		result.createContentLines(contentLine, isAutoWarp(), width, factory);
		atomCountCache += result.getAtomCount();
		return result;
	}

	public void setActiveAtom(long atomIndex)
	{
		synchronized (content)
		{
			if (this.activeLine != null)
			{
				this.activeLine.setActiveAtom(-1);
				this.activeLine = null;
				this.activeLineAtoms = 0;
				this.activeLineY = 0;
				this.activeAtom = -1;
			}

			long atomCount = 0;
			activeLineY = 0;

			if (atomIndex < 0)
			{
				this.activeLine = null;
				this.activeLineAtoms = 0;
				this.activeLineY = 0;
				this.activeAtom = -1;
			}
			for (ContentUserLine line : content)
			{
				atomCount += line.getAtomCount();
				if (atomCount >= atomIndex)
				{
					this.activeLine = line;
					this.activeAtom = atomIndex;
					this.activeLineAtoms = (int) (atomCount - line.getAtomCount());
					this.activeLine.setActiveAtom((int) (atomIndex - (atomCount - line.getAtomCount())));
					return;
				}
				activeLineY += line.getSize().getHeight();
				atomCount++;
			}
		}
		this.activeLine = null;
		this.activeAtom = -1;
	}

	public long findAtomOnPosition(int x, int y)
	{
		int yPos = 0;
		long selectionIndex = 0;
		ContentUserLine selectedLine = null;
		synchronized (content)
		{
			if (y < 0)
			{
				// select last line as y pos is too low
				selectedLine = content.get(0);
				selectionIndex = 0;
			}
			else
			{
				for (ContentUserLine line : content)
				{
					if ((yPos + line.getSize().getHeight()) > y)
					{
						selectedLine = line;
						break;
					}
					else
					{
						selectionIndex += line.getAtomCount() + 1;
					}
					yPos += line.getSize().getHeight();
				}

				if (selectedLine == null)
				{
					selectedLine = content.get(content.size() - 1);
					selectionIndex = this.getAtomCount() - selectedLine.getAtomCount();
					yPos -= selectedLine.getSize().getHeight();
					if (yPos < 0)
						yPos = 0;
				}
			}

			// line found, search in content line
			if (selectedLine != null)
			{
				long atoms = selectedLine.findAtomOnPosition(x, y - yPos);
				if (atoms < 0)
					selectionIndex = -1;
				else
					selectionIndex += atoms;
			}
			else
			{
				// no line found
				selectionIndex = -1;
			}
		}
		return selectionIndex;
	}

	public boolean hasActiveLine()
	{
		synchronized (content)
		{
			return this.activeLine != null && this.activeLine.hasActiveLine();
		}
	}

	/**
	 * Adds a character at the selection start index. If a selection is present it will be
	 * removed.
	 * 
	 * @param c
	 * @return true if the character could be added.
	 */
	public boolean addChar(char c)
	{
		synchronized (content)
		{
			if (hasActiveLine())
			{
				if ((c != '\n' && c != '\t') || multiline)
				{
					clearSelection();

					if (c == '\n')
					{
						//split contentline as user wants a newline
						ContentUserLine newline = this.activeLine.splitActive(factory, hideContent);
						this.content.add(this.content.indexOf(activeLine) + 1, newline); //insert after active line
						newline.UpdateContent(this.requestedWidthCache, autoWarp, factory);

						//update active line
						this.activeAtom++;
						this.activeLineAtoms += this.activeLine.getAtomCount();
						this.activeLineY += this.activeLine.getSize().getHeight();
						this.activeLine = newline;
						updateContentCache();
						updateSize();
					}
					else
					{
						if (this.activeLine.addChar(c))
						{
							this.activeAtom++;
							this.activeLine.UpdateContent(this.requestedWidthCache, autoWarp, factory);
							updateContentCache();
							updateSize();
							return true;
						}
					}
				}

			}
		}

		return false;
	}

	/**
	 * Adds a line to the content and updates all values.
	 * 
	 * @param line
	 */
	public void addAtBeginning(ContentUserLine line)
	{
		synchronized (content)
		{
			content.add(0, line);
		}
		updateContentCache();
		updateSize();
	}

	public void addAtBeginning(String content)
	{
		String[] lines = content.split("\n", -1);
		synchronized (content)
		{
			for (int i = 0; i < lines.length; i++)
			{
				String line = lines[i];
				ContentUserLine newLine = new ContentUserLine(factory, hideContent);
				newLine.setActiveAtom(0);
				newLine.addContent(line, factory);
				this.content.add(i, newLine);
				newLine.UpdateContent(requestedWidthCache, autoWarp, factory);
			}

			//remove lines that are too much
			while (this.getContentLineCount() > maxLines && maxLines >= 0)
			{
				this.removeLastContentLine();
			}
		}

		updateContentCache();
		updateSize();
	}

	/**
	 * Adds a line to the content and updates all values.
	 * 
	 * @param line
	 */
	public void addAtEnd(ContentUserLine line)
	{
		synchronized (content)
		{
			content.add(line);
		}
		updateContentCache();
		updateSize();
	}

	public void addAtEnd(String content)
	{
		String[] lines = content.split("\n", -1);
		synchronized (content)
		{
			for (int i = 0; i < lines.length; i++)
			{
				String line = lines[i];
				ContentUserLine newLine = new ContentUserLine(factory, hideContent);
				newLine.setActiveAtom(0);
				newLine.addContent(line, factory);
				this.content.add(newLine);
				newLine.UpdateContent(requestedWidthCache, autoWarp, factory);
			}

			//remove lines that are too much
			while (this.getContentLineCount() > maxLines && maxLines >= 0)
			{
				this.removeFirstContentLine();
			}

		}
		updateContentCache();
		updateSize();
	}

	public void removeLastContentLine()
	{
		synchronized (content)
		{
			if (this.content.size() >= 1)
			{
				this.content.remove(this.content.size() - 1);
				updateContentCache();
				updateSize();
			}
		}
	}

	public void removeFirstContentLine()
	{
		synchronized (content)
		{
			if (this.content.size() >= 1)
			{
				this.content.remove(0);
				updateContentCache();
				updateSize();
			}
		}
	}

	public int getContentLineCount()
	{
		return content.size();
	}

	/**
	 * Adds the content to the active position.
	 * 
	 * @param content
	 */
	public void addContent(String content)
	{
		synchronized (content)
		{
			if (hasActiveLine())
			{
				if (hasSelection())
					removeSelection(factory, hideContent);

				String[] lines = content.split("\n", -1);

				for (int i = 0; i < lines.length; i++)
				{
					String line = lines[i];
					if (this.activeLine.addContent(line, factory))
					{
						this.activeAtom += line.length();
						this.activeLine.UpdateContent(this.requestedWidthCache, autoWarp, factory);
					}

					if (i < lines.length - 1)
					{
						//on last line add no new line
						ContentUserLine newline = this.activeLine.splitActive(factory, hideContent);
						this.content.add(this.content.indexOf(activeLine) + 1, newline); //insert after active line
						//update active line
						this.activeAtom++;
						this.activeLineAtoms += this.activeLine.getAtomCount();
						this.activeLineY += this.activeLine.getSize().getHeight();
						this.activeLine = newline;
					}
				}

				updateContentCache();
				updateSize();
				setActiveAtom(this.activeAtom);
			}
		}
	}

	public Character removeNextChar()
	{
		synchronized (content)
		{
			if (!hasActiveLine())
				return null;

			clearSelection();
			Character result = this.activeLine.removeNextChar();
			if (result != null)
			{
				updateContentCache();
				updateContent(this.requestedWidthCache);
				setActiveAtom(this.activeAtom);
			}
			else
			{
				// char is null -> nothing to remove in this line at the selected position,
				// remove linebreak
				int index = content.indexOf(this.activeLine) + 1;
				if (index >= content.size())
				{
					result = null;
				}
				else
				{
					ContentUserLine next = content.get(index);
					content.remove(index);

					for (ContentLine line : next.getContentLines())
					{
						this.activeLine.add(line);
					}
					this.activeLine.update();
					result = '\n';

					updateContentCache();
					updateContent(this.requestedWidthCache);
					setActiveAtom(this.activeAtom);
				}
			}
			return result;
		}
	}

	public Character removePreviousChar()
	{
		synchronized (content)
		{
			if (activeLine == null)
				return null;

			clearSelection();
			Character result = this.activeLine.removePreviousChar();
			if (result != null)
			{
				this.activeAtom--;
				updateContentCache();
				updateContent(this.requestedWidthCache);
				setActiveAtom(this.activeAtom);
			}
			else
			{
				// char is null -> nothing to remove in this line at the selected position,
				// remove linebreak
				int index = content.indexOf(this.activeLine) - 1;
				if (index < 0)
				{
					result = null;
				}
				else
				{
					ContentUserLine prev = content.get(index);
					content.remove(index + 1);
					for (ContentLine line : this.activeLine.getContentLines())
					{
						prev.add(line);
					}
					prev.update();
					result = '\n';

					this.activeAtom--;
					updateContentCache();
					updateContent(this.requestedWidthCache);
					setActiveAtom(this.activeAtom);
				}
			}

			return result;
		}
	}

	public void moveLeft()
	{
		if (activeAtom == -1)
			return;

		this.activeAtom--;
		if (activeAtom < 0)
			activeAtom = 0;
		setActiveAtom(this.activeAtom);
	}

	public void moveRight()
	{
		if (activeAtom == -1)
			return;

		this.activeAtom++;
		if (activeAtom >= getAtomCount())
		{
			activeAtom = getAtomCount();
		}
		setActiveAtom(this.activeAtom);
	}

	public void moveUp()
	{
		synchronized (content)
		{
			if (activeLine == null)
				return;

			Point currentPos = activeLine.getActivePosition();

			currentPos.setY(activeLineY + currentPos.getY() - activeLine.getSelectedLine().getSize().getHeight());
			if (currentPos.getY() < 0)
			{
				currentPos.setY(0);
			}
			activeAtom = findAtomOnPosition(currentPos.getX(), currentPos.getY());
			setActiveAtom(this.activeAtom);
		}
	}

	public void moveDown()
	{
		synchronized (content)
		{
			if (activeLine == null)
				return;

			Point currentPos = activeLine.getActivePosition();

			currentPos.setY(activeLineY + currentPos.getY() + (activeLine.getSelectedLine().getSize().getHeight()));
			if (currentPos.getY() > getSize().getHeight())
			{
				currentPos.setY(getSize().getHeight());
			}
			activeAtom = findAtomOnPosition(currentPos.getX(), currentPos.getY());
			setActiveAtom(this.activeAtom);
		}
	}

	public Point getActivePosition()
	{
		synchronized (content)
		{
			if (activeLine != null)
			{
				Point result = activeLine.getActivePosition();
				if (result != null)
					result.setY(result.getY() + activeLineY);
				return result;
			}
			else
			{
				return null;
			}
		}
	}

	public Dimension getSize()
	{
		if (size == null)
			updateSize();
		return size;
	}

	public List<ContentUserLine> getContentObjects()
	{
		return content;
	}

	/**
	 * @return Returns the autoWarp.
	 */
	public boolean isAutoWarp()
	{
		return autoWarp;
	}

	/**
	 * @param autoWarp
	 *          The autoWarp to set.
	 */
	public void setAutoWarp(boolean autoWarp, int width)
	{
		this.autoWarp = autoWarp;
		updateContent(width);
	}

	/**
	 * @return Returns the startSelection.
	 */
	public long getActiveAtom()
	{
		return activeAtom;
	}

	/**
	 * @return Returns the factory.
	 */
	public IContentFactory getFactory()
	{
		return factory;
	}

	/**
	 * @param factory
	 *          The factory to set.
	 */
	public void setFactory(IContentFactory factory)
	{
		this.factory = factory;
		updateContent(this.contentCache, Math.max(this.requestedWidthCache, 50));
		setActiveAtom(this.activeAtom);
	}

	public long getAtomCount()
	{
		return atomCountCache;
	}

	/**
	 * @return Returns the multiline.
	 */
	public boolean isMultiline()
	{
		return multiline;
	}

	/**
	 * @param multiline
	 *          The multiline to set.
	 */
	public void setMultiline(boolean multiline)
	{
		this.multiline = multiline;
	}

	/**
	 * @return the passwordField
	 */
	public boolean hasHiddenContent()
	{
		return hideContent;
	}

	/**
	 * @param passwordField
	 *          the passwordField to set
	 */
	public void setHideContent(boolean passwordField)
	{
		this.hideContent = passwordField;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#getSelectionEnd()
	 */
	public int getSelectionEnd()
	{
		int selectionAtoms = 0;
		boolean inSelection = false;
		ContentUserLine lastLine = null;

		for (ContentUserLine line : content)
		{
			boolean lineSelection = line.hasSelection();

			if (lineSelection)
			{
				inSelection = true;
			}

			if (inSelection)
			{
				if (lineSelection)
				{
					lastLine = line;
				}
				else
				{
					break;
				}
			}

			selectionAtoms += line.getAtomCount() + 1;
		}

		if (inSelection)
		{
			if (lastLine != null)
				selectionAtoms -= (lastLine.getAtomCount() + 1);

			int lineSelection = lastLine.getSelectionEnd();
			//			if (lineSelection == lastLine.getAtomCount())
			//				lineSelection++; //also select '\n'
			selectionAtoms += lineSelection;
			return selectionAtoms;
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#getSelectionStart()
	 */
	public int getSelectionStart()
	{
		int selectionAtoms = 0;

		for (ContentUserLine line : content)
		{
			if (line.hasSelection())
			{
				return selectionAtoms + line.getSelectionStart();
			}
			selectionAtoms += line.getAtomCount() + 1;
		}

		return -1;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#hasSelection()
	 */
	public boolean hasSelection()
	{
		for (ContentUserLine line : content)
		{
			if (line.hasSelection())
			{
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#clearSelection()
	 */
	public void clearSelection()
	{
		for (ContentUserLine line : content)
		{
			line.clearSelection();
		}
		optimizeContent();
		updateContent(this.requestedWidthCache);
	}

	public void removeSelection()
	{
		this.removeSelection(factory, hideContent);
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#removeSelection()
	 */
	public void removeSelection(IContentFactory factory, boolean pwdField)
	{
		int pos = this.getSelectionStart();
		List<ContentUserLine> removables = new ArrayList<ContentUserLine>();

		synchronized (content)
		{
			for (ContentUserLine line : content)
			{
				line.removeSelection(factory, pwdField);
				if (line.isEmpty() && line.hasSelection())
				{
					if (removables.contains(activeLine))
					{
						int index = content.indexOf(removables.get(0)) - 1;

						if (index >= 0)
						{
							activeLine = content.get(index);
						}
						else
						{
							activeLine = null;
						}
					}
					//remove line
					removables.add(line);
				}
			}

			content.removeAll(removables);

			//take first part if none anymore
			if (activeLine == null && content.size() > 0)
			{
				activeLine = content.get(0);
				activeLineAtoms = 0;
				activeLineY = 0;
			}

			if (content.size() <= 0)
			{
				//keep at least one line
				ContentUserLine line = new ContentUserLine(factory, pwdField);
				content.add(line);
				activeLine = line;
				activeLineAtoms = (int) line.getAtomCount();
				activeLineY = 0;
			}
			this.setActiveAtom(pos);

			updateContentCache();
			updateSize();
		}
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#setSelection(int, int)
	 */
	public void setSelection(int start, int end)
	{
		int selectionAtoms = 0;

		if (this.hasSelection())
			this.clearSelection();

		synchronized (content)
		{
			for (ContentUserLine line : content)
			{
				long lineAtoms = line.getAtomCount();
				if (selectionAtoms + lineAtoms > start && selectionAtoms < end)
				{
					line.setSelection(start - selectionAtoms, end - selectionAtoms);
				}
				else if (selectionAtoms > end)
					break;

				selectionAtoms += line.getAtomCount() + 1;
			}
		}
	}

	public int getMaxLines()
	{
		return maxLines;
	}

	public void setMaxLines(int maxLines)
	{
		this.maxLines = maxLines;
	}

	protected void fireElementEnteredEvent(ElementEvent<AbstractContentPart> event)
	{
		for (IElementListener<AbstractContentPart> l : elementListener)
		{
			l.enteredElement(event);
		}
	}

	protected void fireElementLeftEvent(ElementEvent<AbstractContentPart> event)
	{
		for (IElementListener<AbstractContentPart> l : elementListener)
		{
			l.leftElement(event);
		}
	}

	protected void fireElementActivatedEvent(ElementEvent<AbstractContentPart> event)
	{
		for (IElementListener<AbstractContentPart> l : elementListener)
		{
			l.activatedElement(event);
		}
	}
}
