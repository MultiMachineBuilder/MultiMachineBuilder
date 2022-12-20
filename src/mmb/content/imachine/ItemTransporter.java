/**
 * 
 */
package mmb.content.imachine;

import java.awt.image.BufferedImage;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Rotation;
import mmb.engine.rotate.Side;
import mmb.engine.texture.Textures;
import mmb.engine.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class ItemTransporter extends BlockEntityRotary {

	@NN public static final BufferedImage TEXTURE = Textures.get("machine/imover.png");
	@NN public static final RotatedImageGroup RTEXTURE = RotatedImageGroup.create(TEXTURE);
	
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
	
	@NN private SingleItemInventory inv = new SingleItemInventory();
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
			int moved = dst.insert(record.item());
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
