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
 * Created on 2005-3-2
 * $Id: Label.java 569 2008-06-24 10:08:17Z marcmenghin $
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
 * Widget for displaying a line of text or a pixmap. This widget is passive and does not
 * react on anything.
 * 
 * @author Johannes Schaback aka Schabby, last edited by $Author: marcmenghin $, $Date:
 *         2007-11-28 11:16:38 +0100 (Mi, 28 Nov 2007) $
 * @version $Revision: 569 $
 */
public class Label extends StandardWidget implements ILabel {
	private Pixmap	               pixmap	              = null;
	private LabelAppearance	       appearance	          = null;

	protected ISizeChangedListener	sizeChangedListener	= new ISizeChangedListener() {

		                                                    public void sizeChanged(SizeChangedEvent event) {
			                                                    updateMinSize();
		                                                    }

	                                                    };

	/**
	 * Creates a new empty label
	 * 
	 */
	public Label() {
		this("");
	}

	/**
	 * Creates a new label with a given text.
	 * 
	 * @param text
	 *          the text
	 */
	public Label(String text) {
		setAppearance(new LabelAppearance(this));
		setText(text);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param widget
	 */
	public Label(Label widget) {
		super(widget);
		this.pixmap = widget.pixmap;
		setAppearance(new LabelAppearance(this, widget.appearance));
	}

	public void setAppearance(LabelAppearance appearance) {
		if (this.appearance != null)
			this.appearance.removeMinSizeChangedListener(sizeChangedListener);
		this.appearance = appearance;
		this.appearance.addMinSizeChangedListener(sizeChangedListener);
		appearance.getData().adaptSize(appearance.getContentWidth(), appearance.getContentHeight());
		updateMinSize();
	}

	@Override
	public LabelAppearance getAppearance() {
		return appearance;
	}

	public Pixmap getPixmap() {
		return pixmap;
	}

	public void setPixmap(Pixmap pixmap) {
		this.pixmap = pixmap;
		updateMinSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.Widget#sizeChanged(org.fenggui.event.SizeChangedEvent)
	 */
	@Override
	public void sizeChanged(SizeChangedEvent event) {
		getAppearance().getData().adaptSize(getAppearance().getContentWidth(), getAppearance().getContentHeight());
		super.sizeChanged(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.ITextWidget#getText()
	 */
	public String getText() {
		return getAppearance().getData().getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.ITextWidget#setText(java.lang.String)
	 */
	public void setText(String text) {
		appearance.getData().setText(text);
	}

	@Override
	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException {
		super.process(stream);

		setText(stream.processAttribute("text", getText(), getText()));

		if (stream.isInputStream()) // XXX: only support read-in at the moment :(
			pixmap = stream.processChild("Pixmap", pixmap, null, Pixmap.class);
	}

	@Override
	public Dimension getMinContentSize() {
		int width;
		int height;

		String text = getAppearance().getData().getText();
		Dimension size = getAppearance().getTextContentSize();
		width = size.getWidth();
		height = size.getHeight();

		if (pixmap != null) {
			width += pixmap.getWidth();
			if (text != null && text.length() > 0)
				width += appearance.getGap();
			height = Math.max(pixmap.getHeight(), height);
		}

		if (getAppearance().getData().isConfined()) {
			width = 5;
			height = 5;
		} else if (getAppearance().getData().isWordWarping()) {
			width = 5;
			height = 5;
		}

		return new Dimension(width, height);
	}

	@Override
	public void paintContent(Graphics g, IOpenGL gl) {
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;

		int contentWidth = getAppearance().getContentWidth();
		int contentHeight = getAppearance().getContentHeight();
		IComplexTextRenderer textRenderer = getAppearance().getRenderer();
		Dimension textSize = getAppearance().getTextContentSize();
		String text = getAppearance().getData().getText();
		
		if (pixmap != null) {
			width = pixmap.getWidth();
			height = pixmap.getHeight();
			if (text != null && text.length() > 0)
				width += getAppearance().getGap();
		} else if (text == null || textRenderer == null)
			return;

		if (text != null && text.length() > 0) {
			width += textSize.getWidth();
			height = Math.max(height, textSize.getHeight());
		}

		x = x + getAppearance().getAlignment().alignX(contentWidth, width);

		if (pixmap != null) {
			g.setColor(Color.WHITE);
			y = getAppearance().getAlignment().alignY(contentHeight, pixmap.getHeight());
			g.drawImage(pixmap, x, y);
			x += pixmap.getWidth() + getAppearance().getGap();
		}

		if (textRenderer != null && text != null && text.length() > 0) {
			y = getAppearance().getAlignment().alignY(contentHeight, textSize.getHeight()) + textSize.getHeight();
			g.setColor(getAppearance().getData().getColor());
			getAppearance().renderText(x, y, g, gl);
		}
	}
}
