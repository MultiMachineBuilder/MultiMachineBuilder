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
	public static JsonArray stackToEntry(ItemStack is) {
		JsonArray ja = new JsonArray();
		ja.add(itemToEntry(is.item));
		ja.add(is.amount);
		return ja;
	}
	public static ItemStack entryToStack(JsonElement je) {
		if(je.isJsonArray()) {
			JsonArray ja = je.getAsJsonArray();
			return new ItemStack(entryToItem(ja.get(0)), ja.get(1).getAsInt());
		}
		return new ItemStack(entryToItem(je), 1);
	}
	public static Item entryToItem(JsonElement je) {
		if(je.isJsonObject()) {
			//TODO item entities
			return null;
		}else if(je.isJsonPrimitive()) {
			//simple items
			return Items.items.get(je.getAsString());
		}else{
			//TODO future types
			return null;
		}
	}
	
	public static Inventory entryToInventory(JsonObject jo) {
		boolean isStackable = false;
		if(jo.has("stack")) isStackable = jo.get("stack").getAsBoolean();
		Inventory i;
		if(isStackable) {
			i = new InventoryStackable();
		}else {
			i = new InventoryUnstackable();
		}
		
		int slots = -1;
		if(jo.has("maxSlots")) slots = jo.get("maxSlots").getAsInt();
		double volume = 2;
		if(jo.has("maxVolume")) volume = jo.get("maxVolume").getAsDouble();
		ItemStack[] items = new ItemStack[0];
		if(jo.has("contents")) {
			JsonArray ja = jo.get("contents").getAsJsonArray();
			items = new ItemStack[ja.size()];
			for(int j = 0; j < ja.size(); j++) {
				JsonElement e = ja.get(j);
				items[j] = ItemJSONConverter.entryToStack(ja.get(j));
			}
		}
		i.capacity = volume;
		i.setLimited(slots);
		i.setContents(items);
		return i;
	}
	
	public static JsonObject inventoryToEntry(Inventory inv) {
		JsonObject jo = new JsonObject();
		jo.add("maxSlots", new JsonPrimitive(inv.getMaxSlots()));
		jo.add("maxVolume", new JsonPrimitive(inv.capacity));
		jo.add("stack", new JsonPrimitive(inv.isStackable()));
		ItemStack[] contents = inv.getContents();
		JsonArray result = new JsonArray();
		for(int i = 0 ; i < contents.length; i++) {
			result.add(stackToEntry(contents[i])); 
		}
		jo.add("contents", result);
		return jo;
	}
}
