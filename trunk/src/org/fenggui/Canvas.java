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
 * $Id: Canvas.java 397 2007-11-19 13:53:14Z marcmenghin $
 */
package org.fenggui;

import org.fenggui.appearance.DecoratorAppearance;
import org.fenggui.appearance.DefaultAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.util.Dimension;


/**
 * Widget for customized drawing.
 * By giving an implementation of 
 * <code>IPaintListener</code> to this class, the programmer
 * can define his own drawing routine for a Widget. Note
 * that this Widget is not state-enabled. 
 * The Canvas poses an
 * alternative to subclassing <code>Widget</code> if the programmer
 * just needs a quick way to draw some stuff in a Widget.
 * 
 * 
 * @todo Comment this class... #
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2007-11-19 14:53:14 +0100 (Mo, 19 Nov 2007) $
 * @version $Revision: 397 $
 */
public class Canvas extends StandardWidget {

	private DecoratorAppearance appearance = null;
	
	public Canvas()
	{
		appearance = new DefaultAppearance(this);
	}

	public DecoratorAppearance getAppearance()
	{
		return appearance;
	}

	public void setAppearance(DecoratorAppearance appearance)
	{
		this.appearance = appearance;
	}

	@Override
	public void paintContent(Graphics g, IOpenGL gl)
	{
	}

	@Override
	public Dimension getMinContentSize()
	{
		return new Dimension(0,0);
	}
}
