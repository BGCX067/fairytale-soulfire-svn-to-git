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
 * Created on 30.11.2007
 * $Id$
 */
package org.fenggui.binding.render.text.advanced;

import org.fenggui.appearance.TextEditorAppearance;
import org.fenggui.binding.render.text.IComplexTextRenderer;
import org.fenggui.binding.render.text.IComplexTextRendererData;

/**
 * TextRenderer used for more advanced widgets like the TextEditor. Supports things like
 * selection, text cursor, multi-styled text and much more.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface IAdvancedTextRenderer extends IComplexTextRenderer
{

	/**
	 * Checks if this is a valid character. A valid character is one where a representation
	 * of that character can be drawn.
	 * 
	 * @param data
	 * @param c
	 * @return
	 */
	public boolean isValidChar(IComplexTextRendererData data, char c);

	public IAdvancedTextRendererData createData(IAdvancedTextRendererData data, TextEditorAppearance appearance);
}
