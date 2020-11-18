/**
 * 
 */
package mmb.MENU.toolkit.components;

import mmb.MENU.toolkit.UIComponent;

/**
 * @author oskar
 *
 */
public interface ComponentGenerator<T> {
	public UIComponent getComponent(T arg0);
}
