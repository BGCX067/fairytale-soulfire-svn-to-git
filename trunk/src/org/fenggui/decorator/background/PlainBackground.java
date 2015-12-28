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
 * Created on Apr 30, 2005
 * $Id: PlainBackground.java 421 2007-11-29 17:55:01Z marcmenghin $
 */
package org.fenggui.decorator.background;

import java.io.IOException;

import org.fenggui.binding.render.Graphics;
import org.fenggui.decorator.IDecorator;
import org.fenggui.theme.xml.DefaultElementName;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;


/**
 * Single, plain color background.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2007-11-29 18:55:01 +0100 (Do, 29 Nov 2007) $
 * @version $Revision: 421 $
 */
@DefaultElementName("PlainBackground")
public class PlainBackground extends Background
{
	private Color color = Color.GRAY;

	public PlainBackground() 
	{
		this(Color.BLACK_HALF_TRANSPARENT);
	}


	public PlainBackground(Color g)
	{
		color = g;
	}
	
	
	public PlainBackground(InputOnlyStream stream)
	throws IOException, IXMLStreamableException {
		process(stream);
	}


	public Color getColor()
	{
		return color;
	}


	public void setColor(Color background)
	{
		this.color = background;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.io.IOStreamSaveable#process(org.fenggui.io.InputOutputStream)
	 */
	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		super.process(stream);
		
		color = stream.processChild("Color", color, Color.class);
	}


	public void paint(Graphics g, int localX, int localY, int width, int height)
	{
		g.setColor(color);
		//System.out.println(localX+" "+localY+"  "+ width+"  "+height);
		g.drawFilledRectangle(localX, localY, width, height);
	}


	/* (non-Javadoc)
	 * @see org.fenggui.decorator.background.Background#copy()
	 */
	@Override
	public IDecorator copy()
	{
		PlainBackground result = new PlainBackground(this.color);
		this.copy(result);
		return result;
	}
	
}
