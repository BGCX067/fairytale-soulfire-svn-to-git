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
 * Created on 2005-4-12
 * $Id: ButtonPressedEvent.java 535 2008-05-15 08:00:37Z marcmenghin $
 */
package org.fenggui.event;

import java.util.Set;

import org.fenggui.Button;
import org.fenggui.event.key.Key;

/**
 * 
 * Event type for button pressed events.
 * 
 * @author Johannes Schaback ($Author: marcmenghin $)
 * 
 */
public class ButtonPressedEvent extends WidgetEvent
{
	private Button button;
	private Set<Key> modifiers;

	/**
	 */
	public ButtonPressedEvent(Button trigger, Set<Key> modifiers)
	{
		super(trigger);
		button = trigger;
		this.modifiers = modifiers;
	}

	public Button getTrigger()
	{
		return button;
	}

	public boolean isPressed(Key key)
	{
		return modifiers.contains(key);
	}

	public Set<Key> getModifiers()
	{
		return modifiers;
	}
}
