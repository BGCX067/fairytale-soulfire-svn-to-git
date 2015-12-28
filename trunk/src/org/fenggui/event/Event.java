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
 * $Id: Event.java 486 2008-03-27 10:50:32Z marcmenghin $
 */
package org.fenggui.event;

/**
 * Base class for all events.
 * 
 * @author marcmenghin, last edited by $Author: marcmenghin $, $Date: 2008-03-27 11:50:32 +0100 (Do, 27 Mrz 2008) $
 * @version $Revision: 486 $
 */
public abstract class Event
{

	private boolean alreadyUsed = false;

	public Event()
	{
	}

	/**
	 * @return Returns the alreadyUsed.
	 */
	public boolean isAlreadyUsed()
	{
		return alreadyUsed;
	}

	/**
	 * @param alreadyUsed The alreadyUsed to set.
	 */
	public void setUsed()
	{
		this.alreadyUsed = true;
	}

}