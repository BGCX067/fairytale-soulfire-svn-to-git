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
 * $Id: TextEditor.java 569 2008-06-24 10:08:17Z marcmenghin $
 */
package org.fenggui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

import org.fenggui.appearance.TextEditorAppearance;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.CursorFactory;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.CursorFactory.CursorType;
import org.fenggui.event.*;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.key.KeyTypedEvent;
import org.fenggui.event.mouse.MouseButton;
import org.fenggui.event.mouse.MouseDoubleClickedEvent;
import org.fenggui.event.mouse.MouseEnteredEvent;
import org.fenggui.event.mouse.MouseExitedEvent;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * Implementation of a text editor. Text editors come in multiple lines (text area) or
 * single line variants (text field). If it is set to multiline it is able to auto word
 * warp the text.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2007-08-11
 *         13:20:15 +0200 (Sa, 11 Aug 2007) $
 * @version $Revision: 569 $
 * @dedication No Use For a Name - Invincible
 */
public class TextEditor extends StatefullWidget<TextEditorAppearance> implements ITextWidget
{
	// a few nice definitions to restirct userinput
	public static final String RESTRICT_NUMBERSONLY = "[0-9-+]+";
	public static final String RESTRICT_NUMBERSONLYDECIMAL = "[0-9.,-+]+";
	public static final String RESTRICT_LETTERSONLY = "[A-Z]+";
	public static final String RESTRICT_CHARACTERSOFIP = "[0-9.:]+";
	public static final String RESTRICT_LETTERSANDNUMBERS = "[A-Z0-9]+";
	public static final String RESTRICT_EMAIL = "[A-Z0-9\\._%\\+\\-@]+";

	private ArrayList<ITextChangedListener> textChangedHook = new ArrayList<ITextChangedListener>();

	private TextEditorDnDListener dndListener = null;
	private ISizeChangedListener textSizeChangedListener;

	/**
	 * Define the max number of character that can be added to the TextEditor
	 */
	private int maxCharacters = -1;

	/**
	 * Define a regularExpression representing allowed characters.
	 */
	private Pattern restrict = null;

	/**
	 * Define if the regularExpression accepts unicode characters.
	 */
	private boolean unicodeRestrict = true;

	public TextEditor()
	{
		setAppearance(new TextEditorAppearance(this));
		setupDefaults();
		updateMinSize();
	}

	public TextEditor(TextEditor widget)
	{
		super(widget);

		setAppearance(new TextEditorAppearance(this, widget.getAppearance()));
		setupDefaults();
		updateMinSize();
	}

	private void setupDefaults() {
		dndListener = new TextEditorDnDListener(this);
		setTraversable(true);
		setDefaultHoverCursorType(CursorType.TEXT);
		textSizeChangedListener = new ISizeChangedListener()
		{

			public void sizeChanged(SizeChangedEvent event)
			{
				updateMinSize();
			}

		};		
	}
	
	/* (non-Javadoc)
	 * @see org.fenggui.StatefullWidget#focusChanged(org.fenggui.event.FocusEvent)
	 */
	@Override
	public void focusChanged(FocusEvent focusGainedEvent)
	{
		getAppearance().getData().setEditMode(focusGainedEvent.isFocusGained());

		super.focusChanged(focusGainedEvent);
	}

	/* (non-Javadoc)
	 * @see org.fenggui.Widget#sizeChanged(org.fenggui.event.SizeChangedEvent)
	 */
	@Override
	public void sizeChanged(SizeChangedEvent event)
	{
		this.getAppearance().getData().adaptSize(getAppearance().getContentWidth(), getAppearance().getContentHeight());
		super.sizeChanged(event);
	}

	/* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#keyPressed(org.fenggui.event.key.KeyPressedEvent)
   */
  @Override
  public void keyPressed(KeyPressedEvent keyPressedEvent) {
		if (isEnabled())
		{

  	if (isInWritingState())
			if (handleKeyPressed(keyPressedEvent))
				keyPressedEvent.setUsed();
		}  	
	  super.keyPressed(keyPressedEvent);
  }

	/* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#keyTyped(org.fenggui.event.key.KeyTypedEvent)
   */
  @Override
  public void keyTyped(KeyTypedEvent keyTypedEvent) {
		if (isEnabled())
		{
	  	if (isInWritingState() && !keyTypedEvent.isAlreadyUsed())
				if (handleKeyTyped(keyTypedEvent))
					keyTypedEvent.setUsed();
		}
	  super.keyTyped(keyTypedEvent);
  }

