package org.fenggui.composite.text;

import java.io.IOException;

import org.fenggui.appearance.DecoratorAppearance;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;

public class TextViewAppearance extends DecoratorAppearance
{
	private Color textColor = Color.BLACK;
	private ImageFont font = ImageFont.getDefaultFont();
	
	public TextViewAppearance(TextView w)
	{
		super(w);
	}

	public ImageFont getFont() 
	{
		return font;
	}

	public void setFont(ImageFont font) 
	{
		this.font = font;
	}

	public Color getTextColor() 
	{
		return textColor;
	}

	public void setTextColor(Color textColor)
	{
		this.textColor = textColor;
	}
	
	
	@Override
	public void process(InputOutputStream stream) throws IOException,
			IXMLStreamableException {
		super.process(stream);
		
		if(stream.isInputStream()) // XXX: only support read-in at the moment :(
			setFont(stream.processChild("Font", getFont(), ImageFont.getDefaultFont(), ImageFont.class));
		
		textColor = stream.processChild("Color", textColor, Color.BLACK, Color.class);		
	}
}