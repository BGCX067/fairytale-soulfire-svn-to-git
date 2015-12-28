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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.fenggui.binding.render.IFont;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ComplexTextRendererData implements IComplexTextRendererData, IXMLStreamable
{
	private String text;
	private Color color;
	private int width;
	private int height;

	private String[] content;
	private ITextRenderer renderer;

	private boolean confined;
	private boolean multiline;
	private boolean wordWarping;
	private String endMarker;
	private Dimension contentSizeCache;

	private List<ISizeChangedListener> sizeChangedListeners = new ArrayList<ISizeChangedListener>();

	public ComplexTextRendererData()
	{
		color = Color.BLACK;
		text = "";
		width = 2000;
		height = 2000;
		confined = false;
		multiline = false;
		wordWarping = false;
		endMarker = TextUtil.ENDMARKER0;
		content = new String[0];
		contentSizeCache = new Dimension(0,0);
	}

	public ComplexTextRendererData(ComplexTextRendererData data)
	{
		if (data != null)
		{
			this.text = data.text;
			this.color = new Color(data.color);
			this.confined = data.confined;
			this.multiline = data.multiline;
			this.wordWarping = data.wordWarping;
			this.width = data.width;
			this.height = data.height;
			if (data.renderer != null)
				this.renderer = data.renderer.copy();
			this.content = data.content;
			this.endMarker = data.endMarker;
			this.contentSizeCache = data.contentSizeCache;
		}
	}

	public ComplexTextRendererData(InputOnlyStream stream) throws IOException, IXMLStreamableException
	{
		this.process(stream);
		updateText();
	}

	private void updateText()
	{
		if (text == null || text.length() == 0)
		{
			contentSizeCache = new Dimension(0, 0);
			content = new String[0];
			return;
		}

		if (renderer == null)
			getRenderer();

		String content;
		if (!multiline)
		{
			int h = text.indexOf('\n');
			if (h >= 0)
			{
				content = text.substring(0, h);
			}
			else
			{
				content = text;
			}
		}
		else
		{
			content = text;
		}

		String[] lines = content.split("\n");
		List<String> start = Arrays.asList(lines);
		List<String> result = new Vector<String>(lines.length);

		if (wordWarping && width > 0)
		{
			for (String line : start)
			{
				String part1, part2 = line;
				do
				{
					int h = TextUtil.findLineEnd(part2, renderer, width);
					if (h <= 0 || h >= part2.length())
					{
						result.add(part2);
						part2 = "";
					}
					else
					{
						if (line.charAt(h) == ' ')
							h++;

						part1 = part2.substring(0, h);
						part2 = part2.substring(h, part2.length());
						result.add(part1);
					}
				} while (part2.length() > 0);
			}
			start = result;
		}

		// confine string
		if (confined)
		{
			int height = 0;

			for (String line : start)
			{
				String newLine = TextUtil.confineString(line, width, renderer, endMarker);
				height += renderer.getLineHeight();
				result.add(newLine);

				if (height >= this.height)
				{
					break;
				}
			}
			start = result;
		}

		// build final string
		String finalResult = "";
		for (String line : start)
		{
			finalResult += line + "\n";
		}
		if (finalResult.length() > 0)
		{
			finalResult = finalResult.substring(0, finalResult.length() - 1);
		}
		this.content = finalResult.split("\n");
		Dimension oldSize = contentSizeCache;
		if (finalResult.length() <= 0)
		{
			this.contentSizeCache = new Dimension(0, 0);
		}
		else
		{
			this.contentSizeCache = renderer.calculateSize(this.content);
		}

		fireMinSizeChangedListerer(new SizeChangedEvent(null, oldSize, contentSizeCache));
	}

	public void setText(String text)
	{
		if (this.text != null && !this.text.equals(text))
		{
			this.text = text;
			updateText();
		}
		else if (this.text == null)
		{
			this.text = text;
			updateText();
		}
	}

	public String getText()
	{
		return text;
	}

	public String[] getContent()
	{
		return content;
	}

	private void setSize(int width, int height)
	{
		boolean updated = false;

		if (width != this.width)
		{
			this.width = width;
			updated = true;
		}

		if (height != this.height)
		{
			this.height = height;
			updated = true;
		}

		if (updated)
		{
			updateText();
		}
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public String getUniqueName()
	{
		return GENERATE_NAME;
	}

	public boolean isMultiline()
	{
		return multiline;
	}

	public boolean isConfined()
	{
		return confined;
	}

	public boolean isWordWarping()
	{
		return wordWarping;
	}

	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		this.text = stream.processAttribute("text", this.getText(), "");
		this.endMarker = stream.processAttribute("endMarker", this.endMarker, TextUtil.ENDMARKER0);
		this.multiline = stream.processAttribute("multiline", this.multiline, false);
		this.confined = stream.processAttribute("confined", this.confined, false);
		this.wordWarping = stream.processAttribute("wordwarped", this.wordWarping, false);
		this.color = stream.processChild("Color", this.getColor(), Color.BLACK, Color.class);
		this.renderer = stream.processChild(this.renderer, XMLTheme.TEXTRENDERER_REGISTRY);

		updateText();
	}

	public void setConfined(boolean confined)
	{
		if (this.confined != confined)
		{
			this.confined = confined;
			updateText();
		}
	}

	public void setMultiline(boolean multiline)
	{
		if (multiline != this.multiline)
		{
			this.multiline = multiline;
			updateText();
		}
	}

	public void setWordWarping(boolean warp)
	{
		if (warp != this.wordWarping)
		{
			this.wordWarping = warp;
			updateText();
		}
	}

	/**
	 * @return Returns the renderer.
	 */
	public ITextRenderer getRenderer()
	{
		if (renderer == null)
			this.renderer = new DirectTextRenderer();

		return renderer;
	}

	/**
	 * @param renderer
	 *          The renderer to set.
	 */
	public void setRenderer(ITextRenderer renderer)
	{
		this.renderer = renderer;
		updateText();
	}

	public void setConfinedMarker(String marker)
	{
		if (!this.endMarker.equals(marker))
		{
			endMarker = marker;
			updateText();
		}
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

	public void setActiveFont(IFont font)
	{
		getRenderer().setFont(font);
		updateText();
	}

	public IFont getActiveFont()
	{
		return getRenderer().getFont();
	}

	public Dimension getContentSize()
	{
		return contentSizeCache;
	}

	public void adaptSize(int x, int y)
	{
		this.setSize(x, y);
	}
}
