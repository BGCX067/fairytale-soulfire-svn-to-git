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
 * Created on Jan 18, 2007
 * $Id:XMLTheme.java 323 2007-08-11 10:11:38Z Schabby $
 */
package org.fenggui.theme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fenggui.*;
import org.fenggui.binding.render.AWTFont;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.binding.render.text.ComplexTextRenderer;
import org.fenggui.binding.render.text.ComplexTextRendererData;
import org.fenggui.binding.render.text.DirectTextRenderer;
import org.fenggui.binding.render.text.JOGLTextRenderer;
import org.fenggui.binding.render.text.advanced.AdvancedTextRenderer;
import org.fenggui.binding.render.text.advanced.AdvancedTextRendererData;
import org.fenggui.binding.render.text.advanced.ContentFactory;
import org.fenggui.composite.Window;
import org.fenggui.composite.menu.Menu;
import org.fenggui.composite.menu.MenuBar;
import org.fenggui.decorator.PixmapDecorator;
import org.fenggui.decorator.background.FunnyBackground;
import org.fenggui.decorator.background.GradientBackground;
import org.fenggui.decorator.background.PixmapBackground;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.decorator.border.*;
import org.fenggui.decorator.switches.SetActiveFontSwitch;
import org.fenggui.decorator.switches.SetMarginSwitch;
import org.fenggui.decorator.switches.SetPixmapSwitch;
import org.fenggui.decorator.switches.SetTextColorSwitch;
import org.fenggui.layout.*;
import org.fenggui.theme.xml.*;
import org.fenggui.tooltip.LabelTooltipDecorator;
import org.fenggui.tooltip.LabelTooltipManager;
import org.fenggui.util.Color;
import org.fenggui.util.jdom.Document;
import org.fenggui.util.jdom.Element;
import org.fenggui.util.jdom.Reader;

/**
 * Loads a theme from a XML file. 
 * 
 * Please note that you can switch to loading via
 * the class loader by enabling Binding.setUseClassLoader(true). 
 * 
 * @author Johannes Schaback, last edited by $Author:Schabby $, $Date:2007-08-11 12:11:38 +0200 (Sat, 11 Aug 2007) $
 * @version $Revision:323 $
 */
public class XMLTheme implements ITheme
{
	private Document document = null;
	private final List<String> warnings = new ArrayList<String>();
	private String resourcePath = null;
	private GlobalContextHandler contextHandler = null;
	
	public static final TypeRegister TYPE_REGISTRY = new TypeRegister();
	public static final TypeRegister COMPLEXTEXTRENDERER_REGISTRY = new TypeRegister();
	public static final TypeRegister COMPLEXTEXTRENDERERDATA_REGISTRY = new TypeRegister();
	public static final TypeRegister TEXTRENDERER_REGISTRY = new TypeRegister();
	public static final TypeRegister FONT_REGISTRY = new TypeRegister();
	public static final TypeRegister TOOLTIPDATA_REGISTRY = new TypeRegister();
	public static final TypeRegister TOOLTIPMANAGER_REGISTRY = new TypeRegister();
	
