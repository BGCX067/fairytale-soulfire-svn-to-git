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
 * $Id: CheckBox.java 569 2008-06-24 10:08:17Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;
import java.util.ArrayList;

import org.fenggui.event.ISelectionChangedListener;
import org.fenggui.event.SelectionChangedEvent;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyAdapter;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.mouse.MouseAdapter;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;

/**
 * Implementation of a check box. A check box can be used to toggle between two states.
 * <br/> <br/> It is currently not supported to disable the check box.
 * 
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2007-11-19
 *         14:53:14 +0100 (Mo, 19 Nov 2007) $
 * @dedication NOFX - Lower
 * @version $Revision: 569 $
 */
public class CheckBox<E> extends ObservableLabelWidget implements IToggable<E>
{
	private ArrayList<ISelectionChangedListener> selectionChangedHook = new ArrayList<ISelectionChangedListener>();

	private E value = null;
	private boolean selected = false;

	public static final String STATE_SELECTED = "selected";
	public static final String STATE_DESELECTED = "deselected";

	/**
	 * Creates a new <code>CheckBox</code> widget.
	 */
	public CheckBox()
	{
		this("");
	}

	public CheckBox(CheckBox<?> checkBox)
	{
		super(checkBox);
		buildLogic();

		getAppearance().setEnabled(STATE_SELECTED, false);
		getAppearance().setEnabled(STATE_DESELECTED, true);
		setTraversable(true);
		updateMinSize();
	}
	
	void buildLogic()
	{
		addMouseListener(new MouseAdapter() {

			/* (non-Javadoc)
			 * @see org.fenggui.event.mouse.MouseAdapter#mousePressed(org.fenggui.event.mouse.MousePressedEvent)
			 */
			@Override
			public void mousePressed(MousePressedEvent mousePressedEvent)
			{
				setSelected(!isSelected());
			}
			
		});

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyPressedEvent keyPressedEvent)
			{

				if (keyPressedEvent.getKeyClass() == Key.ENTER || Character.isSpaceChar(keyPressedEvent.getKey()))
				{
					setSelected(!isSelected());
				}
			}
		});
	}

	/**
	 * 
	 * Creates a new <code>CheckBox</code> widget.
	 * 
	 * @param text
	 *          the text displayed next to the check box
	 */
	public CheckBox(String text)
	{
		super();
		buildLogic();

		getAppearance().setEnabled(STATE_SELECTED, false);
		getAppearance().setEnabled(STATE_DESELECTED, true);
		setText(text); // does an updateMinSize()
		setTraversable(true);
	}

	/**
	 * Returns whether the check box is selected or not
	 * 
	 * @return true if selected, false otherwise
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/**
	 * Selects or deselects this check box manually.
	 */
	public IToggable<E> setSelected(boolean b)
	{
		selected = b;

		this.updateState();
		if (b)
		{
			getAppearance().setEnabled(STATE_DESELECTED, false);
			getAppearance().setEnabled(STATE_SELECTED, true);
		}
		else
		{
			getAppearance().setEnabled(STATE_SELECTED, false);
			getAppearance().setEnabled(STATE_DESELECTED, true);
		}

		fireSelectionChangedEvent(this, this, b);

		return this;
	}

	/**
	 * Returns the value associated with this check box.
	 * 
	 * @return value
	 */
	public E getValue()
	{
		return value;
	}

	/**
	 * Sets the associated value for this check box.
	 * 
	 * @param value
	 *          value
	 */
	public void setValue(E value)
	{
		this.value = value;
	}

	/**
	 * Add a {@link ISelectionChangedListener} to the widget. The listener can be added only
	 * once.
	 * 
	 * @param l
	 *          Listener
	 */
	public void addSelectionChangedListener(ISelectionChangedListener l)
	{
		if (!selectionChangedHook.contains(l))
		{
			selectionChangedHook.add(l);
		}
	}

	/**
	 * Add the {@link ISelectionChangedListener} from the widget
	 * 
	 * @param l
	 *          Listener
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener l)
	{
		selectionChangedHook.remove(l);
	}

	/**
	 * Fire a {@link SelectionChangedEvent}
	 * 
	 * @param source
	 * @param t
	 * @param s
	 */
	private void fireSelectionChangedEvent(IWidget source, IToggable<E> t, boolean s)
	{
		SelectionChangedEvent e = new SelectionChangedEvent(source, t, s);

		for (ISelectionChangedListener l : selectionChangedHook)
		{
			l.selectionChanged(e);
		}
	}

	@Override
	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		super.process(stream);
		setSelected(stream.processAttribute("selected", isSelected(), false));
	}

}
