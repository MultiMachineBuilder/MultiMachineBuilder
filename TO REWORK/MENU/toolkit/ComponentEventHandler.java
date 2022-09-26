/**
 * 
 */
package mmb.MENU.toolkit;

import mmb.MENU.toolkit.events.UIMouseEvent;

/**
 * @author oskar
 *
 */
@Deprecated
public interface ComponentEventHandler {
	public default void drag(UIMouseEvent e) {};
	public default void press(UIMouseEvent e) {}
	public default void release(UIMouseEvent e) {};
	/**
	 * Not yet avaiable for subcomponents, that would require entered() and exited() methods
	 */
	public default void mouseMoved(UIMouseEvent e) {};
}
