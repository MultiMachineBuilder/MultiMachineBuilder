/**
 * 
 */
package mmb.world.modulars.universal;

import mmb.world.chance.Chance;
import mmb.world.electric.Electricity;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.NoSuchInventory;
import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.item.RotableItem;
import mmb.world.modulars.ModularBlock;
import mmb.world.modulars.chest.BlockModuleUniversal;
import mmb.world.modulars.gui.ModuleConfigHandler;
import mmb.world.part.RotablePart;
import mmb.world.rotate.Side;

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
