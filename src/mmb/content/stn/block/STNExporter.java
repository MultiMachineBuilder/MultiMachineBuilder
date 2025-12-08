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
import mmb.engine.item.ItemEntry;
import mmb.engine.rotate.RotatedImageGroup;

/**
 * Export items from STN to the outside. Items must be specified.
 * Items can be restricted, and the quantity of items transferred may be limited.
 * This block accepts speed upgrades
 * @author oskar
 */
public class STNExporter extends STNCycler{
	@NN private static final RotatedImageGroup rig = RotatedImageGroup.create("stn/exporter.png");
	
	@Override
	public BlockType type() {
		return STN.STN_exporter;
	}

	@Override
	public RotatedImageGroup getImage() {
		return rig;
	}

	@Override
	protected void runCycle(@Nil ItemEntry item, InventoryWriter writer, InventoryReader reader) {
		if(item != null) Inventories.transferStack(network().inv, writer, item, 1);
	}

	@Override
	protected String title() {
		return "STN pusher";
	}

	@Override
	protected STNCycler copy1() {
		return new STNExporter();
	}
}
