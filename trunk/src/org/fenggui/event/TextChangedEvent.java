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
 * $Id: TextChangedEvent.java 473 2008-03-12 11:25:07Z marcmenghin $
 */

package org.fenggui.event;

import org.fenggui.ITextWidget;
import org.fenggui.IWidget;

/**
 * Event type for text editor's text change events.
 *
 * @author Boris Beaulant, last edited by $Author: marcmenghin $, $Date: 2008-03-12 12:25:07 +0100 (Mi, 12 Mrz 2008) $
 * @version $Revision: 473 $
 */
public class TextChangedEvent extends WidgetEvent
{
	private ITextWidget trigger;
	private String text;
	
	/**
	 * TextChanged constructor
	 */
	public TextChangedEvent(ITextWidget trigger, String text) 
	{
		super((IWidget)trigger);
		this.trigger = trigger;
		this.text = text;
	}

	/**
	 * @return trigger
	 */
	public ITextWidget getTrigger() {
		return trigger;
	}


	/**
	 * @return text of change
	**/
	public String getText() {
		return text;
	}

}
