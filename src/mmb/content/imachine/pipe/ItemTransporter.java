/**
 * 
 */
package mmb.content.imachine.pipe;

import java.awt.image.BufferedImage;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.inv.Inventories;
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

	public static final BufferedImage TEXTURE = Textures.get("machine/imover.png");
	public static final RotatedImageGroup RTEXTURE = RotatedImageGroup.create(TEXTURE);
	
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
	
	private SingleItemInventory inv = new SingleItemInventory();
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
		Inventories.transferAll(src, buffer);
		//Stage 2: Buffer -> output
		Inventories.transferAll(buffer, dst);
	}

	@Override
	public BlockEntry blockCopy() {
		ItemTransporter copy = new ItemTransporter();
		copy.inv.setContents(inv.getContents());
		return copy;
	}
}
