/**
 * 
 */
package mmbgame.modular.universal;

import mmbeng.chance.Chance;
import mmbeng.inv.Inventory;
import mmbeng.inv.NoSuchInventory;
import mmbeng.inv.io.InventoryReader;
import mmbeng.inv.io.InventoryWriter;
import mmbeng.item.RotableItem;
import mmbeng.rotate.Side;
import mmbgame.electric.Electricity;
import mmbgame.modular.ModularBlock;
import mmbgame.modular.chest.BlockModuleUniversal;
import mmbgame.modular.gui.ModuleConfigHandler;
import mmbgame.modular.part.RotablePart;

/**
 * @author oskar
 *
 */
public class Plug extends RotablePart implements BlockModuleUniversal {
	@Override
	public Chance dropItems() {
		return Chance.NONE;
	}

	@Override
	public Inventory provideInventory(ModularBlock<?, BlockModuleUniversal, ?, ?> blk, Side s) {
		return NoSuchInventory.INSTANCE;
	}

	@Override
	public InventoryWriter provideInput(ModularBlock<?, BlockModuleUniversal, ?, ?> blk, Side s) {
		return InventoryWriter.NONE;
	}

	@Override
	public InventoryReader provideOutput(ModularBlock<?, BlockModuleUniversal, ?, ?> blk, Side s) {
		return InventoryReader.NONE;
	}

	@Override
	public boolean provideSignal(ModularBlock<?, BlockModuleUniversal, ?, ?> blk, Side s) {
		return false;
	}

	@Override
	public Electricity provideElectricity(ModularBlock<?, BlockModuleUniversal, ?, ?> blk, Side s) {
		return null;
	}

	
	@Override
	public ModuleConfigHandler<BlockModuleUniversal, ?> mch() {
		return null;
	}
}
