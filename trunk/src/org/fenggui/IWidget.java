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
 * Created on Nov 12, 2006
 * $Id: IWidget.java 547 2008-05-27 11:02:01Z marcmenghin $
 */
package org.fenggui;

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


public interface IWidget
{

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getLayoutData()
	 */
	public abstract ILayoutData getLayoutData();


	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getParent()
	 */
	public abstract IBasicContainer getParent();

	/**
	 * Called when the mouse enters this Widget
	 * @param mouseEnteredEvent event type
	 */
	public abstract void mouseEntered(MouseEnteredEvent mouseEnteredEvent);


	/**
	 * Called when the mouse exits this Widget
	 * @param mouseExitedEvent event type
	 */
	public abstract void mouseExited(MouseExitedEvent mouseExitedEvent);


	/**
	 * Called when a mouse button is pressed on this Widget.
	 * @param mp event type
	 */
	public abstract void mousePressed(MousePressedEvent mp);


	/**
	 * Called when the mouse is moved over this Widget. Do not
	 * make computationally expensive things here because this
	 * method is constantly called by Display
	 * 
	 * @todo evaluate the idea to introduce a flag for Containers that
	 * indicate whether the Container holds mouse-over sensitive Widgets
	 * to avoid fining the underlying Widget on every mouse move event. #
	 * 
	 * @param displayX the x coordinate of the mouse cursor in display
	 * coordinates
	 * @param displayY the y coordinate of the mouse cursor in display
	 * coordinates
	 */
	public abstract void mouseMoved(int displayX, int displayY);


	/**
	 * Called when the mouse is dragged (moved while pressing a
	 * mouse button down) over this Widget.
	 * 
	 * @todo mouseDragged is acutally only a special case of
	 * mouseMoved. Consider to merge both events #
	 * @param mp event type
	 */
	public abstract void mouseDragged(MouseDraggedEvent mp);


	/**
	 * Called when a previously pressed mouse button is 
	 * released
	 * on this Widget.
	 * @param mr event type
	 */
	public abstract void mouseReleased(MouseReleasedEvent mr);

	/**
	 * Called when the user clicks one something.
	 * 
	 * @param event event type
	 */
	public abstract void mouseClicked(MouseClickedEvent event);

	/**
	 * Called when the mouse button is pressed twice in a short time.
	 * 
	 * @param event event type
	 */
	public abstract void mouseDoubleClicked(MouseDoubleClickedEvent event);

	/**
	 * Called when there is a mouse wheel event
	 * @param mouseWheelEvent event type
	 */
	public abstract void mouseWheel(MouseWheelEvent mouseWheelEvent);


	/**
	 * Called when a key is pressed on the keyboard providing
	 * this Widget has the focus.
	 * 
	 * @param keyPressedEvent event type
	 */
	public abstract void keyPressed(KeyPressedEvent keyPressedEvent);


	/**
	 * Called when a previously pressed key on the keyboard
	 * is released, providing this Widget has the
	 * focus.
	 * @param keyReleasedEvent event type
	 */
	public abstract void keyReleased(KeyReleasedEvent keyReleasedEvent);
	
	/**
	 * Called when a key is Typed.
	 * @param keyTypedEvent event type
	 */
	public abstract void keyTyped(KeyTypedEvent keyTypedEvent);

	/**
	 * Called if the size changed.
	 * 
	 * @param event
	 */
	public abstract void sizeChanged(SizeChangedEvent event);
	
	/**
	 * Called of the position changed.
	 * @param event
	 */
	public abstract void positionChanged(PositionChangedEvent event);

	public abstract void addSizeChangedListener(ISizeChangedListener l);

	public abstract void removeSizeChangedListener(ISizeChangedListener l);
	
	public abstract void addPositionChangedListener(IPositionChangedListener l);

	public abstract void removePositionChangedListener(IPositionChangedListener l);
	
	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#paint(org.fenggui.render.Graphics)
	 */
	//public abstract void paint(Graphics g);


	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getDisplayX()
	 */
	public abstract int getDisplayX();


	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getDisplayY()
	 */
	public abstract int getDisplayY();

	//public IAppearance getAppearance();

	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getDisplay()
	 */
	public abstract Display getDisplay();


	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getWidget(int, int)
	 */
	public abstract IWidget getWidget(int x, int y);


	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#updateMinSize()
	 */
	public abstract void updateMinSize();


	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#getBackground()
	 */
	//public abstract Background getBackground();


	/**
	 * Called when a widget gets the focus
	 *
	 */
	public abstract void focusChanged(FocusEvent focusEvent);

	public Dimension getSize();
	
	public Dimension getMinSize();


	public abstract int getX();
	public abstract int getY();
	public abstract Point getPosition();
	public abstract void setX(int x);
	public abstract void setY(int y);


	/* (non-Javadoc)
	 * @see org.fenggui.IWidget#isTraversable()
	 */
	public abstract boolean isTraversable();

/*
	public abstract int getWidth();

	public abstract int getHeight();
*/

	public abstract void removedFromWidgetTree();


	public abstract void setParent(IBasicContainer object);


	public abstract void addedToWidgetTree();

	/**
	 * Returns whether this widget is registered in a widget tree.
	 * @return true if registered, else otherwise
	 */
	public abstract boolean isInWidgetTree();
	

	public abstract void layout();
	/*

	public abstract int getValidMinHeight();


	public abstract int getValidMinWidth();

	
	public abstract void setValidWidth(int width);
	public abstract void setValidHeight(int height);

	
	public abstract void setHeight(int height);
	public abstract void setWidth(int width);
	
	
	public abstract void setSize(int width, int height);
*/

	public abstract void setSize(Dimension d);
	
	/**
	 * Checks if the widget is set to be visible or not. Doesn't check if the
	 * widget is actually drawn on screen.
	 * 
	 * @return true if the widget should be visible.
	 */
	public abstract boolean isVisible();
	
	/**
	 * Sets the visibility state of the widget. If set to false the widget will
	 * not be drawn and will not receive any events but takes its space (remains
	 * in the widget-tree). Also all child elements will not be drawn or receive
	 * any events.
	 * 
	 * @param visible True if the widget should be drawn, false otherwise.
	 */
	public abstract void setVisible(boolean visible);
	
	/**
	 * Returns the TooltipData object associated with this widget or null if there
	 * is none.
	 * 
	 * @return A TooltipData object or null.
	 */
	public abstract ITooltipData getTooltipData();
	
	/**
	 * Sets the tooltipdata object for this widget. Set it to null to remove the
	 * tooltipdata object.
	 * 
	 * @param data tooltip data object or null.
	 */
	public abstract void setTooltipData(ITooltipData data);
	
	public abstract boolean isExpandable();
	public abstract boolean isShrinkable();

	public abstract void paint(Graphics g);
	
	/**
	 * Set a custom data object. This is a method to associate arbitrary data
	 * with a widget. It is not removed or overwritten by FengGUI. The Idea is
	 * to use a HashMap with defined keys to reference data objects.
	 * 
	 * @param data the data object
	 * @see org.fenggui.util.Util
	 */
	public abstract void setCustomData(Object data);
	
	/**
	 * Returns the data object associated with this widget.
	 * 
	 * @return a data object.
	 * @see org.fenggui.util.Util
	 */
	public abstract Object getCustomData();
}