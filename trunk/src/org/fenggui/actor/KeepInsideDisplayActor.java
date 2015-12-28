/**
 * 
 */
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
 * Created on 20.11.2007
 * $Id$
 */
package org.fenggui.actor;

import org.fenggui.Display;
import org.fenggui.IWidget;
import org.fenggui.composite.Window;
import org.fenggui.event.DisplayResizedEvent;
import org.fenggui.event.IDisplayResizedListener;
import org.fenggui.util.Dimension;

/**
 * Positions windows which are outside the display new to the border of the display.
 * A window can be moved half outside the display except the top. It can't be moved outside
 * the top because windows can only be moved on the title. If that would be outside the
 * Display a user wouldn't be able to move that window again. 
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class KeepInsideDisplayActor implements IActor
{

	private Display display = null;
	private IDisplayResizedListener listener = null;

	public KeepInsideDisplayActor(final Display display)
	{
		this.display = display;
		this.listener = new IDisplayResizedListener()
		{

			public void displayResized(DisplayResizedEvent displayResizedEvent)
			{
				for (IWidget widget : display.getWidgets())
				{
					if (widget instanceof Window)
					{
						Dimension size = widget.getSize();
						int x = widget.getX();
						int y = widget.getY();

						if (x < (-3 * size.getWidth() / 4))
						{
							widget.setX((-3 * size.getWidth() / 4));
						}

						if (x + (3 * size.getWidth() / 4) > display.getSize().getWidth())
						{
							widget.setX(display.getSize().getWidth() - (3 * size.getWidth() / 4));
						}

						if (y < (-3 * size.getHeight() / 4))
						{
							widget.setY((-3 * size.getHeight() / 4));
						}

						if (y + (size.getHeight()) > display.getSize().getHeight())
						{
							widget.setY(display.getSize().getHeight() - (size.getHeight()));
						}
					}
				}
			}

		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.actor.IActor#hook(org.fenggui.IWidget)
	 */
	public void hook(IWidget widget)
	{
		display.getBinding().addDisplayResizedListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.actor.IActor#unHook()
	 */
	public void unHook(IWidget widget)
	{
		display.getBinding().removeDisplayResizedListener(listener);
	}

}
