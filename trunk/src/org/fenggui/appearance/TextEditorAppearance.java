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
 * Created on 03.01.2008
 * $Id$
 */
package org.fenggui.appearance;

import java.io.IOException;

import org.fenggui.StandardWidget;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.text.advanced.AdvancedTextRenderer;
import org.fenggui.binding.render.text.advanced.AdvancedTextRendererData;
import org.fenggui.binding.render.text.advanced.IAdvancedTextRenderer;
import org.fenggui.binding.render.text.advanced.IAdvancedTextRendererData;
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
public class TextEditorAppearance extends DefaultAppearance {
	private IAdvancedTextRenderer	    renderer;
	private IAdvancedTextRendererData	data;
	private IAdvancedTextRendererData	emptyData;
	private Alignment	                alignment	    = Alignment.LEFT;

	public TextEditorAppearance(StandardWidget w, TextEditorAppearance appearance) {
		super(w, appearance);

		if (appearance.getRenderer() != null) {
			this.setRenderer((IAdvancedTextRenderer) appearance.getRenderer().copy());
			this.data = getRenderer().createData(appearance.data, appearance);
			this.emptyData = getRenderer().createData(appearance.emptyData, appearance);
		}
		this.alignment = appearance.getAlignment();
	}

	public TextEditorAppearance(StandardWidget w, InputOnlyStream stream) throws IOException, IXMLStreamableException {
		this(w);
		this.process(stream);
	}

	public TextEditorAppearance(StandardWidget w) {
		super(w);
		this.renderer = new AdvancedTextRenderer();
		data = new AdvancedTextRendererData();
		emptyData = new AdvancedTextRendererData();
	}

	/**
	 * Renders text from the current IKomplexTextRenderData object.
	 * 
	 * @param x
	 * @param y
	 * @param g
	 * @param gl
	 */
	public void renderText(int x, int y, Graphics g, IOpenGL gl) {
		if (renderer != null) {
			renderer.render(x, y, getTextContentSize().getWidth(), getTextContentSize().getHeight(), getCorrectData(), g, gl);
		}
	}

	private IAdvancedTextRendererData getCorrectData() {
		if (data.getText() != null && data.getText().length() <= 0 && !data.isEditMode()) {
			return emptyData;
		} else {
			return data;
		}
	}

	public int getTextLineHeight() {
		if (renderer == null)
			return 0;
		else
			return renderer.getLineHeight(getCorrectData(), 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.appearance.DecoratorAppearance#process(org.fenggui.theme.xml.InputOutputStream)
	 */
	@Override
	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException {
		super.process(stream);
		
		alignment = stream.processEnum("alignment", alignment, Alignment.LEFT, Alignment.class, Alignment.STORAGE_FORMAT);
		this.setRenderer(stream.processChild(getRenderer(), XMLTheme.COMPLEXTEXTRENDERER_REGISTRY));
		this.setData(stream.processChild(getData(), XMLTheme.COMPLEXTEXTRENDERERDATA_REGISTRY));
		if (stream.startSubcontext("EmptyStyle"))
		{
			this.setEmptyData(stream.processChild(getEmptyData(), XMLTheme.COMPLEXTEXTRENDERERDATA_REGISTRY));
			stream.endSubcontext();
		}
	}

	/**
	 * @return Returns the renderer.
	 */
	public IAdvancedTextRenderer getRenderer() {
		return renderer;
	}

	/**
	 * @param renderer
	 *          The renderer to set.
	 */
	public void setRenderer(IAdvancedTextRenderer renderer) {
		this.renderer = renderer;
		setData(this.renderer.createData(getData(), this));
	}

	public void setRenderText(String text) {
		this.data.setText(text);
	}

	protected void setData(IAdvancedTextRendererData data) {
		this.data = data;
	}

	/**
	 * @return Returns the data.
	 */
	public IAdvancedTextRendererData getData() {
		return data;
	}

	public Dimension getTextContentSize()
	{
		if (data == null) return new Dimension(0, 0);
		else return data.getContentSize();
	}
	
	public Dimension calculateTextSize() {
		if (renderer == null)
			return new Dimension(0, 0);
		else
			return renderer.calculateSize(getCorrectData());
	}

	public int getTextWidth() {
		if (renderer == null)
			return 0;
		else
			return renderer.getTextWidth(getCorrectData());
	}

	/**
	 * @return Returns the alignment.
	 */
	public Alignment getAlignment() {
		return alignment;
	}

	/**
	 * @param alignment
	 *          The alignment to set (not null).
	 */
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	/**
	 * @return the emptyData
	 */
	public IAdvancedTextRendererData getEmptyData() {
		return emptyData;
	}

	/**
	 * @param emptyData
	 *          the emptyData to set
	 */
	public void setEmptyData(IAdvancedTextRendererData emptyData) {
		this.emptyData = emptyData;
	}

	/**
   * @return the passwordField
   */
  public boolean isPasswordField() {
  	return this.getData().isPasswordField();
  }

	/**
   * @param passwordField the passwordField to set
   */
  public void setPasswordField(boolean passwordField) {
  	this.getData().setPasswordField(passwordField);
  }
}
