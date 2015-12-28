package org.fenggui.binding.render.jogl;

import java.awt.Component;

import org.fenggui.binding.render.Cursor;

public class JOGLCursor extends Cursor 
{
	private java.awt.Cursor cursor = null;
	private Component component = null;
	
	public JOGLCursor(java.awt.Cursor c, Component awtComponent)
	{
		this.cursor = c;
		this.component = awtComponent;
	}

	@Override
	public void show() 
	{
		component.setCursor(cursor);
	}
}
