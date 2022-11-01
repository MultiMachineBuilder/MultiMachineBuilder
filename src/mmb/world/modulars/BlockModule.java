/**
 * 
 */
package mmb.world.modulars;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.world.chance.Chance;
import mmb.world.electric.Electricity;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.item.RotableItemEntry;
import mmb.world.rotate.Side;

/**
 * An interface which provides addition/removal handling for the player.
 * @author oskar
 * @param <Tmodule> type of this module
 */
public interface BlockModule<Tmodule extends BlockModule<Tmodule>> extends RotableItemEntry{
	//Provide the access points from a block
	public default @Nonnull  Inventory provideInventory(ModularBlock<?, Tmodule, ?, ?> blk, Side s) {
		return blk.i_inv(s);
	}
	public default @Nonnull  InventoryWriter provideInput(ModularBlock<?, Tmodule, ?, ?> blk, Side s) {
		return blk.i_in(s);
	}
	public default @Nonnull  InventoryReader provideOutput(ModularBlock<?, Tmodule, ?, ?> blk, Side s) {
		return blk.i_out(s);
	}
	public default           boolean provideSignal(ModularBlock<?, Tmodule, ?, ?> blk, Side s) {
		return blk.i_signal(s);
	}
	public default @Nullable Electricity provideElectricity(ModularBlock<?, Tmodule, ?, ?> blk, Side s) {
		return blk.i_elec(s);
	}
	
	/**
	 * Provides signal for the block
	 * @param signal input signal
	 * @return a signal as seen by the block
	 */
	public default boolean decorateExternalSignal(boolean signal){
		return signal;
	}
	
	/**
	 * Runs the module
	 * @param block the block running this module
	 * @param storedSide side from a perspective of the block
	 * @param realSide side in the world
	 */
	public default void runModule(ModularBlock<?, Tmodule, ?, ?> block, Side storedSide, Side realSide) {
		//unused
	}
	/**
	 * Called when a module is removed from a block
	 * @param blk the block running this module
	 * @param storedSide side from a perspective of the block
	 * @param realSide side in the world
	 */
	public default void onBroken(ModularBlock<?, Tmodule, ?, ?> blk, Side storedSide, Side realSide) {
		//empty
	}
	/**
	 * Called when a module is added to a block
	 * @param blk the block running this module
	 * @param storedSide side from a perspective of the block
	 * @param realSide side in the world
	 */
	public default void onAdded(ModularBlock<?, Tmodule, ?, ?> blk, Side storedSide, Side realSide) {
		//empty
	}
	/** @return items dropped when module is removed */
	public Chance dropItems();
	
	//Config UI
}
