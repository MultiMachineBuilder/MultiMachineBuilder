/**
 * 
 */
package mmbeng.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.beans.Positioned;
import mmbeng.worlds.world.World;

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
	@Nonnull public default World owner() {
		World owner = nowner();
		if(owner == null) throw new IllegalStateException("Not placed in map");
		return owner;
	}
	/**
	 * Gets the current holder of the block
	 * @return the current owner or null
	 */
	@Nullable public World nowner();
}
