/**
 * 
 */
package mmb.content.modular.universal;

import mmb.content.electric.Electricity;
import mmb.content.modular.ModularBlock;
import mmb.content.modular.chest.BlockModuleUniversal;
import mmb.content.modular.gui.ModuleConfigHandler;
import mmb.content.modular.part.RotablePart;
import mmb.engine.chance.Chance;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.NoSuchInventory;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.RotableItem;
import mmb.engine.rotate.Side;

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