	static
	{
		//Decorators
		TYPE_REGISTRY.register("PixmapDecorator", PixmapDecorator.class);
		
		TYPE_REGISTRY.register("PixmapBackground", PixmapBackground.class);
		TYPE_REGISTRY.register("FunnyBackground", FunnyBackground.class);
		TYPE_REGISTRY.register("GradientBackground", GradientBackground.class);
		TYPE_REGISTRY.register("PlainBackground", PlainBackground.class);
		
		TYPE_REGISTRY.register("BevelBorder", BevelBorder.class);
		TYPE_REGISTRY.register("PixmapBorder", PixmapBorder.class);
		TYPE_REGISTRY.register("PixmapBorder16", PixmapBorder16.class);
		TYPE_REGISTRY.register("PixmapBorder16FixedCenters", PixmapBorder16FixedCenters.class);
		TYPE_REGISTRY.register("PlainBorder", PlainBorder.class);
		TYPE_REGISTRY.register("TitledBorder", TitledBorder.class);
		TYPE_REGISTRY.register("RoundedBorder", RoundedBorder.class);
		
		//Tooltip stuff
		TYPE_REGISTRY.register("Tooltip", LabelTooltipDecorator.class);
		TYPE_REGISTRY.register("TooltipManager", LabelTooltipManager.class);
		
		//Switches
		TYPE_REGISTRY.register("PixmapSwitch", SetPixmapSwitch.class);
		TYPE_REGISTRY.register("MarginSwitch", SetMarginSwitch.class);
		TYPE_REGISTRY.register("ActiveFontSwitch", SetActiveFontSwitch.class);
		TYPE_REGISTRY.register("TextColorSwitch", SetTextColorSwitch.class);
		
		//Widgets
		TYPE_REGISTRY.register("Window", Window.class);
		TYPE_REGISTRY.register("Menu", Menu.class);
		TYPE_REGISTRY.register("MenuBar", MenuBar.class);
		TYPE_REGISTRY.register("Button", Button.class);
		TYPE_REGISTRY.register("Canvas", Canvas.class);
		TYPE_REGISTRY.register("CheckBox", CheckBox.class);
		TYPE_REGISTRY.register("ComboBox", ComboBox.class);
		TYPE_REGISTRY.register("Container", Container.class);
		TYPE_REGISTRY.register("Display", Display.class);
		TYPE_REGISTRY.register("Label", Label.class);
		TYPE_REGISTRY.register("List", org.fenggui.List.class);
		TYPE_REGISTRY.register("ProgressBar", ProgressBar.class);
		TYPE_REGISTRY.register("RadioButton", RadioButton.class);
		TYPE_REGISTRY.register("ScrollBar", ScrollBar.class);
		TYPE_REGISTRY.register("ScrollContainer", ScrollContainer.class);
		TYPE_REGISTRY.register("Slider", Slider.class);
		TYPE_REGISTRY.register("SplitContainer", SplitContainer.class);
		TYPE_REGISTRY.register("TextEditor", TextEditor.class);
		
		//Other things
		TYPE_REGISTRY.register("DecoratorLayer", DecoratorLayer.class);
		TYPE_REGISTRY.register("Pixmap", Pixmap.class);
		TYPE_REGISTRY.register("Color", Color.class);

		//Layout managers
		TYPE_REGISTRY.register("GridLayout", GridLayout.class);
		TYPE_REGISTRY.register("BorderLayout", BorderLayout.class);
		TYPE_REGISTRY.register("FormLayout", FormLayout.class);
		TYPE_REGISTRY.register("RowLayout", RowLayout.class);
		TYPE_REGISTRY.register("StaticLayout", StaticLayout.class);
		
		//Text renderer things
		TYPE_REGISTRY.register("ImageFont", ImageFont.class);
		TYPE_REGISTRY.register("AWTFont", AWTFont.class);

		TYPE_REGISTRY.register("DirectTextRenderer", DirectTextRenderer.class);
		TYPE_REGISTRY.register("JOGLTextRenderer", JOGLTextRenderer.class);
		
		TYPE_REGISTRY.register("ComplexTextRenderer", ComplexTextRenderer.class);
		TYPE_REGISTRY.register("ComplexTextRendererData", ComplexTextRendererData.class);
		TYPE_REGISTRY.register("AdvancedTextRenderer", AdvancedTextRenderer.class);
		TYPE_REGISTRY.register("AdvancedTextRendererData", AdvancedTextRendererData.class);
		
		TYPE_REGISTRY.register("ContentFactory", ContentFactory.class);
		
		//SIMPLE TEXT RENDERERS
		TEXTRENDERER_REGISTRY.register("DirectTextRenderer", DirectTextRenderer.class);
		TEXTRENDERER_REGISTRY.register("JOGLTextRenderer", JOGLTextRenderer.class);
		
		//COMPLEX TEXT RENDERERS
		COMPLEXTEXTRENDERER_REGISTRY.register("ComplexTextRenderer", ComplexTextRenderer.class);
		COMPLEXTEXTRENDERER_REGISTRY.register("AdvancedTextRenderer", AdvancedTextRenderer.class);
		
		//COMPLEX TEXT RENDERER DATA
		COMPLEXTEXTRENDERERDATA_REGISTRY.register("ComplexTextRendererData", ComplexTextRendererData.class);
		COMPLEXTEXTRENDERERDATA_REGISTRY.register("AdvancedTextRendererData", AdvancedTextRendererData.class);
		
		//FONTS
		FONT_REGISTRY.register("ImageFont", ImageFont.class);
		FONT_REGISTRY.register("AWTFont", AWTFont.class);
		
		//TOOLTIP
		TOOLTIPDATA_REGISTRY.register("Tooltip", LabelTooltipDecorator.class);
		TOOLTIPMANAGER_REGISTRY.register("TooltipManager", LabelTooltipManager.class);
	}
	
