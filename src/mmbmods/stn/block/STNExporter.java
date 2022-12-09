/**
 * 
 */
package mmbmods.stn.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.engine.block.BlockType;
import mmb.engine.inv.Inventories;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.ItemEntry;
import mmb.engine.rotate.RotatedImageGroup;
import mmbmods.stn.STN;

/**
 * @author oskar
 *
 */
public class STNExporter extends STNCycler{
	@Nonnull private static final RotatedImageGroup rig = RotatedImageGroup.create("stn/exporter.png");
	
	@Override
	public BlockType type() {
		return STN.STN_exporter;
	}

	@Override
	public RotatedImageGroup getImage() {
		return rig;
	}

	@Override
	protected void runCycle(@Nullable ItemEntry item, InventoryWriter writer, InventoryReader reader) {
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
