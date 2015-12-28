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
 * $Id: List.java 569 2008-06-24 10:08:17Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;
import java.util.ArrayList;

import org.fenggui.appearance.EntryAppearance;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.event.mouse.MouseEnteredEvent;
import org.fenggui.event.mouse.MouseExitedEvent;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Alignment;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * Implementation of a vertical list of items with each item be selectable. The items used
 * in this list are <code>ListItems</code> rather than widgets.
 * 
 * 
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2008-06-24 12:08:17 +0200 (Di, 24 Jun 2008) $
 * @version $Revision: 569 $
 * @dedication The Offspring - Defy You
 */
public class List<E> extends StatefullWidget<EntryAppearance>
{
	private ToggableGroup<E> toggableWidgetGroup = null;
	private ArrayList<ListItem<E>> items = new ArrayList<ListItem<E>>();
	private int mouseOverRow = -1;
	private boolean toggleOn = true;
	
	/**
	 * Creates a new list
	 * 
	 */
	public List()
	{
		this(ToggableGroup.SINGLE_SELECTION);
	}

	/**
	 * Creates a new List object.
	 * 
	 * @param selectionType
	 */
	public List(int selectionType)
	{
		toggableWidgetGroup = new ToggableGroup<E>(selectionType);

		setAppearance(new EntryAppearance(this));
		updateMinSize();
	}

	/**
	 * Function so always true click items like ComboBoxes work properly
	 *
	 * @param b Set to true for the ListItem to toggle between selected and
	 * deselected as you click it.  false to always set as true, regardless
	 * of how many times in a row it is clicked.
	 *
	 */

	public void setToggle(boolean b)
	{
		toggleOn = b;
	}
	
	
	public ToggableGroup<E> getToggableWidgetGroup()
	{
		return toggableWidgetGroup;
	}

	public void addItem(ListItem<E> li)
	{
		items.add(li);
		updateMinSize();
	}

	public void addItem(String text)
	{
		addItem(new ListItem<E>(text));
	}

	public ListItem<E> getItem(int row)
	{
		return items.get(row);
	}

	public void removeItem(int row)
	{
		removeItem(items.get(row));
	}

	public void removeItem(ListItem<E> item)
	{
		items.remove(item);
		updateMinSize();
	}

	public void clear()
	{
		items.clear();
		updateMinSize();
	}

	@Override
	public void updateMinSize()
	{

		if (getAppearance() != null)
		{
			setMinSize(getAppearance().getMinSizeHint());
		}

		if (getParent() != null && getParent() instanceof ScrollContainer)
		{
			((ScrollContainer) getParent()).layout();
		}
		else if (getParent() != null)
			getParent().updateMinSize();
	}

	@Override
	public void mousePressed(MousePressedEvent mp)
	{
		setSelectedIndex(mouseOverRow);
		super.mousePressed(mp);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		super.process(stream);

		if (stream.startSubcontext("listItems"))
		{
			stream.processChildren("ListItem", items, ListItem.class);
			stream.endSubcontext();
		}
		updateMinSize();
	}

	@Override
	public void mouseMoved(int displayX, int displayY)
	{
		// TODO cache getDisplayY()!!
		int mouseY = displayY - getDisplayY();

		mouseOverRow = (getAppearance().getContentHeight() - mouseY) / getRowHeight();
		super.mouseMoved(displayX, displayY);
	}

