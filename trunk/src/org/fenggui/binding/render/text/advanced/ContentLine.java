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
 * Created on 26.11.2007
 * $Id$
 */
package org.fenggui.binding.render.text.advanced;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

/**
 * Represents a line in the content of a ContentManager.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ContentLine implements IContentSelection, IContent<ContentLine>
{
	private List<AbstractContentPart> content = new ArrayList<AbstractContentPart>(5);
	private Dimension size;
	private int atomCount = 0;
	private AbstractContentPart activePart = null;
	private int activePartX = 0;
	private int indexCount = 0;

	public ContentLine(IContentFactory factory, boolean hideContent)
	{
		size = new Dimension(0, 0);
		content.add(factory.getEmptyLine(hideContent));
		recalculateSize();
	}

	public ContentLine(List<AbstractContentPart> content)
	{
		this.content = content;
		recalculateSize();
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContent#getContent()
	 */
	public String getContent()
	{
		String result = "";
		for (AbstractContentPart part : content)
		{
			result += part.getContent();
		}
		return result;
	}

	/**
	 * Cuts the content on the currently selected position.
	 * 
	 * @return
	 */
	public ContentLine splitActive(IContentFactory factory, boolean hideContent)
	{
		if (!hasActivePart())
			return null;

		ContentLine result = new ContentLine(factory, hideContent);
		result.removeAll();

		AbstractContentPart rest = this.activePart.splitAtChar(this.activePart.getActivePosition());
		if (rest != null)
			result.addAtEnd(rest);

		// add all other lines
		boolean found = false;
		List<AbstractContentPart> removable = new ArrayList<AbstractContentPart>(this.content.size());
		for (AbstractContentPart line : this.content)
		{
			if (line == this.activePart)
			{
				found = true;
			}
			else
			{
				if (found == true)
				{
					removable.add(line);
					result.addAtEnd(line);
				}
			}
		}

		this.content.removeAll(removable);
		if (activePart.isEmpty())
		{
			//remove active part if empty from content
			this.content.remove(this.activePart);
		}

		if (this.content.isEmpty())
		{
			//add empty line if content is empty now
			this.content.add(factory.getEmptyLine(this.activePart.isHideContent()));
		}

		this.recalculateSize();

		//update active
		if (rest == null && result.content.size() > 0)
			rest = result.content.get(0);

		result.activePart = rest;
		result.activePartX = 0;
		if (result.activePart != null)
			result.activePart.setActiveAtom(0);

		this.activePart.setActiveAtom(-1);
		this.activePart = null;
		this.activePartX = 0;

		return result;
	}

	public void addAtEnd(AbstractContentPart part)
	{
		size.setHeight(Math.max(size.getHeight(), part.getSize().getHeight()));
		size.setWidth(size.getWidth() + part.getSize().getWidth());
		atomCount += part.getAtomCount();
		indexCount += part.getIndexCount();
		if (!content.isEmpty())
		{
			if (part.canMerge(content.get(content.size() - 1)))
			{
				content.get(content.size() - 1).mergePart(part);
			}
			else
			{
				content.add(content.size(), part);
			}
		}
		else
		{
			content.add(content.size(), part);
		}
	}

	public void addAtBeginning(AbstractContentPart part)
	{
		size.setHeight(Math.max(size.getHeight(), part.getSize().getHeight()));
		size.setWidth(size.getWidth() + part.getSize().getWidth());
		atomCount += part.getAtomCount();
		indexCount += part.getIndexCount();

		if (part.canMerge(content.get(0)))
		{
			AbstractContentPart part2 = content.remove(0);
			part.mergePart(part2);
		}
		content.add(0, part);
	}

	public boolean isEmpty()
	{
		return content.isEmpty() || (content.size() == 1 && content.get(0).isEmpty());
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContent#removeAll()
	 */
	public void removeAll()
	{
		content.clear();
		size = new Dimension(0, 0);
		atomCount = 0;
		indexCount = 0;
		activePart = null;
		activePartX = 0;
	}

	private void recalculateSize()
	{
		int width = 0;
		int height = 0;
		atomCount = 0;
		indexCount = 0;
		for (AbstractContentPart part : content)
		{
			width += part.getSize().getWidth();
			height = Math.max(height, part.getSize().getHeight());
			atomCount += part.getAtomCount();
			indexCount += part.getIndexCount();
		}
		size = new Dimension(width, height);
	}

	private void recalculateHeight()
	{
		int newMax = 0;

		for (AbstractContentPart part : content)
		{
			int height = part.getSize().getHeight();
			if (height == size.getHeight())
				return;

			newMax = Math.max(newMax, height);
		}
		size.setHeight(newMax);
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContent#mergeLines(org.fenggui.binding.render.text.advanced.ContentLine)
	 */
	public void mergeContent(ContentLine line)
	{
		content.addAll(line.content);
		if (line.hasActivePart())
		{
			activePart = line.activePart;
			activePartX = line.activePartX + size.getWidth();
		}
		atomCount += line.atomCount;
		indexCount += line.getIndexCount();
		size.setWidth(size.getWidth() + line.size.getWidth());
		size.setHeight(Math.max(size.getHeight(), line.size.getHeight()));
		this.optimizeContent();
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContent#optimizeParts()
	 */
	public void optimizeContent()
	{
		List<AbstractContentPart> newContent = new ArrayList<AbstractContentPart>(content.size());
		int activeX = 0;

		AbstractContentPart oldPart = null;
		for (AbstractContentPart part : content)
		{
			//remove empty parts as this doesn't change anything
			if (part.isEmpty())
				continue;

			//just add first part that is not empty
			if (newContent.isEmpty())
			{
				newContent.add(part);
				oldPart = part;
				continue;
			}

			//try to merge new part with old one
			if (oldPart.canMerge(part))
			{
				if (activePart == part)
				{
					activePart = oldPart;
					activePartX = activeX;
				}
				oldPart.mergePart(part);
			}
			else
			{
				activeX += oldPart.getSize().getWidth();
				newContent.add(part);
				oldPart = part;
			}
		}
		
		if (newContent.size() <= 0 && content.size() <= 1)
		{
			newContent.add(content.get(0));
			if (content.contains(activePart))
			{
				activePart = content.get(0);
				activePartX = 0;
			}
		}
		
		content = newContent;
	}

	/**
	 * Returns if this line has an active Part
	 * 
	 * @return
	 */
	public boolean hasActivePart()
	{
		return activePart != null && activePart.hasActiveAtom();
	}

	/**
	 * Cuts the line at the specified Width and returns ContentParts that are too much. It
	 * also removes the returned ContentParts from the current list of ContentParts.
	 * 
	 * @param width
	 *          width to remove in pixels.
	 * @return list of content parts
	 */
	public ContentLine splitAt(int width, IContentFactory factory)
	{
		if (width > size.getWidth())
		{
			//this can not happen
			throw new IllegalArgumentException("Given width can't be bigger than avaiable width.");
		}

		List<AbstractContentPart> result = new ArrayList<AbstractContentPart>(0);
		ContentLine resultLine = new ContentLine(result);

		int partId = 0;
		int currentWidth = content.get(partId).getSize().getWidth();
		int oldActivePartX = 0;
		int partWidth = currentWidth;

		// search for first part that is too much
		while (currentWidth < width && partId < content.size() - 1)
		{
			oldActivePartX += partWidth;
			partId++;
			partWidth = content.get(partId).getSize().getWidth();
			currentWidth += partWidth;
		}

		//break problematic part
		int shouldBeSize = partWidth - (currentWidth - width);
		AbstractContentPart oldPart = content.get(partId);
		AbstractContentPart part = oldPart.splitAtWord(shouldBeSize, partId == 0);

		if (partId != 0 && oldPart.getSize().getWidth() > shouldBeSize)
		{
			result.add(oldPart);
			atomCount -= oldPart.getAtomCount();
			indexCount -= oldPart.getIndexCount();
			size.setWidth(size.getWidth() - oldPart.getSize().getWidth());

			if (oldPart == activePart)
			{
				resultLine.activePart = oldPart;
				resultLine.activePartX = oldActivePartX + oldPart.getActivePosition();
				activePart = null;
				activePartX = 0;
			}
		}

		result.add(part);
		atomCount -= part.getAtomCount();
		indexCount -= part.getIndexCount();
		size.setWidth(size.getWidth() - part.getSize().getWidth());

		if (activePart == oldPart)
		{
			if (!oldPart.hasActiveAtom() && part.hasActiveAtom())
			{
				resultLine.activePart = part;
				resultLine.activePartX = part.getActivePosition();
				activePart = null;
				activePartX = 0;
			}
		}
		partId++;

		//add other parts
		if (partId < content.size())
		{
			List<AbstractContentPart> list = new Vector<AbstractContentPart>(content.subList(partId, content.size()));
			int overFlowWidth = 0;
			for (AbstractContentPart overflow : list)
			{
				result.add(overflow);
				if (overflow == activePart)
				{
					resultLine.activePart = overflow;
					resultLine.activePartX = overFlowWidth;
				}
				overFlowWidth += overflow.getSize().getWidth();
				atomCount -= overflow.getAtomCount();
				indexCount -= overflow.getIndexCount();
			}
			size.setWidth(size.getWidth() - overFlowWidth);
		}

		content.removeAll(result);

		if (content.isEmpty())
		{
			//if all lines removed add an empty line to content
			content.add(factory.getEmptyLine(part.isHideContent()));
		}

		recalculateHeight();

		//recalc size and atomcount of result
		resultLine.recalculateSize();

		return resultLine;
	}

	private void cutAtIndex(long index)
	{
		long currentIndex = 0;
		AbstractContentPart partToCut = null;
		int partIndex = -1;

		for (AbstractContentPart part : content)
		{
			long partIndexComplete = part.getAtomCount();
			if (index < currentIndex + partIndexComplete)
			{
				partToCut = part;
				partIndex = (int) (index - currentIndex);
				break;
			}
			currentIndex += partIndexComplete;
		}

		//cut  stuff
		if (partToCut != null)
		{
			if (partIndex > 0)
			{
				AbstractContentPart newPart = partToCut.splitAtAtom(partIndex);
				if (!newPart.isEmpty())
				{
					int i = content.indexOf(partToCut);
					content.add(i + 1, newPart);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#setSelection(int, int)
	 */
	public void setSelection(int start, int end)
	{
		long index = 0;
		long currentIndex = 0;

		cutAtIndex(start);
		cutAtIndex(end);

		//parts are already cut to fit the selection
		for (AbstractContentPart part : content)
		{
			currentIndex = part.getAtomCount();

			if (index >= end)
			{
				break;
			}

			if (index >= start)
			{
				part.setSelected(true);
			}

			index += currentIndex;
		}
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#getSelectionStart()
	 */
	public int getSelectionStart()
	{
		int currentIndex = 0;
		int realIndex = -1;
		for (AbstractContentPart part : content)
		{
			if (part.isSelected())
			{
				realIndex = currentIndex;
				break;
			}
			currentIndex += part.getAtomCount();
		}

		return realIndex;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#getSelectionEnd()
	 */
	public int getSelectionEnd()
	{
		int currentIndex = 0;
		boolean inSelection = false;
		int realIndex = -1;

		for (AbstractContentPart part : content)
		{
			if (part.isSelected())
			{
				inSelection = true;
			}
			else if (inSelection)
			{
				realIndex = currentIndex;
				break;
			}

			currentIndex += part.getAtomCount();
		}

		if (realIndex == -1 && inSelection)
			realIndex = currentIndex;

		return realIndex;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#hasSelection()
	 */
	public boolean hasSelection()
	{
		for (AbstractContentPart part : content)
		{
			if (part.isSelected())
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#clearSelection()
	 */
	public void clearSelection()
	{
		for (AbstractContentPart part : content)
		{
			if (part.isSelected())
			{
				part.setSelected(false);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContentSelection#removeSelection()
	 */
	public void removeSelection(IContentFactory factory, boolean pwdField)
	{
		List<AbstractContentPart> removables = new ArrayList<AbstractContentPart>(content.size());

		for (AbstractContentPart part : content)
		{
			if (part.isSelected())
			{
				removables.add(part);
			}
		}

		if (removables.contains(activePart))
		{
			int index = content.indexOf(removables.get(0)) - 1;
			if (index > 0)
			{
				activePart = content.get(index);
				activePartX = 0;
			}
		}
		content.removeAll(removables);

		if (content.size() > 0 && !content.contains(activePart))
		{
			activePart = content.get(0);
			activePart.setActiveAtom(0);
			activePartX = 0;
		}
		
		//recalculate activePartX
		int newX = 0;
		for (AbstractContentPart part : content)
		{
			if (activePart == part)
			{
				activePartX = newX;
				break;
			}
			newX += part.getSize().getWidth();
		}

		if (content.size() <= 0)
		{
			//add at least one empty line to content
			AbstractContentPart line = factory.getEmptyLine(pwdField);
			content.add(line);
			activePart = line;
			line.setSelected(true);
			line.setActiveAtom(0);
			activePartX = 0;
		}
		
		recalculateSize();
	}

	/**
	 * Returns the index of the active position. This is the index within the content
	 * String and not equal to the Atom position. Returns -1 if there is no active position.
	 * 
	 * @return Position of active atom within the content string or -1 if
	 *  no active position. 
	 */
	public long getIndexOfAtom(int atom)
	{
		long index = 0;
		int atomCount = 0;
		if (this.hasActivePart())
		{
			AbstractContentPart active = null;

			for (AbstractContentPart part : content)
			{
				if (atomCount + part.getAtomCount() >= atom)
				{
					index += active.getBeforeLength() + (atom - atomCount);
					break;
				}
				index += part.getBeforeLength() + part.getAtomCount() + part.getAfterLength();
				atomCount = part.getAtomCount();
			}
		}
		else
		{
			index = -1;
		}

		return index;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContent#getSize()
	 */
	public Dimension getSize()
	{
		return size;
	}

	/**
	 * Calculates the position of an atom from the left of this ContentLine. Returns -1 if
	 * the atom is not reached (not within this line). Returns 0 if there is nothing in this
	 * line or the given atom is equal or less than 0.
	 * 
	 * @param atom
	 * @return
	 */
	public int calculatePositionInPixel(int atom)
	{
		int currentPos = 0;
		int currentAtom = 0;
		if (atom <= 0)
			return 0;

		for (AbstractContentPart part : content)
		{
			if (currentAtom + part.getAtomCount() >= atom)
			{
				int newPos = part.getAtomPosition(atom - currentAtom);
				if (newPos == -1)
					return currentPos + part.getSize().getWidth();
				else
					return currentPos + newPos;
			}
			currentAtom += part.getAtomCount();
			currentPos += part.getSize().getWidth();
		}

		return -1;
	}

	public void render(int x, int y, Graphics g, IOpenGL gl)
	{
		int xCurrent = x;
		for (AbstractContentPart part : content)
		{
			part.render(xCurrent, y, g, gl);
			xCurrent += part.getSize().getWidth();
		}
	}

	public int calculatePositionInAtoms(int x)
	{
		int result = 0;
		int xPos = 0;
		for (AbstractContentPart part : content)
		{
			xPos += part.getSize().getWidth();
			if (xPos >= x)
			{
				int pos = part.calculatePositionInAtoms(x - (xPos - part.getSize().getWidth()));
				if (pos < 0)
				{
					result = -1;
				}
				else
				{
					result += pos;
				}
				break;
			}
			else
			{
				result += part.getAtomCount();
			}
		}

		return result;
	}

	/**
	 * Sets the active content part. Returns null if no content is in
	 * this line or the atoms exceeds the atom count of this line.
	 * 
	 * @param atoms
	 * @return
	 */
	public AbstractContentPart setActiveContentPart(int atoms)
	{
		int currentAtoms = 0;

		clearActiveContentPart();

		for (AbstractContentPart part : content)
		{
			if ((currentAtoms + part.getAtomCount()) >= atoms)
			{
				activePart = part;
				activePart.setActiveAtom(atoms - currentAtoms);
				return part;
			}
			currentAtoms += part.getAtomCount();
			activePartX += part.getSize().getWidth();
		}

		return null;
	}

	/**
	 * Removes the selected contentPart
	 */
	public void clearActiveContentPart()
	{
		if (activePart != null)
		{
			activePart.setActiveAtom(-1);
			activePart = null;
		}
		activePartX = 0;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContent#addChar(char)
	 */
	public boolean addChar(char c)
	{
		if (this.hasActivePart())
		{
			this.atomCount++;
			indexCount++;
			boolean result = this.activePart.addChar(c);
			recalculateSize();
			return result;
		}
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContent#addContent(java.lang.String, org.fenggui.binding.render.text.advanced.IContentFactory)
	 */
	public boolean addContent(String content, IContentFactory factory)
	{
		if (this.hasActivePart())
		{
			this.atomCount += content.length();
			indexCount += content.length();
			boolean result = this.activePart.addContent(content);
			recalculateSize();
			return result;
		}
		else
		{
			throw new IllegalStateException("Can't add new content if none is set to active.");
		}
	}

	/**
	 * Removes the next character from the active position. Returns null if there
	 * is no character after the active position.
	 * 
	 * @return
	 */
	public Character removeNextChar()
	{
		if (this.hasActivePart())
		{
			int width = this.activePart.getSize().getWidth();
			Character result = this.activePart.removeNextAtom();
			if (result != null)
			{
				this.atomCount--;
				indexCount--;
				this.size.setWidth(this.size.getWidth() - (width - this.activePart.getSize().getWidth()));
				recalculateHeight();
			}
			else
			{
				int index = this.content.indexOf(this.activePart) + 1;
				if (index < this.content.size())
				{
					AbstractContentPart nextPart = this.content.get(index);
					int width2 = nextPart.getSize().getWidth();
					result = nextPart.removeNextAtom();
					if (result != null)
					{
						this.atomCount--;
						indexCount--;
						this.size.setWidth(this.size.getWidth() - (width2 - nextPart.getSize().getWidth()));

						if (nextPart.isEmpty())
							this.removePart(nextPart);
					}
				}
			}
			return result;
		}
		else
		{
			//if there is no active part here remove from the first part
			if (content.size() >= 1)
			{
				AbstractContentPart nextPart = this.content.get(0);
				int width2 = nextPart.getSize().getWidth();
				Character result = nextPart.removeNextAtom();
				if (result != null)
				{
					this.atomCount--;
					indexCount--;
					this.size.setWidth(this.size.getWidth() - (width2 - nextPart.getSize().getWidth()));

					if (nextPart.isEmpty())
						this.removePart(nextPart);
				}
				return result;
			}
		}
		return null;
	}

	/**
	 * Removes the previous Character from the current position. Returns null if
	 * there is no character before the current position.
	 * 
	 * @return
	 */
	public Character removePreviousChar()
	{
		if (this.hasActivePart())
		{
			int width = this.activePart.getSize().getWidth();
			Character result = this.activePart.removePreviousAtom();
			if (result != null)
			{
				this.size.setWidth(this.size.getWidth() - (width - this.activePart.getSize().getWidth()));
				this.atomCount--;
				indexCount--;
				recalculateHeight();
			}
			else
			{
				int index = this.content.indexOf(this.activePart) - 1;
				if (index >= 0)
				{
					AbstractContentPart previousPart = this.content.get(index);
					int width2 = previousPart.getSize().getWidth();
					result = previousPart.removePreviousAtom();
					if (result != null)
					{
						this.atomCount--;
						indexCount--;
						this.size.setWidth(this.size.getWidth() - (width2 - previousPart.getSize().getWidth()));

						if (previousPart.isEmpty())
							removePart(previousPart);
					}
				}
			}
			return result;
		}
		else
		{
			//if there is no active part here remove from the last part
			if (content.size() >= 1)
			{
				AbstractContentPart previousPart = this.content.get(this.content.size() - 1);
				int width2 = previousPart.getSize().getWidth();
				Character result = previousPart.removePreviousAtom();
				if (result != null)
				{
					this.atomCount--;
					indexCount--;
					this.size.setWidth(this.size.getWidth() - (width2 - previousPart.getSize().getWidth()));

					if (previousPart.isEmpty())
						this.removePart(previousPart);
				}
				return result;
			}
		}
		return null;
	}

	private void removePart(AbstractContentPart part)
	{
		boolean front = false;
		int indexPrev = this.content.indexOf(part) - 1;
		if (indexPrev < 0)
		{
			indexPrev = 0;
			front = true;
		}

		this.content.remove(part);
		if (activePart == part)
		{
			activePart = this.content.get(0);
			activePartX -= activePart.getSize().getWidth();
			if (activePartX < 0)
				activePartX = 0;
			if (!front)
				activePart.setActiveAtom(activePart.getAtomCount());
		}
	}

	/* (non-Javadoc)
	 * @see org.fenggui.binding.render.text.advanced.IContent#getAtomCount()
	 */
	public long getAtomCount()
	{
		return atomCount;
	}

	public Point getActivePosition()
	{
		if (hasActivePart())
		{
			int result = activePart.getActivePosition();
			return new Point(result + activePartX, 0);
		}
		else
		{
			return null;
		}
	}

	public int getContentIndexOfAtom(int atom)
	{
		int index = 0;
		int currentAtom = 0;
		AbstractContentPart currentPart = null;

		for (AbstractContentPart part : content)
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
				index += currentPart.getBeforeLength() + preIndex;
			if (preIndex == currentPart.getAtomCount())
				index += currentPart.getAfterLength();
		}
		return index;
	}

	public long getIndexCount()
	{
		return indexCount;
	}
}
