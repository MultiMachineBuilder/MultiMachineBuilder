/**
 * 
 */
package mmb.MENU.toolkit.menus;

import mmb.MENU.toolkit.UIComponent;
import mmb.MENU.toolkit.components.ComponentSource;

/**
 * @author oskar
 *
 */
public interface MenuSource extends ComponentSource{
	public String getName();
	public Runnable getLoadHandler();
	default public void handleLoad() {
		getLoadHandler().run();
	}
	public Runnable getExitHandler();
	default public void handleExit() {
		getExitHandler().run();
	}
	public MenuSource getWaitingScreen();
}
