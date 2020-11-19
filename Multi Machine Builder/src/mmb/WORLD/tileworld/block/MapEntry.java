/**
 * 
 */
package mmb.WORLD.tileworld.block;

import mmb.WORLD.inventory.Item;
import mmb.WORLD.tileworld.BlockDrawer;

/**
 * @author oskar
 *
 */
public interface MapEntry extends Cloneable, Item {
	BlockDrawer getTexture();
	default boolean isBlockEntity() {return false;}
	@Deprecated default Block getBlock() {
		return getType();
	}
	
	/**
	 * 
	 * @return
	 */
    BlockEntityData getBlockData();
	
	/**
	 * Clones only the block, leaving the block entity connected to the previous block.
	 */
    MapEntry clone();
	
	/**
	 * Clones the block and the tile entity.
	 * When used with Block, it is equivalent to clone();
	 */
    MapEntry deepClone();
	
	@Override
	default String getName() {
		return getType().getName();
	}
	@Override
	default String getID() {
		return getType().getID();
	}
	/**
	 * Survival mode clone. Coming in 0.4
	 * @return
	 */
	@Deprecated
    MapEntry survivalClone();
	
	/**
	 * Creates a map entry for the following block. Guarantees that block is immediately ready for use.
	 * @param b
	 * @return
	 */
	static MapEntry createNew(Block b) {
		if(b.bel == null || b.bel.blockEntityGen == null) return b;
		return new BlockEntity(b);
	}
	@Override
	public Block getType();
	
}
