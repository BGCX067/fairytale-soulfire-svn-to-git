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
 * Created on Mar 10, 2007
 * $Id: LineCarretRenderer.java 473 2008-03-12 11:25:07Z marcmenghin $
 */
package org.fenggui.composite.console;

import org.fenggui.binding.render.Graphics;

public class LineCarretRenderer implements ICarretRenderer
{
	private int height = 10;
		
	public LineCarretRenderer(int height)
	{
		this.height = height;
	}
	
	public void render(int x, int y, Graphics g)
	{
    	//final int localX = x + g.getTranslation().getX();
    	//final int localY = y + g.getTranslation().getY();
		//g.drawLine(localX, localY, localX, localY + height);
		g.drawLine(x, y, x, y + height);
	}

}
