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
 * $Id: Slider.java 556 2008-06-02 17:55:49Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.fenggui.appearance.DefaultAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.event.ActivationEvent;
import org.fenggui.event.IActivationListener;
import org.fenggui.event.IDragAndDropListener;
import org.fenggui.event.ISliderMovedListener;
import org.fenggui.event.SliderMovedEvent;
import org.fenggui.event.key.Key;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;

/**
 * Implementation of a slider Widget. Sliders are used to 
 * quickly (and roughly) select numbers in a certain range. For example
 * to select a percentage between 0% and 100%. Of course other  
 * values are possible as well but the implementor has to map the numbers
 * into the desired domain. By moving the Slider the user modifies the
 * slider <code>value</code> between 0 and 1. This value can be queried through the
 * <code>getValue()</code> method.
 * 
 * @author Johannes Schaback
 * @dedication The Beatles - Sgt. Peppers Lonely Hearts Club Band
 */
public class Slider extends StatefullWidget<DefaultAppearance> implements IBasicContainer
{
	private ArrayList<ISliderMovedListener> sliderMovedHook = new ArrayList<ISliderMovedListener>();
	
	/**
	 * SliderMovedEvent event that is used when the according event
	 * occurs. I buffer it here so that we do not need to
	 * re-instantiate every time the slider was dragged.
	 */
	private SliderMovedEvent sliderMoved = new SliderMovedEvent(this);

	/**
	 * Value of the Slider. Ranges from 0 to 1.
	 */
	private double value = 0;
	private double buttonSize = 0;
	private Button sliderButton = null;
	/**
	 * Flag indicating if the Slider is moved vertically oder horizontally.
	 */
	private boolean horizontal = true;

	private double clickJump = 0.03;

	private IDragAndDropListener dndListener = new SliderDnDListener();

	/**
	 * Creates a new Slider object.
	 * @param horizontal flag that indicates whether the Slider is moved
	 * vertically or horizontally.
	 */
	public Slider(boolean horizontal) 
	{
		super();
		this.horizontal = horizontal;

		sliderButton = new Button();
		sliderButton.setParent(this);
		sliderButton.setSize(10,10);
		sliderButton.setXY(0, 0);

		this.setAppearance(new DefaultAppearance(this));
		
		setTraversable(true);
		
		buildListeners();
		updateMinSize();
	}

	/**
	 * Copy constructor
	 * 
	 * @param slider
	 */
	public Slider(Slider slider)
	{
		super(slider);
		this.horizontal = slider.horizontal;
		sliderButton = new Button(slider.sliderButton);
		sliderButton.setParent(this);
		this.setAppearance(new DefaultAppearance(this, slider.getAppearance()));
		setTraversable(true);
		
		buildListeners();
		updateMinSize();
	}
	
	public Slider(InputOnlyStream stream) throws IOException, IXMLStreamableException
	{
		process(stream);
	}	
	
	/**
	 * Returns the selected value according to the position of
	 * the slider in a range between 0 and 1. For horizontal Sliders,
	 * the 0 value is at the left most side. For vertical Sliders, the
	 * 0 value is at the bottom.
	 * @return current value.
	 */
	public double getValue() 
	{
		return value;
	}

	/**
	 * Sets the value of the Slider. The value stands for his position. 
	 * The value has to be between 0 and
	 * 1. Values outside of this domain get cropped. Emits a
	 * SliderMove signal.
	 * @param position the value of the slider.
	 */
	public void setValue(double position) 
	{
		if(position > 1) position = 1;
		if(position < 0) position = 0;
		this.value = position;

		if(horizontal)
		{
			sliderButton.setX((int)((double)(getAppearance().getContentWidth() - sliderButton.getWidth())*value));
		}
		else
		{
			sliderButton.setY((int)((double)(getAppearance().getContentHeight() - sliderButton.getHeight())*value));
		}

		//System.out.println(horizontal+" "+value+" "+sliderButton+" "+(double)(getInnerSize().getWidth() - sliderButton.getWidth()));
		fireSliderMovedEvent();
	}



	public Button getSliderButton() {
		return sliderButton;
	}

	private void buildListeners() {
		addActivationListener(new IActivationListener() {

			public void widgetActivationChanged(ActivationEvent activationEvent)
			{
				boolean enabled = activationEvent.isEnabled();

				sliderButton.setEnabled(enabled);
				
				if (enabled) {
					if(getDisplay() != null) {
						getDisplay().addDndListener(dndListener);
					}
				} else {
					if(getDisplay() != null) {
						getDisplay().removeDndListener(dndListener);
					}
				}
			}});	
	}
	
