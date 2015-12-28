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
 * Created on 10.01.2008
 * $Id$
 */
package org.fenggui;

import org.fenggui.composite.table.ITableModel;
import org.fenggui.composite.table.Table;
import org.fenggui.composite.text.TextView;
import org.fenggui.composite.tree.Tree;
import org.fenggui.theme.XMLTheme;
import org.fenggui.util.Alignment;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class FengGUIOptional
{

	private static boolean singleInit = false;

	//Optional prototypes
	public static org.fenggui.composite.tab.TabItem ptTabItem = null;

	public static void initOptional()
	{
		//add stuff to XML theming
		if (!singleInit)
		{
			XMLTheme.TYPE_REGISTRY.register("SnappingSlider", SnappingSlider.class);
			XMLTheme.TYPE_REGISTRY.register("Tab", org.fenggui.composite.tab.TabContainer.class);
			XMLTheme.TYPE_REGISTRY.register("TabItem", org.fenggui.composite.tab.TabItem.class);
			XMLTheme.TYPE_REGISTRY.register("TabContainer", TabContainer.class);
			XMLTheme.TYPE_REGISTRY.register("TabItemLabel", TabItemLabel.class);
			XMLTheme.TYPE_REGISTRY.register("Tree", Tree.class);
			XMLTheme.TYPE_REGISTRY.register("Table", Table.class);
			XMLTheme.TYPE_REGISTRY.register("TextView", TextView.class);
			XMLTheme.TYPE_REGISTRY.register("VerticalList", VerticalList.class);

			singleInit = true;
		}

		createTabItem();
	}

	public static org.fenggui.composite.tab.TabItem createTabItem(ToggableGroup<Integer> group)
	{
		org.fenggui.composite.tab.TabItem result = createTabItem();
		result.setGroup(group);
		return result;
	}

	public static org.fenggui.composite.tab.TabItem createTabItem()
	{
		if (ptTabItem == null)
		{
			ptTabItem = new org.fenggui.composite.tab.TabItem();
			FengGUI.setUpAppearance(ptTabItem);
		}
		org.fenggui.composite.tab.TabItem result = new org.fenggui.composite.tab.TabItem(ptTabItem);
		return result;
	}

	public static org.fenggui.composite.tab.TabContainer createTab(IContainer parent, Alignment containerAlignment,
			Alignment headerAlignment)
	{
		org.fenggui.composite.tab.TabContainer result = new org.fenggui.composite.tab.TabContainer(containerAlignment,
				headerAlignment);
		FengGUI.setUpAppearance(result);
		parent.addWidget(result);
		return result;
	}

	public static RotatedLabel createRotatedLabel(IContainer parent, String text)
	{
		RotatedLabel l = new RotatedLabel(text);
		FengGUI.setUpAppearance(l);
		parent.addWidget(l);
		return l;
	}

	/**
	 * Create a TabContainer widget.
	 * 
	 * @param parent the parent container
	 * @param tabOnTop true if tabs are on top
	 * @return new TabContainer widget.
	 */
	public static TabContainer createTabContainer(IContainer parent, boolean tabOnTop)
	{
		TabContainer result = new TabContainer(tabOnTop);
		FengGUI.setUpAppearance(result);
		parent.addWidget(result);
		return result;
	}

	/**
	 * Creates a new TabContainer widget.
	 * 
	 * @param parent the parent container
	 * @return new TabContainer widget.
	 */
	public static TabContainer createTabContainer(IContainer parent)
	{
		return createTabContainer(parent, true);
	}

	/**
	 * Create a Tree widget.
	 * @param <T> type parameter
	 * @param parent the parent container
	 * @return new tree widget.
	 */
	public static <T> Tree<T> createTree(IContainer parent)
	{
		Tree<T> result = new Tree<T>();
		FengGUI.setUpAppearance(result);
		parent.addWidget(result);
		return result;
	}

	/**
	 * Creates a new Table.
	 * @param parent the parent container
	 * @return new Table
	 */
	public static Table createTable(IContainer parent)
	{
		Table table = new Table();
		FengGUI.setUpAppearance(table);
		parent.addWidget(table);
		return table;
	}

	/**
	 * Creates a new Table.
	 * @param parent the parent container
	 * @param model the model used to provide the table with data
	 * @return new Table
	 */
	public static Table createTable(IContainer parent, ITableModel model)
	{
		Table table = createTable(parent);
		table.setModel(model);
		return table;
	}

	public static <T> VerticalList<T> createVerticalList()
	{
		VerticalList<T> v = new VerticalList<T>();
		FengGUI.setUpAppearance(v);
		return v;
	}

	public static <T> VerticalList<T> createVerticalList(IContainer parent)
	{
		VerticalList<T> v = FengGUIOptional.<T> createVerticalList();
		parent.addWidget(v);
		return v;
	}
}
