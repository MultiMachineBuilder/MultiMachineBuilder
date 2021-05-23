/**
 * 
 */
package mmb.BEANS;

import java.awt.Component;

import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *  Describes a class that caches open windows and can close them
 */
public interface OpenWindow {
	public Component openWindow(WorldWindow window);
	public void closeWindow();
}
