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
 * $Id: ObservableWidget.java 528 2008-05-08 17:27:05Z marcmenghin $
 */
package org.fenggui;

import org.fenggui.event.*;
import org.fenggui.event.key.*;
import org.fenggui.event.mouse.*;

import java.util.ArrayList;

/**
 * Widget that travels between states.
 * <p/>
 * Every Widget can be disabled and enabled. Thus, StateWidget dictates its subclasses to
 * implement <code>getDefaultState</code> and <code>getDisabledtState</code>. The
 * default state is regardes as the 'enabled' state.<br/> <br/> As a convention,
 * subclasses of <code>StateWidget</code> should contain at least the methods
 * <code>getDefaultAppearnace</code> and <code>getDisabledAppearance</code>. Both
 * appearances can then be access through the theme loader by 'defaultAppearance' and
 * 'disbledAppearance'.
 * <p/>
 * TODO Does a container really have to a StateWidget? Only for enabling/disabling?
 *
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2007-11-29
 *         18:32:35 +0100 (Do, 29 Nov 2007) $
 * @version $Revision: 528 $
 * @todo Comment this class... #
 */
public abstract class ObservableWidget extends StandardWidget
{
	private boolean enabled = true;
	private IKeyListener keyTraversalListener = null;
	private IWidget nextWidget = null;
	private IWidget previousWidget = null;
	private IEventListener globalListener;
	private boolean mousePressedOnWidget = false;

	public ObservableWidget()
	{
		super();
		hookEvents();
	}

	public ObservableWidget(ObservableWidget widget)
	{
		super(widget);
		hookEvents();
	}

