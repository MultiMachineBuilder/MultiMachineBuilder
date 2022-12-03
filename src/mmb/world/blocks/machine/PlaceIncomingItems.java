/**
 * 
 */
package mmb.world.blocks.machine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.MMBUtils;
import mmb.debug.Debugger;
import mmb.menu.world.Placer;
import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.blocks.SkeletalBlockEntityRotary;
import mmb.world.crafting.RecipeOutput;
import mmb.world.inventory.InvUtils;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.item.ItemEntry;
import mmb.world.rotate.RotatedImageGroup;
import mmb.world.rotate.Side;

/**
 * @author oskar
 *
 */
public class PlaceIncomingItems extends SkeletalBlockEntityRotary {
	private static final Debugger debug = new Debugger("BLOCK PLACE MACHINE");
	@Nonnull private InventoryWriter placer = new InventoryWriter() {

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
			return 0;
		}

		@Override
		public int bulkInsert(RecipeOutput block, int amount) {
			if(block.items().size() > 1) return 0;
			if(block.items().isEmpty()) return amount;
			ItemEntry ent = InvUtils.onlyItem(block);
			BlockEntry blk = getAtSide(getRotation().U());
			if(!blk.isSurface()) return 0;
			debug.printl("Item given");
			if(ent instanceof Placer) {
				debug.printl("Placer given, placing");
				Placer iplacer = (Placer)ent;
				BlockEntry newBlock = iplacer.place(getRotation().U().offset(posX(), posY()), owner());
				if(newBlock != null) return 1;
			}
			return 0;
		}

		@Override
		public int toInsertBulk(RecipeOutput block, int amount) {
			return toInsert(InvUtils.onlyItem(block), amount);
		}

		@Override
		public int toInsert(@Nullable ItemEntry item, int amount) {
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
