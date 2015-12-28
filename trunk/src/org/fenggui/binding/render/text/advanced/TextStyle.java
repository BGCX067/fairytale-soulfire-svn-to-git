/**
 * 
 */
package org.fenggui.binding.render.text.advanced;

/**
 * @author Marc Menghin
 * 
 */
public class TextStyle
{

	public TextStyleEntry defaultStyle = new TextStyleEntry();
	public TextStyleEntry hoveredStyle = new TextStyleEntry();
	public TextStyleEntry clickedStyle = new TextStyleEntry();
	public TextStyleEntry selectedStyle = new TextStyleEntry();

	public TextStyleEntry current;

	/**
	 * 
	 */
	public TextStyle()
	{
		current = defaultStyle;
	}

	public void setStyle(TextStyleEntry style)
	{
		current = style;
	}
}
