/**
 * 
 */
package org.fenggui.event;

import org.fenggui.IWidget;

/**
 * @author Marc Menghin
 *
 */
public class WidgetListChangedEvent extends WidgetEvent {

	private IWidget addedWidget = null;
	
	public WidgetListChangedEvent(IWidget source, IWidget addedWidget)
	{
		super(source);
		this.addedWidget = addedWidget;
	}

	/**
   * @return the added widget
   */
  public IWidget getAddedWidget() {
  	return addedWidget;
  }
}
