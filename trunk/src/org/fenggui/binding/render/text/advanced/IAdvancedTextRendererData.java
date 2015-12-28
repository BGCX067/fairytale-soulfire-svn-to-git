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

import java.util.Set;

import org.fenggui.DecoratorLayer;
import org.fenggui.binding.render.text.IComplexTextRendererData;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.key.Key;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * Data object interface for use with the IAdvancedTextRenderer.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface IAdvancedTextRendererData extends IComplexTextRendererData
{
	public enum MoveDirection
	{
		Up, Left, Right, Down
	}

	public Color getColor();

	public void setColor(Color color);

	public Color getSelectionColor();

	public void setSelectionColor(Color color);

	public DecoratorLayer getSelectionBackground();

	public void setSelectionBackground(DecoratorLayer background);

	public Dimension getSize();

	public void clickedOn(int x, int y, Set<Key> modifiers);

	public void dragedTo(int x, int y, Set<Key> modifiers);

	public int getMaxLines();

	public void setMaxLines(int lines);

	public void addContentAtEnd(String content);

	public void addContentAtBeginning(String content);

	public int getContentLineCount();

	public void removeContentLineFromEnd();

	public void removeContentLineFromBeginning();

	public boolean isEditMode();

	public void setEditMode(boolean editMode);

	public boolean isPasswordField();

	public void setPasswordField(boolean passwordField);

	public boolean addMinSizeChangedListener(ISizeChangedListener listener);

	public boolean removeMinSizeChangedListener(ISizeChangedListener listener);

	public boolean handleKeyPresses(Key key, Set<Key> modifiers);

	public void setReadonly(boolean readonly);

	public boolean isReadonly();

	public int getActivePositionIndex();
	public void setActivePositionIndex(int index);
	
	public boolean hasSelection();
	
	public int getSelectionStartIndex();
	
	public void setSelectionIndex(int index1, int index2);
	
	public int getSelectionEndIndex();

	public boolean handleTextInput(char character);

	/**
	 * @param listener
	 * @return
	 * @see org.fenggui.binding.render.text.advanced.ContentManager#addSizeChangedListener(org.fenggui.event.ISizeChangedListener)
	 */
	public boolean addSizeChangedListener(ISizeChangedListener listener);

	/**
	 * @param listener
	 * @return
	 * @see org.fenggui.binding.render.text.advanced.ContentManager#removeSizeChangedListener(org.fenggui.event.ISizeChangedListener)
	 */
	public boolean removeSizeChangedListener(ISizeChangedListener listener);
}