	private void hookEvents()
	{
		this.addMouseListener(new MouseAdapter()
		{

			/* (non-Javadoc)
			 * @see org.fenggui.event.mouse.MouseAdapter#mousePressed(org.fenggui.event.mouse.MousePressedEvent)
			 */
			@Override
			public void mousePressed(MousePressedEvent mousePressedEvent)
			{
				mousePressedOnWidget = true;
			}

			/* (non-Javadoc)
			 * @see org.fenggui.event.mouse.MouseAdapter#mouseReleased(org.fenggui.event.mouse.MouseReleasedEvent)
			 */
			@Override
			public void mouseReleased(MouseReleasedEvent mouseReleasedEvent)
			{
				mousePressedOnWidget = false;
			}

		});
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setTraversable(boolean b)
	{
		if (b == isTraversable())
			return;

		if (b)
		{
			keyTraversalListener = new KeyAdapter()
			{
				public void keyPressed(KeyPressedEvent keyPressedEvent)
				{
					if (keyPressedEvent.getKeyClass() == Key.TAB)
					{
						IWidget w;
						if (keyPressedEvent.isPressed(Key.SHIFT))
						{
							w = getPreviousTraversableWidget();
						}
						else
						{
							w = getNextTraversableWidget();
						}

						Display disp = getDisplay();
						if (disp != null)
							disp.setFocusedWidget(w);
					}
				}
			};
			keyHook.add(keyTraversalListener);
		}
		else
		{
			keyHook.remove(keyTraversalListener);
			keyTraversalListener = null;
		}
	}

	public IWidget getNextTraversableWidget()
	{
		return getParent().getNextTraversableWidget(this);
	}

	public IWidget getPreviousTraversableWidget()
	{
		return getParent().getPreviousTraversableWidget(this);
	}

	// public IWidget getNextWidget()
	// {
	// return getParent().getNextWidget(this);
	// }
	//
	// public IWidget getPreviousWidget()
	// {
	// return getParent().getPreviousWidget(this);
	// }

	@Override
	public boolean isTraversable()
	{
		return keyTraversalListener != null && enabled && isVisible();
	}

	public void setEnabled(boolean enabled)
	{
		if (this.enabled == enabled)
		{
			// No need to (des)activate the same widget twice or more
			return;
		}

		this.enabled = enabled;

		ActivationEvent e = new ActivationEvent(this, enabled);

		for (IActivationListener l : activationHook)
		{
			l.widgetActivationChanged(e);
		}
	}

	private ArrayList<IActivationListener> activationHook = new ArrayList<IActivationListener>(0);
	private ArrayList<IMouseEnteredListener> mouseEnteredHook = new ArrayList<IMouseEnteredListener>(0);
	private ArrayList<IMouseMovedListener> mouseMovedHook = new ArrayList<IMouseMovedListener>(0);
	private ArrayList<IMouseExitedListener> mouseExitedHook = new ArrayList<IMouseExitedListener>(0);
	private ArrayList<IMousePressedListener> mousePressedHook = new ArrayList<IMousePressedListener>(0);
	private ArrayList<IMouseReleasedListener> mouseReleasedHook = new ArrayList<IMouseReleasedListener>(0);
	private ArrayList<IFocusListener> focusGainedHook = new ArrayList<IFocusListener>(0);
	private ArrayList<IMouseDraggedListener> mouseDraggedHook = new ArrayList<IMouseDraggedListener>(0);
	private ArrayList<IMouseWheelListener> mouseWheeledHook = new ArrayList<IMouseWheelListener>(0);
	private ArrayList<IKeyPressedListener> keyPressedHook = new ArrayList<IKeyPressedListener>(0);
	private ArrayList<IKeyReleasedListener> keyReleasedHook = new ArrayList<IKeyReleasedListener>(0);
	private ArrayList<IKeyTypedListener> keyTypedHook = new ArrayList<IKeyTypedListener>(0);
	private ArrayList<IMouseListener> mouseHook = new ArrayList<IMouseListener>(0);
	private ArrayList<IKeyListener> keyHook = new ArrayList<IKeyListener>(0);

	public void addKeyListener(IKeyListener l)
	{
		keyHook.add(l);
	}

	public void removeKeyListener(IKeyListener l)
	{
		keyHook.remove(l);
	}

	public void addMouseListener(IMouseListener l)
	{
		mouseHook.add(l);
	}

	public void removeMouseListener(IMouseListener l)
	{
		mouseHook.remove(l);
	}

	@Deprecated
	public void addKeyReleasedListener(IKeyReleasedListener l)
	{
		keyReleasedHook.add(l);
	}

	@Deprecated
	public void removeKeyReleasedListener(IKeyReleasedListener l)
	{
		keyReleasedHook.remove(l);
	}

	@Deprecated
	public void addKeyPressedListener(IKeyPressedListener l)
	{
		keyPressedHook.add(l);
	}

	@Deprecated
	public void removeKeyPressedListener(IKeyPressedListener l)
	{
		keyPressedHook.remove(l);
	}

	@Deprecated
	public void addKeyTypedListener(IKeyTypedListener l)
	{
		keyTypedHook.add(l);
	}

	@Deprecated
	public void removeKeyTypedListener(IKeyTypedListener l)
	{
		keyTypedHook.remove(l);
	}

	@Deprecated
	public void addMouseDraggedListener(IMouseDraggedListener l)
	{
		mouseDraggedHook.add(l);
	}

	@Deprecated
	public void removeMouseDraggedListener(IMouseDraggedListener l)
	{
		mouseDraggedHook.remove(l);
	}

	@Deprecated
	public void addMouseMovedListener(IMouseMovedListener l)
	{
		mouseMovedHook.add(l);
	}

	@Deprecated
	public void removeMouseMovedListener(IMouseMovedListener l)
	{
		mouseMovedHook.remove(l);
	}

	@Deprecated
	public void addMouseReleasedListener(IMouseReleasedListener l)
	{
		mouseReleasedHook.add(l);
	}

	@Deprecated
	public void removeMouseReleasedListener(IMouseReleasedListener l)
	{
		mouseReleasedHook.remove(l);
	}

	@Deprecated
	public void addMousePressedListener(IMousePressedListener l)
	{
		mousePressedHook.add(l);
	}

	@Deprecated
	public void removeMousePressedListener(IMousePressedListener l)
	{
		mousePressedHook.remove(l);
	}

	@Deprecated
	public void addMouseExitedListener(IMouseExitedListener l)
	{
		mouseExitedHook.add(l);
	}

	@Deprecated
	public void removeMouseExitedListener(IMouseExitedListener l)
	{
		mouseExitedHook.remove(l);
	}

	@Deprecated
	public void addMouseEnteredListener(IMouseEnteredListener l)
	{
		mouseEnteredHook.add(l);
	}

	@Deprecated
	public void removeMouseEnteredListener(IMouseEnteredListener l)
	{
		mouseEnteredHook.remove(l);
	}

	public void addFocusListener(IFocusListener l)
	{
		focusGainedHook.add(l);
	}

	public void removeFocusListener(IFocusListener l)
	{
		focusGainedHook.remove(l);
	}

	@Deprecated
	public void addMouseWheelListener(IMouseWheelListener l)
	{
		mouseWheeledHook.add(l);
	}

	@Deprecated
	public void removeMouseWheelListener(IMouseWheelListener l)
	{
		mouseWheeledHook.remove(l);
	}

	public void mouseEntered(MouseEnteredEvent mouseEnteredEvent)
	{
		if (!enabled || !isVisible() || !isInWidgetTree())
			return;

		for (IMouseEnteredListener l : mouseEnteredHook)
		{
			l.mouseEntered(mouseEnteredEvent);
		}

		for (IMouseListener l : mouseHook)
		{
			l.mouseEntered(mouseEnteredEvent);
		}
	}

	public void addActivationListener(IActivationListener l)
	{
		activationHook.add(l);
	}

	public void removeActivationListener(IActivationListener l)
	{
		activationHook.remove(l);
	}

	public void mouseExited(MouseExitedEvent mouseExitedEvent)
	{
		if (!enabled || !isVisible() || !isInWidgetTree())
			return;

		for (IMouseExitedListener l : mouseExitedHook)
		{
			l.mouseExited(mouseExitedEvent);
		}

		for (IMouseListener l : mouseHook)
		{
			l.mouseExited(mouseExitedEvent);
		}
	}

	public void mousePressed(MousePressedEvent mousePressedEvent)
	{
		if (!enabled || !isVisible() || !isInWidgetTree())
			return;

		for (IMousePressedListener l : mousePressedHook)
		{
			l.mousePressed(mousePressedEvent);
		}

		for (IMouseListener l : mouseHook)
		{
			l.mousePressed(mousePressedEvent);
		}

		super.mousePressed(mousePressedEvent);
	}

	public void mouseMoved(int displayX, int displayY)
	{
		if ((mouseHook.isEmpty() && mouseMovedHook.isEmpty()) || !enabled || !isVisible() || !isInWidgetTree())
			return;

		MouseMovedEvent e = new MouseMovedEvent(null, displayX, displayY, getDisplay().getKeyPressTracker().getModifiers());

		for (IMouseMovedListener l : mouseMovedHook)
		{
			l.mouseMoved(e);
		}

		for (IMouseListener l : mouseHook)
		{
			l.mouseMoved(e);
		}

		super.mouseMoved(displayX, displayY);
	}

	public void mouseDragged(MouseDraggedEvent mouseDraggedEvent)
	{
		if (!enabled || !isVisible() || !isInWidgetTree())
			return;

		for (IMouseDraggedListener l : mouseDraggedHook)
		{
			l.mouseDragged(mouseDraggedEvent);
		}

		for (IMouseListener l : mouseHook)
		{
			l.mouseDragged(mouseDraggedEvent);
		}
		super.mouseDragged(mouseDraggedEvent);
	}

	/* (non-Javadoc)
   * @see org.fenggui.Widget#mouseClicked(org.fenggui.event.mouse.MouseClickedEvent)
   */
  @Override
  public void mouseClicked(MouseClickedEvent event) {
		if (!enabled || !isVisible() || !isInWidgetTree())
			return;
		
		for (IMouseListener l : mouseHook)
		{
			l.mouseClicked(event);
		}
		
	  super.mouseClicked(event);
  }

	/* (non-Javadoc)
   * @see org.fenggui.Widget#mouseDoubleClicked(org.fenggui.event.mouse.MouseDoubleClickedEvent)
   */
  @Override
  public void mouseDoubleClicked(MouseDoubleClickedEvent event) {
		if (!enabled || !isVisible() || !isInWidgetTree())
			return;
		
		for (IMouseListener l : mouseHook)
		{
			l.mouseDoubleClicked(event);
		}
		
	  super.mouseDoubleClicked(event);
  }

	public void mouseReleased(MouseReleasedEvent mouseReleasedEvent)
	{
		if (!enabled || !isVisible() || !isInWidgetTree())
			return;

		for (IMouseReleasedListener l : mouseReleasedHook)
		{
			l.mouseReleased(mouseReleasedEvent);
		}

		for (IMouseListener l : mouseHook)
		{
			l.mouseReleased(mouseReleasedEvent);
		}
		super.mouseReleased(mouseReleasedEvent);
	}

	public void keyPressed(KeyPressedEvent keyPressedEvent)
	{
		if (!enabled || !isVisible() || !isInWidgetTree())
			return;

		for (IKeyPressedListener l : keyPressedHook)
		{
			l.keyPressed(keyPressedEvent);
		}

		for (IKeyListener l : keyHook)
		{
			l.keyPressed(keyPressedEvent);
		}
		super.keyPressed(keyPressedEvent);
	}

	public void keyReleased(KeyReleasedEvent keyReleasedEvent)
	{
		if (!enabled || !isVisible() || !isInWidgetTree())
			return;

		for (IKeyReleasedListener l : keyReleasedHook)
		{
			l.keyReleased(keyReleasedEvent);
		}

		for (IKeyListener l : keyHook)
		{
			l.keyReleased(keyReleasedEvent);
		}
		super.keyReleased(keyReleasedEvent);
	}

	public void keyTyped(KeyTypedEvent keyTypedEvent)
	{
		if (!enabled || !isVisible() || !isInWidgetTree())
			return;

		for (IKeyTypedListener l : keyTypedHook)
		{
			l.keyTyped(keyTypedEvent);
		}

		for (IKeyListener l : keyHook)
		{
			l.keyTyped(keyTypedEvent);
		}
		super.keyTyped(keyTypedEvent);
	}

	public void focusChanged(FocusEvent focusGainedEvent)
	{
		if (!enabled || !isVisible() || !isInWidgetTree())
			return;

		for (IFocusListener l : focusGainedHook)
		{
			l.focusChanged(focusGainedEvent);
		}
		super.focusChanged(focusGainedEvent);
	}

	public void mouseWheel(MouseWheelEvent e)
	{
		if (!enabled || !isVisible() || !isInWidgetTree())
			return;

		for (IMouseWheelListener l : mouseWheeledHook)
		{
			l.mouseWheel(e);
		}

		for (IMouseListener l : mouseHook)
		{
			l.mouseWheel(e);
		}
		super.mouseWheel(e);
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer(super.toString());
		sb.append("\n");
		sb.append("enabled : ");
		sb.append(enabled);

		return sb.toString();
	}

	/** @return Returns the nextWidget. */
	public IWidget getNextWidget()
	{
		return nextWidget;
	}

	/** @param nextWidget The nextWidget to set. */
	public void setNextWidget(IWidget nextWidget)
	{
		this.nextWidget = nextWidget;
	}

	/** @return Returns the previousWidget. */
	public IWidget getPreviousWidget()
	{
		return previousWidget;
	}

	/** @param previousWidget The previousWidget to set. */
	public void setPreviousWidget(IWidget previousWidget)
	{
		this.previousWidget = previousWidget;
	}

	/*
	  * (non-Javadoc)
	  *
	  * @see org.fenggui.Widget#addedToWidgetTree()
	  */
	@Override
	public void addedToWidgetTree()
	{
		super.addedToWidgetTree();
		if (getDisplay() != null)
		{
			// Send a mouse released event to the widget if the mouse was pressed here
			globalListener = new IEventListener()
			{
				public void processEvent(Event event)
				{
					if (event instanceof MouseReleasedEvent)
					{
						MouseReleasedEvent mouseReleasedEvent = (MouseReleasedEvent) event;
						if (mouseReleasedEvent.getSource() != ObservableWidget.this && mousePressedOnWidget)
						{
							ObservableWidget.this.mouseReleased(mouseReleasedEvent);
						}
					}
				}
			};
			getDisplay().addGlobalEventListener(globalListener);
		}
	}

	/*
	  * (non-Javadoc)
	  *
	  * @see org.fenggui.Widget#removedFromWidgetTree()
	  */
	@Override
	public void removedFromWidgetTree()
	{
		super.removedFromWidgetTree();
		if (getDisplay() != null)
		{
			if (globalListener != null)
			{
				getDisplay().removeGlobalEventListener(globalListener);
			}
		}
	}
}
