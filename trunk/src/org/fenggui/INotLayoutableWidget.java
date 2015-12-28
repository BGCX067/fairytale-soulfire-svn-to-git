package org.fenggui;

/**
 * Widgets that can not compute their inner size without knowing
 * their actual size are not layoutable widgets. They need a
 * hint about their size to become layoutable again.<br/>
 * Currently only concidered by <code>ScrollContainer</code>.
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2007-12-20 15:47:28 +0100 (Do, 20 Dez 2007) $
 * @version $Revision: 426 $
 */
public interface INotLayoutableWidget {

	public void heightHint(int height);
	public void widthHint(int width);
	
}