	@Override
	public void mouseExited(MouseExitedEvent mouseExitedEvent)
	{
		mouseOverRow = -1;
		Binding.getInstance().getCursorFactory().getDefaultCursor().show();
		super.mouseExited(mouseExitedEvent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.Widget#mouseEntered(org.fenggui.event.mouse.MouseEnteredEvent)
	 */
	@Override
	public void mouseEntered(MouseEnteredEvent mouseEnteredEvent)
	{
		Binding.getInstance().getCursorFactory().getHandCursor().show();
		super.mouseEntered(mouseEnteredEvent);
	}

	public boolean isEmpty()
	{
		return items.isEmpty();
	}

	public int size()
	{
		return items.size();
	}

	public void setSelectedIndex(int index)
	{
		if (index < 0 || index >= items.size())
			return;

		ListItem<E> item = items.get(index);
		boolean selected = !item.isSelected();
		
		if (!toggleOn)
			selected = true;
		
		toggableWidgetGroup.setSelected(this, item, selected);
		item.setSelected(selected);
	}

	@SuppressWarnings("unchecked")
	public void setSelectedIndex(int index, boolean selected)
	{
		if (index < 0 || index >= items.size())
			return;

		ListItem item = items.get(index);
		toggableWidgetGroup.setSelected(this, item, selected);
		item.setSelected(selected);
	}

	public int getItem(E value)
	{
		for (int i = 0; i < items.size(); i++)
		{
			ListItem<E> item = items.get(i);
			if (item.getValue().equals(value))
			{
				return i;
			}
		}

		return -1;
	}

	public ArrayList<ListItem<E>> getItems()
	{
		return items;
	}

	public int getMouseOverRow()
	{
		return mouseOverRow;
	}

	public ListItem<E> getSelectedItem()
	{
		return (ListItem<E>) toggableWidgetGroup.getSelectedItem();
	}

	private int getRowHeight()
	{
		int rowHeight = getAppearance().getRowHeight();
		//fix rowHeight if too small for text
		int textHeight = getAppearance().getRenderer().getLineHeight(getAppearance().getData(), 0);
		if (rowHeight < textHeight)
			rowHeight = textHeight;

		return rowHeight;
	}

	@Override
	public Dimension getMinContentSize()
	{
		int minWidth = 5;

		if (isEmpty())
			return new Dimension(5, 5);

		for (ListItem<E> item : items)
		{
			getAppearance().getData().setText(item.getText());
			int width = 0;

			if (item.getText() != null)
				width += getAppearance().getTextWidth();

			if (item.getPixmap() != null)
				width += item.getPixmap().getWidth();

			if (width > minWidth)
				minWidth = width;
		}

		return new Dimension(minWidth, size() * getRowHeight());
	}

	@Override
	public void paintContent(Graphics g, IOpenGL gl)
	{
		if (isEmpty())
			return;

		EntryAppearance appearance = getAppearance();
		int rowHeight = getRowHeight();
		int lowerClipBound = g.getClipSpace().getY() - rowHeight;
		int upperClipBound = g.getClipSpace().getY() + g.getClipSpace().getHeight();

		int lowerContentBound = getDisplayY();
		int upperContentBound = lowerContentBound + rowHeight * size();

		// compute start row
		int row = (upperContentBound - upperClipBound) / rowHeight;
		if (row < 0)
		{
			row = 0;
		}

		if (row > size())
		{
			return;
		}

		int y = appearance.getContentHeight() - row * rowHeight;
		Alignment alignment = appearance.getAlignment();
		while (y + lowerContentBound > lowerClipBound && row < size())
		{
			ListItem<?> item = getItem(row);
			appearance.getData().setColor(appearance.getColor());
			if (item.isSelected())
			{
				appearance.getSelectionUnderlay().paint(g, 0, y - rowHeight, appearance.getContentWidth(), rowHeight);
				appearance.getData().setColor(appearance.getSelectionColor());
			}

			if (getMouseOverRow() == row)
			{
				appearance.getHoverUnderlay().paint(g, 0, y - rowHeight, appearance.getContentWidth(), rowHeight);
				appearance.getData().setColor(appearance.getHoverColor());
			}

			appearance.getData().setText(item.getText());
			Dimension textSize = appearance.getTextContentSize();
			Pixmap pixmap = item.getPixmap();

			// alignment
			int alignedX = alignment.alignX(appearance.getContentWidth(), textSize.getWidth()
					+ (pixmap != null ? pixmap.getWidth() + appearance.getGap() : 0));
			int alignedY = alignment.alignY(rowHeight, textSize.getHeight());

			appearance.getRenderer().render(alignedX + (pixmap != null ? pixmap.getWidth() + appearance.getGap() : 0),
				y + alignedY, appearance.getContentWidth(), rowHeight, appearance.getData(), g, gl);

			if (pixmap != null)
			{
				g.setColor(Color.WHITE);
				int heightDif = (rowHeight - pixmap.getHeight()) / 2;
				g.drawImage(pixmap, 0, y - rowHeight + heightDif);
			}

			row++;
			y -= rowHeight;
		}

	}

}
