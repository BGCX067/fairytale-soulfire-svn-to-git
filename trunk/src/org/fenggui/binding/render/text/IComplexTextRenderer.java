package org.fenggui.binding.render.text;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.util.Dimension;
import org.fenggui.util.ICopyable;

/**
 * A complex TextRenderer is a class that uses one or more TextRenderers to render text.
 * What it is capable of depends on its implementation. A simple implementation 
 * (@see ComplexTextRenderer) will just render the text. A more komplex implementation
 * will know how to word-warp, use bigger line heights, write with different fonts, use
 * different colors, aso. So a complex TextRenderer does all the high level text manipulation.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface IComplexTextRenderer extends IXMLStreamable, ICopyable<IComplexTextRenderer>
{
	/**
	 * Renders the text.
	 * 
	 * @param x x-position to start rendering
	 * @param y y-position to start rendering
	 * @param w width of render area
	 * @param h height of render area
	 * @param data data to render
	 * @param g graphics object
	 * @param gl opengl object
	 */
	public void render(int x, int y, int w, int h, IComplexTextRendererData data, Graphics g, IOpenGL gl);

	/**
	 * Calculates the resulting size of the given TextRenderData object. This can be time
	 * intensive depending on the implementation.
	 * 
	 * @param data
	 * @return
	 */
	public Dimension calculateSize(IComplexTextRendererData data);

	/**
	 * Returns the line height. With different implementations the
	 * line height may vary depending on the current TextRenderer in use.
	 * @return
	 */
	public int getLineHeight(IComplexTextRendererData data, int linenumber);

	/**
	 * Returns the width of the first line of text in the data object.
	 * 
	 * @param data
	 * @return
	 */
	public int getTextWidth(IComplexTextRendererData data);

	/**
	 * Returns a correct data object for this textrenderer.
	 * 
	 * @param data Data object to use for initialization.
	 * @return A data object for this textrenderer.
	 */
	public IComplexTextRendererData createData(IComplexTextRendererData data);
	
}
