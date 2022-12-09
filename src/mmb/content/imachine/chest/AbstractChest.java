/**
 * 
 */
package mmb.content.imachine.chest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.engine.block.BlockEntity;
import mmb.engine.block.BlockEntityData;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.json.JsonTool;
import mmb.engine.rotate.Side;

/**
 * @author oskar
 *
 */
public abstract class AbstractChest extends BlockEntityData implements ArbitraryChest{
	@Nonnull protected SimpleInventory inv = new SimpleInventory();
	@Override
	public final void load(@Nullable JsonNode data) {
		if(data == null) return;
		inv.load(JsonTool.requestArray("inventory", (ObjectNode) data));
		load1((ObjectNode)data);
	}
	@Override
	protected final void save0(ObjectNode node) {
		node.set("inventory", inv.save());
		save1(node);
	}
	
	/**
	 * Additional function used to save additional data
	 * @param node node, to which data can be saved
	 */
	protected void save1(ObjectNode node) {
		//optional
	}
	/**
	 * Additional function used to save additional data
	 * @param node node, to which data can be loaded
	 */
	protected void load1(ObjectNode node) {
		//optional
	}
	@Override
	public Inventory getInventory(Side s) {
		return inv;
	}
	@Override
	public Inventory inv() {
		return inv;
	}
}
