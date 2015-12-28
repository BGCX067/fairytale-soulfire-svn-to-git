/*
 * FengGUI - Java GUIs in OpenGL (http://www.fenggui.org)
 * 
 * Copyright (c) 2005-2008 FengGUI Project
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
 * Created on 20.04.2008
 * $Id$
 */
package org.fenggui.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fenggui.IContainer;
import org.fenggui.IWidget;

/**
 * Utility class with helpful methods.
 * 
 * @author marc menghin
 * 
 */
public final class Util
{

	private Util()
	{
	}

	/**
	 * Adds data to the given widget. It uses a HashMap to store the key - data values pairs.
	 * 
	 * @param widget Widget to store the data to.
	 * @param key key for the data.
	 * @param data data to store.
	 * @return  the previous data associated with the widget, or null if
	 *  there was no data. (A null return can also indicate that the data
	 *  previously associated with the widget was null.)
	 */
	public static Object setCustomData(IWidget widget, String key, Object data)
	{
		Object rawData = widget.getCustomData();
		if (rawData == null)
		{
			rawData = new HashMap<String, Object>(0);
			widget.setCustomData(rawData);
		}

		if (rawData instanceof Map)
		{
			Map mapData = (Map) rawData;
			return mapData.put(key, data);
		}
		else
		{
			throw new UnsupportedOperationException("Already a CustomData object present on widget which is not a Map");
		}
	}

	/**
	 * Receives the data from a widget.
	 * 
	 * 
	 * @param widget widget to receive data from
	 * @param key key of the data object
	 * @return data associated with the key or null if data was null or no data was found.
	 */
	public static Object getCustomData(IWidget widget, String key)
	{
		Object rawData = widget.getCustomData();
		if (rawData == null)
			return null;

		if (rawData instanceof Map)
		{
			Map mapData = (Map) rawData;
			return mapData.get(key);
		}

		return null;
	}
	
	/**
	 * This method recursively searches for the first widget that has the data
	 * object assigned to it using the given key.
	 * 
	 * @param root
	 * @param key
	 * @param data
	 * @return Widget with the data object or null if none found.
	 */
	public static IWidget findWidgetWithData(IWidget root, String key, Object data)
	{
		if (data.equals(getCustomData(root, key))){
			return root;
		}
		
		if (root instanceof IContainer)
		{
			IContainer container = (IContainer) root;
			List<IWidget> childs = container.getContent();
			
			for (IWidget widget: childs)
			{
				IWidget result = findWidgetWithData(widget, key, data);
				if (result != null)
					return result;
			}
		}
		
		return null;
	}
}
