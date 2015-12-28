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

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.text.TextUtil;
import org.fenggui.util.Dimension;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class TextPart extends AbstractContentPart
{
	private String text;
	private String displayText;
	private int activeAtom = -1;
	private boolean hideContent;
	private char character;

	/**
	 * This constructor is used for every content that is not plaintext. A selection
	 * needs to be mapped into the content therefore the indexStart and indexAddedLength are used.
	 * @param text
	 * @param style
	 * @param hideContent
	 * @param character
	 * @param indexStart
	 * @param indexAddedLength
	 */
	public TextPart(String text, TextStyle style, boolean hideContent, char character, int beforeLength, int afterLength)
	{
		super(beforeLength, afterLength, style);
		this.text = text;
		this.hideContent = hideContent;
		this.character = character;
		updateSize();
	}

	/**
	 * This constructor is used to create a plaintext textpart. The index used
	 * will be just like using the String index for the given text.
	 * 
	 * @param text
	 * @param style
	 * @param hideContent
	 * @param character
	 */
	public TextPart(String text, TextStyle style, boolean hideContent, char character)
	{
		this(text, style, hideContent, character, 0, 0);
	}

	public String getContent()
	{
		return text;
	}

	private void updateSize()
	{
		updateDisplayText();
		Dimension size = new Dimension(0, 0);
		if (displayText.length() > 0)
		{
			size.setSize(getStyle().current.renderer.getWidth(displayText), getStyle().current.renderer.getLineHeight());
		}
		else
		{
			size.setSize(0, getStyle().current.renderer.getLineHeight());
		}
		this.setSize(size);
	}

	private void updateDisplayText()
	{
		if (hideContent)
		{
			StringBuffer tmpStr = new StringBuffer(text.length());
			for (int i = 0; i < text.length(); i++)
			{
				tmpStr.append(character);
			}

			displayText = tmpStr.toString();
		}
		else
		{
			displayText = text;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#splitAt(int, boolean)
	 */
	@Override
	public TextPart splitAtWord(int width, boolean firstPart)
	{
		String part1, part2;

		int h = TextUtil.findLineEnd(text, getStyle().current.renderer, width);
		if (h < 0 || h > text.length())
		{
			TextPart newPart = new TextPart("", getStyle(), hideContent, character, getBeforeLength(), getAfterLength());
			newPart.setSelected(this.isSelected());
			return newPart;
		}
		else
		{
			if (h + 1 < text.length() && text.charAt(h) == ' ')
			{
				int curWidth = getStyle().current.renderer.getWidth(text.substring(0, h + 1));
				if (width >= curWidth)
					h++;
			}
			part1 = text.substring(0, h);
			part2 = text.substring(h, text.length());

			if (firstPart && getStyle().current.renderer.getWidth(part1) > width)
			{
				//if width smaller then first word split on char level, don' care about words
				h = TextUtil.findLineEndChar(text, getStyle().current.renderer, width);
				if (h <= 0)
					h = 1; //take at least one character

				part1 = text.substring(0, h);
				part2 = text.substring(h, text.length());
			}

			text = part1;
			updateSize();
			TextPart newPart = new TextPart(part2, getStyle(), hideContent, character, getBeforeLength(), getAfterLength());
			newPart.setSelected(this.isSelected());
			if (activeAtom > text.length())
			{
				newPart.activeAtom = activeAtom - text.length();
				activeAtom = -1;
			}
			return newPart;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#isBreakable()
	 */
	@Override
	public boolean isBreakable()
	{
		//		return (text.indexOf(' ') >= 0);
		return text.length() > 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#mergePart(org.fenggui.binding.render.text.advanced.AbstractContentPart)
	 */
	@Override
	public void mergePart(AbstractContentPart part)
	{
		if (canMerge(part))
		{
			TextPart tPart = (TextPart) part;
			if (tPart.activeAtom != -1)
			{
				this.activeAtom = text.length() + tPart.activeAtom;
			}
			this.text += tPart.text;
			updateSize();
		}
		else
		{
			throw new IllegalArgumentException("AbstractContentPart not compatible with this TextPart.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#canMerge(org.fenggui.binding.render.text.advanced.AbstractContentPart)
	 */
	@Override
	public boolean canMerge(AbstractContentPart part)
	{
		if (part instanceof TextPart)
		{
			if (this.getStyle().current.renderer == ((TextPart) part).getStyle().current.renderer
					&& this.getStyle().current.color.equals(((TextPart) part).getStyle().current.color)
					&& this.hideContent == ((TextPart) part).hideContent && this.isSelected() == ((TextPart) part).isSelected())
			{
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.advanced.AbstractContentPart#render(int, int,
	 *      org.fenggui.binding.render.Graphics, org.fenggui.binding.render.IOpenGL)
	 */
	@Override
	public void render(int x, int y, Graphics g, IOpenGL gl)
	{
		if (isSelected())
		{
			if (getStyle().selectedStyle.background != null)
			{
				getStyle().selectedStyle.background.paint(g, x, y - getSize().getHeight(), getSize().getWidth(), getSize()
						.getHeight());
			}
			getStyle().selectedStyle.renderer.render(x, y, displayText, getStyle().selectedStyle.color, g, gl);
		}
		else
		{
			if (getStyle().current.background != null)
			{
				getStyle().current.background.paint(g, x, y - getSize().getHeight(), getSize().getWidth(), getSize()
						.getHeight());
			}
			getStyle().current.renderer.render(x, y, displayText, getStyle().current.color, g, gl);
		}
	}

	@Override
	public int getAtomCount()
	{
		return text.length();
	}

	@Override
	public int calculatePositionInAtoms(int x)
	{
		int maxCharWidth = getStyle().current.renderer.getWidth('M');
		int curChar = (x / maxCharWidth) - 1;
		if (curChar >= displayText.length())
		{
			return -1;
		}
		else
		{
			if (curChar < 0)
				curChar = 0;
		}

		int length = getStyle().current.renderer.getWidth(displayText.substring(0, curChar));
		while (length < x)
		{
			curChar++;
			if (curChar < displayText.length())
			{
				length = getStyle().current.renderer.getWidth(displayText.substring(0, curChar));
			}
			else
			{
				curChar = displayText.length();
				break;
			}
		}

		curChar--;
		if (curChar < 0)
			curChar = 0;

		//move char one up if over half of char
		int charWidth = 0;
		if (displayText.length() > 0)
			charWidth = getStyle().current.renderer.getWidth(displayText.charAt(curChar));
		length = getStyle().current.renderer.getWidth(displayText.substring(0, curChar));
		if ((x - length) > charWidth / 2)
			curChar++;

		return curChar;
	}

	@Override
	public int getAtomPosition(int atom)
	{
		if (atom < 0 || atom > displayText.length())
		{
			return -1;
		}

		return getStyle().current.renderer.getWidth(displayText.substring(0, atom));
	}

	@Override
	public boolean addChar(char c)
	{
		if (activeAtom >= 0)
		{
			text = text.substring(0, activeAtom) + c + text.substring(activeAtom);
			updateSize();
			this.activeAtom++;

			return true;
		}
		else
			return false;
	}

	@Override
	public boolean addContent(String content)
	{
		if (activeAtom >= 0)
		{
			text = text.substring(0, activeAtom) + content + text.substring(activeAtom);
			this.activeAtom += content.length();
			updateSize();
			return true;
		}
		else
			return false;
	}

	@Override
	public Character removeNextAtom()
	{
		int activeAtom = this.activeAtom;
		if (activeAtom <= 0)
		{
			activeAtom = 0;
			this.activeAtom = 0;
		}

		if (activeAtom < text.length())
		{
			char removableChar = text.charAt(activeAtom);
			String textBefore = text.substring(0, activeAtom);
			String textAfter = "";
			if (activeAtom + 1 < text.length())
				textAfter = text.substring(activeAtom + 1, text.length());

			text = textBefore + textAfter;
			updateSize();
			return removableChar;
		}
		return null;
	}

	@Override
	public Character removePreviousAtom()
	{
		int activeAtom = this.activeAtom;
		if (activeAtom < 0)
		{
			activeAtom = text.length();
			this.activeAtom = activeAtom;
		}

		if (activeAtom >= 1)
		{
			char removableChar = text.charAt(activeAtom - 1);
			String textBefore = text.substring(0, activeAtom - 1);
			String textAfter = text.substring(activeAtom, text.length());
			text = textBefore + textAfter;
			updateSize();
			this.activeAtom--;
			return removableChar;
		}
		return null;
	}

	@Override
	public void setActiveAtom(int atom)
	{
		activeAtom = atom;
	}

	@Override
	public int getActivePosition()
	{
		return getAtomPosition(activeAtom);
	}

	@Override
	public boolean hasActiveAtom()
	{
		return activeAtom >= 0;
	}

	@Override
	public boolean isEmpty()
	{
		if (text == null || text.length() <= 0)
		{
			return true;
		}
		return false;
	}

	@Override
	public AbstractContentPart splitAtChar(int width)
	{
		int h = TextUtil.findLineEndChar(text, getStyle().current.renderer, width);
		return splitAtAtom(h);
	}

	@Override
	public AbstractContentPart splitAtAtom(int atom)
	{
		String part1, part2;

		part1 = text.substring(0, atom);
		part2 = text.substring(atom, text.length());

		text = part1;
		updateSize();

		TextPart newPart = new TextPart(part2, getStyle(), hideContent, character, getBeforeLength(), getAfterLength());
		newPart.setSelected(this.isSelected());
		if (activeAtom > text.length())
		{
			newPart.activeAtom = activeAtom - text.length();
			activeAtom = -1;
		}
		return newPart;
	}
}
