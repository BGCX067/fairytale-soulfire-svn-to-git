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
 * $Id: SpacingAppearance.java 569 2008-06-24 10:08:17Z marcmenghin $
 */
package org.fenggui.appearance;

import java.io.IOException;

import org.fenggui.StandardWidget;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;
import org.fenggui.util.Spacing;

/**
 * Holds appearance properties. 
 * 
 * * The following ASCII-Art is supposed to illustrate the
 * margins.
 * <pre>
 * +---------------------------------------------------------------------------------------------------+
 * |                                                |                                                  |
 * |                                          Outer Margin Top                                         |
 * |                                                |                                                  |
 * |            +-------------------------------------------------------------------------+            |
 * |            |                                   |                                     |            |
 * |            |                              Border Top                                 |            |
 * |            |                                   |                                     |            |
 * |            |            +-----------------------------------------------+            |            |
 * |            |            |                      |                        |            |            |
 * |            |            |                 Padding Top                   |            |            |
 * |            |            |                      |                        |            |            |
 * |            |            |             +-------------------+             |            |            |
 * |   Outer    |            |             |        |          |             |            |   Outer    |
 * |<- Margin ->|<- Border ->|<- Padding ->|<-  Inner Width  ->|<- Padding ->|<- Border ->|<- Margin ->|
 * |    Left    |    Left    |     Left    |        |          |    Right    |    Right   |   Right    |
 * |            |            |             +-------------------+             |            |            |
 * |            |            |                      |                        |            |            |
 * |            |            |                Padding Bottom                 |            |            |
 * |            |            |                      |                        |            |            |
 * |            |            +-----------------------------------------------+            |            |
 * |            |                                   |                                     |            |
 * |            |                             Border Bottom                               |            |
 * |            |                                   |                                     |            |
 * |            +-------------------------------------------------------------------------+            |
 * |                                                |                                                  |
 * |                                     Outer Margin Bottom                                           |
 * |                                                |                                                  |
 * +---------------------------------------------------------------------------------------------------+
 * </pre>
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2008-06-24 12:08:17 +0200 (Di, 24 Jun 2008) $
 * @version $Revision: 569 $
 * @dedication Lagwagon - Stokin' the Neighbors
 */
public abstract class SpacingAppearance implements IAppearance, IXMLStreamable
{
	private Spacing margin = Spacing.ZERO_SPACING;
	private Spacing border = Spacing.ZERO_SPACING;
	private Spacing padding = Spacing.ZERO_SPACING;
	private StandardWidget widget = null;

	public SpacingAppearance(StandardWidget w)
	{
		this.widget = w;
	}

	/**
	 * Copy constructor
	 * 
	 * @param w
	 * @param appearance
	 */
	public SpacingAppearance(StandardWidget w, SpacingAppearance appearance)
	{
		this(w);
		widget = w;
		this.margin = new Spacing(appearance.margin);
		this.border = new Spacing(appearance.border);
		this.padding = new Spacing(appearance.padding);
	}

	/**
	 * Returns the space reserved for borders.
	 * @return the border spacing
	 */
	public Spacing getBorder()
	{
		return border;
	}

	public StandardWidget getWidget()
	{
		return widget;
	}

	/**
	 * Reserves space for the border. Do not confuse with addBorder().
	 * @param border the spacing which says how much space to reserve
	 */
	public void setBorder(Spacing border)
	{
		if (border == null)
			border = Spacing.ZERO_SPACING;

		this.border = border;
		getWidget().updateMinSize();
	}

	public Spacing getMargin()
	{
		return margin;
	}

	public void setMargin(Spacing margin)
	{
		if (margin == null)
			margin = Spacing.ZERO_SPACING;

		this.margin = margin;
		getWidget().updateMinSize();
	}

	public Spacing getPadding()
	{
		return padding;
	}

	public void setPadding(Spacing padding)
	{
		if (padding == null)
			padding = Spacing.ZERO_SPACING;

		this.padding = padding;
		getWidget().updateMinSize();
	}

	public void clearSpacings()
	{
		clearSpacings(Spacing.ZERO_SPACING);
	}

	public void clearSpacings(Spacing clearSpacing)
	{
		this.margin = clearSpacing;
		this.border = clearSpacing;
		this.padding = clearSpacing;
	}

	public final Dimension getMinSizeHint()
	{
		Dimension contentSize = this.getWidget().getMinContentSize();

		if (contentSize == null)
			return new Dimension(10, 10);

		contentSize.setSize(contentSize.getWidth() + border.getLeftPlusRight() + margin.getLeftPlusRight()	+ padding.getLeftPlusRight(),
				contentSize.getHeight() + border.getBottomPlusTop() + margin.getBottomPlusTop() + padding.getBottomPlusTop());
		
		return contentSize; 
	}

