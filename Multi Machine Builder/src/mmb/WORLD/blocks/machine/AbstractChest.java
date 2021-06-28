/**
 * 
 */
package mmb.WORLD.blocks.machine;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.DATA.json.JsonTool;
import mmb.WORLD.Side;
import mmb.WORLD.block.SkeletalBlockEntityData;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.storage.SimpleInventory;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 *
 */
public abstract class AbstractChest extends SkeletalBlockEntityData{
	protected AbstractChest(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}
	@Nonnull protected SimpleInventory inv = new SimpleInventory();
	@Override
	public final void load(JsonNode data) {
		inv.load(JsonTool.requestArray("inventory", (ObjectNode) data));
		load1(data);
	}
	@Override
	protected final void save0(ObjectNode node) {
		node.set("inventory", inv.save());
		save1(node);
	}
	
	protected void load1(JsonNode node) {}
	protected void save1(JsonNode node) {}
	@Override
	public Inventory getInventory(Side s) {
		return inv;
	}

	
}
