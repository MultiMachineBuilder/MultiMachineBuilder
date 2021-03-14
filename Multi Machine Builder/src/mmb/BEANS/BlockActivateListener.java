/**
 * 
 */
package mmb.BEANS;

import javax.annotation.Nullable;

import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
@FunctionalInterface
public interface BlockActivateListener {
	public void run(int blockX, int blockY, World map);
}