	/* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseDoubleClicked(org.fenggui.event.mouse.MouseDoubleClickedEvent)
   */
  @Override
  public void mouseDoubleClicked(MouseDoubleClickedEvent event) {
		if (isEnabled())
		{
	  	if (event.getButton() == MouseButton.LEFT)
	  	{
	  		getAppearance().getData().setSelectionIndex(0, getText().length());
	  		event.setUsed();
	  	}
		}
	  super.mouseDoubleClicked(event);
  }

	/* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseEntered(org.fenggui.event.mouse.MouseEnteredEvent)
   */
  @Override
  public void mouseEntered(MouseEnteredEvent mouseEnteredEvent) {
		if (isEnabled())
		{
	  	Binding.getInstance().getCursorFactory().getCursor(CursorFactory.CursorType.TEXT).show();
			getDisplay().addDndListener(dndListener);
		}
		super.mouseEntered(mouseEnteredEvent);
  }

	/* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseExited(org.fenggui.event.mouse.MouseExitedEvent)
   */
  @Override
  public void mouseExited(MouseExitedEvent mouseExitedEvent) {
  	Binding.getInstance().getCursorFactory().getCursor(CursorFactory.CursorType.DEFAULT).show();
		if (getDisplay() != null)
			getDisplay().removeDndListener(dndListener);
		
  	super.mouseExited(mouseExitedEvent);
  }

	private void buildSpecialEvents()
	{
		getAppearance().getData().removeSizeChangedListener(textSizeChangedListener);
		getAppearance().getData().addSizeChangedListener(textSizeChangedListener);
	}

	private boolean handleKeyTyped(KeyTypedEvent e)
	{
		boolean result = false;
		
		if (this.maxCharacters >= 0 && this.getText().length() >= this.maxCharacters && !this.getAppearance().getData().hasSelection())
			return false;

		if (!(e.getKey() == '\t') && !(e.isPressed(Key.META)))
		{
		if (getAppearance().getRenderer().isValidChar(getAppearance().getData(), e.getKey())
				&& Character.isDefined(e.getKey()))
		{
			if (restrict != null && !restrict.matcher(Character.toString(e.getKey())).matches())
			{
				return false;
			}

			if (e.getKeyClass() != Key.TAB)
				if (!isReadonly())
					result = getAppearance().getData().handleTextInput(e.getKey());
		}
	}
		processTextChange(null);
		
		return result;
	}

	private boolean handleKeyPressed(KeyPressedEvent e)
	{
		switch (e.getKeyClass())
		{
		case LETTER:
			// TODO: move this into event binding somehow (so all benefit from this)
			// also this is not platform independent
	    String vers = System.getProperty("os.name").toLowerCase();
	    boolean isMac = vers.indexOf("mac") != -1;
			if ((!isMac && e.isPressed(Key.CTRL)) || (isMac && e.isPressed(Key.META)))
			{
				if (e.getKey() == 'C')
				{
					e.setUsed();
					return getAppearance().getData().handleKeyPresses(Key.COPY, e.getModifiers());
				}
				else if (e.getKey() == 'X')
				{
					e.setUsed();
					return getAppearance().getData().handleKeyPresses(Key.CUT, e.getModifiers());
				}
				else if (e.getKey() == 'V')
				{
					e.setUsed();
					return getAppearance().getData().handleKeyPresses(Key.PASTE, e.getModifiers());
				}
				else
				{
					return getAppearance().getData().handleKeyPresses(e.getKeyClass(), e.getModifiers());
				}
			}
			else
			{
				return getAppearance().getData().handleKeyPresses(e.getKeyClass(), e.getModifiers());
			}
		default:
			return getAppearance().getData().handleKeyPresses(e.getKeyClass(), e.getModifiers());
		}
	}

	/**
	 * 
	 * @return the maxCharacters
	 */
	public int getMaxCharacters()
	{
		return maxCharacters;
	}

	/**
	 * The maximum number of characters a user can enter into this field.
	 * 
	 * @param maxCharacters
	 *          the max number of characters in the textEditor
	 */
	public void setMaxCharacters(int maxCharacters)
	{
		this.maxCharacters = maxCharacters;
	}

	/**
	 * @return the validCharacters
	 */
	public String getRestrict()
	{
		return restrict.pattern();
	}

