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
 * Created on 2005-3-2
 * $Id: Widget.java 569 2008-06-24 10:08:17Z marcmenghin $
 */
package org.fenggui;

import java.util.ArrayList;

import org.fenggui.binding.render.Graphics;
import org.fenggui.event.*;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.key.KeyReleasedEvent;
import org.fenggui.event.key.KeyTypedEvent;
import org.fenggui.event.mouse.*;
import org.fenggui.layout.ILayoutData;
import org.fenggui.tooltip.ITooltipData;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

/**
 * Implementation of a widget. A widget is the most basic unit of
 * a GUI system. 
 * <br/>
 * The background spans over the content, padding and border, but
 * not over the margin.<br/>
 * <br/>
 * The minium size of a widget is maintained by the widget itself and is
 * always kept up
 * to date, while the actual size of the widget is usually set by the layout manager.
 * Alternatively, one can set the actual size of a widget manually via the <code>setSize<method>.
 * 
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2008-06-24 12:08:17 +0200 (Di, 24 Jun 2008) $
 * @version $Revision: 569 $
 * @dedication Billie Holiday - They Can't Take That Away from Me
 * @see org.fenggui.background.Background
 * @see org.fenggui.border.Border
 */
public class Widget implements IWidget
{

	private Dimension size = new Dimension(10, 10);
	private Dimension minSize = new Dimension(10, 10);

	private boolean shrinkable = true;
	private boolean expandable = true;
	private boolean visible = true;
	private Object data = null;

	private ITooltipData tooltip = null;

	private ArrayList<ISizeChangedListener> sizeHook = new ArrayList<ISizeChangedListener>(0);
	private ArrayList<IPositionChangedListener> positionHook = new ArrayList<IPositionChangedListener>(0);

	/**
	 * The parent Container in which the Widget lays.
	 */
	private IBasicContainer parent = null;

	/**
	 * Position of the Widget in the coordinate system of the parent
	 * Container.
	 */
	private Point position = new Point(0, 0);

	/**
	 * Layout Data that is associated with this Widget. Layout data is
	 * required by the layout managers to know how to layout a Widget.
	 */
	private ILayoutData layoutData = null;

	/**
	 * Creates a new widget.
	 *
	 */
	public Widget()
	{
	}

	/**
	 * Copy constructor. Should be overwritten by all implementing subclasses.
	 * 
	 * @param widget widget to copy.
	 */
	public Widget(Widget widget)
	{
		this();
		if (widget != null)
		{
			this.size = new Dimension(widget.size);
			this.minSize = new Dimension(widget.minSize);
			this.shrinkable = widget.shrinkable;
			this.expandable = widget.expandable;
			this.visible = widget.visible;
//			TODO: copy this object aswell
//			this.layoutData = widget.layoutData
		}
	}

