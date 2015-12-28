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
 * Created on 21.11.2007
 * $Id$
 */
package org.fenggui.binding.render.text.advanced;

import java.io.IOException;
import java.util.List;

import org.fenggui.appearance.TextEditorAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.binding.render.text.IComplexTextRenderer;
import org.fenggui.binding.render.text.IComplexTextRendererData;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Alignment;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class AdvancedTextRenderer implements IAdvancedTextRenderer
{
	public AdvancedTextRenderer()
	{

	}

	public AdvancedTextRenderer(InputOnlyStream stream) throws IOException, IXMLStreamableException
	{
		this.process(stream);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.IComplexTextRenderer#calculateSize(org.fenggui.binding.render.text.IComplexTextRendererData)
	 */
	public Dimension calculateSize(IComplexTextRendererData data)
	{
		if (!(data instanceof AdvancedTextRendererData))
			throw new IllegalArgumentException("Wrong Textrender Data object.");
		AdvancedTextRendererData rData = (AdvancedTextRendererData) data;

		return rData.getManager().getSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.IComplexTextRenderer#createData(org.fenggui.binding.render.text.IComplexTextRendererData,
	 *      org.fenggui.appearance.TextAppearance)
	 */
	public IComplexTextRendererData createData(IComplexTextRendererData data)
	{
		if (data instanceof AdvancedTextRendererData)
		{
			AdvancedTextRendererData rData = (AdvancedTextRendererData) data;
			return new AdvancedTextRendererData(rData);
		}
		return new AdvancedTextRendererData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.IComplexTextRenderer#getLineHeight(org.fenggui.binding.render.text.IComplexTextRendererData,
	 *      int)
	 */
	public int getLineHeight(IComplexTextRendererData data, int linenumber)
	{
		if (!(data instanceof AdvancedTextRendererData))
			throw new IllegalArgumentException("Wrong Textrender Data object.");
		AdvancedTextRendererData rData = (AdvancedTextRendererData) data;
		ITextRenderer renderer = rData.getManager().getFactory().getRenderer(IContentFactory.TEXTRENDERER_DEFAULT);
		if (renderer != null)
		{
			return renderer.getLineHeight();
		} else {
			return 1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.IComplexTextRenderer#getTextWidth(org.fenggui.binding.render.text.IComplexTextRendererData)
	 */
	public int getTextWidth(IComplexTextRendererData data)
	{
		if (!(data instanceof AdvancedTextRendererData))
			throw new IllegalArgumentException("Wrong Textrender Data object.");
		AdvancedTextRendererData rData = (AdvancedTextRendererData) data;

		return rData.getManager().getSize().getWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.IComplexTextRenderer#isValidChar(org.fenggui.binding.render.text.IComplexTextRendererData,
	 *      char)
	 */
	public boolean isValidChar(IComplexTextRendererData data, char c)
	{
		if (Character.isIdentifierIgnorable(c))
			return false;
		else
			// too hard to really get as no one knows what renderers are used for the char
			return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.IComplexTextRenderer#render(int, int, int, int,
	 *      org.fenggui.binding.render.text.IComplexTextRendererData,
	 *      org.fenggui.binding.render.Graphics, org.fenggui.binding.render.IOpenGL)
	 */
	public void render(int x, int y, int w, int h, IComplexTextRendererData data, Graphics g, IOpenGL gl)
	{
		if (!(data instanceof AdvancedTextRendererData))
			throw new IllegalArgumentException("Wrong Textrender Data object.");

		AdvancedTextRendererData rData = (AdvancedTextRendererData) data;

		List<ContentUserLine> content = rData.getManager().getContentObjects();

//		g.setColor(new Color(0,255,0,100));
//		g.drawFilledRectangle(x, y - h, w, h);
		
		synchronized (content) {
	    
		if (content == null || content.isEmpty())
		{
			return;
		}

		int yPos = y;
		int currentAtomCount = 0;
		// boolean hasSelection = rData.getManager().getStartSelection() >= 0;
		long selection = rData.getManager().getActiveAtom();
		boolean isPainted = rData.isReadonly();
		for (ContentUserLine parts : content)
		{
			for (ContentLine part : parts.getContentLines())
			{
				part.render(x, yPos, g, gl);
				if (rData.isEditMode())
				{
					if (!isPainted && currentAtomCount + part.getAtomCount() >= selection)
					{
						isPainted = true;
						// cursor within this part
						int posX = part.calculatePositionInPixel((int) (selection - currentAtomCount));
						if (posX >= 0 && posX <= w)
						{
							Pixmap cursor = rData.getCursor();
							int height = part.getSize().getHeight();
							Color cursorColor = rData.getCursorColor();
							g.setColor(cursorColor.getRed(), cursorColor.getGreen(), cursorColor.getBlue(), cursorColor.getAlpha()
									* rData.getCursorAlpha());
							if (cursor != null)
							{
								height = Math.max(part.getSize().getHeight(), cursor.getHeight());
								int cY = yPos + Alignment.MIDDLE.alignY(height, cursor.getHeight()) - height;
								int cX = posX;

								g.drawImage(cursor, cX, cY);
							}
							else
							{
								// draw simple rectangle cursor if none defined
								g.drawFilledRectangle(posX - 1, yPos - height, 2, height);
							}
							g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
							rData.updateCursor();
						}
					}
					currentAtomCount += part.getAtomCount();
				}
				else
				{
					rData.setCursorAlpha(1.0f);
				}
				yPos -= part.getSize().getHeight();
			}
			currentAtomCount++;
		}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.theme.xml.IXMLStreamable#getUniqueName()
	 */
	public String getUniqueName()
	{
		return GENERATE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.theme.xml.IXMLStreamable#process(org.fenggui.theme.xml.InputOutputStream)
	 */
	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.util.ICopyable#copy()
	 */
	public IComplexTextRenderer copy()
	{
		return new AdvancedTextRenderer();
	}

	public IAdvancedTextRendererData createData(IAdvancedTextRendererData data, TextEditorAppearance appearance)
	{
		if (data instanceof AdvancedTextRendererData)
		{
			AdvancedTextRendererData rData = (AdvancedTextRendererData) data;
			return new AdvancedTextRendererData(rData);
		}
		return new AdvancedTextRendererData();
	}
}
