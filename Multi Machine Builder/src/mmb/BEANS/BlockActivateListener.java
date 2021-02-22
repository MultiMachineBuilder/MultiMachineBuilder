/**
 * 
 */
package mmb.BEANS;

import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
@FunctionalInterface
public interface BlockActivateListener {
	public void run(int blockX, int blockY, BlockMap map);
}
