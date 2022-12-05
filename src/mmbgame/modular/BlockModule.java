/**
 * 
 */
package mmbgame.modular;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmbeng.inv.Inventory;
import mmbeng.inv.io.InventoryReader;
import mmbeng.inv.io.InventoryWriter;
import mmbeng.item.RotableItemEntry;
import mmbeng.rotate.Side;
import mmbgame.electric.Electricity;
import mmbgame.modular.gui.ModuleConfigHandler;
import mmbgame.modular.part.RotablePartEntry;

/**
 * An interface which provides addition/removal handling for the player.
 * @author oskar
 * @param <Tmodule> type of this module
 */
public interface BlockModule<Tmodule extends BlockModule<Tmodule>> extends RotablePartEntry, BlockModuleOrCore<
Tmodule, ModularBlock<?, Tmodule, ?, ?>, BlockModule.BlockModuleParams<Tmodule>>{
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
	public default void run(ModularBlock<?, Tmodule, ?, ?> block, Side storedSide, Side realSide) {
		//unused
	}
	/**
	 * Called when a module is removed from a block
	 * @param info the information about breakage
	 */
	@Override
	public default void onBroken(BlockModuleParams<Tmodule> info) {
		//empty
	}
	/**
	 * Called when a module is added to a block
	 * @param info the information about creation
	 */
	@Override
	public default void onAdded(BlockModuleParams<Tmodule> info) {
		//empty
	}
	
	public static class BlockModuleParams<Tmodule extends BlockModule<Tmodule>>{
		@Nonnull public final ModularBlock<?, Tmodule, ?, ?> blk;
		@Nonnull public final Side storedSide;
		@Nonnull public final Side realSide;
		public BlockModuleParams(ModularBlock<?, Tmodule, ?, ?> blk, Side storedSide, Side realSide) {
			this.blk = blk;
			this.storedSide = storedSide;
			this.realSide = realSide;
		}
	}
	
	
}
