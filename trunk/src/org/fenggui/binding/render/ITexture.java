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
 * Created on Apr 18, 2005
 * $Id: ITexture.java 499 2008-04-15 10:09:43Z marcmenghin $
 */
package org.fenggui.binding.render;

import org.fenggui.IDisposable;
import org.fenggui.theme.xml.IXMLStreamable;

/**
 * This interface provides the basic needs to deal with a texture in FengGUI. It
 * is an interface because different OpengGL bindings need to implement
 * it. 
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2008-04-15 12:09:43 +0200 (Di, 15 Apr 2008) $
 * @version $Revision: 499 $
 */
public interface ITexture extends IXMLStreamable, IDisposable
{

    public void bind();
    
    public int getTextureWidth();
    public int getTextureHeight();
    
    public int getImageWidth();
    public int getImageHeight();
    
    public boolean hasAlpha();
    
    public int getID();
    
    // -->> never used anywhere
    // Also why is this here anyway? In my opinion is this not the responsibility of this class.
    //public void texSubImage2D(int xOffset, int yOffset, int width, int height, ByteBuffer buffer);
}
