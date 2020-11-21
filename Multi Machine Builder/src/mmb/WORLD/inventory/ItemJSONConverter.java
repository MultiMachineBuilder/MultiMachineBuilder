/**
 * 
 */
package mmb.WORLD.inventory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import mmb.WORLD.inventory.items.Items;

/**
 * @author oskar
 *
 */
public class ItemJSONConverter {
	/**
	 * Encodes an item into JSON entry
	 * @param itm
	 * @return
	 */
	public static JsonElement itemToEntry(Item itm) {
		if(itm instanceof ItemType) {
			return new JsonPrimitive(itm.getID());
		}
		return null;
	}
	
	public static Item entryToItem(JsonElement je) {
		if(je.isJsonObject()) {
			//TODO item entities
			return null;
		}else if(je.isJsonPrimitive()) {
			//simple items
			return Items.items.get(je.getAsString());
		}else {
			//TODO future types
			return null;
		}
	}
	
	public static InventoryUnstackable entryToInventory(JsonObject jo) {
		InventoryUnstackable i = new InventoryUnstackable();
		int slots = -1;
		if(jo.has("maxSlots")) slots = jo.get("maxSlots").getAsInt();
		double volume = 2;
		if(jo.has("maxVolume")) volume = jo.get("maxVolume").getAsDouble();
		Item[] items = new Item[0];
		if(jo.has("contents")) {
			JsonArray ja = jo.get("contents").getAsJsonArray();
			items = new Item[ja.size()];
			for(int j = 0; j < ja.size(); j++) {
				items[j] = ItemJSONConverter.entryToItem(ja.get(j));
			}
		}
		i.capacity = volume;
		i.setLimited(slots);
		i.setContents(items);
		return i;
	}
	
	public static JsonObject inventoryToEntry(InventoryUnstackable inv) {
		JsonObject jo = new JsonObject();
		jo.add("maxSlots", new JsonPrimitive(inv.items.maxAmount));
		jo.add("maxVolume", new JsonPrimitive(inv.capacity));
		Item[] itms = new Item[inv.items.size()];
		for(int i = 0; i < itms.length; i++) {
			itms[i] = inv.items.get(i);
		}
		JsonArray result = new JsonArray();
		for(int i = 0 ; i < itms.length; i++) {
			result.add(itms[i].getID()); 
		}
		jo.add("contents", result);
		return jo;
	}
}