	/*
	@Override
	public void setValidHeight(int height) 
	{
		if(horizontal)
		{
			setHeight(getMinHeight());
		}
		else
		{
			super.setValidHeight(height);
		}
	}

	@Override
	public void setValidWidth(int width) 
	{
		if(horizontal)
		{
			super.setValidWidth(width);
		}
		else
		{
			setWidth(getMinWidth());
		}
	}
	 */

	@Override
	public IWidget getWidget(int x, int y) 
	{
		if(!getAppearance().insideMargin(x, y)) 
		{
			return null;
		}

		if(sliderButton.getSize().contains(x-sliderButton.getX(), y-sliderButton.getY()))
			return sliderButton;

		return this;
	}

	private class SliderDnDListener implements IDragAndDropListener 
	{
		private int deltaX = 0;
		private int deltaY = 0;
		private int cacheDisplayX = -1; // only for peformance reasons
		private int cacheDisplayY = -1;

		public void select(int x, int y, Set<Key> modifiers) 
		{
			cacheDisplayX = getDisplayX();
			cacheDisplayY = getDisplayY();
			x -= cacheDisplayX;
			y -= cacheDisplayY;
			//pressed = hitsHorizontalSlider(x, y);
			deltaX = getSliderStart()-x;
			deltaY = getSliderStart()-y;
		}

		public void drag(int x, int y, Set<Key> modifiers) 
		{
			if(horizontal) 
			{
				x -= cacheDisplayX;
				y -= cacheDisplayY;

				x += deltaX;

				setValue((double)x/(double)(getWidth() - sliderButton.getWidth()));
				
				fireSliderMovedEvent();
				
			} 
			else 
			{
				x -= cacheDisplayX;
				y -= cacheDisplayY;
				
				y += deltaY;
				
				setValue((double)y/(double)(getHeight() - sliderButton.getHeight()));
				
				fireSliderMovedEvent();

			}
		}

		public void drop(int x, int y, IWidget dropOn, Set<Key> modifiers)
		{
			//pressed = false;
		}

		public boolean isDndWidget(IWidget w, int x, int y)
		{
			return w.equals(sliderButton);
		}
	}

	@Override
	public void addedToWidgetTree() 
	{
		if(getDisplay() != null && isEnabled())
			getDisplay().addDndListener(dndListener);
	}

	@Override
	public void removedFromWidgetTree() 
	{
		if(getDisplay() != null)
			getDisplay().removeDndListener(dndListener);
	}

	/**
	 * Returns the start position of the Slider Button within the
	 * Slider Widget. This method a x-coordinate value if the Slider is set 
	 * to horizontal, a y-coordinate value otherwise. 
	 * @return the start position.
	 */
	public int getSliderStart() 
	{
		if(horizontal)
			return (int)(value*(double)(getWidth()-sliderButton.getWidth()));
		else 
			return (int)(value*(double)(getHeight()-sliderButton.getHeight()));
	}
	
	/**
	 * Indicates whether the Slider is dragged horizontally.
	 * @return true if Slider moves horizontally, false if vertically
	 */
	public boolean isHorizontal() 
	{
		return horizontal;
	}

	public double getClickJump() 
	{
		return clickJump;
	}

	public void setClickJump(double clickJump) 
	{
		this.clickJump = clickJump;
	}
	
	public void layout() 
	{
		int contentHeight = getAppearance().getContentHeight();
		int contentWidth = getAppearance().getContentWidth();
		
		if(horizontal)
		{
			int width = Math.max ((int)(((double)getAppearance().getContentWidth())*buttonSize), sliderButton.getMinWidth());
			if(width < 15) width = 15;
			sliderButton.setWidth(width);
			sliderButton.setHeight(Math.max(contentHeight, 15));
			sliderButton.setY(contentHeight / 2 - sliderButton.getHeight() / 2);
			sliderButton.setX((int)((double)(getAppearance().getContentWidth() - sliderButton.getWidth())*value));
		}
		else
		{
			int height = Math.max ((int)(((double)getAppearance().getContentHeight())*buttonSize), sliderButton.getMinHeight());
			if(height < 15) height = 15;
			sliderButton.setWidth(Math.max(contentWidth, 15));
			sliderButton.setHeight(height);
			sliderButton.setX(contentWidth / 2 - sliderButton.getWidth() / 2);
			sliderButton.setY((int)((double)(getAppearance().getContentHeight() - sliderButton.getHeight())*value));
		}
	}

