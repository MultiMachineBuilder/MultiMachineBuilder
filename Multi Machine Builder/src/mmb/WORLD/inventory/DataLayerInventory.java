/**
 * 
 */
package mmb.WORLD.inventory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import mmb.DATA.save.DataLayer;

/**
 * @author oskar
 *
 */
public class DataLayerInventory implements DataLayer {
	public Inventory inv;
	
	public static DataLayerInventory load(JsonObject jo) {
		return new DataLayerInventory(ItemJSONConverter.entryToInventory(jo));
	}
	public static DataLayerInventory empty() {
		return new DataLayerInventory();
	}
	public DataLayerInventory(Inventory inv) {
		super();
		this.inv = inv;
	}
	/**
	 * 
	 */
	private DataLayerInventory() {
		inv = new InventoryUnstackable();
		inv.capacity = 2;
		inv.setLimited(-1);
	}
	@Override
	public JsonObject save() {
		return ItemJSONConverter.inventoryToEntry(inv);
	}

	@Override
	public String name() {
		return "PlayerInv";
	}

}
