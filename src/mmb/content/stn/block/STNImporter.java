/**
 * 
 */
package mmb.content.stn.block;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.content.stn.STN;
import mmb.engine.block.BlockType;
import mmb.engine.inv.Inventories;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.io.InventoryReader.ExtractionLevel;
import mmb.engine.item.ItemEntry;
import mmb.engine.rotate.RotatedImageGroup;

/**
 * Imports items from the outside to the STN.
 * Items can be restricted, and the quantity of items transferred may be limited.
 * This block accepts speed upgrades
 * @author oskar
 */
public class STNImporter extends STNCycler{
	@NN private static final RotatedImageGroup rig = RotatedImageGroup.create("stn/importer.png");
	
	@Override
	public BlockType type() {
		return STN.STN_importer;
	}

	@Override
	public RotatedImageGroup getImage() {
		return rig;
	}

	@Override
	protected void runCycle(@Nil ItemEntry item, InventoryWriter writer, InventoryReader reader) {
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
