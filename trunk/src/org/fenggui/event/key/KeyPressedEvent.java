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
 * Created on Oct 8, 2006
 * $Id: KeyPressedEvent.java 473 2008-03-12 11:25:07Z marcmenghin $
 */
package org.fenggui.event.key;

import java.util.Set;

import org.fenggui.IWidget;

public class KeyPressedEvent extends KeyEvent
{

	public KeyPressedEvent(IWidget source, char key, Key keyClass, Set<Key> modifiers)
	{
		super(source, key, keyClass, modifiers);
	}

}
