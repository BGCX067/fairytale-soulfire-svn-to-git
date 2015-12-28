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
 * $Id: Key.java 565 2008-06-06 08:34:07Z marcmenghin $
 */
package org.fenggui.event.key;

/**
 * Enumeration class that holds key types. 
 * Non-alpha-numerical keys all have their own class.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2008-06-06 10:34:07 +0200 (Fr, 06 Jun 2008) $
 * @version $Revision: 565 $
 *
 */
public enum Key {

	UNDEFINED, LETTER, DIGIT, ESCAPE,
	LEFT, RIGHT, UP, DOWN, SHIFT, CTRL, 
	ALT, ALT_GRAPH, ENTER, INSERT, DELETE, HOME,
	END, PAGE_UP, PAGE_DOWN, BACKSPACE, TAB,
	COPY, CUT, PASTE, UNDO, REDO,
	F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12,
	META
	
	
	
}
