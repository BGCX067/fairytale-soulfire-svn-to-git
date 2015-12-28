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
 * $Id: VerticalList.java 568 2008-06-11 09:30:16Z marcmenghin $
 */
package org.fenggui;

import java.util.ArrayList;

import org.fenggui.appearance.EntryAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * List that aligns the items vertically in several columns.
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2008-06-11 11:30:16 +0200 (Mi, 11 Jun 2008) $
 * @version $Revision: 568 $
 */
public class VerticalList<E> extends StandardWidget implements INotLayoutableWidget
{
	private ArrayList<ListItem<E>> items = new ArrayList<ListItem<E>>();
	private ToggableGroup<E> toggableWidgetGroup = null;

	private ArrayList<Integer> columnWidth = new ArrayList<Integer>();
	private EntryAppearance appearance = null;
	/**
	 * Creates a new <code>VerticalList</code> object.
	 *
	 */
	public VerticalList() 
	{
		this(1);
	}
	
	public Iterable<ListItem<E>> getItems()
	{
		return items;
	}
	
	/**
	 * Creates a new <code>List</code> object.
	 * @param selectionType number of selectable items
	 */
	public VerticalList(int selectionType) 
	{
		toggableWidgetGroup = new ToggableGroup<E>(selectionType);
		appearance = new EntryAppearance(this);
		updateMinSize();

	}
	
	public int getColumnWidth(int i)
	{
		return columnWidth.get(i);
	}
	
	public EntryAppearance getAppearance()
	{
		return appearance;
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

	public int getWidth(int proposedHeight)
	{
		int x = 0;
		int y = getAppearance().getContentHeight() + appearance.getTextLineHeight();
		
		int currentMax = 0;
		int tmp = 0;
		
		for(ListItem<E> item: items)
		{
			appearance.getData().setText(item.getText());
			tmp = appearance.getRenderer().getTextWidth(appearance.getData());
			
			if(tmp > currentMax) currentMax = tmp;
			
			y -= appearance.getTextLineHeight();
			
			if(y <= 0)
			{
				x += currentMax + 10;
				currentMax = 0;
				y = getAppearance().getContentHeight() - appearance.getTextLineHeight();
			}
		}
		
		return x + currentMax + 10;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void mousePressed(MousePressedEvent mp) 
	{
		
		int mx = mp.getDisplayX() - getDisplayX();
		int my = mp.getDisplayY() - getDisplayY();
		
		int x = 0;
		int y = getAppearance().getContentHeight() + appearance.getTextLineHeight();
		
		int columnCounter = 0;

		for(ListItem item: items)
		{
			if(y <= my && y + appearance.getTextLineHeight() >= my &&
					x <= mx && x + columnWidth.get(columnCounter) >= mx)
			{
				toggableWidgetGroup.setSelected(this, item, true);
				item.setSelected(true);
			}			
			
			y -= appearance.getTextLineHeight();
			

			
			if(y <= 0)
			{
				x += columnWidth.get(columnCounter);
				columnCounter++;
				y = getAppearance().getContentHeight() - appearance.getTextLineHeight();
			}
		}
	}

	
	
	@Override
	public void layout() 
	{
		int x = 0;
		int y = getAppearance().getContentHeight() + appearance.getTextLineHeight();
		
		int currentMax = 0;
		int tmp = 0;
		columnWidth.clear();
		for(ListItem<E> item: items)
		{
			appearance.getData().setText(item.getText());
			tmp = appearance.getRenderer().getTextWidth(appearance.getData());
			
			if(tmp > currentMax) currentMax = tmp;
			
			y -= appearance.getTextLineHeight();
			
			if(y <= 0)
			{
				x += currentMax + 10;
				columnWidth.add(currentMax + 10);
				currentMax = 0;
				y = getAppearance().getContentHeight() - appearance.getTextLineHeight();
			}
		}
		columnWidth.add(currentMax + 10);
	}

	public void heightHint(int height) 
	{
		int x = 0;
		int y = getAppearance().getContentHeight() + appearance.getTextLineHeight();
		
		int currentMax = 0;
		int tmp = 0;
		
		for(ListItem<E> item: items)
		{
			appearance.getData().setText(item.getText());
			tmp = appearance.getRenderer().getTextWidth(appearance.getData());
			
			if(tmp > currentMax) currentMax = tmp;
			
			y -= appearance.getTextLineHeight();
			
			if(y <= 0)
			{
				x += currentMax + 10;
				currentMax = 0;
				y = getAppearance().getContentHeight() - appearance.getTextLineHeight();
			}
		}
		
		setMinSize(x + currentMax, height);
	}

	public void widthHint(int width) 
	{
		// ignore
	}

	@Override
	public Dimension getMinContentSize()
	{
		return new Dimension(0, 0);
	}

	@Override
	public void paintContent(Graphics g, IOpenGL gl)
	{
		int x = 0;
		int y = appearance.getContentHeight() + appearance.getTextLineHeight();
		
		int columnCounter = 0;
		
		g.setColor(Color.BLACK);
		
		for(ListItem<E> item: getItems())
		{
			appearance.getData().setText(item.getText());
			if(item.isSelected())
			{
				g.setColor(Color.BLUE);
				g.drawFilledRectangle(x-5, y, getColumnWidth(columnCounter), appearance.getTextLineHeight());
				g.setColor(Color.WHITE);
				appearance.getData().setText(item.getText());
				g.setColor(Color.BLACK);
			}
			else
			{
				appearance.getData().setText(item.getText());
			}
			
			y -= appearance.getTextLineHeight();
			
			if(y <= 0)
			{
				x += getColumnWidth(columnCounter);
				columnCounter++;
				y = appearance.getContentHeight() - appearance.getTextLineHeight();
			}
		}		
	}
}
