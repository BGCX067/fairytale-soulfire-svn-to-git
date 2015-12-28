/*
 * FengGUI - Java GUIs in OpenGL (http://www.fenggui.org)
 * 
 * Copyright (c) 2005, 2006 FengGUI Project
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
 * $Id: ProgressBar.java 467 2008-03-06 10:32:27Z marcmenghin $
 */
package org.fenggui;

import org.fenggui.appearance.EntryAppearance;
import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.util.Dimension;

/**
 * Horizontal progress bar Widget.
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2008-03-06 11:32:27 +0100 (Do, 06 Mrz 2008) $
 * @version $Revision: 467 $
 */
public class ProgressBar extends StandardWidget
{

	private double value = 0.5;
	private boolean horizontal = true;

	private EntryAppearance appearance = null;

	public ProgressBar(String text)
	{
		appearance = new EntryAppearance(this);
		setText(text);
	}

	public ProgressBar()
	{
		appearance = new EntryAppearance(this);
	}

	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		if (value > 1)
			value = 1;
		if (value < 0)
			value = 0;
		this.value = value;
	}

	public void setHorizontal(boolean horizontal)
	{
		this.horizontal = horizontal;
	}

	public boolean isHorizontal()
	{
		return horizontal;
	}

	public void setText(String text)
	{
		this.appearance.getData().setText(text);
		updateMinSize();
	}

	public TextAppearance getAppearance()
	{
		return appearance;
	}

	@Override
	public Dimension getMinContentSize()
	{
		return new Dimension(appearance.getTextWidth(), appearance.getTextLineHeight());
	}

	@Override
	public void paintContent(Graphics g, IOpenGL gl)
	{
		if (horizontal)
		{
			appearance.getSelectionUnderlay().paint(g, 0, 0, (int) (appearance.getContentWidth() * this.getValue()),
				appearance.getContentHeight());
		}
		else
		{
			appearance.getSelectionUnderlay().paint(g, 0, 0, (int) (appearance.getContentWidth()),
				(int) (appearance.getContentHeight() * this.getValue()));
		}

		Dimension size = appearance.getTextContentSize();
		int x = appearance.getContentWidth() / 2 - size.getWidth() / 2;
		int y = appearance.getContentHeight() / 2 - size.getHeight() / 2;
		appearance.renderText(x, y + size.getHeight(), g, gl);

	}
}
