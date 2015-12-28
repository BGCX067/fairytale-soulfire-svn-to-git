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
 * Created on Feb 23, 2007
 * $Id: ITextRenderer.java 549 2008-05-27 18:51:11Z marcmenghin $
 */
package org.fenggui.binding.render.text;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IFont;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;
import org.fenggui.util.ICopyable;

/**
 * Interface that abstracts different kinds of text renderers. Text renderes get
 * a string and a font and render the text in <code>render()</code>.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2008-05-27 20:51:11 +0200 (Di, 27 Mai 2008) $
 * @version $Revision: 549 $
 */
public interface ITextRenderer extends IXMLStreamable, ICopyable<ITextRenderer>
{
	public void render(int x, int y, String[] text, Color color, Graphics g, IOpenGL gl);
	public void render(int x, int y, String text, Color color, Graphics g, IOpenGL gl);
	
	public Dimension calculateSize(String[] text);
	public int getLineHeight();
	public int getLineSpacing();
	public int getWidth(String text);
	public int getWidth(char text);
	
	public boolean isValidChar(char c);
	
	public void setFont(IFont font);
	public IFont getFont();
}
