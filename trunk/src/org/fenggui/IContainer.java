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
 * Created on Nov 9, 2006
 * $Id: IContainer.java 520 2008-05-05 15:10:32Z marcmenghin $
 */
package org.fenggui;

public interface IContainer extends IBasicContainer
{
	public void addWidget(IWidget w);
	public void addWidget(IWidget w, int position);
	public void removeWidget(IWidget c);
	
	/**
	 * Returns the children of this container.
	 * @return the children Widgets
	 */
	public java.util.List<IWidget> getContent();
}
