/**
 * 
 */
package mmb.WORLD.tileworld.block;

import java.awt.Point;

import mmb.WORLD.tileworld.world.BlockProxy;

/**
 * @author oskar
 *
 */
public class BlockUpdateEvent {
	public Point block;
	public BlockProxy world;
	
	public Block getBlock() {
		return world.getBlock(block);
	}
	public MapEntry getEntry() {
		return world.get(block);
	}
	public BlockEntityData getBED() {
		return getEntry().getBlockData();
	}
}
