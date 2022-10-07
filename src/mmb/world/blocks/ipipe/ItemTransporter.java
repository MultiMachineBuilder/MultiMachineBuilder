/**
 * 
 */
package mmb.world.blocks.ipipe;

import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.data.contents.Textures;
import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
import mmb.world.block.SkeletalBlockEntityRotary;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.ItemRecord;
import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.inventory.storage.SingleItemInventory;
import mmb.world.items.ItemEntry;
import mmb.world.rotate.RotatedImageGroup;
import mmb.world.rotate.Rotation;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class ItemTransporter extends SkeletalBlockEntityRotary {

	@Nonnull public static final BufferedImage TEXTURE = Textures.get("machine/imover.png");
	@Nonnull public static final RotatedImageGroup RTEXTURE = RotatedImageGroup.create(TEXTURE);
	
	@Override
	public BlockType type() {
		return ContentsBlocks.IMOVER;
	}

	@Override
	public RotatedImageGroup getImage() {
		return RTEXTURE;
	}

	@Override
	public void onTick(MapProxy map) {
		moveItems(posX(), posY(), map, getRotation(), inv);
	}
	
	@Nonnull private SingleItemInventory inv = new SingleItemInventory();
	@Override
	protected void save1(ObjectNode node) {
		node.set("tmp", ItemEntry.saveItem(inv.getContents()));
	}

	@Override
	protected void load1(ObjectNode node) {
		inv.setContents(ItemEntry.loadFromJson(node.get("tmp")));
	}

	public static void moveItems(int x, int y, MapProxy map, Rotation side, Inventory buffer) {
		Side d = side.D();
		Side u = side.U();
		
		InventoryReader src = map.getAtSide(x, y, d).getOutput(u);
		InventoryWriter dst = map.getAtSide(x, y, u).getInput(d);
		
		//Stage 1: Input -> buffer
		while(src.hasCurrent() || src.hasNext()) {
			if(!buffer.isEmpty()) break; //If there are items, break out
			//Try moving the items to a buffer
			if(src.hasCurrent()) {
				ItemEntry toextract = src.currentItem();
				if(toextract.volume() > buffer.remainVolume()) {
					src.next();
					continue;
				}
				int moved = src.extract(1);
				if(moved == 1) {
					buffer.insert(toextract, moved);
					break;
				}
				
			}else if(src.hasNext()) {
				src.next();
			}
		}
		//Stage 2: Buffer -> output
		for(ItemRecord record: buffer) {
			int moved = dst.write(record.item());
			if(moved == 1) {
				record.extract(1);
				return;
			}
		}
		return;
	}

	@Override
	public BlockEntry blockCopy() {
		ItemTransporter copy = new ItemTransporter();
		copy.inv.setContents(inv.getContents());
		return copy;
	}
}
