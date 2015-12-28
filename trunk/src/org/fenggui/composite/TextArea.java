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
 * $Id: TextArea.java 426 2007-12-20 14:47:28Z marcmenghin $
 */

package org.fenggui.composite;

import org.fenggui.ITextWidget;
import org.fenggui.ScrollContainer;
import org.fenggui.TextEditor;
import org.fenggui.event.ITextChangedListener;
import org.fenggui.event.TextChangedEvent;

/**
 * Implementation of an autoscroll text editor.
 * 
 * 
 * @author Boris Beaulant, last edited by $Author: marcmenghin $, $Date: 2007-12-20 15:47:28 +0100 (Do, 20 Dez 2007) $
 * @version $Revision: 426 $
 */
public class TextArea extends ScrollContainer implements ITextWidget
{

	private TextEditor textEditor;

	/**
	 * ScrollTextEditor constructor
	 */
	public TextArea()
	{
		textEditor = new TextEditor();
		textEditor.addTextChangedListener(new ITextChangedListener()
		{
			public void textChanged(TextChangedEvent textChangedEvent)
			{
				layout();
			}
		});
		setInnerWidget(textEditor);
		setSize(10, 10);
	}

	/**
	 * Returns the enclosed TextEditor
	 * @return textEditor the enclosed TextEditor
	 */
	public TextEditor getTextEditor()
	{
		return textEditor;
	}

	/**
	 * @return the text editor's text
	 */
	public String getText()
	{
		return textEditor.getText();
	}

	/**
	 * Define the textEditor's text
	 * 
	 * @param text
	 *            Text to set
	 */
	public void setText(String text)
	{
		textEditor.setText(text);
	}
}
