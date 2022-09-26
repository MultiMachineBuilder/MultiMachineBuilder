/**
 * 
 */
package mmb.WORLD.event;

import javax.annotation.Nullable;

import mmb.GameObject;
import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.worlds.world.World;

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
	/** The player which demolished the block */
	public final GameObject player;
	/**
	 * Constructs a block entity demolition event
	 * @param x X coordinate of the block entity
	 * @param y Y coordinate of the block entity
	 * @param block the block entity
	 * @param world world
	 * @param player player
	 */
	public BlockEntityDemolitionEvent(int x, int y, BlockEntity block, World world, @Nullable GameObject player){
		super();
		this.x = x;
		this.y = y;
		this.block = block;
		this.world = world;
		this.player = player;
	}
}
