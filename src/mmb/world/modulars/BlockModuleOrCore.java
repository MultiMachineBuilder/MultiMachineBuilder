/**
 * 
 */
package mmb.world.modulars;

import javax.annotation.Nonnull;

import mmb.world.chance.Chance;
import mmb.world.crafting.RecipeOutput;
import mmb.world.items.ItemEntry;

/**
 * Common representation of block modules and cores
 * @author oskar
 * @param <Tmc> the type of this block module or core
 * @param <Tblock> type of the block
 * @param <Tparams> type of broken/added params
 */
public interface BlockModuleOrCore<Tmc extends BlockModuleOrCore<Tmc, Tblock, Tparams>, Tblock extends ModularBlock<?, ?, ?, ?>, Tparams> extends ItemEntry{
	/** @return items dropped when module is removed */
	@Nonnull public Chance dropItems();
	/** @return items returned to the player */
	@Nonnull public default RecipeOutput returnToPlayer() {
		return this;
	}
	/**
	 * Called when a a module or a core is removed from a block
	 * @param blk the block running this module
	 */
	public default void onBroken(Tparams blk) {
		//empty
	}
	/**
	 * Called when a module or a core is added to a block
	 * @param blk the block running this module
	 */
	public default void onAdded(Tparams blk) {
		//empty
	}
}
