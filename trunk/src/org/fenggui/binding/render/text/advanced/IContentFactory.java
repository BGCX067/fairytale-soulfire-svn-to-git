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
 * Created on 22.11.2007
 * $Id$
 */
package org.fenggui.binding.render.text.advanced;

import java.util.List;

import org.fenggui.binding.render.text.ITextRenderer;

/**
 * Factory which is responsible to map from the content to a displayable structure
 * using ContentParts. It is used by the AdvancedTextRenderer system. 
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface IContentFactory {
	// Render stuff
	public static final String	TEXTRENDERER_DEFAULT	= "<TextRenderer_Default>";

	/**
	 * Adds a renderer to the factory which can be used to display parts of the content.
	 * 
	 * @param type
	 * @param renderer
	 */
	public void addRenderer(String type, ITextRenderer renderer);

	/**
	 * Returns the renderer registered with that type or null.
	 * 
	 * @param type
	 * @return
	 */
	public ITextRenderer getRenderer(String type);

/**
 * Transforms a String of characters (the content) to a ContentLine containing
 * ContentParts that represent the String.
 * 
 * @param text
 * @param pwdfield
 * @return
 */
	public ContentLine getParts(String text, boolean pwdfield);

	/**
	 * Returns an empty Line.
	 * 
	 * @param pwdfield
	 * @return
	 */
	public AbstractContentPart getEmptyLine(boolean pwdfield);

/**
 * Used by Paste to insert parts into the content. Very similar to the getParts(..)
 * method but doesn't create a ContentLine.
 * 
 * @param text
 * @param pwdfield
 * @return
 */
	public List<AbstractContentPart> stringToContentParts(String text, boolean pwdfield);

	/**
	 * Transforms ContentParts to there String representation.
	 * 
	 * @param list
	 * @param pwdfield
	 * @return
	 */
	public String contentPartsToString(List<AbstractContentPart> list, boolean pwdfield);
	
	/**
	 * This method is used to transform special objects like images into the representing
	 * content string.
	 * 
	 * @param obj
	 * @return
	 */
	public String getContentObject(Object obj);
	
	/**
	 * Sets the active style using one from an other part.
	 * 
	 * @param activePart
	 */
	public void setActiveStyle(AbstractContentPart activePart);

	/**
	 * Sets a new active style to be used.
	 * 
	 * @param newStyle
	 */
	public void setActiveStyle(TextStyle newStyle);
	
	/**
	 * Returns the active style.
	 * 
	 * @return
	 */
	public TextStyle getActiveStyle();
	
	/**
	 * Will transform the given part to the active style.
	 * 
	 * @param part
	 */
	public void transformStyle(AbstractContentPart part);
}
