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
package org.fenggui.binding.render.text;

import java.io.IOException;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;

/**
 * A very simple implementation of a ComplexTextRenderer. It has one TextRenderer
 * and just renders everything with it. This is the fastest way of rendering Text.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ComplexTextRenderer implements IComplexTextRenderer
{

	public ComplexTextRenderer()
	{
	}

	public ComplexTextRenderer(InputOnlyStream stream) throws IOException, IXMLStreamableException
	{
		this();
		this.process(stream);
	}

	public void render(int x, int y, int w, int h, IComplexTextRendererData data, Graphics g, IOpenGL gl)
	{
		if (!(data instanceof ComplexTextRendererData))
			throw new IllegalArgumentException("Wrong render Data in use.");
		ComplexTextRendererData cData = (ComplexTextRendererData) data;
//		cData.setSize(w, h);
		cData.getRenderer().render(x, y, cData.getContent(), cData.getColor(), g, gl);
	}

	public ComplexTextRenderer copy()
	{
		return this;
	}

	public Dimension calculateSize(IComplexTextRendererData data)
	{
		if (!(data instanceof ComplexTextRendererData))
			throw new IllegalArgumentException("Wrong render Data in use.");
		ComplexTextRendererData cData = (ComplexTextRendererData) data;
		Dimension size = cData.getRenderer().calculateSize(cData.getContent());
		return size;
	}

	public IComplexTextRendererData createData(IComplexTextRendererData data)
	{
		if (data instanceof ComplexTextRendererData)
		{
			return new ComplexTextRendererData((ComplexTextRendererData) data);
		}

		return new ComplexTextRendererData();
	}

	public int getLineHeight(IComplexTextRendererData data, int linenumber)
	{
		if (!(data instanceof ComplexTextRendererData))
			throw new IllegalArgumentException("Wrong render Data in use.");
		ComplexTextRendererData cData = (ComplexTextRendererData) data;
		return cData.getRenderer().getLineHeight();
	}

	public boolean isValidChar(IComplexTextRendererData data, char c)
	{
		if (!(data instanceof ComplexTextRendererData))
			throw new IllegalArgumentException("Wrong render Data in use.");
		ComplexTextRendererData cData = (ComplexTextRendererData) data;
		return cData.getRenderer().isValidChar(c);
	}

	public int getTextWidth(IComplexTextRendererData data)
	{
		if (!(data instanceof ComplexTextRendererData))
			throw new IllegalArgumentException("Wrong render Data in use.");
		ComplexTextRendererData cData = (ComplexTextRendererData) data;
		return cData.getRenderer().getWidth(cData.getContent()[0]);
	}

	public String getUniqueName()
	{
		return IXMLStreamable.GENERATE_NAME;
	}

	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
	}
}
