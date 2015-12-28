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
 * Created on 22.11.2007
 * $Id$
 */
package org.fenggui.binding.render.text.advanced;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.fenggui.DecoratorLayer;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;

/**
 * This factory uses one style and creates the content using that. The content here is plain text, no
 * special characters are available for formating. It is a very simple and straight forward
 * implementation of a ContentFactory.
 * 
 * @author marc menghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ContentFactory implements IContentFactory, IXMLStreamable
{
	private static IContentFactory defaultFactory = null;
	private Map<String, ITextRenderer> renderers = new HashMap<String, ITextRenderer>(5);

	private char character;
//	private static final char DEFAULT_CHAR = (char) 0x25CF;
	private static final char DEFAULT_CHAR = '*';
	private TextStyle defaultStyle;

	public ContentFactory()
	{
		this.character = DEFAULT_CHAR;
		defaultStyle = null;
	}

	public ContentFactory(InputOnlyStream stream) throws IOException, IXMLStreamableException
	{
		this.process(stream);
		if (ContentFactory.defaultFactory == null)
			ContentFactory.setDefaultFactory(this);
	}

	public void addRenderer(ITextRenderer renderer)
	{
		renderers.put(TEXTRENDERER_DEFAULT, renderer);
		createDefaultStyle();
	}

	public void addRenderer(String type, ITextRenderer renderer)
	{
		addRenderer(renderer);
		if (type != TEXTRENDERER_DEFAULT)
		{
			System.out.println("renderer will never be used by this ContentFactory as it only uses the default one.");
		}
	}

	public ITextRenderer getRenderer(String type)
	{
		return renderers.get(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.advanced.IContentPartFactory#getPart(java.lang.String)
	 */
	public ContentLine getParts(String text, boolean pwdfield)
	{
		TextPart part = new TextPart(text, getActiveStyle(), pwdfield, character, 0, 0);
		List<AbstractContentPart> parts = new Vector<AbstractContentPart>();
		parts.add(part);
		return new ContentLine(parts);
	}

	private void createDefaultStyle()
	{
		defaultStyle = new TextStyle();
		defaultStyle.defaultStyle.renderer = (ITextRenderer) renderers.get(IContentFactory.TEXTRENDERER_DEFAULT);
		defaultStyle.defaultStyle.color = Color.BLACK;
		defaultStyle.selectedStyle.color = Color.WHITE;
		defaultStyle.selectedStyle.background = new DecoratorLayer();
		defaultStyle.selectedStyle.background.add(new PlainBackground(new Color(0.4f, 0.4f, 0.4f, 0.8f)));
		defaultStyle.selectedStyle.renderer = defaultStyle.defaultStyle.renderer;
		defaultStyle.current = defaultStyle.defaultStyle;
	}

	public String contentPartsToString(List<AbstractContentPart> list, boolean pwdfield)
	{
		String result = "";
		for (AbstractContentPart part : list)
		{
			result += part.getContent();
		}
		return result;
	}

	public List<AbstractContentPart> stringToContentParts(String text, boolean pwdfield)
	{
		List<AbstractContentPart> result = new Vector<AbstractContentPart>(1);
		result.add(new TextPart(text, getActiveStyle(), pwdfield, character, 0, 0));
		return result;
	}

	public static IContentFactory getDefaultFactory()
	{
		if (defaultFactory == null)
			defaultFactory = new ContentFactory();

		return defaultFactory;
	}

	public static void setDefaultFactory(IContentFactory factory)
	{
		defaultFactory = factory;
	}

	public String getUniqueName()
	{
		return GENERATE_NAME;
	}

	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		if (stream.isInputStream()) // XXX: only support read-in at the moment :(
		{
			defaultStyle = new TextStyle();
			defaultStyle.defaultStyle.color = stream.processChild("Color", defaultStyle.defaultStyle.color, Color.BLACK,
				Color.class);
			addRenderer(TEXTRENDERER_DEFAULT, stream.processChild(getRenderer(TEXTRENDERER_DEFAULT),
				XMLTheme.TEXTRENDERER_REGISTRY));
			defaultStyle.defaultStyle.renderer = (ITextRenderer) renderers.get(IContentFactory.TEXTRENDERER_DEFAULT);
			String tmpChars = stream.processAttribute("hidingCharacter", "", Character.toString(DEFAULT_CHAR));
			if (tmpChars.length() > 0)
			{
				character = tmpChars.charAt(0);
			}

			boolean defaultFactory = false;
			defaultFactory = stream.processAttribute("default", defaultFactory, false);
			if (defaultFactory)
			{
				ContentFactory.setDefaultFactory(this);
			}
			
			defaultStyle.selectedStyle.color = Color.WHITE;
			defaultStyle.selectedStyle.background = new DecoratorLayer(new PlainBackground(new Color(0.4f, 0.4f, 0.4f, 0.8f)));
			defaultStyle.selectedStyle.renderer = defaultStyle.defaultStyle.renderer;
			defaultStyle.current = defaultStyle.defaultStyle;
		}
	}

	public AbstractContentPart getEmptyLine(boolean pwdfield)
	{
		if (defaultStyle.defaultStyle.renderer == null)
		{
			createDefaultStyle();
		}

		return new TextPart("", defaultStyle, pwdfield, character, 0, 0);
	}

	/**
	 * @return the character
	 */
	public char getCharacter()
	{
		return character;
	}

	/**
	 * @param character
	 *          the character to set
	 */
	public void setCharacter(char character)
	{
		this.character = character;
	}

	public String getContentObject(Object obj)
	{
		return "";
	}

	public TextStyle getActiveStyle()
	{
		if (defaultStyle == null)
			createDefaultStyle();
		
		return defaultStyle;
	}

	public void setActiveStyle(AbstractContentPart activePart)
	{
		defaultStyle = activePart.getStyle();
	}

	public void setActiveStyle(TextStyle newStyle)
	{
		defaultStyle = newStyle;
	}

	public void transformStyle(AbstractContentPart part, TextStyle newStyle)
	{
		part.setStyle(newStyle);
	}

	public void transformStyle(AbstractContentPart part)
	{
		part.setStyle(defaultStyle);
	}
}
