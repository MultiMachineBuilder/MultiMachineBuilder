/**
 * 
 */
package mmb.WORLD.blocks.machine;

import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.Placer;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class PlaceIncomingItems extends SkeletalBlockEntityRotary {
	private static final Debugger debug = new Debugger("BLOCK PLACE MACHINE");
	private InventoryWriter placer = (ent, amount) -> {
		if(amount < 1) return 0;
		BlockEntry blk = getAtSide(side.U());
		if(blk.type().isSurface()) return 0;
		debug.printl("Item given");
		if(ent instanceof Placer) {
			debug.printl("Placer given, placing");
			Placer placer = (Placer)ent;
			BlockEntry newBlock = placer.place(side.U().offset(posX(), posY()), owner());
			if(newBlock != null) return 1;
		}
		return 0;
	};

	@Override
	public BlockType type() {
		return ContentsBlocks.PLACEITEMS;
	}

	public static final RotatedImageGroup TEXTURE = RotatedImageGroup.create("machine/block place interface.png");
	@Override
	public RotatedImageGroup getImage() {
		return TEXTURE;
	}
	
	@Override
	public InventoryWriter getInput(Side s) {
		if(s == side.D()) return placer;
		return InventoryWriter.NONE;
	}

}
