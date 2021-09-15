/**
 * 
 */
package mmb.WORLD.block;

import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public final class BlockEntityDemolitionEvent{
	public final int x;
	public final int y;
	public final BlockEntity block;
	public final World world;
	public BlockEntityDemolitionEvent(int x, int y, BlockEntity block, World world) {
		super();
		this.x = x;
		this.y = y;
		this.block = block;
		this.world = world;
	}
}
