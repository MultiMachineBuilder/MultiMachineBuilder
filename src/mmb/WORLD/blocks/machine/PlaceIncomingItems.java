/**
 * 
 */
package mmb.WORLD.blocks.machine;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.MENU.world.Placer;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class PlaceIncomingItems extends SkeletalBlockEntityRotary {
	private static final Debugger debug = new Debugger("BLOCK PLACE MACHINE");
	@Nonnull private InventoryWriter placer = new InventoryWriter() {

		@Override
		public int write(ItemEntry ent, int amount) {
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
			for(Entry<ItemEntry> entry: block.getContents().object2IntEntrySet()) {
				if(amount < 1) return 0;
				if(entry.getIntValue() > 1) return 0;
				if(entry.getIntValue() == 0) return amount;
				BlockEntry blk = getAtSide(getRotation().U());
				if(!blk.isSurface()) return 0;
				debug.printl("Item given");
				ItemEntry ent = entry.getKey();
				if(ent instanceof Placer) {
					debug.printl("Placer given, placing");
					Placer iplacer = (Placer)ent;
					BlockEntry newBlock = iplacer.place(getRotation().U().offset(posX(), posY()), owner());
					if(newBlock != null) return 1;
				}
			}
			return 0;
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
