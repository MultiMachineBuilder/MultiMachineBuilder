/**
 * 
 */
package mmb.BEANS;

import javax.annotation.Nullable;

import mmb.WORLD.gui.WorldWindow;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
@FunctionalInterface
public interface BlockActivateListener {
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window);
	public default void click(int blockX, int blockY, World map) {
		click(blockX, blockY, map, null);
	}
}

