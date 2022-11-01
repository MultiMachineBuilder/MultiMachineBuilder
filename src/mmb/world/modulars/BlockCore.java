/**
 * 
 */
package mmb.world.modulars;

import mmb.world.chance.Chance;
import mmb.world.items.ItemEntry;
import mmb.world.rotate.Side;

/**
 * A block core
 * @author oskar
 * @param <Tcore> type of this core
 */
public interface BlockCore<Tcore extends BlockCore<Tcore>> extends ItemEntry {
	/**
	 * Runs the core
	 * @param block the block running this module
	 */
	public default void runCore(ModularBlock<?, ?, Tcore, ?> block) {
		//unused
	}
	/**
	 * Called when a core is removed from a block
	 * @param blk the block running this module
	 */
	public default void onBroken(ModularBlock<?, ?, Tcore, ?> blk) {
		//empty
	}
	/**
	 * Called when a core is added to a block
	 * @param blk the block running this module
	 */
	public default void onAdded(ModularBlock<?, ?, Tcore, ?> blk) {
		//empty
	}
	/** @return items returned to the player */
	public default ItemEntry returnToPlayer() {
		return this;
	}
	
	/** @return items dropped when module is removed */
	public Chance dropItems();
}
