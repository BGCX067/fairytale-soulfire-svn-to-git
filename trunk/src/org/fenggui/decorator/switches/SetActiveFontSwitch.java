/*
 * FengGUI - Java GUIs in OpenGL (http://www.fenggui.org)
 * 
 * Copyright (C) 2005-2008 FengGUI Project
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
 * Created on 09.04.2008
 * $Id$
 */
package org.fenggui.decorator.switches;

import org.fenggui.IWidget;
import org.fenggui.StandardWidget;
import org.fenggui.appearance.IAppearance;
import org.fenggui.appearance.TextAppearance;
import org.fenggui.appearance.TextEditorAppearance;
import org.fenggui.binding.render.IFont;

/**
 * Sets the active font on a widget.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class SetActiveFontSwitch extends Switch
{
	private IFont font;
	
	/**
	 * @param label
	 */
	public SetActiveFontSwitch(String label, IFont font)
	{
		super(label);
		this.font = font;
	}

	/**
	 * @param s
	 */
	public SetActiveFontSwitch(SetActiveFontSwitch s)
	{
		super(s);
		this.font = s.font;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.decorator.switches.Switch#copy()
	 */
	@Override
	public Switch copy()
	{
		return this;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.decorator.switches.Switch#setup(org.fenggui.IWidget)
	 */
	@Override
	public void setup(IWidget widget)
	{
		if (widget instanceof StandardWidget)
		{
			StandardWidget stdWidget = (StandardWidget) widget;
			IAppearance appearance = stdWidget.getAppearance();
			if (appearance instanceof TextAppearance)
			{
				TextAppearance textAppearance = (TextAppearance) appearance;
				textAppearance.getData().setActiveFont(font);
			}
			
			if (appearance instanceof TextEditorAppearance)
			{
				TextEditorAppearance textAppearance = (TextEditorAppearance) appearance;
				textAppearance.getData().setActiveFont(font);
			}
		}
	}
}
