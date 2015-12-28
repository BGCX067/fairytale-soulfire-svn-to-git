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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fenggui.DecoratorLayer;
import org.fenggui.binding.clipboard.IClipboard;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.IFont;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.binding.render.text.TextUtil;
import org.fenggui.event.ContentChangedEvent;
import org.fenggui.event.IContentChangedListener;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.event.key.Key;
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
public class AdvancedTextRendererData implements IAdvancedTextRendererData, IXMLStreamable
{
	private int width = 10;
	private int height = 10;
	private String text = "";
	private ContentManager manager;
	private Color selectionColor = Color.BLUE;
	private DecoratorLayer selectionBackground = new DecoratorLayer();
	private String endMarker = TextUtil.ENDMARKER0;
	private Pixmap cursor;
	private Color cursorColor = Color.BLACK;
	private float cursorAlpha = 1.00f;
	private float cursorModify = 0.03f;
	private boolean editMode = false;
	private IFont activeFont = null;
	private int selectionStart = -1;
	private IClipboard clipboard = null;
	private boolean readonly = false;

	private List<ISizeChangedListener> sizeChangedListeners = new ArrayList<ISizeChangedListener>();

	private ISizeChangedListener contentListener = new ISizeChangedListener()
	{

		public void sizeChanged(SizeChangedEvent event)
		{
			fireMinSizeChangedListerer(event);
		}

	};

	private IContentChangedListener contentChangedListener = new IContentChangedListener()
	{

		public void contentChanged(ContentChangedEvent event)
		{
			text = manager.getContent();
		}

	};

	public AdvancedTextRendererData()
	{
		manager = new ContentManager(ContentFactory.getDefaultFactory());
		manager.addSizeChangedListener(contentListener);
		manager.addContentChangedListener(contentChangedListener);
		clipboard = Binding.getInstance().getClipboard();
		this.cursor = null;
	}

	public AdvancedTextRendererData(AdvancedTextRendererData data)
	{
		this.width = data.width;
		this.height = data.height;
		this.text = data.text;
		this.manager = new ContentManager(data.manager);
		this.cursorModify = data.cursorModify;
		this.cursor = data.cursor;
		this.cursorColor = data.cursorColor;
		this.clipboard = data.clipboard;
		manager.addSizeChangedListener(contentListener);
		manager.addContentChangedListener(contentChangedListener);
		updateContent();
	}

	public AdvancedTextRendererData(InputOnlyStream stream) throws IOException, IXMLStreamableException
	{
		manager = new ContentManager(ContentFactory.getDefaultFactory());
		manager.addSizeChangedListener(contentListener);
		manager.addContentChangedListener(contentChangedListener);
		clipboard = Binding.getInstance().getClipboard();
		this.cursor = null;
		this.process(stream);
	}

	public void setSize(int width, int height)
	{
		//		boolean updated = false;

		if (width != this.width)
		{
			this.width = width;
			//			updated = true;

			//for now only width is relevant
			manager.updateContent(width);
		}

		if (height != this.height)
		{
			this.height = height;
			//			updated = true;
		}

		//		if (updated)
		//		{
		//			manager.updateContent(width);
		//		}
	}

	public void updateCursor()
	{
		cursorAlpha += cursorModify;
		if (cursorAlpha > 1.0f)
		{
			cursorAlpha = 1.0f;
			cursorModify = cursorModify * -1.0f;
		}

		if (cursorAlpha < 0.02f)
		{
			cursorModify = cursorModify * -1.0f;
			cursorAlpha = cursorModify;
		}
	}

