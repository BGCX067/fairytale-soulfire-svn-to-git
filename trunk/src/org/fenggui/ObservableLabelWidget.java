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
 * $Id: ObservableLabelWidget.java 549 2008-05-27 18:51:11Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;

import org.fenggui.appearance.LabelAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.binding.render.text.IComplexTextRenderer;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * 
 * State-enabled Widget that helps drawing text and images.
 * 
 * @todo Comment this class... #
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2007-11-28
 *         11:16:38 +0100 (Mi, 28 Nov 2007) $
 * @version $Revision: 549 $
 */
public class ObservableLabelWidget extends StatefullWidget<LabelAppearance> implements ILabel
{
	private Pixmap pixmap = null;
	private String text = "";

	protected ISizeChangedListener sizeChangedListener = new ISizeChangedListener()
	{

		public void sizeChanged(SizeChangedEvent event)
		{
			updateMinSize();
		}

	};

	public ObservableLabelWidget()
	{
		super();
		this.setAppearance(new LabelAppearance(this));
	}

	/**
	 * Copy constructor
	 * 
	 * @param widget
	 */
	public ObservableLabelWidget(ObservableLabelWidget widget)
	{
		super(widget);

		if (widget != null)
		{
			this.text = widget.getText();
			this.pixmap = widget.getPixmap();
			this.setAppearance(new LabelAppearance(this, widget.getAppearance()));
			this.getAppearance().getData().setText(this.text);
			this.getAppearance().addMinSizeChangedListener(sizeChangedListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.StatefullWidget#setAppearance(org.fenggui.appearance.DecoratorAppearance)
	 */
	@Override
	public void setAppearance(LabelAppearance appearance)
	{
		if (this.getAppearance() != null)
			this.getAppearance().removeMinSizeChangedListener(sizeChangedListener);

		super.setAppearance(appearance);

		this.getAppearance().addMinSizeChangedListener(sizeChangedListener);
		this.getAppearance().getData().setText(this.text);
		updateMinSize();
	}

	/**
	 * @return Returns the text.
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @param text
	 *          The text to set.
	 */
	public void setText(String text)
	{
		this.text = text;

		if (getAppearance().getData() != null)
			getAppearance().getData().setText(text);
	}

	public Pixmap getPixmap()
	{
		return pixmap;
	}

	public void setPixmap(Pixmap pixmap)
	{
		this.pixmap = pixmap;

		updateMinSize();
	}

	
	
	/* (non-Javadoc)
	 * @see org.fenggui.Widget#sizeChanged(org.fenggui.event.SizeChangedEvent)
	 */
	@Override
	public void sizeChanged(SizeChangedEvent event)
	{
		this.getAppearance().getData().adaptSize(getAppearance().getContentWidth(), getAppearance().getContentHeight());
		super.sizeChanged(event);
	}

	@Override
	public void updateMinSize()
	{
		setMinSize(getAppearance().getMinSizeHint());

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

	@Override
	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		super.process(stream);

		setText(stream.processAttribute("text", getText(), getText()));

		if (stream.isInputStream()) // XXX: only support read-in at the moment :(
		{
			setPixmap(stream.processChild("Pixmap", pixmap, null, Pixmap.class));
		}
	}

	@Override
	public Dimension getMinContentSize()
	{
		int width = 0;
		int height = 0;

		if (text != null && text.length() > 0)
		{
			getAppearance().getData().setText(text);
			Dimension size = getAppearance().getTextContentSize();
			width = size.getWidth();
			height = size.getHeight();
		}

		if (pixmap != null)
		{
			width += pixmap.getWidth();
			if (text != null && text.length() > 0)
				width += getAppearance().getGap();
			height = Math.max(pixmap.getHeight(), height);
		}

		if (getAppearance().getData().isWordWarping())
		{
			width = 5;
		}

		if (getAppearance().getData().isConfined())
		{
			width = Math.max(5, this.getSize().getWidth());
			height = Math.max(5, this.getSize().getHeight());
		}

		return new Dimension(width, height);
	}

	@Override
	public void paintContent(Graphics g, IOpenGL gl)
	{
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;

		int contentWidth = getAppearance().getContentWidth();
		int contentHeight = getAppearance().getContentHeight();
		IComplexTextRenderer textRenderer = getAppearance().getRenderer();
		Dimension textSize = getAppearance().getTextContentSize();

		if (pixmap != null)
		{
			width = pixmap.getWidth();
			height = pixmap.getHeight();
			if (text != null && text.length() > 0)
				width += getAppearance().getGap();
		}
		else if (text == null || textRenderer == null)
			return;

		if (text != null && text.length() > 0)
		{
			width += textSize.getWidth();
			height = Math.max(height, textSize.getHeight());
		}

		x = x + getAppearance().getAlignment().alignX(contentWidth, width);

		if (pixmap != null)
		{
			g.setColor(Color.WHITE);
			y = getAppearance().getAlignment().alignY(contentHeight, pixmap.getHeight());
			g.drawImage(pixmap, x, y);
			x += pixmap.getWidth() + getAppearance().getGap();
		}

		if (textRenderer != null && text != null && text.length() > 0)
		{
			y = getAppearance().getAlignment().alignY(contentHeight, textSize.getHeight()) + textSize.getHeight();
			g.setColor(getAppearance().getData().getColor());
			getAppearance().renderText(x, y, g, gl);
		}
	}
}
