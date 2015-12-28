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
 * Created on Dec 11, 2006
 * $Id: StandardWidget.java 556 2008-06-02 17:55:49Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;

import org.fenggui.appearance.IAppearance;
import org.fenggui.appearance.SpacingAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;

/**
 * Basic class for widgets that distinguish between appearance and behavior. This separation allows
 * to apply themes because themes only affect the appearance of widgets.<br/> <br/> Subclasses
 * should should call <code>setupTheme(this.getClass())</code> in their constructor.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2007-08-11 13:20:15 +0200
 *         (Sa, 11 Aug 2007) $
 * @version $Revision: 556 $
 */
public abstract class StandardWidget extends Widget implements IXMLStreamable
{

	public abstract IAppearance getAppearance();

	// public abstract void setAppearance(IAppearance appearance);
	public abstract void paintContent(Graphics g, IOpenGL gl);

	/**
	 * Calculates the minimal size the Content of this widget uses.
	 * 
	 * @return The minimal size of the content.
	 */
	public abstract Dimension getMinContentSize();

	public StandardWidget()
	{
		super();
	}

	public StandardWidget(StandardWidget widget)
	{
		super(widget);
	}

	public IWidget getWidget(int x, int y)
	{
		if (!isVisible())
			return null;

		IAppearance appearance = getAppearance();
		if (appearance != null && appearance instanceof SpacingAppearance)
		{
			if (((SpacingAppearance) appearance).insideMargin(x, y))
			{
				return this;
			}
			else
			{
				return null;
			}
		}
		return super.getWidget(x, y);
	}

	public void paint(Graphics g)
	{
		if (isVisible())
		{
			if (getAppearance() != null)
				getAppearance().paint(g, g.getOpenGL());
			else
				paintContent(g, g.getOpenGL());
		}
	}

	public void updateMinSize()
	{
		Dimension minSize;
		if (getAppearance() != null)
			minSize = getAppearance().getMinSizeHint();
		else
			minSize = new Dimension(1,1);

		if (minSize.equals(getMinSize()))
			return;
		
		setMinSize(minSize);
		
		if (getSize().getWidth() < getMinSize().getWidth())
		{
			if (getSize().getHeight() < getMinSize().getHeight())
			{
				this.setSize(getMinSize().getWidth(), getMinSize().getHeight());
			} else {
				this.setSize(getMinSize().getWidth(), getSize().getHeight());
			}
		} else {
			if (getSize().getHeight() < getMinSize().getHeight())
			{
				this.setSize(getSize().getWidth(), getMinSize().getHeight());
			}
		}

		
		if (getParent() != null && getParent() instanceof ScrollContainer)
		{
			((ScrollContainer) getParent()).layout();
		}
		else if (getParent() != null)
			getParent().updateMinSize();
		
	}

	/**
	 * The theme is setup over the FengGUI class as this adds more flexibility to the theming process.
	 * No widget should use this method anymore as it will be removed soon.
	 * 
	 * <p>
	 * Applys the current theme to this widget. Since <code>setupTheme</code> is usually called in
	 * the constructors of widgets, <code>setupTheme</code> is called by each subclass of
	 * <code>Widget</code>. To avoid that the extended widgets override the theme settings of its
	 * subclasses, each widget calls <code>setupTheme</code> with <code>this.getClass()</code> as
	 * parameter. This way <code>setupTheme</code> can ensure that only the most specific widget
	 * applys the theme.<br/> So what this method does is
	 * </p>
	 * 
	 * <pre>
	 * if (this.getClass().equals(clazz))
	 *   FengGUI.getTheme().setUp(this);
	 * </pre>
	 * 
	 * @param clazz
	 *          the class type of the calling widget
	 */
	@Deprecated
	protected final void setupTheme(Class<?> clazz)
	{
		if (this.getClass().equals(clazz))
			FengGUI.getTheme().setUp(this);
	}

	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		setExpandable(stream.processAttribute("expandable", isExpandable(), true));
		setShrinkable(stream.processAttribute("shrinkable", isShrinkable(), true));
		setWidth(stream.processAttribute("width", getWidth(), 10));
		setHeight(stream.processAttribute("height", getHeight(), 10));
		setMinSize(stream.processAttribute("minWidth", getMinWidth(), 10), stream.processAttribute("minHeight",
			getMinHeight(), 10));
		setX(stream.processAttribute("x", getX(), 10));
		setY(stream.processAttribute("y", getY(), 10));

		if (getAppearance() instanceof IXMLStreamable)
		{
			if (stream.startSubcontext("Appearance"))
			{
				((IXMLStreamable) getAppearance()).process(stream);
				stream.endSubcontext();
			}
		}

		//		// need to rethink the way tooltips work here
		//		stream.processChild((IXMLStreamable)this.getTooltipData(), XMLTheme.TOOLTIPDATA_REGISTRY);
	}

	public String getUniqueName()
	{
		return GENERATE_NAME;
	}
}
