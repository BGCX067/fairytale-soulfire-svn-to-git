package org.fenggui.event;

import org.fenggui.IWidget;

public class WindowResizedEvent extends WidgetEvent {

	private int oldWidth;
	private int oldHeight;

	public WindowResizedEvent(IWidget source, int oldWidth, int oldHeight) {
		super(source);
		this.oldHeight = oldHeight;
		this.oldWidth = oldWidth;
	}
	
	/**
	 * Get the previous width of the window. This is not guaranteed to be different from the current width.
	 * @return
	 */
	public int getOldWidth() {
		return oldWidth;
	}
	
	/**
	 * Get the previous height of the window. This is not guaranteed to be different from the current height.
	 * @return
	 */
	public int getOldHeight() {
		return oldHeight;
	}

}
