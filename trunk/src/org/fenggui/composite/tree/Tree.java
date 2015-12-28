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
 * $Id: Tree.java 568 2008-06-11 09:30:16Z marcmenghin $
 */
package org.fenggui.composite.tree;

import java.util.ArrayList;

import org.fenggui.Button;
import org.fenggui.ScrollContainer;
import org.fenggui.StatefullWidget;
import org.fenggui.ToggableGroup;
import org.fenggui.appearance.EntryAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.text.ComplexTextRendererData;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.util.Dimension;

/**
 * Widget to display tree a structure. The data to be displayed has to be provided by an <code>ITreeModel</code>
 * implementation. Note that at the current state of development, changes to the model require to set the entire model
 * new (setModel)/ See here http://sourceforge.net/tracker/index.php?func=detail&aid=1570998&group_id=178317&atid=884747
 * <br/> When the tree is used in a <code>ScrollContainer</code>, changes to the model may require to be commited
 * with a <code>layout</code> call in the scroll container.
 * 
 * @author Johannes Schaback aka Schabby, last edited by $Author: marcmenghin $, $LastChangedDate: 2008-06-11 11:30:16 +0200 (Mi, 11 Jun 2008) $
 * @version $Revision: 568 $
 * @see ITreeModel
 */
public class Tree<E> extends StatefullWidget<EntryAppearance>
{
	private static final int ICON_OFFSET = 2;
	private static final int OFFSET = 10;
	/**
	 * the data model
	 */
	private ITreeModel<E> model = null;
	/**
	 * The root node of the tree
	 */
	private Record<E> root = null;
	
	private ToggableGroup<E> toggableWidgetGroup = new ToggableGroup<E>(1);

	private Button minusButton;
	private Button plusButton;
	private int counter;

	/**
	 * Creates a new <code>Tree</code> instance with an empty data model.
	 * 
	 */
	public Tree()
	{
		this(null);
	}

	/**
	 * Creates a new <code>Tree</code> instance that visualized the given data model.
	 * 
	 * @param model
	 *            the data model
	 */
	public Tree(ITreeModel<E> model)
	{
		if (model != null) setModel(model);

		setAppearance(new EntryAppearance(this));
		
		updateMinSize();

		minusButton = new Button("-");
		plusButton = new Button("+");
		
//		if (getAppearance().getPlusIcon() == null || getAppearance().getMinusIcon() == null) 
//		{ 
//			throw new IllegalArgumentException(
//				"plusIcon == null || minusIcon == null! Make sure you load the icons in your theme!"); 
//		}
	}


	/**
	 * Returns the currently used data model.
	 * 
	 * @return the tree model
	 */
	public ITreeModel<E> getModel()
	{
		return model;
	}


	/**
	 * Sets the data model of the tree.
	 * 
	 * @param model
	 *            the data model.
	 */
	public void setModel(ITreeModel<E> model)
	{
		this.model = model;
		root = new Record<E>(model, model.getRoot());
		root.setExpandable(model.getNumberOfChildren(root.getNode()) > 0);
		updateMinSize();
	}


	@Override
	public void mousePressed(MousePressedEvent mp)
	{
		if(getModel() == null) return;
		
		int row = (getAppearance().getContentHeight() - mp.getLocalY(this)) / getAppearance().getTextLineHeight();
		int x = mp.getLocalX(this);
		Record<E> r = findRecord(root, row);

		if (r != null)
		{
			getAppearance().getData().setText(model.getText(r.getNode()));
			
			// if clicked on the plus or minus icon
			if (x > r.getOffset() && x < r.getOffset() + minusButton.getWidth())
			{
				if (r.getNumberOfChildren() == 0)
				{
					int n = model.getNumberOfChildren(r.getNode());
					for (int i = 0; i < n; i++)
					{
						Record<E> newRec = new Record<E>(model, model.getNode(r.getNode(), i));
						newRec.setExpandable(model.getNumberOfChildren(newRec.getNode()) > 0);
						newRec.setOffset(r.getOffset() + OFFSET);
						r.addChild(newRec);
					}
				}
				else
				{
					r.removeAllChildren();
				}
				updateMinSize();
			}
			else if (x > r.getOffset() + minusButton.getWidth() + ICON_OFFSET
					&& x < r.getOffset() + minusButton.getWidth() + ICON_OFFSET + getAppearance().getTextWidth())
			{
				toggableWidgetGroup.setSelected(this, r, true);
				r.setSelected(true);
				// r.isSelected = !r.isSelected;
			}
		}
	}


	private Record<E> findRecord(Record<E> node, int row)
	{
		if (node.row == row) return node;
		for (Record<E> r : node.getChildren())
		{
			Record<E> p = findRecord(r, row);
			if (p != null) return p;
		}
		return null;
	}

	


