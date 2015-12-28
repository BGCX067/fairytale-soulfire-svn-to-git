/**
 * 
 */
package org.fenggui.event;

import org.fenggui.IWidget;
import org.fenggui.util.Point;

/**
 * @author Marc Menghin
 *
 */
public class PositionChangedEvent extends WidgetEvent {

	private Point oldPosition;
	private Point newPosition;
	
	/**
	 * @param source
	 */
	public PositionChangedEvent(IWidget source, Point oldPosition, Point newPosition) {
		super(source);
		this.oldPosition = oldPosition;
		this.newPosition = newPosition;
	}

	/**
   * @return the oldPosition
   */
  public Point getOldPosition() {
  	return oldPosition;
  }

	/**
   * @return the newPosition
   */
  public Point getNewPosition() {
  	return newPosition;
  }

	
}
