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
 * $Id: IViewPortPaintListener.java 362 2007-10-08 09:46:42Z marcmenghin $
 */
package org.fenggui.event;

import org.fenggui.binding.render.Graphics;

/**
 * 
 * 
 * @todo Comment this class... #
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2007-10-08 11:46:42 +0200 (Mo, 08 Okt 2007) $
 * @version $Revision: 362 $
 */
public interface IViewPortPaintListener {

	public void paint(Graphics g, int viewPortWidth, int viewPortHeight);
	
}
