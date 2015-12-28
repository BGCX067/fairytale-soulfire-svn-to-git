/*
 * FengGUI - Java GUIs in OpenGL (http://fenggui.sf.net)
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
 * Created on 2 oct. 06
 * $Id: TextStyle.java 426 2007-12-20 14:47:28Z marcmenghin $
 */

package org.fenggui.composite.text;

import org.fenggui.binding.render.ImageFont;
import org.fenggui.util.Color;

/**
 * Encapulates a Font and a Color
 * 
 * @author Boris Beaulant, last edited by $Author: marcmenghin $, $Date: 2007-12-20 15:47:28 +0100 (Do, 20 Dez 2007) $
 * @version $Revision: 426 $
 */
public class TextStyle
{
	private ImageFont font;
	private Color color;


	/**
	 * TextStyle constructor
	 *
	 * @param font
	 * @param color
	 */
	public TextStyle(ImageFont font, Color color)
	{
		super();
		this.font = font;
		this.color = color;
	}


	/**
	 * @return color
	 */
	public Color getColor()
	{
		return color;
	}


	/**
	 * @param color to set
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}


	/**
	 * @return font
	 */
	public ImageFont getFont()
	{
		return font;
	}


	/**
	 * @param font to set
	 */
	public void setFont(ImageFont font)
	{
		this.font = font;
	}


}
