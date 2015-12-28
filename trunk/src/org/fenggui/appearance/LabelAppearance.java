package org.fenggui.appearance;

import java.io.IOException;

import org.fenggui.StandardWidget;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;

/**
 * 
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2008-04-15 12:09:43 +0200 (Di, 15 Apr 2008) $
 * @version $Revision: 499 $
 * @dedication Frank Sinatra - Bad Leroy Brown
 */
public class LabelAppearance extends TextAppearance
{
	private int gap = 5;
	
	public LabelAppearance(StandardWidget w)
	{
		super(w);
	}

	public LabelAppearance(StandardWidget w, InputOnlyStream stream) throws IOException, IXMLStreamableException
	{
		this(w);
		this.process(stream);
	}
	
	public LabelAppearance(StandardWidget w, LabelAppearance appearance)
	{
		super(w, appearance);
		
		this.gap = appearance.getGap();
	}
	
	public int getGap()
	{
		return gap;
	}

	public void setGap(int gap)
	{
		this.gap = gap;
	}

	@Override
	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		super.process(stream);
		
		gap = stream.processAttribute("gap", gap, 5);
		
	}

}
