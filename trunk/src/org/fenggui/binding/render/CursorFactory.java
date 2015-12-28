package org.fenggui.binding.render;

import java.awt.image.BufferedImage;

public abstract class CursorFactory 
{
	public enum CursorType
	{
		DEFAULT,
		MOVE,
		TEXT,
		HORIZONTAL_RESIZE,
		VERTICAL_RESIZE,
		NW_RESIZE,
		SW_RESIZE,
		HAND,
		FORBIDDEN,
		WEB
	}
	
	private Cursor defaultCursor = null;
	private Cursor moveCursor = null;
	private Cursor textCursor = null;
	private Cursor horizontalResizeCursor = null;
	private Cursor verticalResizeCursor = null;
	private Cursor NWResizeCursor = null;
	private Cursor SWResizeCursor = null;
	private Cursor handCursor = null;
	private Cursor forbiddenCursor = null;
	private Cursor webCursor = null;
	
	public abstract Cursor createCursor(int xHotspot, int yHotspot, BufferedImage image);

	public Cursor getDefaultCursor() {
		return defaultCursor;
	}

	public void setDefaultCursor(Cursor defaultCursor) {
		this.defaultCursor = defaultCursor;
	}

	public Cursor getHandCursor() {
		return handCursor;
	}

	public void setHandCursor(Cursor handCursor) {
		this.handCursor = handCursor;
	}

	public Cursor getHorizontalResizeCursor() {
		return horizontalResizeCursor;
	}

	public void setHorizontalResizeCursor(Cursor horizontalResizeCursor) {
		this.horizontalResizeCursor = horizontalResizeCursor;
	}

	public Cursor getMoveCursor() {
		return moveCursor;
	}

	public void setMoveCursor(Cursor moveCursor) {
		this.moveCursor = moveCursor;
	}

	public Cursor getNWResizeCursor() {
		return NWResizeCursor;
	}

	public void setNWResizeCursor(Cursor resizeCursor) {
		NWResizeCursor = resizeCursor;
	}

	public Cursor getSWResizeCursor() {
		return SWResizeCursor;
	}

	public void setSWResizeCursor(Cursor resizeCursor) {
		SWResizeCursor = resizeCursor;
	}

	public Cursor getTextCursor() {
		return textCursor;
	}

	public void setTextCursor(Cursor textCursor) {
		this.textCursor = textCursor;
	}

	public Cursor getVerticalResizeCursor() {
		return verticalResizeCursor;
	}

	public void setVerticalResizeCursor(Cursor verticalResizeCursor) {
		this.verticalResizeCursor = verticalResizeCursor;
	}

	public Cursor getForbiddenCursor() {
		return forbiddenCursor;
	}

	public void setForbiddenCursor(Cursor forbiddenCursor) {
		this.forbiddenCursor = forbiddenCursor;
	}
	
	public Cursor getWebCursor() {
		return webCursor;
	}
	
	public void setWebCursor(Cursor webCursor)
	{
		this.webCursor = webCursor;
	}
	
	public Cursor getCursor(CursorType type)
	{
		switch (type)
		{
		case DEFAULT:
			return getDefaultCursor();
		case FORBIDDEN:
			return getForbiddenCursor();
		case HAND:
			return getHandCursor();
		case MOVE:
			return getMoveCursor();
		case TEXT:
			return getTextCursor();
		case HORIZONTAL_RESIZE:
			return getHorizontalResizeCursor();
		case VERTICAL_RESIZE:
			return getVerticalResizeCursor();
		case NW_RESIZE:
			return getNWResizeCursor();
		case SW_RESIZE:
			return getSWResizeCursor();
		case WEB:
			return getWebCursor();
		}
		
		throw new IllegalArgumentException("CursorType value not recognized.");
	}
}
