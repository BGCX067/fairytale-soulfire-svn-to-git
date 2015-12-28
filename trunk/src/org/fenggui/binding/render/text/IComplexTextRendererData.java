package org.fenggui.binding.render.text;

import org.fenggui.binding.render.IFont;
import org.fenggui.event.ISizeChangedListener;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * This stores the data a ComplexTextRenderer needs. Normally this object will hold the
 * Text, Colors and TextRenderers that are used. In most cases it will only be usable with
 * one specific complex TextRenderer.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface IComplexTextRendererData extends IXMLStreamable {
	/**
	 * Sets the text that will be used. This text may be transformed depending on the
	 * implementation.
	 * 
	 * @param text
	 *          text to use.
	 */
	public void setText(String text);

	public String getText();
	
	/**
	 * Sets the color in which the text should be drawn.
	 * 
	 * @param color
	 *          color of the text.
	 */
	public void setColor(Color color);

	/**
	 * Returns the color that should be used to draw the text.
	 * 
	 * @return Returns color of text.
	 */
	public Color getColor();

	/**
	 * If true the text will be cut if there is not enough space for it.
	 * 
	 * @return true if the text should be cut.
	 */
	public boolean isConfined();

	/**
	 * Sets if the text should be cut off if it doesn't fit.
	 * 
	 * @param confined
	 *          true if it should be cut off.
	 */
	public void setConfined(boolean confined);

	/**
	 * If false all Text after the first linebreak will be removed.
	 * 
	 * @return false if all text after the first linebreak should be removed.
	 */
	public boolean isMultiline();

	/**
	 * Sets if the text should only be used till the first linebreak. If false all text
	 * after the linebreak will be removed. If this is false, word-warping still can be
	 * used.
	 * 
	 * @param multiline
	 */
	public void setMultiline(boolean multiline);

	/**
	 * If true, text that is too long for a line will automatically be put on the next line.
	 * 
	 * @return true if too long text should be warped to the next line.
	 */
	public boolean isWordWarping();

	/**
	 * Sets that too long text should be put to the next line. This also works if multiline
	 * is set to false.
	 * 
	 * @param warp
	 *          if true all text that is too long for one line will be split so it fits into
	 *          the line.
	 */
	public void setWordWarping(boolean warp);

	/**
	 * uses this size to break or confine a string. Should be called if the widget is resized.
	 * 
	 * @param x
	 * @param y
	 */
	public void adaptSize(int x, int y);
	
	/**
	 * Sets the string that will be appended to the end if the whole text doesn't fit. This
	 * indicates that there is more text that can't be displayed. The default value is
	 * TextUtil.ENDMARKER0.
	 * 
	 * @see TextUtil
	 * 
	 * @param marker
	 */
	public void setConfinedMarker(String marker);

	/**
	 * 
	 * @param listener
	 */
	public boolean addMinSizeChangedListener(ISizeChangedListener listener);

	/**
	 * 
	 * @param listener
	 */
	public boolean removeMinSizeChangedListener(ISizeChangedListener listener);

	/**
	 * Sets the active font. This font will be used for all text if only one Font is used to
	 * display the text. If multiple Fonts can be used it depends on the implementation. A
	 * TextEditor maybe uses the activeFont for text that is written after it is set here.
	 * 
	 * <p>
	 * NOTE: The font set here needs to be compatible with the used TextRenderer. Different
	 * TextRenderers are compatible to different Font implementations.
	 * </p>
	 * 
	 * @param font
	 */
	public void setActiveFont(IFont font);

	/**
	 * Returns the active Font set or null if none or a wrong font has been set.
	 * 
	 * @return active Font or null.
	 */
	public IFont getActiveFont();
	
	/**
	 * Should return the size of the content (cached if possible).
	 * 
	 * @return size of the content
	 */
	public Dimension getContentSize();
}
