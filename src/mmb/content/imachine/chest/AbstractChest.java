/**
 * 
 */
package mmb.content.imachine.chest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.engine.block.BlockEntityData;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.json.JsonTool;
import mmb.engine.rotate.Side;

/**
 * A base implementation for all chests
 * @author oskar
 */
public abstract class AbstractChest extends BlockEntityData implements ArbitraryChest{
	//Contents and block methods
	@NN protected SimpleInventory inv = new SimpleInventory();
	@Override
	public Inventory getInventory(Side s) {
		return inv;
	}
	@Override
	public Inventory inv() {
		return inv;
	}
	
	//Serialization
	@Override
	public final void load(@Nil JsonNode data) {
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
	
}