	public XMLTheme(String xmlThemeFile) throws IOException, IXMLStreamableException
	{
		/* extract path to xml file */
		if(xmlThemeFile.indexOf('/') != -1)
			this.resourcePath = xmlThemeFile.substring(0, xmlThemeFile.lastIndexOf('/')+1);
		else
			this.resourcePath = xmlThemeFile.substring(0, xmlThemeFile.lastIndexOf('\\')+1);
		
		/* parse the XML file in out JDOM */
		Reader r = new Reader();
		document = r.parse(Binding.getInstance().getResource(xmlThemeFile));
		
		/* will throw an exception if the bnding has not been initialized yet
		   see http://www.jmonkeyengine.com/jmeforum/index.php?topic=4483.15 */
		Binding.getInstance();
		
		Element el = document.getChild("FengGUI:init");
		
		if(el != null)
		{
		
			XMLInputStream xis = new XMLInputStream(el);
			xis.setResourcePath(resourcePath);
			List<IXMLStreamable> contents = new ArrayList<IXMLStreamable>();
			xis.processChildren(contents, XMLTheme.TYPE_REGISTRY);
			handleWarnings(xis);
			contextHandler = xis.getContextHandler();
		}
	}

	public Document getRoot()
	{
		return document;
	}

	
	@SuppressWarnings("unchecked")
	private String findSupertype(IWidget w)
	{
		Class clazz = w.getClass();
		String s = null;
		while(s == null && !clazz.equals(Object.class))
		{
			s = XMLTheme.TYPE_REGISTRY.getName(clazz);
			clazz = clazz.getSuperclass();
		}
		return s;
	}
	
	public void setUp(IWidget widget)
	{
		if(!(widget instanceof StandardWidget))
		{
			throw new IllegalArgumentException(
				"widget "+widget.getClass().getCanonicalName()+" is not a StandardWidget!");
		}
		
		StandardWidget w = (StandardWidget) widget;
		
		String type = findSupertype(widget);
		
		if(type == null)
		{
			System.err.println("Warning: "+widget.getClass().getCanonicalName()+" is not registered in org.theme.XMLTheme.TYPE_REGISTRY");
			return;
		}
		
		Element el = document.getChild(type);
		
		if(el == null)
		{
			System.err.println("Warning: <"+type+"> could not be found in theme definition file");
			return;
		}
		
		XMLInputStream xis = new XMLInputStream(el);
		xis.setContextHandler(contextHandler);
		xis.setResourcePath(resourcePath);
		
		try
		{
			w.process(xis);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		catch (IXMLStreamableException e)
		{
			throw new RuntimeException(e);
		}
		
		handleWarnings(xis);
	}
	
	public List<String> getWarnings() {
		return warnings;
	}
	
	private void handleWarnings(InputOutputStream stream) {
		String warningsStr = stream.getWarningsAsString().trim();
		if(warningsStr.length() > 0)
			System.out.println(warningsStr);
		warnings.addAll(stream.getWarnings());
	}
}
