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
 * Created on 2005-4-13
 * $Id: IDragAndDropListener.java 500 2008-04-15 11:27:21Z marcmenghin $
 */
package org.fenggui.event;

import java.util.Set;

import org.fenggui.IWidget;
import org.fenggui.event.key.Key;

/**
 * 
 * Abstraction for classes that listen to Drag and Drop
 * events. If the user clicks on a Widget all registered
 * <code>IDragAndDropListener</code>s are queried through
 * the <code>isDndWidget</code> whether the selected Widget
 * is a drag and droppable Widget for this listener or not. If yes,
 * the <code>select</code> method is invoked. While the user
 * is draggen, the <code>drag</code> method is constantly
 * invoked so that the listener can reposition the Widget.
 * If the user releases the mouse button, the <code>drop</code>
 * is called telling the listenere where the user dropped the
 * selected item.
 * 
 * @todo Write a more elaborated Tutorial/Example for Dnd #
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2008-04-15 13:27:21 +0200 (Di, 15 Apr 2008) $
 * @version $Revision: 500 $
 *
 */
public interface IDragAndDropListener
{
	public boolean isDndWidget(IWidget w, int displayX, int displayY);

	public void select(int displayX, int displayY, Set<Key> modifiers);

	public void drag(int displayX, int displayY, Set<Key> modifiers);

	public void drop(int displayX, int displayY, IWidget droppedOn, Set<Key> modifiers);
}
