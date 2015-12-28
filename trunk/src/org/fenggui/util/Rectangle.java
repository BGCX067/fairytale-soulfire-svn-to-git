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
 * Created on Jul 17, 2005
 * $Id: Rectangle.java 456 2008-01-30 10:57:51Z marcmenghin $
 */
package org.fenggui.util;

/**
 * Implementation of a rectangle. The coordinates x, y denote the point were it
 * is located (usually the lower left corner in the FengGui coordinate system)
 * and width and height specify the size.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2008-01-30 11:57:51 +0100 (Mi, 30 Jan 2008) $
 * @version $Revision: 456 $
 * 
 * 
 */
public class Rectangle
{

	private int width, height, x, y;


	public Rectangle()
	{
	}


	public Rectangle(Rectangle copy)
	{
		width = copy.width;
		height = copy.height;
		x = copy.x;
		y = copy.y;
	}


	public Rectangle(int x, int y, int width, int height)
	{
		super();
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}


	public void set(Rectangle copy)
	{
		width = copy.width;
		height = copy.height;
		x = copy.x;
		y = copy.y;
	}


	/**
	 * Sets all values at once.
	 * @param x the x value of the rectangle
	 * @param y the y value of the rectangle
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 */
	public void set(int x, int y, int width, int height)
	{
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}


	public int getHeight()
	{
		return height;
	}


	public void setHeight(int height)
	{
		this.height = height;
	}


	public void setWidth(int width)
	{
		this.width = width;
	}


	public void setX(int x)
	{
		this.x = x;
	}


	public void setY(int y)
	{
		this.y = y;
	}


	public int getWidth()
	{
		return width;
	}


	public int getX()
	{
		return x;
	}


	public int getY()
	{
		return y;
	}


	public boolean contains(int x, int y)
	{
		return (x >= this.x && x <= this.x + width) && (y >= this.y && y <= this.y + height);
	}


	/**
	 * Check the intersection with an other <code>Rectangle</code>
	 * @param rect
	 * @return <code>true</code> if <code>rect</code> instersects this rectangle.
	 */
	public boolean intersect(Rectangle rect)
	{
		int minX = Math.min(x, rect.x);
		int minY = Math.min(y, rect.y);
		int maxX = Math.max(x, rect.x);
		int maxY = Math.max(y, rect.y);
		if (maxX - minX < width + rect.getWidth())
		{
			if (maxY - minY < height + rect.getHeight()) { return true; }
		}
		return false;
	}


	public String toString()
	{
		return "[" + x + "," + y + " " + width + "x" + height + "]";
	}

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Rectangle rectangle = (Rectangle) o;

    if (height != rectangle.height) return false;
    if (width != rectangle.width) return false;
    if (x != rectangle.x) return false;
    if (y != rectangle.y) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = width;
    result = 31 * result + height;
    result = 31 * result + x;
    result = 31 * result + y;
    return result;
  }
}
