/**
 * 
 */
package mmb.content.imachine;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.content.ContentsBlocks;
import mmb.engine.MMBUtils;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.block.Placer;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.InvUtils;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Side;

/**
 * @author oskar
 *
 */
public class PlaceIncomingItems extends BlockEntityRotary {
	private static final Debugger debug = new Debugger("BLOCK PLACE MACHINE");
	@NN private InventoryWriter placer = new InventoryWriter() {

		@Override
		public int insert(ItemEntry ent, int amount) {
			if(amount < 1) return 0;
			BlockEntry blk = getAtSide(getRotation().U());
			if(blk.isSurface()) return 0;
			debug.printl("Item given");
			if(ent instanceof Placer) {
				debug.printl("Placer given, placing");
				Placer iplacer = (Placer)ent;
				BlockEntry newBlock = iplacer.place(getRotation().U().offset(posX(), posY()), owner());
				if(newBlock != null) return 1;
			}
			return 1;
		}

		@Override
		public int bulkInsert(RecipeOutput block, int amount) {
			ItemEntry item = InvUtils.onlyItem(block);
			if(item == null) return 0;
			return insert(item, amount);
		}

		@Override
		public int toInsertBulk(RecipeOutput block, int amount) {
			return toInsert(InvUtils.onlyItem(block), amount);
		}

		@Override
		public int toInsert(@Nil ItemEntry item, int amount) {
			if(amount == 0) return 0;
			return MMBUtils.bool2int(item instanceof Placer);
		}
		
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
		if(s == getRotation().D()) return placer;
		return InventoryWriter.NONE;
	}

	@Override
	public BlockEntry blockCopy() {
		PlaceIncomingItems copy = new PlaceIncomingItems();
		copy.setRotation(getRotation());
		return copy;
	}

}