    @Override
    public void mousePressed(MousePressedEvent mousePressedEvent)
	{
    	super.mousePressed(mousePressedEvent);
    	if (isEnabled()) {
    		if (isHorizontal())	
    		{
				int x = mousePressedEvent.getDisplayX() - getDisplayX();
				double size = (double) sliderButton.getWidth() / (double) getAppearance().getContentWidth();
				if (x < getSliderStart())
				{
					setValue(getValue() - size);
				}
				else
				{
					setValue(getValue() + size);
				}
    		} 
    		else
    		{
    			int y = mousePressedEvent.getDisplayY() - getDisplayY();
    			double size = (double) sliderButton.getHeight() / (double) getAppearance().getContentHeight();

    			if (y < getSliderStart())
    			{
    				setValue(getValue() - size);
    			}
    			else
    			{
    				setValue(getValue() + size);
    			}		
    		}
    	}
	}
    

    /**
     * Set the slider button size in relation to the available free space.
     * @param d the size (between 0 and 1)
     */
	public void setSize(double d) 
	{
		if(d < 0) d = 0;
		else if(d > 1) d = 1;
	
		buttonSize = d;
		
		if(horizontal)
		{
			int size = (int)(((double)getAppearance().getContentWidth())*buttonSize);
			if(size < 15) size = 15;
			sliderButton.setWidth(size);
		}
		else
		{
			int size = (int)(((double)getAppearance().getContentHeight())*buttonSize);
			if(size < 15) size = 15;
			sliderButton.setHeight(size);
		}
		
		// update the slider button position!
		setValue(getValue());
	}
	
	public IWidget getNextTraversableWidget(IWidget start) 
	{
		return getParent().getNextTraversableWidget(this);
	}

	public IWidget getPreviousTraversableWidget(IWidget start) 
	{
		return getParent().getPreviousTraversableWidget(this);
	}	
	
	public IWidget getNextWidget(IWidget start)
	{
		return getParent().getNextWidget(this);
	}
   
	public IWidget getPreviousWidget(IWidget start)
	{
		return getParent().getPreviousWidget(this);
	}

	@Override
	public void updateMinSize()
	{
		setMinSize(this.getAppearance().getMinSizeHint());
		
		if(getParent() != null) getParent().updateMinSize();
	}
	
	/**
	 * Add a {@link ISliderMovedListener} to the widget. The listener can be added only once.
	 * @param l Listener
	 */
	public void addSliderMovedListener(ISliderMovedListener l)
	{
		if (!sliderMovedHook.contains(l))
		{
			sliderMovedHook.add(l);
		}
	}
	
	/**
	 * Add the {@link ISliderMovedListener} from the widget
	 * @param l Listener
	 */
	public void removeSliderMovedListener(ISliderMovedListener l)
	{
		sliderMovedHook.remove(l);
	}
	
	/**
	 * Fires the {@link SliderMovedEvent} 
	 */
	private void fireSliderMovedEvent()
	{
		for(ISliderMovedListener l: sliderMovedHook)
		{
			l.sliderMoved(sliderMoved);
		}
	}

	@Override
	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		super.process(stream);
		
		// we shall not override the 'horizontal' field!
		
		if(horizontal)
		{
			stream.processInherentChild("HorizontalSliderButton", sliderButton);
			stream.processInherentChild("HorizontalAppearance", getAppearance());
		}
		else
		{
			stream.processInherentChild("VerticalSliderButton", sliderButton);
			stream.processInherentChild("VerticalAppearance", getAppearance());
		}

	}

	@Override
	public Dimension getMinContentSize()
	{
		//Size in slider direction + 1 so it is movable from min to max value.
		if(horizontal) 
		{
			return new Dimension(sliderButton.getMinWidth() + 1, sliderButton.getMinHeight());
		} 
		else 
		{
			return new Dimension(sliderButton.getMinWidth(), sliderButton.getMinHeight() + 1);
		}
	}

	@Override
	public void paintContent(Graphics g, IOpenGL gl)
	{
		// If the slider is part of a scrollbar, 
		// we don't display its button when Disabled
		if (isEnabled() || !(getParent() instanceof ScrollBar)) {
			Button sliderButton = getSliderButton();
		
			g.translate(sliderButton.getX(), sliderButton.getY());
			sliderButton.paint(g);
			g.translate(-sliderButton.getX(), -sliderButton.getY());
		}		
	}

	public boolean isKeyTraversalRoot() {
	  return false;
  }
	
}
