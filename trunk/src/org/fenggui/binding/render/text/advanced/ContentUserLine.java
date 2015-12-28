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
 * Created on 02.01.2008
 * $Id$
 */
package org.fenggui.binding.render.text.advanced;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

/**
 * A UserLine is a line where the user wants a linebreak after it. This means that in the
 * content there will be a '\n' before and/or after this userline. Within a userline there
 * will never be a linebreak ('\n').
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ContentUserLine implements IContentSelection, IContent<ContentUserLine>
{
	private Dimension size;
	private List<ContentLine> content;
	private ContentLine activeLine = null;
	private int activeLineY = 0;
	private int activeLineAtoms = 0;
	private long atomCountCache;
	private long indexCountCache;
	private String contentCache;

	public ContentUserLine(IContentFactory factory, boolean hideContent)
	{
		content = new ArrayList<ContentLine>();
		content.add(new ContentLine(factory, hideContent));
		update();
	}

	public ContentUserLine(ArrayList<ContentLine> lines, IContentFactory factory, boolean hideContent)
	{
		content = lines;
		update();
	}

	public List<ContentLine> getContentLines()
	{
		return content;
	}

	public int getAtomsTillActiveLine()
	{
		if (hasActiveLine())
		{
			return activeLineAtoms;
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
			return (int) (activeLineAtoms + activeLine.getAtomCount());
		}
		else
		{
			return 0;
		}
	}

	public void setContentLines(List<ContentLine> content)
	{
		this.content = content;
		this.update();
	}

	public void update()
	{
		updateSize();
		updateContentCache();
	}

	private void updateSize()
	{
		int y = 0;
		int x = 0;

		for (ContentLine line : content)
		{
			x = Math.max(x, line.getSize().getWidth());
			y += line.getSize().getHeight();
		}

		// Dimension oldSize = this.size;
		this.size = new Dimension(x, y);
	}

	public void add(ContentLine line)
	{
		content.add(line);
	}

	public int getContentLineCount()
	{
		return content.size();
	}

	/**
	 * Adds a line to the content and updates all values.
	 * 
	 * @param line
	 */
	public void addAtBeginning(ContentLine line)
	{
		content.add(0, line);
		update();
	}

	/**
	 * Adds a line to the content and updates all values.
	 * 
	 * @param line
	 */
	public void addAtEnd(ContentLine line)
	{
		content.add(line);
		update();
	}

	public ContentLine getSelectedLine()
	{
		return activeLine;
	}

	public long getAtomCount()
	{
		return atomCountCache;
	}

	public long findAtomOnPosition(int x, int y)
	{
		// find line:
		int posY = 0;
		int atomCount = 0;
		ContentLine line = null;
		for (ContentLine parts : content)
		{
			if ((posY + parts.getSize().getHeight()) > y)
			{
				line = parts;
				break;
			}
			posY += parts.getSize().getHeight();
			atomCount += parts.getAtomCount();
		}

		if (line == null)
		{
			// select last line
			line = content.get(content.size() - 1);
			atomCount -= line.getAtomCount();
		}

		atomCount += line.calculatePositionInAtoms(x);

		return atomCount;
	}

	/**
	 * Splitts the Content on the current active position. Returns null if nothing is
	 * selected.
	 * 
	 * @return Second part of the content or null.
	 */
	public ContentUserLine splitActive(IContentFactory factory, boolean hideContent)
	{
		if (!hasActiveLine())
			return null;

		// prepare ContentUserLine
		ContentUserLine uline = new ContentUserLine(factory, hideContent);
		List<ContentLine> lines = uline.getContentLines();
		lines.clear();

		// add last part of selected line
		ContentLine rest = this.activeLine.splitActive(factory, hideContent);
		if (rest != null)
			lines.add(rest);

		// add all other lines
		boolean found = false;
		for (ContentLine line : this.content)
		{
			if (line == this.activeLine)
			{
				found = true;
			}
			else
			{
				if (found == true)
				{
					lines.add(line);
				}
			}
		}

		this.content.removeAll(lines);

		// updates size & contentCache
		uline.update();
		this.update();

		// update active lines
		uline.activeLine = rest;
		uline.activeLineY = 0;
		uline.activeLineAtoms = 0;

		this.activeLine = null;
		this.activeLineAtoms = 0;
		this.activeLineY = 0;

		return uline;
	}

	public void mergeContent(ContentUserLine line)
	{
		this.content.addAll(line.content);
		if (!hasActiveLine() && line.hasActiveLine())
		{
			this.activeLine = line.activeLine;
			this.activeLineAtoms = line.activeLineAtoms + (int) this.atomCountCache;
			this.activeLineY = line.activeLineY + this.getSize().getHeight();
		}
		this.size.setHeight(line.size.getHeight() + this.size.getHeight());
		this.size.setWidth(Math.max(line.size.getWidth(), this.size.getWidth()));
		this.atomCountCache += line.atomCountCache;
		indexCountCache += line.getIndexCount();
		this.contentCache += line.contentCache;
	}

	public void optimizeContent()
	{
		ContentLine first = null;
		for (ContentLine line : content)
		{
			if (first == null)
				first = line;
			else
				first.mergeContent(line);
		}

		if (first != null)
		{
			first.optimizeContent();
			content.clear();
			content.add(first);
			if (first.hasActivePart())
			{
				activeLine = first;
				activeLineAtoms = 0;
				activeLineY = 0;
			}
			updateSize();
		}
	}

	public boolean hasActiveLine()
	{
		return this.activeLine != null && activeLine.hasActivePart();
	}

	/**
	 * Updates the content by merging together the lines and splitting them again with the
	 * given width.
	 * 
	 * @param width
	 */
	public void UpdateContent(int width, boolean wordWarp, IContentFactory factory)
	{
		List<ContentLine> result = new Vector<ContentLine>(this.content.size());

		ContentLine curLine = null;
		for (ContentLine line : this.content)
		{
			if (curLine == null)
			{
				curLine = line;
			}
			else
			{
				if (activeLine == line)
				{
					activeLine = curLine;
					activeLineAtoms -= curLine.getAtomCount();
					activeLineY -= line.getSize().getHeight();
				}
				curLine.mergeContent(line);
			}

			if (wordWarp && curLine.getSize().getWidth() > width)
			{
				ContentLine newLine = curLine;
				result.add(newLine);
				curLine = curLine.splitAt(width, factory);
				if (newLine == activeLine && !newLine.hasActivePart())
				{
					activeLine = curLine;
					activeLineY += newLine.getSize().getHeight();
					activeLineAtoms += newLine.getAtomCount();
				}
			}
		}
		// add rest lines
		if (curLine != null)
		{
			while (wordWarp && curLine.getSize().getWidth() > width)
			{
				ContentLine newLine = curLine;
				result.add(newLine);
				curLine = curLine.splitAt(width, factory);
			}
			if (curLine != null)
				result.add(curLine);
		}
		this.content.clear();
		this.content.addAll(result);

		update();
		UpdateActiveLine();
	}

	/**
	 * Updates the data for the active Line. It will use the first active line it finds.
	 */
	private void UpdateActiveLine()
	{
		int lineY = 0;
		int atoms = 0;

		this.activeLine = null;
		this.activeLineY = 0;
		this.activeLineAtoms = 0;

		for (ContentLine line : this.content)
		{
			if (line.hasActivePart())
			{
				this.activeLine = line;
				this.activeLineY = lineY;
				this.activeLineAtoms = atoms;
				break;
			}

			atoms += line.getAtomCount();
			lineY += line.getSize().getHeight();
		}

	}

	/**
	 * 
	 * @param contentLine
	 * @param wordwarp
	 * @param width
	 * @param factory
	 */
	public void createContentLines(ContentLine contentLine, boolean wordwarp, int width, IContentFactory factory)
	{
		removeAll();

		if (wordwarp)
		{
			while (contentLine.getSize().getWidth() > width)
			{
				this.content.add(contentLine);
				contentLine = contentLine.splitAt(width, factory);
			}
		}
		this.content.add(contentLine);
		UpdateContent(width, wordwarp, factory);
		update();
	}

	public void removeAll()
	{
		this.content.clear();
		this.contentCache = "";
		this.activeLine = null;
		this.activeLineAtoms = 0;
		this.activeLineY = 0;
		this.size = new Dimension(0, 0);
	}

	private void updateContentCache()
	{
		String result = "";
		long atoms = 0;
		long index = 0;

		for (ContentLine line : content)
		{
			result += line.getContent();
			atoms += line.getAtomCount();
			index += line.getIndexCount();
		}
		atomCountCache = atoms;
		indexCountCache = index;
		contentCache = result;
	}

	public String getContent()
	{
		return contentCache;
	}

	public boolean addChar(char c)
	{
		if (this.hasActiveLine())
		{
			if (this.activeLine.addChar(c))
			{
				updateContentCache();
				updateSize();
				return true;
			}
			else
			{
				return false;
			}
		}
		else
			return false;
	}

	public boolean addContent(String content, IContentFactory factory)
	{
		if (this.activeLine != null)
		{
			if (this.activeLine.addContent(content, factory))
			{
				updateContentCache();
				updateSize();
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			throw new IllegalStateException("Can't add new content if none is set to active.");
		}
	}

	/**
	 * Removes the next character after the active position. Does not realign any lines.
	 * 
	 * @return
	 */
	public Character removeNextChar()
	{
		Character result = null;
		if (hasActiveLine())
		{
			result = activeLine.removeNextChar();

			if (result == null)
			{
				int index = this.content.indexOf(activeLine) + 1;
				if (index < this.content.size())
				{
					ContentLine nextLine = this.content.get(index);
					result = nextLine.removeNextChar();

					if (result != null && nextLine.isEmpty())
					{
						content.remove(nextLine);
					}
				}
			}

			if (result != null)
			{
				update();
			}
			return result;
		}
		else
		{
			if (content.size() >= 1)
			{
				ContentLine nextLine = this.content.get(0);
				result = nextLine.removeNextChar();

				if (result != null && nextLine.isEmpty())
				{
					content.remove(nextLine);
				}
				if (result != null)
				{
					update();
				}
			}
			return result;
		}
	}

	/**
	 * Removes previous next character after the active position. Does not realign any
	 * lines.
	 * 
	 * @return
	 */
	public Character removePreviousChar()
	{
		if (hasActiveLine())
		{
			Character result = activeLine.removePreviousChar();
			if (result == null)
			{
				int index = this.content.indexOf(activeLine) - 1;
				if (index >= 0)
				{
					this.activeLine = this.content.get(index);
					this.activeLineY -= this.activeLine.getSize().getHeight();
					this.activeLineAtoms -= this.activeLine.getAtomCount();
					result = this.activeLine.removePreviousChar();
				}
			}

			if (result != null)
			{
				atomCountCache--;
				indexCountCache--;
				updateContentCache();
				updateSize();
			}
			return result;
		}
		else
			return null;
	}

	public ContentLine setActiveAtom(int atomIndex)
	{
		long atomCount = 0;
		activeLineY = 0;

		if (hasActiveLine())
		{
			this.activeLine.setActiveContentPart(-1);
			this.activeLine = null;
			this.activeLineAtoms = 0;
			this.activeLineY = 0;
		}

		if (atomIndex >= 0)
		{
			for (ContentLine line : content)
			{
				atomCount += line.getAtomCount();
				if (atomCount >= atomIndex)
				{
					this.activeLine = line;
					this.activeLineAtoms = (int) (atomCount - line.getAtomCount());
					this.activeLine.setActiveContentPart(atomIndex - activeLineAtoms);
					return line;
				}
				activeLineY += line.getSize().getHeight();
			}
		}
		this.activeLine = null;
		this.activeLineY = 0;
		this.activeLineAtoms = 0;
		return null;
	}

	public Dimension getSize()
	{
		return size;
	}

	public Point getActivePosition()
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#hasSelection()
	 */
	public boolean hasSelection()
	{
		for (ContentLine line : content)
		{
			if (line.hasSelection())
				return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#getSelectionStart()
	 */
	public int getSelectionStart()
	{
		int result = 0;
		for (ContentLine line : content)
		{
			if (line.hasSelection())
			{
				return result + line.getSelectionStart();
			}
			result += line.getAtomCount();
		}

		return -1; // no selection start found
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#getSelectionEnd()
	 */
	public int getSelectionEnd()
	{
		boolean inSelection = false;
		int endSelection = 0;
		ContentLine lastLine = null;

		for (ContentLine line : content)
		{
			boolean lineSelected = line.hasSelection();
			if (lineSelected)
			{
				inSelection = true;
			}

			if (inSelection == true)
			{
				if (lineSelected)
				{
					lastLine = line;
				}
				else
				{
					break;
				}
			}

			endSelection += line.getAtomCount();
		}

		if (inSelection)
		{
			if (lastLine != null)
				endSelection -= lastLine.getAtomCount();

			endSelection += lastLine.getSelectionEnd();
			return endSelection;
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#removeSelection()
	 */
	public void clearSelection()
	{
		for (ContentLine line : content)
		{
			line.clearSelection();
		}
	}

	public boolean isEmpty()
	{
		return content.isEmpty() || (content.size() == 1 && content.get(0).isEmpty());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#removeSelectionWithContent()
	 */
	public void removeSelection(IContentFactory factory, boolean pwdField)
	{
		List<ContentLine> removables = new ArrayList<ContentLine>();

		for (ContentLine line : content)
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
				// remove line
				removables.add(line);
			}
		}

		content.removeAll(removables);

		// take first part if none anymore
		if (activeLine == null && content.size() > 0)
		{
			activeLine = content.get(0);
			activeLine.setActiveContentPart(0);
			activeLineAtoms = 0;
			activeLineY = 0;
		}

		if (content.size() <= 0)
		{
			// keep at least one line
			ContentLine line = new ContentLine(factory, pwdField);
			content.add(line);
			activeLine = line;
			activeLineAtoms = (int) line.getAtomCount();
			line.setSelection(0, 1);
			line.setActiveContentPart(0);
			activeLineY = 0;
		}

		update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#setSelection(int,
	 *      int)
	 */
	public void setSelection(int start, int end)
	{
		int current = 0;
		boolean inSelection = false;

		for (ContentLine line : content)
		{
			long lineAtoms = line.getAtomCount();
			if (current + lineAtoms > start && current < end)
			{
				line.setSelection(start - current, end - current);
				inSelection = true;
			}

			if (inSelection && current > end)
			{
				break;
			}
			current += line.getAtomCount();
		}
	}

	public int getContentIndexOfAtom(int atom)
	{
		int index = 0;
		int currentAtom = 0;
		ContentLine currentPart = null;

		for (ContentLine part : content)
		{
			index += part.getIndexCount();
			currentAtom += part.getAtomCount();
			currentPart = part;
			if (currentAtom > atom)
			{
				break;
			}
		}

		if (currentPart != null)
		{
			currentAtom -= currentPart.getAtomCount();
			int preIndex = atom - currentAtom;
			index -= currentPart.getIndexCount();
			if (preIndex != 0)
				index += currentPart.getContentIndexOfAtom(preIndex);
		}
		return index;
	}

	public long getIndexCount()
	{
		return indexCountCache;
	}
}
