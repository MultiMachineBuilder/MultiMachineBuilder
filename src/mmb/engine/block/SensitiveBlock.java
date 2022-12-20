/**
 * 
 */
package mmb.engine.block;

import mmb.NN;
import mmb.Nil;
import mmb.engine.worlds.world.World;
import mmbbase.beans.Positioned;

/**
 * A block which is instantiated prior to placement, in contrast to placement directly as a simple block
 * @author oskar
 */
public interface SensitiveBlock extends BlockEntry, Positioned {
	/**
	 * Gets the current holder of the block
	 * @throws IllegalArgumentException when there is no onwer
	 * @return the current owner
	 */
	@NN public default World owner() {
		World owner = nowner();
		if(owner == null) throw new IllegalStateException("Not placed in map");
		return owner;
	}
	/**
	 * Gets the current holder of the block
	 * @return the current owner or null
	 */
	@Nil public World nowner();
}