	public ToggableGroup<E> getToggableWidgetGroup()
	{
		return toggableWidgetGroup;
	}

	public Record<E> getRoot()
	{
		return root;
	}

	@Override
	public void updateMinSize()
	{
		
		setMinSize(getAppearance().getMinSizeHint());
		
		if (getParent() != null && getParent() instanceof ScrollContainer)
		{
			ScrollContainer parent = (ScrollContainer) getParent();
			parent.layout();
		}
		else if (getParent() != null) getParent().updateMinSize();
	}

	@Override
	public Dimension getMinContentSize()
	{
		Record<E> root = getRoot();
		ITreeModel<E> model = getModel();
		
		if (root == null) return new Dimension(0,0);
		ArrayList<Record<E>> stack = new ArrayList<Record<E>>();
		ArrayList<Integer> offsetStack = new ArrayList<Integer>();
		stack.add(root);
		offsetStack.add(0);
		int width = 0;
		int height = 0;
		
		while (!stack.isEmpty())
		{
			Record<E> r = stack.remove(0);
			int offset = offsetStack.remove(0);
			getAppearance().getData().setText(model.getText(r.getNode()));
			int recordWidth = offset + ICON_OFFSET + 3 + getAppearance().getTextWidth();
			if (width < recordWidth) width = recordWidth;
			
			height += getAppearance().getTextLineHeight();
			
			for (Record<E> p : r.getChildren())
			{
				stack.add(p);
				offsetStack.add(offset + OFFSET);
			}
		}

		return new Dimension(width, height);
		}

	@Override
	public void paintContent(Graphics g, IOpenGL gl)
	{
		if(getRoot() == null) return;
		counter = 0;
		if (!(minusButton == null || plusButton == null))
			paintRecord(g, getRoot(), true, false);
		
	}
	
	private void paintRecord(Graphics g, Record<E> node, boolean isLastOne, boolean hasSisters)
	{
		node.row = counter;
		counter++;
		ITreeModel<E> model = getModel();
		int y = getAppearance().getContentHeight() - counter * getAppearance().getTextLineHeight();
		getAppearance().getData().setText(model.getText(node.getNode()));
		
		if (node.isSelected())
		{
			getAppearance().getSelectionUnderlay().paint(g, node.getOffset() + ICON_OFFSET + 1, y, getAppearance().getTextWidth() + 3, getAppearance().getTextLineHeight());
		}

		getAppearance().renderText(node.getOffset() + ICON_OFFSET + 3, y, g, g.getOpenGL());
		
		g.setColor(((ComplexTextRendererData)minusButton.getAppearance().getData()).getColor());
		g.drawLine(node.getOffset() + minusButton.getWidth() / 2, y + getAppearance().getTextLineHeight() / 2, node.getOffset() + ICON_OFFSET, y
				+ getAppearance().getTextLineHeight() / 2);


		if (node.getNumberOfChildren() > 0)
		{
			g.drawLine(node.getOffset() + OFFSET + plusButton.getWidth() / 2, y + getAppearance().getTextLineHeight() / 2, node.getOffset() + OFFSET
					+ plusButton.getWidth() / 2, y - (node.getNumberOfChildren() * getAppearance().getTextLineHeight()) + getAppearance().getTextLineHeight() / 2);
		}


		for (int i = 0; i < node.getNumberOfChildren(); i++)
		{
			paintRecord(g, node.getChild(i), i + 1 >= node.getNumberOfChildren(), false);
		}

		if (!isLastOne)
		{
			g.setColor(((ComplexTextRendererData)minusButton.getAppearance().getData()).getColor());
			g.drawLine(node.getOffset() + plusButton.getWidth() / 2, y + getAppearance().getTextLineHeight()
					+ (getAppearance().getTextLineHeight() / 2 - minusButton.getHeight() / 2), node.getOffset() + plusButton.getWidth() / 2,
				getAppearance().getContentHeight() - counter * getAppearance().getTextLineHeight()
						- (getAppearance().getTextLineHeight() / 2 - plusButton.getHeight() / 2));
		}

		if (node.getNumberOfChildren() > 0)
		{
			Button minus = new Button(minusButton);
			minus.setXY(node.getOffset(), y + (getAppearance().getTextLineHeight() / 2 - minus.getHeight() / 2));
			minus.paint(g);
		}
		else if (node.isExpandable())
		{
			Button plus = new Button(plusButton);
			plus.setXY(node.getOffset(), y + (getAppearance().getTextLineHeight() / 2 - plus.getHeight() / 2));
			plus.paint(g);
		}
	}
}
