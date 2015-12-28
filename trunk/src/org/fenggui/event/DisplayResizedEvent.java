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
 * Created on 2005-4-13
 * $Id: DisplayResizedEvent.java 473 2008-03-12 11:25:07Z marcmenghin $
 */
package org.fenggui.event;

/**
 * 
 * Class that represents the resize event of <code>Display</code>. It is used
 * by <code>Binding</code> to notify t
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2008-03-12 12:25:07 +0100 (Mi, 12 Mrz 2008) $
 * @version $Revision: 473 $
 */
public class DisplayResizedEvent extends Event
{
	int width, height;

	public DisplayResizedEvent(int width, int height)
	{
		super();
		this.width = width;
		this.height = height;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
}