	/**
	 * Sets the LayoutData of this Widget. Note that the parameter is not
	 * type safe. If you pass a wrong type, the LayoutManager may crash
	 * with a ClassCashException.
	 * 
	 * @param layoutData layout data
	 */
	public void setLayoutData(ILayoutData layoutData)
	{
		this.layoutData = layoutData;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getLayoutData()
	 */
	public ILayoutData getLayoutData()
	{
		return layoutData;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getParent()
	 */
	public IBasicContainer getParent()
	{
		return parent;
	}

	public Dimension getMinContentSize()
	{
		return new Dimension(0, 0);
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#mouseEntered(org.fenggui.event.mouse.MouseEnteredEvent)
	 */
	public void mouseEntered(MouseEnteredEvent mouseEnteredEvent)
	{
		if (!mouseEnteredEvent.isAlreadyUsed() && this.getParent() != null)
		{
			this.getParent().mouseEntered(mouseEnteredEvent);
		}
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#mouseExited(org.fenggui.event.mouse.MouseExitedEvent)
	 */
	public void mouseExited(MouseExitedEvent mouseExitedEvent)
	{
		if (!mouseExitedEvent.isAlreadyUsed() && this.getParent() != null)
		{
			this.getParent().mouseExited(mouseExitedEvent);
		}
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#mousePressed(org.fenggui.event.mouse.MousePressedEvent)
	 */
	public void mousePressed(MousePressedEvent mp)
	{
		if (!mp.isAlreadyUsed() && this.getParent() != null)
		{
			this.getParent().mousePressed(mp);
		}
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#mouseMoved(int, int)
	 */
	public void mouseMoved(int displayX, int displayY)
	{
		if (this.getParent() != null)
		{
			this.getParent().mouseMoved(displayX, displayY);
		}
	}

	/*
	public void paint(Graphics g)
	{

	}
	
	public void paintEverything(Graphics g)
	{
		if(getAppearance() != null)
		{
			getAppearance().beforePaint(g, g.getOpenGL());
			paint(g);
			getAppearance().afterPaint(g, g.getOpenGL());
		}
	}

	*/
	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#mouseDragged(org.fenggui.event.mouse.MouseDraggedEvent)
	 */
	public void mouseDragged(MouseDraggedEvent mp)
	{
		if (!mp.isAlreadyUsed() && this.getParent() != null)
		{
			this.getParent().mouseDragged(mp);
		}
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#mouseReleased(org.fenggui.event.mouse.MouseReleasedEvent)
	 */
	public void mouseReleased(MouseReleasedEvent mr)
	{
		if (!mr.isAlreadyUsed() && this.getParent() != null)
		{
			this.getParent().mouseReleased(mr);
		}
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#mouseDoubleClicked(org.fenggui.event.mouse.MouseDoubleClickedEvent)
	 */
	public void mouseDoubleClicked(MouseDoubleClickedEvent event) {
		if (!event.isAlreadyUsed() && this.getParent() != null)
		{
			this.getParent().mouseDoubleClicked(event);
		}
  }
	
	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#mouseClicked(org.fenggui.event.mouse.MouseClickedEvent)
	 */
	public void mouseClicked(MouseClickedEvent event) {
		if (!event.isAlreadyUsed() && this.getParent() != null)
		{
			this.getParent().mouseClicked(event);
		}
  }
	
	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#mouseWheel(org.fenggui.event.mouse.MouseWheelEvent)
	 */
	public void mouseWheel(MouseWheelEvent mouseWheelEvent)
	{
		if (!mouseWheelEvent.isAlreadyUsed() && this.getParent() != null)
		{
			this.getParent().mouseWheel(mouseWheelEvent);
		}
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#keyPressed(org.fenggui.event.KeyPressedEvent)
	 */
	public void keyPressed(KeyPressedEvent keyPressedEvent)
	{
		if (!keyPressedEvent.isAlreadyUsed() && this.getParent() != null)
		{
			this.getParent().keyPressed(keyPressedEvent);
		}
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#keyReleased(org.fenggui.event.KeyReleasedEvent)
	 */
	public void keyReleased(KeyReleasedEvent keyReleasedEvent)
	{
		if (!keyReleasedEvent.isAlreadyUsed() && this.getParent() != null)
		{
			this.getParent().keyReleased(keyReleasedEvent);
		}
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#keyTyped(org.fenggui.event.KeyTypedEvent)
	 */
	public void keyTyped(KeyTypedEvent keyTypedEvent)
	{
		if (!keyTypedEvent.isAlreadyUsed() && this.getParent() != null)
		{
			this.getParent().keyTyped(keyTypedEvent);
		}
	}

	public void positionChanged(PositionChangedEvent event) {
		if (!isVisible() || !isInWidgetTree())
			return;
		
		for (IPositionChangedListener listener : positionHook)
		{
			listener.positionChanged(event);
		}
  }

	public void sizeChanged(SizeChangedEvent event) {
		if (!isVisible() || !isInWidgetTree())
			return;
		
		for (ISizeChangedListener listener : sizeHook)
		{
			listener.sizeChanged(event);
		}

  }
	
	public void addSizeChangedListener(ISizeChangedListener l)
	{
		sizeHook.add(l);
	}

	public void removeSizeChangedListener(ISizeChangedListener l)
	{
		sizeHook.remove(l);
	}
	
	public void addPositionChangedListener(IPositionChangedListener l)
	{
		positionHook.add(l);
	}

	public void removePositionChangedListener(IPositionChangedListener l)
	{
		positionHook.remove(l);
	}
	
	/**
	 * Returns whether this widget is registered in a widget tree.
	 * @return true if registered, else otherwise
	 */
	public boolean isInWidgetTree()
	{
		if (getDisplay() == null)
			return false;
		return true;
	}

	/**
	 * Called when this widget is removed from the widget tree. This means
	 * <ol>
	 * <li>it does not receive event messages anymore,</li>
	 * <li>it is not rendered anymore,</li>
	 * <li>it is not layouted anymore by its parent container</li>
	 * <ol>
	 */
	public void removedFromWidgetTree()
	{
		// does nothing		
	}

	/**
	 * Called when this widget is added to the widget tree.
	 *
	 */
	public void addedToWidgetTree()
	{
		// does nothing
	}

	/**
	 * Draws the Widget in the given Graphics context. The origin of 
	 * the Graphics context 
	 * is set to the origin of the Widgets. A pixel drawn
	 * at 0, 0 would be drawn in the lower left corner of the Widget.
	 * The sequence of commands of this method is outlined in the following
	 * list.
	 * <ol>
	 * <li>Translate the origin to the border space and draw the border</li>
	 * <li>Translate the origin to the background space and draw the background</li>
	 * <li>Translate the origin to the internal space (by adding the padding to the
	 * current origin) and call the associated PaintListener
	 * (<code>getAppearance().getPaintListener().paint(g, this)</code>)</li>
	 * <li>Translate the origin back to the Widgets origin.</li>
	 * <ol>
	 * 
	 */
	/*
	public final void paintEverything(Graphics g) 
	{
	*/
	/*
	if(background != null) 
	{
		background.paint(g, 
			getMargin().getLeft() + getBorder().getLeft(), getMargin().getBottom() + getBorder().getBottom(),
			getPadding().getLeftPlusRight() + getInnerSize().getWidth(),
			getPadding().getBottomPlusTop() + getInnerSize().getHeight());
	}
	
	getBorder().paint(g, 
		getMargin().getLeft(), getMargin().getBottom(), 
		getPadding().getLeftPlusRight() + getInnerSize().getWidth(),
		getPadding().getBottomPlusTop() + getInnerSize().getHeight());
	
	g.translate(getLeftMargins(), getBottomMargins());
	
	paint(g);
	
	g.translate(-getLeftMargins(), -getBottomMargins());
	*/
	//}
	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#paint(org.fenggui.render.Graphics)
	 
	public void paint(Graphics g) 
	{
		if(appearance != null) appearance.paint(g, g.getOpenGL());
	}
	*/

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#layout()
	 */
	public void layout()
	{
		// does nothing
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getDisplayX()
	 */
	public int getDisplayX()
	{
		// FIXME Is it the right thing to do, if parent == null ?
		IBasicContainer parent = getParent();
		if (parent != null)
		{
			return parent.getDisplayX() + getX();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getDisplayY()
	 */
	public int getDisplayY()
	{
		// FIXME Is it the right thing to do, if parent == null ?
		IBasicContainer parent = getParent();
		if (parent != null)
		{
			return parent.getDisplayY() + getY();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getDisplay()
	 */
	public Display getDisplay()
	{
		if (parent == null)
			return null;
		return getParent().getDisplay();
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getWidget(int, int)
	 */
	public IWidget getWidget(int x, int y)
	{
		/*
		if(insideMargin(x, y)) return this;
		return null;
		*/

		if (isVisible() && x > 0 && y > 0 && x < getWidth() && y < getHeight())
			return this;

		return null;
	}

	public void setSizeToMinSize()
	{
		setSize(getMinSize());
	}

	public void setSize(Dimension s)
	{
		Dimension oldSize = size;
		size = s;
		this.sizeChanged(new SizeChangedEvent(this, oldSize, size));
	}

	/**
	 * Moves the Widget by adding x and y to the current position.
	 * Note that usually the layout managers are responsible for positioning
	 * Widgets, so hands off!
	 * @param x pixel to the right 
	 * @param y pixel to the top
	 */
	public void move(int x, int y)
	{
		this.setPosition(new Point(this.getX() + x, this.getY() + y));
	}

	/**
	 * Convenience method to set the x and y coordinates of this
	 * Widget
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void setXY(int x, int y)
	{
		this.setPosition(new Point(x, y));
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#updateMinSize()
	 */
	public void updateMinSize()
	{
	}

	/**
	 * Sets the parent of this widget.
	 * @param parent parent
	 */
	public final void setParent(IBasicContainer parent)
	{
		this.parent = parent;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("--- ");
		sb.append(this.getClass().getSimpleName());
		sb.append(" ---\n");
		sb.append("size    : ");
		sb.append(size);
		sb.append('\n');
		sb.append("position: ");
		sb.append(position);
		sb.append('\n');
		sb.append("minSize : ");
		sb.append(minSize);

		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#focusChanged(org.fenggui.event.FocusEvent)
	 */
	public void focusChanged(FocusEvent focusEvent)
	{
		// does nothing. Supposed to be overriden
	}

	/**
	 * Sets the local <code>y</code> coordinate of this widget. Note that widget coordinates are measured
	 * in the coordinate system of its parent container.
	 * @param y <code>y</code> coordinate
	 */
	public void setY(int y)
	{
		this.setPosition(new Point(getX(), y));
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getY()
	 */
	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getY()
	 */
	public int getY()
	{
		return position.getY();
	}

	public boolean hasFocus()
	{
		Display d = getDisplay();

		if (d == null)
			return false;

		IWidget w = d.getFocusedWidget();

		if (w == null)
			return false;

		return w.equals(this);
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#isTraversable()
	 */
	public boolean isTraversable()
	{
		return false;
	}

	public int getWidth()
	{
		return size.getWidth();
	}

	public int getHeight()
	{
		return size.getHeight();
	}

	public boolean isExpandable()
	{
		return expandable;
	}

	public void setExpandable(boolean expandable)
	{
		this.expandable = expandable;
	}

	public boolean isShrinkable()
	{
		return shrinkable;
	}

	public Dimension getSize()
	{
		return size;
	}

	public Dimension getMinSize()
	{
		return minSize;
	}

	public void setShrinkable(boolean shrinkable)
	{
		this.shrinkable = shrinkable;
	}

	public void setMinSize(Dimension dim)
	{
		minSize.setSize(dim);
	}

	public void paint(Graphics g)
	{
		// does nothing. Supposed to be overridden
	}

	public Point getPosition()
	{
		return position;
	}

	public int getMinWidth()
	{
		return getMinSize().getWidth();
	}

	public int getMinHeight()
	{
		return getMinSize().getHeight();
	}

	public void setMinSize(int minWidth, int minHeight)
	{
		setMinSize(new Dimension(minWidth, minHeight));
	}

	public void setSize(int width, int height)
	{
		setSize(new Dimension(width, height));
	}

	public void setHeight(int height)
	{
		setSize(new Dimension(getWidth(), height));
	}

	public void setWidth(int width)
	{
		setSize(new Dimension(width, getHeight()));
	}

	/**
	 * Sets the local <code>x coordinate of this widget. Note that widget coordinates are measured
	 * in the coordinate system of its parent container.
	 * @param x <code>x coordinate
	 */
	public void setX(int x)
	{
		this.setPosition(new Point(x, getY()));
	}

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getX()
	 */
	/**
	 * Abbreviation for <code>getPosition().getX()</code>.
	 */
	public int getX()
	{
		return position.getX();
	}

	public void setPosition(Point p)
	{
		Point oldPosition = position;
		position = p;
		PositionChangedEvent event = new PositionChangedEvent(this, oldPosition, position);
		this.positionChanged(event);
	}

	public boolean isVisible()
	{
		if (getParent() != null)
			return visible && getParent().isVisible();
		else
			return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
		if (getParent() != null)
		{
//			getParent().updateMinSize();
			getParent().layout();
		}
	}

	public ITooltipData getTooltipData()
	{
		return tooltip;
	}

	public void setTooltipData(ITooltipData data)
	{
		this.tooltip = data;
	}

	public Object getCustomData()
	{
		return data;
	}

	public void setCustomData(Object data)
	{
		this.data = data;
	}
}
