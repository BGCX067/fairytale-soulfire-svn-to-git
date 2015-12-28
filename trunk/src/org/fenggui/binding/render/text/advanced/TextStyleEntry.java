/**
 * 
 */
package org.fenggui.binding.render.text.advanced;

import org.fenggui.DecoratorLayer;
import org.fenggui.binding.render.CursorFactory.CursorType;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.util.Color;

/**
 * @author Marc Menghin
 * 
 */
public class TextStyleEntry
{
	public ITextRenderer renderer = null;
	public Color color;
	public Color selectionColor;
	public DecoratorLayer background;
	public DecoratorLayer selectionBackground;
	public CursorType mouseCursor;
}
