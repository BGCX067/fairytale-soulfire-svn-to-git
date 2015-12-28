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
 * Created on 2005-3-21
 * $Id: LayoutManager.java 569 2008-06-24 10:08:17Z marcmenghin $
 */
package org.fenggui.layout;

import org.fenggui.Container;
import org.fenggui.IWidget;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.util.Dimension;


/**
 * Layout Managers layout the position and size of Widgets. 
 * They are used within Containers
 * to arrange their children Widgets. 
 * They only affect the children
 * that are in the in the container, not the grand children.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2008-06-24 12:08:17 +0200 (Di, 24 Jun 2008) $
 * @version $Revision: 569 $
 * @dedication Millencollin - Mr. Clean
 * 
 */
public abstract class LayoutManager implements IXMLStreamable
{
	public int getValidMinHeight(IWidget w)
	{
		if(w.isShrinkable()) return w.getMinSize().getHeight();
    	else return w.getSize().getHeight();
	}

	public int getValidMinWidth(IWidget w)
	{
    	if(w.isShrinkable()) return w.getMinSize().getWidth();
    	else return w.getSize().getWidth();
	}
	
	public void setValidSize(IWidget widget, int width, int height)
	{
		int height2Set = widget.getSize().getHeight();
		int width2Set = widget.getSize().getWidth();
		
		if (widget.getSize().getHeight() > height)
		{
			if (widget.isShrinkable()) height2Set = height;
		}
		else
		{
			if (widget.isExpandable()) height2Set = height;
		}
		
		if (widget.getSize().getWidth() > width)
		{
			if (widget.isShrinkable()) width2Set = width;
		}
		else
		{
			if (widget.isExpandable()) width2Set = width;
		}

		widget.setSize(new Dimension(width2Set, height2Set));
	}
	
	public void setValidHeight(IWidget widget, int height)
	{
		if (widget.getSize().getHeight() > height)
		{
			if (widget.isShrinkable()) 
				widget.setSize(new Dimension(widget.getSize().getWidth(), height));
		}
		else
		{
			if (widget.isExpandable()) 
				widget.setSize(new Dimension(widget.getSize().getWidth(), height));
		}
	}
	
	public void setValidWidth(IWidget widget, int width)
	{
		if (widget.getSize().getWidth() > width)
		{
			if (widget.isShrinkable()) 
				widget.setSize(new Dimension(width, widget.getSize().getHeight()));
		}
		else
		{
			if (widget.isExpandable())
				widget.setSize(new Dimension(width, widget.getSize().getHeight()));
		}
	}
	
	/**
	 * Layouts the children of the specified Container.
	 * @param container the Container whose children are to be layouted
	 * @param content the list of children Widget
	 */
    public abstract void doLayout(Container container, java.util.List<IWidget> content);

    /**
     * Computes and sets the minimum size of the specified Container.
     * @param container the container whose minimum size is to be updated
     * @param content the list of children Widgets
     */
    public abstract Dimension computeMinSize(Container container, java.util.List<IWidget> content);

	public String getUniqueName() {
		return GENERATE_NAME;
	}
}
