/**
 * 
 */
package mmb.MENU.toolkit.components;

import mmb.MENU.toolkit.UIComponent;

/**
 * @author oskar
 *
 */
public interface ComponentGenerator2<T, U> {
	public UIComponent getComponent(T arg0, U arg1);
}
