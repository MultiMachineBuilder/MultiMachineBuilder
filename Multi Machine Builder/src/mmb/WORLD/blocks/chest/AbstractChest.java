/**
 * 
 */
package mmb.WORLD.blocks.chest;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.DATA.json.JsonTool;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.block.BlockEntityData;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.storage.SimpleInventory;

/**
 * @author oskar
 *
 */
public abstract class AbstractChest extends BlockEntityData{
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
	@Override
	public BlockEntity clone() {
		AbstractChest copy = (AbstractChest) super.clone();
		copy.inv = new SimpleInventory(inv);
		return copy;
	}	
}
