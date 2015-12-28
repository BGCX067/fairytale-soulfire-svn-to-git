package org.fenggui.binding.render;

import java.awt.image.BufferedImage;

import org.fenggui.util.CharacterPixmap;

public interface IImageFont
{
	public void uploadToVideoMemory();
	public BufferedImage getImage();
	public CharacterPixmap getCharPixMap(char ch);
}
