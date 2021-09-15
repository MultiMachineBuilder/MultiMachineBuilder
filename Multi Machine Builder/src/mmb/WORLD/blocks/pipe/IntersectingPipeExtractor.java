/**
 * 
 */
package mmb.WORLD.blocks.pipe;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.inventory.storage.SingleItemInventory;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class IntersectingPipeExtractor extends Pipe {
	@SuppressWarnings("javadoc")
	public IntersectingPipeExtractor(Side sideA, Side sideB, BlockType type, RotatedImageGroup rig) {
		super(sideA, sideB, type, rig);
	}

	@Override
	public void onTick(MapProxy map) {
		super.onTick(map);
		ItemTransporter.moveItems(posX(), posY(), map, side, inv);
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
	

}
