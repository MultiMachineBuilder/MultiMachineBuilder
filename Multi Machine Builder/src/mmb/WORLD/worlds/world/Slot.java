/**
 * 
 */
package mmb.WORLD.worlds.world;

import mmb.WORLD.block.BlockEntry;

/**
 * @author oskar
 * <br> A reserved location in a block map.
 * This is used by:
 * <ul>
 * 	<li>NYI unanchoring</li>
 * 	<li>NYI anchoring</li>
 * 	<li>NYI machine placement</li>
 * </ul>
 * Other threads may not reserve the slot or modify world under this slot's position until this slot is released
 */
public interface Slot extends AutoCloseable{
	@Override
	void close();
	/**
	 * @return the block in this slot
	 */
	public BlockEntry get();
	/**
	 * Sets the block in this slot. This will not fail unless slot is released.
	 * @param block block to be placed
	 * @return the new block
	 * @throws IllegalStateException if the slot is closed
	 */
	public BlockEntry set(BlockEntry block);
	/**
	 * @return the owning block map
	 */
	public World getMap();
}
