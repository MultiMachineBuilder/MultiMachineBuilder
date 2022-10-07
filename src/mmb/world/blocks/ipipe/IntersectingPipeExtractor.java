/**
 * 
 */
package mmb.world.blocks.ipipe;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
import mmb.world.inventory.storage.SingleItemInventory;
import mmb.world.items.ItemEntry;
import mmb.world.rotate.ChirotatedImageGroup;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class IntersectingPipeExtractor extends Pipe {
	@SuppressWarnings("javadoc")
	public IntersectingPipeExtractor(Side sideA, Side sideB, BlockType type, ChirotatedImageGroup rig) {
		super(sideA, sideB, type, rig);
	}

	@Override
	public void onTick(MapProxy map) {
		super.onTick(map);
		ItemTransporter.moveItems(posX(), posY(), map, getRotation(), inv);
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

	@Override
	public BlockEntry blockCopy() {
		IntersectingPipeExtractor result = new IntersectingPipeExtractor(sideA, sideB, type(), getImage());
		System.arraycopy(items, 0, result.items, 0, 2);
		result.inv.setContents(inv.getContents());
		return result;
	}
	

}