	public ContentManager getManager()
	{
		return manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.IComplexTextRendererData#getText()
	 */
	public String getText()
	{
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.binding.render.text.IComplexTextRendererData#setText(java.lang.String)
	 */
	public void setText(String text)
	{
		if (!this.text.equals(text))
		{
			this.text = text;
		}
		updateContent();
	}

	private void updateContent()
	{
		if (isMultiline())
		{
			manager.updateContent(text, width);
		}
		else
		{
			if (text != null)
				manager.updateContent(TextUtil.noLineBreaks(text), width);
			else
				manager.updateContent(text, width);
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
		this.selectionColor = stream.processChild("SelectionColor", this.getSelectionColor(), Color.WHITE, Color.class);
		this.selectionBackground = stream.processChild("SelectionBackground", this.selectionBackground,
			new DecoratorLayer(), DecoratorLayer.class);
		setColor(stream.processChild("Color", this.getColor(), Color.BLACK, Color.class));
		this.text = stream.processAttribute("text", this.getText(), "");
		this.endMarker = stream.processAttribute("endMarker", this.endMarker, TextUtil.ENDMARKER0);
		this.setMultiline(stream.processAttribute("multiline", this.isMultiline(), false));
		this.setWordWarping(stream.processAttribute("wordwarped", this.isWordWarping(), false));
		this.setPasswordField(stream.processAttribute("passwordField", this.isPasswordField(), false));

		cursor = stream.processChild("Cursor", cursor, null, Pixmap.class);
		cursorColor = stream.processChild("CursorColor", cursorColor, Color.BLACK, Color.class);
	}

	public Color getColor()
	{
		return this.manager.getFactory().getActiveStyle().current.color;
	}

	public DecoratorLayer getSelectionBackground()
	{
		return selectionBackground;
	}

	public Color getSelectionColor()
	{
		return selectionColor;
	}

	public boolean isWordWarping()
	{
		return manager.isAutoWarp();
	}

	public boolean isConfined()
	{
		return false;
	}

	public boolean isMultiline()
	{
		return manager.isMultiline();
	}

	public void setColor(Color color)
	{
		this.manager.getFactory().getActiveStyle().current.color = color;
		//TODO set style to active part
	}

	public void setSelectionBackground(DecoratorLayer background)
	{
		this.selectionBackground = background;
	}

	public void setSelectionColor(Color color)
	{
		this.selectionColor = color;
	}

	public void setWordWarping(boolean warping)
	{
		if (warping != manager.isAutoWarp())
		{
			manager.setAutoWarp(warping, width);
		}
	}

	public void setConfined(boolean confined)
	{
		// Do nothing
	}

	public void setMultiline(boolean multiline)
	{
		if (multiline != manager.isMultiline())
		{
			manager.setMultiline(multiline);
			updateContent();
		}
	}

	private void setSelection(int pos1, int pos2)
	{
		if (pos1 == pos2)
			return;

		if (pos1 > pos2)
		{
			manager.setSelection(pos2, pos1);
		}
		else
		{
			manager.setSelection(pos1, pos2);
		}
	}

	public void clickedOn(int x, int y, Set<Key> modifiers)
	{
		manager.clearSelection();
		long pos = manager.findAtomOnPosition(x, this.getSize().getHeight() - y);
		if (manager.hasActiveLine() && modifiers.contains(Key.SHIFT))
		{
			selectionStart = (int) manager.getActiveAtom();
		}
		else
		{
			manager.setActiveAtom(pos);
			selectionStart = (int) pos;
		}
		manager.updateContent(width);
	}

	public void dragedTo(int x, int y, Set<Key> modifiers)
	{
		manager.clearSelection();
		long pos = manager.findAtomOnPosition(x, this.getSize().getHeight() - y);
		// long oldPos = manager.getStartSelection();
		//		
		// if (pos > manager.getStartSelection())
		// {
		// manager.setSelectionLength((int)(pos - oldPos));
		// } else {
		manager.setActiveAtom(pos);
		setSelection(selectionStart, (int) pos);
		// manager.setSelectionLength((int)oldPos);
		// }
	}

	public Dimension getSize()
	{
		return manager.getSize();
	}

	public void setConfinedMarker(String marker)
	{
		this.endMarker = marker;
	}

	/**
	 * @return Returns the cursorAlpha.
	 */
	public float getCursorAlpha()
	{
		return cursorAlpha;
	}

	/**
	 * @param cursorAlpha
	 *          The cursorAlpha to set.
	 */
	public void setCursorAlpha(float cursorAlpha)
	{
		this.cursorAlpha = cursorAlpha;
	}

	/**
	 * @return Returns the cursorModify.
	 */
	public float getCursorModify()
	{
		return cursorModify;
	}

	/**
	 * @param cursorModify
	 *          The cursorModify to set.
	 */
	public void setCursorModify(float cursorModify)
	{
		this.cursorModify = cursorModify;
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

	/**
	 * @return the editMode
	 */
	public boolean isEditMode()
	{
		return editMode;
	}

	/**
	 * @param editMode
	 *          the editMode to set
	 */
	public void setEditMode(boolean editMode)
	{
		if (this.editMode != editMode)
		{
			this.editMode = editMode;
			if (editMode && manager.getActiveAtom() < 0)
			{
				manager.setActiveAtom(0);
			}
		}
	}

	/**
	 * @return the passwordField
	 */
	public boolean isPasswordField()
	{
		return manager.hasHiddenContent();
	}

	/**
	 * @param passwordField
	 *          the passwordField to set
	 */
	public void setPasswordField(boolean passwordField)
	{
		manager.setHideContent(passwordField);
		updateContent();
	}

	/**
	 * @return the cursor
	 */
	public Pixmap getCursor()
	{
		return cursor;
	}

	/**
	 * @param cursor
	 *          the cursor to set
	 */
	public void setCursor(Pixmap cursor)
	{
		this.cursor = cursor;
	}

	/**
	 * @return the cursorColor
	 */
	public Color getCursorColor()
	{
		return cursorColor;
	}

	/**
	 * @param cursorColor
	 *          the cursorColor to set
	 */
	public void setCursorColor(Color cursorColor)
	{
		this.cursorColor = cursorColor;
	}

	public void setActiveFont(IFont font)
	{
		this.activeFont = font;
	}

	public IFont getActiveFont()
	{
		return activeFont;
	}

	public boolean handleKeyPresses(Key key, Set<Key> modifiers)
	{
		boolean result = false;
		
		switch (key)
		{
		case BACKSPACE:
			if (!isReadonly())
			{
				if (manager.hasSelection())
				{
					int atom = manager.getSelectionStart();
					manager.removeSelection();
					manager.setActiveAtom(atom);
				}
				else
				{
					manager.removePreviousChar();
					selectionStart = (int) manager.getActiveAtom();
				}
				result = true;
			}
			break;
		case SHIFT:
			break;
		case DELETE:
			if (!isReadonly())
			{
				if (manager.hasSelection())
				{
					int atom = manager.getSelectionStart();
					manager.removeSelection();
					manager.setActiveAtom(atom);
				}
				else
				{
					manager.removeNextChar();
				}
				result = true;
			}
			break;
		case UP:
			if (!isReadonly() && modifiers.contains(Key.SHIFT))
			{
				manager.clearSelection();
				manager.moveUp();
				long atom = manager.getActiveAtom();
				if (atom == -1)
					break;
				setSelection(selectionStart, (int) atom);
				manager.setActiveAtom(atom);
			}
			else
			{
				if (manager.hasSelection())
				{
					manager.clearSelection();
				}
				else
				{
					manager.moveUp();
				}
				selectionStart = (int) manager.getActiveAtom();
			}
			result = true;
			break;
		case RIGHT:
			if (!isReadonly() && modifiers.contains(Key.SHIFT))
			{
				manager.clearSelection();
				manager.moveRight();
				long atom = manager.getActiveAtom();
				if (atom == -1)
					break;
				setSelection(selectionStart, (int) atom);
				manager.setActiveAtom(atom);
			}
			else
			{
				if (manager.hasSelection())
				{
					manager.clearSelection();
				}
				else
				{
					manager.moveRight();
				}
				selectionStart = (int) manager.getActiveAtom();
			}
			result = true;
			break;
		case LEFT:
			if (!isReadonly() && modifiers.contains(Key.SHIFT))
			{
				manager.clearSelection();
				manager.moveLeft();
				long atom = manager.getActiveAtom();
				if (atom == -1)
					break;
				setSelection(selectionStart, (int) atom);
				manager.setActiveAtom(atom);
			}
			else
			{
				if (manager.hasSelection())
				{
					manager.clearSelection();
				}
				else
				{
					manager.moveLeft();
				}
				selectionStart = (int) manager.getActiveAtom();
			}
			result = true;
			break;
		case DOWN:
			if (!isReadonly() && modifiers.contains(Key.SHIFT))
			{
				manager.clearSelection();
				manager.moveDown();
				long atom = manager.getActiveAtom();
				setSelection(selectionStart, (int) atom);
				manager.setActiveAtom(atom);
			}
			else
			{
				if (manager.hasSelection())
				{
					manager.clearSelection();
				}
				else
				{
					manager.moveDown();
				}
				selectionStart = (int) manager.getActiveAtom();
			}
			result = true;
			break;
		case END:
			if (!isReadonly() && modifiers.contains(Key.SHIFT))
			{
				manager.clearSelection();
				manager.setActiveAtom(manager.getAtomsTillActiveLineEnd());
				long atom = manager.getActiveAtom();
				setSelection(selectionStart, (int) atom);
				manager.setActiveAtom(atom);
			}
			else
			{
				manager.clearSelection();
				manager.setActiveAtom(manager.getAtomsTillActiveLineEnd());
				selectionStart = (int) manager.getActiveAtom();
			}
			result = true;
			break;
		case HOME:
			if (!isReadonly() && modifiers.contains(Key.SHIFT))
			{
				manager.clearSelection();
				manager.setActiveAtom(manager.getAtomsTillActiveLine());
				long atom = manager.getActiveAtom();
				setSelection(selectionStart, (int) atom);
				manager.setActiveAtom(atom);
			}
			else
			{
				manager.clearSelection();
				manager.setActiveAtom(manager.getAtomsTillActiveLine());
				selectionStart = (int) manager.getActiveAtom();
			}
			result = true;
			break;
		case COPY:
			String selectedContent;
			if (!isPasswordField())
			{
				selectedContent = manager.getSelectedContent();
				clipboard.setText(selectedContent);
				result = true;
			}
			break;
		case CUT:
			selectedContent = manager.getSelectedContent();
			if (!isReadonly())
				manager.removeSelection();
			clipboard.setText(selectedContent);
			result = true;
			break;
		case PASTE:
			if (!isReadonly())
			{
				selectedContent = clipboard.getText();
				manager.addContent(selectedContent);
				result = true;
			}
			break;
		case CTRL:
			break;
		}
		
		return result;
	}

	public boolean handleTextInput(char character)
	{
		boolean result = false;
		
		if (manager.hasSelection())
		{
			manager.removeSelection();
		}

		result = manager.addChar(character);
		selectionStart = (int) manager.getActiveAtom();
		return result;
	}

	/**
	 * @param listener
	 * @return
	 * @see org.fenggui.binding.render.text.advanced.ContentManager#addSizeChangedListener(org.fenggui.event.ISizeChangedListener)
	 */
	public boolean addSizeChangedListener(ISizeChangedListener listener)
	{
		return manager.addSizeChangedListener(listener);
	}

	/**
	 * @param listener
	 * @return
	 * @see org.fenggui.binding.render.text.advanced.ContentManager#removeSizeChangedListener(org.fenggui.event.ISizeChangedListener)
	 */
	public boolean removeSizeChangedListener(ISizeChangedListener listener)
	{
		return manager.removeSizeChangedListener(listener);
	}

	public Dimension getContentSize()
	{
		return manager.getSize();
	}

	public void addContentAtBeginning(String content)
	{
		manager.addAtBeginning(content);
	}

	public void addContentAtEnd(String content)
	{
		manager.addAtEnd(content);
	}

	public void removeContentLineFromBeginning()
	{
	}

	public void removeContentLineFromEnd()
	{
	}

	public int getContentLineCount()
	{
		return manager.getContentLineCount();
	}

	public int getMaxLines()
	{
		return manager.getMaxLines();
	}

	public void setMaxLines(int lines)
	{
		manager.setMaxLines(lines);
	}

	public boolean isReadonly()
	{
		return readonly;
	}

	public void setReadonly(boolean readonly)
	{
		this.readonly = readonly;
	}

	public int getActivePositionIndex()
	{
		return (int) manager.getActiveAtom();
	}

	public boolean hasSelection()
	{
		return manager.hasSelection();
	}
	
	public int getSelectionEndIndex()
	{
		return manager.getSelectionEnd();
	}

	public int getSelectionStartIndex()
	{
		return manager.getSelectionStart();
	}

	public void setActivePositionIndex(int index)
	{
		manager.setActiveAtom(index);
	}

	public void setSelectionIndex(int index1, int index2)
	{
		manager.setSelection(index1, index2);
	}

	public void setManager(ContentManager manager)
	{
		this.manager = manager;
	}

	public void adaptSize(int x, int y)
	{
		setSize(x, y);
	}
}
