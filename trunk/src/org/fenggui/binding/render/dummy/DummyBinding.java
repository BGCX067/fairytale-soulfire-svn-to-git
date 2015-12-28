package org.fenggui.binding.render.dummy;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import org.fenggui.binding.clipboard.DummyClipboard;
import org.fenggui.binding.clipboard.IClipboard;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.CursorFactory;
import org.fenggui.binding.render.ITexture;
import org.fenggui.theme.XMLTheme;

public class DummyBinding extends Binding
{
	private DummyClipboard clipboard = new DummyClipboard();
	
	public DummyBinding()
	{
		super(new DummyOpenGL());
		XMLTheme.TYPE_REGISTRY.register("Texture", DummyTexture.class);
	}


	@Override
	public ITexture getTexture(InputStream is) throws IOException
	{
		return new DummyTexture();
	}


	@Override
	public ITexture getTexture(BufferedImage bi)
	{
		return new DummyTexture();
	}


	@Override
	public int getCanvasWidth()
	{
		return 1024;
	}


	@Override
	public int getCanvasHeight()
	{
		return 768;
	}


	@Override
	public CursorFactory getCursorFactory()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public IClipboard getClipboard()
	{
		return clipboard;
	}

}
