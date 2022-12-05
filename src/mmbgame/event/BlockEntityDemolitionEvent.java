/**
 * 
 */
package mmbgame.event;

import mmbeng.block.BlockEntity;
import mmbeng.worlds.world.World;

/**
 * @author oskar
 *
 */
public final class BlockEntityDemolitionEvent{
	/** The X coordinate of the block entity */
	public final int x;
	/** The Y coordinate of the block entity */
	public final int y;
	/** The block entity, which was demolished */
	public final BlockEntity block;
	/** The world, to which the block entity belonged */
	public final World world;
	/**
	 * Constructs a block entity demolition event
	 * @param x X coordinate of the block entity
	 * @param y Y coordinate of the block entity
	 * @param block the block entity
	 * @param world world
	 */
	public BlockEntityDemolitionEvent(int x, int y, BlockEntity block, World world){
		super();
		this.x = x;
		this.y = y;
		this.block = block;
		this.world = world;
	}
}
