/**
 * 
 */
package mmb.WORLD.tileworld.block;

import mmb.WORLD.tileworld.BlockDrawer;

/**
 * @author oskar
 *
 */
public class BlockEntity implements MapEntry{
	public BlockEntity(Block type, BlockEntityData bed) {
		super();
		this.type = type;
		this.bed = bed;
	}
	
	/**
	 * Creates a block entity for given block. Doesn't work for blocks without block entity support.
	 */
	public BlockEntity(Block b) {
		type = b;
		bed = b.bel.blockEntityGen.get();
	}

	public Block type;
	public BlockEntityData bed;

	@Override
	public BlockDrawer getTexture() {
		return type.getTexture();
	}

	@Override
	public String getID() {
		return type.getID();
	}

	@Override
	public boolean isBlockEntity() {
		return true;
	}

	@Override
	public BlockEntityData getBlockData() {
		return bed;
	}

	@Override
	public Block getBlock() {
		return type;
	}
	
	/**
	 * Clones only the block, leaving the block entity connected to the previous block.
	 */
	@Override
	public BlockEntity clone() {
		return new BlockEntity(type, bed);
	}
	
	/**
	 * Clones the block and the tile entity.
	 * @return
	 */
	public BlockEntity deepClone() {
		return new BlockEntity(type, bed.clone());
	}
	
	/**
	 * Survival mode clone. Coming in 0.4
	 * @return
	 */
	@Override
	@Deprecated
	public BlockEntity survivalClone() {
		return null;
	}
}