	/**
	 * Sets the valid characters as a RegularExpression. if we want to enable only letters
	 * from a to z and numbers, we would set : "[a-zA-Z0-9]+"
	 * 
	 * @param validCharacters
	 *          a regular expression representing valid characters
	 */
	public void setRestrict(String restrict)
	{
		if (restrict != null)
		{
			if (unicodeRestrict)
			{
				this.restrict = Pattern.compile(restrict, Pattern.UNICODE_CASE);
			}
			else
			{
				this.restrict = Pattern.compile(restrict);
			}
		}
		else
		{
			this.restrict = null;
		}
	}

	/**
	 * @return the unicodeRestrict
	 */
	public boolean isUnicodeRestrict()
	{
		return unicodeRestrict;
	}

	/**
	 * @param unicodeRestrict
	 *          a d�fnir
	 */
	public void setUnicodeRestrict(boolean unicodeRestrict)
	{
		this.unicodeRestrict = unicodeRestrict;
	}

	/**
	 * @return the passwordField
	 */
	public boolean isPasswordField()
	{
		return getAppearance().isPasswordField();
	}

	/**
	 * Sets if this field is a password field. If so instead of the user input the
	 * PasswordChar will be displayed to the user. A Entered password will only be stored in
	 * one String within this class.
	 * 
	 * @param passwordField
	 *          the passwordField to set
	 */
	public void setPasswordField(boolean passwordField)
	{
		this.getAppearance().setPasswordField(passwordField);
	}

	/**
	 * @return the text editor's text
	 */
	public String getText()
	{
		return getAppearance().getData().getText();
	}

	public void setEmptyText(String text)
	{
		getAppearance().getEmptyData().setText(text);
		updateMinSize();
	}

	/**
	 * Define the textEditor's text
	 * 
	 * @param text
	 */
	public void setText(String text)
	{
		String fittingText = "";
		if (text != null && text.length() != 0)
		{
			if (maxCharacters < 0 || text.length() <= maxCharacters)
			{
				fittingText = text;
			}
			else
			{
				fittingText = text.substring(0, maxCharacters);
			}
		}

		getAppearance().getData().setText(fittingText);
		buildSpecialEvents();
		processTextChange(text);
	}

	/**
	 * Add content at the end of the current content. This content gets a new line. Also it
	 * will remove all lines from the opposite side.
	 * 
	 * @param content
	 */
	public void addContentAtEnd(String content)
	{
		getAppearance().getData().addContentAtEnd(content);
	}

	/**
	 * Adds the given content before the current content within the TextEditor. Also it will
	 * remove all lines from the opposite side.
	 * 
	 * @param content
	 */
	public void addContentAtBeginning(String content)
	{
		getAppearance().getData().addContentAtBeginning(content);
	}

