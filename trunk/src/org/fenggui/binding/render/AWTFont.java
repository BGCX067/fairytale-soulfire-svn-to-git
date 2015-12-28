package org.fenggui.binding.render;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.io.IOException;

import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;

public class AWTFont extends org.fenggui.binding.render.Font implements IFont, IAWTFont
{
	private Font font;
	private FontMetrics metric = null;
	private boolean antialiased = false;
	private boolean useFractionalMetrics = false;
	private boolean underlined = false;
	
	public AWTFont(Font font, boolean antialiased, boolean useFractionalMetrics, boolean underlined)
	{
		this.font = font;
		this.antialiased = antialiased;
		this.useFractionalMetrics = useFractionalMetrics;
		this.underlined = underlined;
	}

	/**
	 * Loads a Font from an InputOnlyStream
	 * @throws IXMLStreamableException 
	 * @throws IOException 
	 */
	public AWTFont(InputOnlyStream stream) throws IOException, IXMLStreamableException
	{
		process(stream);
	}

	public Dimension calculateSize(String text)
	{
		return null;
	}

	private FontMetrics getMetric()
	{
		if (this.metric == null) this.metric = Toolkit.getDefaultToolkit().getFontMetrics(font);

		return metric;
	}

	public int getLineHeight()
	{

		return getMetric().getHeight();
	}

	public int getWidth(String text)
	{
		return getMetric().stringWidth(text);
	}

	public boolean isCharacterMapped(char c)
	{
		return font.canDisplay(c);
	}

	public Font getFont()
	{
		return font;
	}

	public String getUniqueName()
	{
		return GENERATE_NAME;
	}

	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		String name = stream.processAttribute("fontName", "not-set");
		String typeStr = stream.processAttribute("type", "not-set", "plain");
		int type = java.awt.Font.PLAIN;

		if (typeStr.equalsIgnoreCase("plain")) type = java.awt.Font.PLAIN;
		else if (typeStr.equalsIgnoreCase("bold")) type = java.awt.Font.BOLD;
		else if (typeStr.equalsIgnoreCase("italic")) type = java.awt.Font.ITALIC;
		else throw new IllegalArgumentException("Unknwown font type '" + typeStr + "'");

		int size = stream.processAttribute("size", 16);
		font = new java.awt.Font(name, type, size);
		antialiased = stream.processAttribute("antialiasing", true, false);
		useFractionalMetrics = stream.processAttribute("fractionalMetrics", true, false);
	}

	/**
	 * @return Returns the antialiased.
	 */
	public boolean isAntialiased()
	{
		return antialiased;
	}

	/**
	 * @return Returns the useFractionalMetrics.
	 */
	public boolean isUseFractionalMetrics()
	{
		return useFractionalMetrics;
	}

	public boolean isUnderlined() {
  	return underlined;
  }

	public void setUnderlined(boolean underlined) {
  	this.underlined = underlined;
  }
}
