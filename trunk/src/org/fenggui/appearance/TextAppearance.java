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
 * Created on 22.10.2007
 * $Id$
 */
package org.fenggui.appearance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fenggui.StandardWidget;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.text.ComplexTextRenderer;
import org.fenggui.binding.render.text.ComplexTextRendererData;
import org.fenggui.binding.render.text.IComplexTextRenderer;
import org.fenggui.binding.render.text.IComplexTextRendererData;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Alignment;
import org.fenggui.util.Dimension;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class TextAppearance extends DefaultAppearance
{
	private IComplexTextRenderer renderer;
	private IComplexTextRendererData data;
	private Alignment alignment = Alignment.LEFT;
	private List<ISizeChangedListener> sizeChangedListeners = new ArrayList<ISizeChangedListener>();
	private ISizeChangedListener listener = new ISizeChangedListener() {

		public void sizeChanged(SizeChangedEvent event) {
			fireMinSizeChangedListerer(event);
    }
		
	};
	
	public TextAppearance(StandardWidget w, TextAppearance appearance)
	{
		super(w, appearance);

		if (appearance.getRenderer() != null)
		{
			this.setRenderer(appearance.getRenderer().copy());
			setData(getRenderer().createData(appearance.data));
		}
		this.alignment = appearance.getAlignment();
	}

	public TextAppearance(StandardWidget w, InputOnlyStream stream) throws IOException, IXMLStreamableException
	{
		this(w);
		this.process(stream);
	}

	public TextAppearance(StandardWidget w)
	{
		super(w);
		this.renderer = new ComplexTextRenderer();
		setData(new ComplexTextRendererData());
	}

	/**
	 * Renders text from the current IKomplexTextRenderData object.
	 * 
	 * @param x
	 * @param y
	 * @param g
	 * @param gl
	 */
	public void renderText(int x, int y, Graphics g, IOpenGL gl)
	{
		if (renderer != null) renderer.render(x, y, getContentWidth(), getContentHeight(), data, g, gl);
	}

	public int getTextLineHeight()
	{
		if (renderer == null) return 0;
		else return renderer.getLineHeight(data, 0);
	}

	/* (non-Javadoc)
	 * @see org.fenggui.appearance.DecoratorAppearance#process(org.fenggui.theme.xml.InputOutputStream)
	 */
	@Override
	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		alignment = stream.processEnum("alignment", alignment, Alignment.LEFT, Alignment.class,
			Alignment.STORAGE_FORMAT);
		
		this.setRenderer(stream.processChild(getRenderer(), XMLTheme.COMPLEXTEXTRENDERER_REGISTRY));
		this.setData(stream.processChild(getData(), XMLTheme.COMPLEXTEXTRENDERERDATA_REGISTRY));
		
		super.process(stream);
	}

	/**
	 * @return Returns the renderer.
	 */
	public IComplexTextRenderer getRenderer()
	{
		return renderer;
	}

	/**
	 * @param renderer The renderer to set.
	 */
	public void setRenderer(IComplexTextRenderer renderer)
	{
		this.renderer = renderer;
		setData(this.renderer.createData(getData()));
	}

	public void setRenderText(String text)
	{
		this.data.setText(text);
	}
	
	protected void setData(IComplexTextRendererData data)
	{
		if (this.data != null)
			this.data.removeMinSizeChangedListener(listener);
		
		this.data = data;
		
		data.addMinSizeChangedListener(listener);
	}
	
	/**
	 * @return Returns the data.
	 */
	public IComplexTextRendererData getData()
	{
		return data;
	}

	public Dimension getTextContentSize()
	{
		if (data == null) return new Dimension(0, 0);
		else return data.getContentSize();
	}

	public int getTextWidth()
	{
		if (renderer == null) return 0;
		else return renderer.getTextWidth(data);
	}

	/**
	 * @return Returns the alignment.
	 */
	public Alignment getAlignment()
	{
		return alignment;
	}

	/**
	 * @param alignment The alignment to set (not null).
	 */
	public void setAlignment(Alignment alignment)
	{
		this.alignment = alignment;
	}
	
	protected void fireMinSizeChangedListerer(SizeChangedEvent event)
	{
		for (ISizeChangedListener sizeChanged : sizeChangedListeners)
		{
			sizeChanged.sizeChanged(event);
		}
	}

	public boolean addMinSizeChangedListener(ISizeChangedListener listener)
	{
		return sizeChangedListeners.add(listener);
	}

	public boolean removeMinSizeChangedListener(ISizeChangedListener listener)
	{
		return sizeChangedListeners.remove(listener);

	}
}