	@Override
	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		super.process(stream);
	}

	private void processTextChange(String txt)
	{
		scrollToCursorPosition();
		fireTextChangedEvent(txt);
	}

	private void scrollToCursorPosition()
	{
//		 if (getParent() != null && getParent() instanceof ScrollContainer)
//		 {
//		 // TODO: implement for vertical
//		
//		 if (multiline)
//		 {
//		 int cursorIndex = this.getCursorWarped();
//		
//		 // FIXME: sometimes this happens but shouldn't, don't know why anyone else? :)
//		 if (cursorIndex > text_prepared.length())
//		 {
//		 cursorIndex = text_prepared.length();
//		 }
//		 String[] lines = text_prepared.toString().substring(0, cursorIndex).split("\n", -1);
//		 int height = lines.length + 1;
//		 height *= getAppearance().getTextLineHeight();
//		 double scroll = 100.0d - ((100.0d / this.getMinHeight()) * height);
//		 if (scroll < 0.0d)
//		 scroll = 0.0d;
//		
//		 ((ScrollContainer) getParent()).scrollVertical(scroll);
//		 }
//		 }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fenggui.StatefullWidget#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);

		if (!enabled)
			this.getAppearance().getData().setEditMode(false);
	}

	private class TextEditorDnDListener implements IDragAndDropListener
	{
		TextEditor parent = null;

		public TextEditorDnDListener(TextEditor parent)
		{
			this.parent = parent;
		}

		public boolean isDndWidget(IWidget w, int displayX, int displayY)
		{
			return w.equals(parent);
		}

		public void select(int displayX, int displayY, Set<Key> modifiers)
		{
			if (!parent.isEnabled())
				return;
			
			int xPos = displayX - parent.getDisplayX();
			int yPos = displayY - parent.getDisplayY();

			int emptyHeight = getHeight() - getAppearance().getData().getSize().getHeight();
			if (emptyHeight > 0)
				yPos -= emptyHeight;

			if (yPos < 0)
				yPos = 0;

			getAppearance().getData().clickedOn(xPos, yPos, modifiers);
		}

		public void drag(int displayX, int displayY, Set<Key> modifiers)
		{
			if (!parent.isEnabled())
				return;

			int xPos = displayX - parent.getDisplayX();
			int yPos = displayY - parent.getDisplayY();

			int emptyHeight = getHeight() - getAppearance().getData().getSize().getHeight();
			if (emptyHeight > 0)
				yPos -= emptyHeight;

			if (yPos < 0)
				yPos = 0;

			getAppearance().getData().dragedTo(xPos, yPos, modifiers);
		}

		public void drop(int displayX, int displayY, IWidget droppedOn, Set<Key> modifiers)
		{
			if (!parent.isEnabled())
				return;

			int xPos = displayX - parent.getDisplayX();
			int yPos = displayY - parent.getDisplayY();

			int emptyHeight = getHeight() - getAppearance().getData().getSize().getHeight();
			if (emptyHeight > 0)
				yPos -= emptyHeight;

			if (yPos < 0)
				yPos = 0;

			getAppearance().getData().dragedTo(xPos, yPos, modifiers);
		}
	}

	public boolean isInWritingState()
	{
		return getAppearance().getData().isEditMode();
	}

	/**
	 * Add a {@link ITextChangedListener} to the widget. The listener can be added only
	 * once.
	 * 
	 * @param l
	 *          Listener
	 */
	public void addTextChangedListener(ITextChangedListener l)
	{
		if (!textChangedHook.contains(l))
		{
			textChangedHook.add(l);
		}
	}

	/**
	 * Add the {@link ITextChangedListener} from the widget
	 * 
	 * @param l
	 *          Listener
	 */
	public void removeTextChangedListener(ITextChangedListener l)
	{
		textChangedHook.remove(l);
	}

	/**
	 * Fire a {@link TextChangedEvent}
	 */
	private void fireTextChangedEvent(String text)
	{
		TextChangedEvent e = new TextChangedEvent(this, text);

		for (ITextChangedListener l : textChangedHook)
		{
			l.textChanged(e);
		}

		// Why should this be fired globally?
		// Display display = getDisplay();
		// if (display != null)
		// {
		// display.fireGlobalEventListener(e);
		// }
	}

	@Override
	public Dimension getMinContentSize()
	{
		Dimension size = getAppearance().getTextContentSize();

		if (getAppearance().getData().isWordWarping())
		{
			size.setWidth(5);
		}

		if (getAppearance().getData().isConfined())
		{
			size.setWidth(Math.max(5, this.getSize().getWidth()));
			size.setHeight(Math.max(5, this.getSize().getHeight()));
		}

		return size;
	}

	@Override
	public void paintContent(Graphics g, IOpenGL gl)
	{
		// user contribution to make text appear within visible area
		// if(font.getWidth(text.toCharArray(), 0,
		// text.toCharArray().length)>=(editor.getWidth()-editor.getAppearance().getPadding().getLeftPlusRight()))
		// {
		// x-=font.getWidth(text.toCharArray(), 0,
		// text.toCharArray().length)-(editor.getWidth()-editor.getAppearance().getPadding().getLeftPlusRight());
		// }
	
		Dimension textSize = getAppearance().getTextContentSize();
		int x = getAppearance().getAlignment().alignX(getAppearance().getContentWidth(), textSize.getWidth());
		int y = getAppearance().getContentHeight();
		
//		g.setColor(new Color(255,0,0,100));
//		g.drawFilledRectangle(0, 0, getAppearance().getContentWidth(), getAppearance().getContentHeight());
		
		if (y > textSize.getHeight())
		{
			y = getAppearance().getAlignment().alignY(y, textSize.getHeight()) + textSize.getHeight();
		}
		getAppearance().renderText(x, y, g, gl);
	}

	public int getMaxLines()
	{
		return getAppearance().getData().getMaxLines();
	}

	public void setMaxLines(int maxLines)
	{
		getAppearance().getData().setMaxLines(maxLines);
	}

	public boolean isReadonly()
	{
		return getAppearance().getData().isReadonly();
	}

	public void setReadonly(boolean readonly)
	{
		getAppearance().getData().setReadonly(readonly);
	}
}
