/**
 * 
 */
package mmbmods.stn.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.world.block.BlockType;
import mmb.world.inventory.Inventories;
import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryReader.ExtractionLevel;
import mmb.world.item.ItemEntry;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.rotate.RotatedImageGroup;
import mmbmods.stn.STN;

/**
 * @author oskar
 *
 */
public class STNImporter extends STNCycler{
	@Nonnull private static final RotatedImageGroup rig = RotatedImageGroup.create("stn/importer.png");
	
	@Override
	public BlockType type() {
		return STN.STN_importer;
	}

	@Override
	public RotatedImageGroup getImage() {
		return rig;
	}

	@Override
	protected void runCycle(@Nullable ItemEntry item, InventoryWriter writer, InventoryReader reader) {
		if(item == null) {
			Inventories.transferFirst(reader, network().inv);
		}else if(reader.level() == ExtractionLevel.RANDOM){
			Inventories.transferStack(reader, network().inv, item, 1);
		}
	}

	@Override
	protected String title() {
		return "STN puller";
	}

	@Override
	protected STNCycler copy1() {
		return new STNImporter();
	}
}