	/**
	 * Translates the coordiante system to the lower left corner of the
	 * content area. It calls <code>paintBackgroundContent</code> to actually
	 * draw the content.
	 */
	public abstract void paintBackground(Graphics g, IOpenGL gl);

	/**
	 * Translates the coordiante system to the lower left corner of the
	 * content area. It calls <code>paintForeground</code> to actually
	 * draw the content.
	 */
	public abstract void paintForeground(Graphics g, IOpenGL gl);

	/* (non-Javadoc)
	 * @see org.fenggui.appearance.IAppearance#paint(org.fenggui.binding.render.Graphics, org.fenggui.binding.render.IOpenGL)
	 */
	public void paint(Graphics g, IOpenGL gl)
	{
		int offsetX = margin.getLeft() + border.getLeft() + padding.getLeft();
		int offsetY = margin.getBottom() + border.getBottom() + padding.getBottom();

		paintBackground(g, gl);

		g.translate(offsetX, offsetY);

		widget.paintContent(g, gl);

		g.translate(-offsetX, -offsetY);

		paintForeground(g, gl);
	}

	/**
	 * Return how much height in pixels is available to draw the content.
	 * @return available space for content
	 */
	public int getContentWidth()
	{
		return widget.getSize().getWidth() - border.getLeftPlusRight() - margin.getLeftPlusRight()
				- padding.getLeftPlusRight();
	}

	/**
	 * Return how much width in pixels is available to draw the content.
	 * @return available space for content
	 */
	public int getContentHeight()
	{
		return widget.getSize().getHeight() - border.getBottomPlusTop() - margin.getBottomPlusTop()
				- padding.getBottomPlusTop();
	}

	/**
	 * Returns if the specified point lays inside the box
	 * without margin.
	
	 * @param pX The x-coordinate of the position to test.
	 * @param pY The y-coordinate of the position to test.
	 * @return Whether or not the point was inside the box.
	 */
	public final boolean insideMargin(int pX, int pY)
	{
		// translating pX, pY to the inner Box
		pX -= margin.getLeft();
		pY -= margin.getBottom();

		int innerWidth = getContentWidth();
		int innerHeight = getContentHeight();

		return pX >= 0
				&& pX < innerWidth + padding.getLeft() + padding.getRight() + getBorder().getLeft() + getBorder().getRight()
				&& pY >= 0
				&& pY < innerHeight + padding.getBottom() + padding.getTop() + getBorder().getTop() + getBorder().getBottom();
	}

	/**
	 * Returns the sum of bottom margin, bottom border and the
	 * bottom padding.
	 * @return sum
	 */
	public int getBottomMargins()
	{
		return margin.getBottom() + getBorder().getBottom() + padding.getBottom();
	}

	/**
	 * Returns the sum of the top margin, top border and top padding.
	 * @return sum
	 */
	public int getTopMargins()
	{
		return margin.getTop() + getBorder().getTop() + padding.getTop();
	}

	/**
	 * Returns the sum of the left margin, left border and left 
	 * padding.
	 * @return sum
	 */
	public int getLeftMargins()
	{
		return margin.getLeft() + getBorder().getLeft() + padding.getLeft();
	}

	/**
	 * Returns the sum of the right margin, right border and
	 * right padding.
	 * @return sum
	 */
	public int getRightMargins()
	{
		return margin.getRight() + getBorder().getRight() + padding.getRight();
	}

	public int getContentMinWidth()
	{
		return getWidget().getMinSize().getWidth() - getLeftMargins() - getRightMargins();
	}

	public int getContentMinHeight()
	{
		return getWidget().getMinSize().getWidth() - getTopMargins() - getBottomMargins();
	}

	@Override
	public String toString()
	{
		String s = "";
		s += "content size         : " + getContentWidth() + ", " + getContentHeight() + "\n";
		s += "content min size hint: " + getWidget().getMinContentSize() + "\n";
		s += "padding              : " + getPadding() + "\n";
		s += "border               : " + getBorder() + "\n";
		s += "margin               : " + getMargin();
		return s;
	}

	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		this.border = stream.processChild("Border", border, Spacing.ZERO_SPACING, Spacing.class);
		this.margin = stream.processChild("Margin", margin, Spacing.ZERO_SPACING, Spacing.class);
		this.padding = stream.processChild("Padding", padding, Spacing.ZERO_SPACING, Spacing.class);
	}

	public String getUniqueName()
	{
		return GENERATE_NAME;
	}
}
