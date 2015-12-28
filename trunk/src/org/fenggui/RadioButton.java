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
 * $Id: RadioButton.java 569 2008-06-24 10:08:17Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;
import java.util.ArrayList;

import org.fenggui.event.ISelectionChangedListener;
import org.fenggui.event.SelectionChangedEvent;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;

/**
 * Implementation of a radio button which usually represents a single option out of a set
 * of options where only one option can be selected.
 * 
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2008-01-21
 *         14:05:20 +0100 (Mo, 21 Jan 2008) $
 * @version $Revision: 569 $
 */
public class RadioButton<E> extends ObservableLabelWidget implements IToggable<E>
{
	private ToggableGroup<E> radioButtonGroup = null;
	private E value = null;
	private boolean selected = false;
	private ArrayList<ISelectionChangedListener> selectionChangedHook = new ArrayList<ISelectionChangedListener>();

	public static final String STATE_SELECTED = "selected";
	public static final String STATE_DESELECTED = "deselected";

	public RadioButton(String text, ToggableGroup<E> group, E data)
	{
		super();
		setRadioButtonGroup(group);
		setValue(data);

		getAppearance().setEnabled(STATE_SELECTED, false);
		getAppearance().setEnabled(STATE_DESELECTED, true);
		setText(text);
		setTraversable(true);
		updateMinSize();
	}

	public RadioButton(RadioButton<?> button)
	{
		super(button);
		selected = button.selected;
		
		setRadioButtonGroup(null);
		setValue(null);

		getAppearance().setEnabled(STATE_SELECTED, false);
		getAppearance().setEnabled(STATE_DESELECTED, true);
		setTraversable(true);
		updateMinSize();
	}
	
	public RadioButton(InputOnlyStream stream) throws IOException, IXMLStreamableException
	{
		process(stream);

		getAppearance().setEnabled(STATE_SELECTED, false);
		getAppearance().setEnabled(STATE_DESELECTED, true);
		setTraversable(true);
		updateMinSize();
	}
	
	public RadioButton(String text, ToggableGroup<E> group)
	{
		this(text, group, null);
	}

	public RadioButton(ToggableGroup<E> group)
	{
		this(null, group, null);
	}

	public RadioButton()
	{
		this("", null, null);
	}

	public RadioButton(String text)
	{
		this(text, null, null);
	}

	/* (non-Javadoc)
	 * @see org.fenggui.ObservableWidget#keyPressed(org.fenggui.event.key.KeyPressedEvent)
	 */
	@Override
	public void keyPressed(KeyPressedEvent keyPressedEvent)
	{
		if (keyPressedEvent.getKeyClass() == Key.ENTER || Character.isSpaceChar(keyPressedEvent.getKey()))
		{
			setSelected(true);
		}
		super.keyPressed(keyPressedEvent);
	}

	/* (non-Javadoc)
	 * @see org.fenggui.ObservableWidget#mousePressed(org.fenggui.event.mouse.MousePressedEvent)
	 */
	@Override
	public void mousePressed(MousePressedEvent mousePressedEvent)
	{
		setSelected(true);
		super.mousePressed(mousePressedEvent);
	}

	public boolean isSelected()
	{
		return selected;
	}
	
	private void fireSelectionChangedEvent(boolean b)
	{
		SelectionChangedEvent e = new SelectionChangedEvent(this, this, b);
		for (ISelectionChangedListener l : selectionChangedHook)
		{
			l.selectionChanged(e);
		}
	}

	public RadioButton<E> setSelected(boolean s)
	{
		updateState();
		if (s)
		{
			if (radioButtonGroup != null)
				radioButtonGroup.setSelected(this, this, true);
			getAppearance().setEnabled(STATE_DESELECTED, false);
			getAppearance().setEnabled(STATE_SELECTED, true);
		}
		else
		{
			getAppearance().setEnabled(STATE_SELECTED, false);
			getAppearance().setEnabled(STATE_DESELECTED, true);
		}
		selected = s;
		fireSelectionChangedEvent(s);
		return this;
	}

	public ToggableGroup<E> getRadioButtonGroup()
	{
		return radioButtonGroup;
	}

	public void setRadioButtonGroup(ToggableGroup<E> radioButtonGroup)
	{
		this.radioButtonGroup = radioButtonGroup;
		if (isSelected())
			radioButtonGroup.setSelected(this, this, isSelected());
	}

	public E getValue()
	{
		return value;
	}

	public void setValue(E value)
	{
		this.value = value;
	}

	public void addSelectionChangedListener(ISelectionChangedListener l)
	{
		selectionChangedHook.add(l);
	}
}
