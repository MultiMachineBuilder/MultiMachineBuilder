/**
 * 
 */
package mmb.WORLD.tileworld.block;

import mmb.WORLD.tileworld.BlockDrawer;

/**
 * @author oskar
 *
 */
public interface MapEntry extends Cloneable {
	BlockDrawer getTexture();
	String getID();
	default boolean isBlockEntity() {return false;}
	Block getBlock();
	
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
	
}
