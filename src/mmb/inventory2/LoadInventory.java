package mmb.inventory2;

import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;
import mmb.engine.json.JsonTool;
import mmb.engine.recipe.ItemStack;
import mmb.inventory2.storage.Inventory;

public final class LoadInventory {
	private LoadInventory() {}
	
	public static void loadInventory(@NN Inventory target, @Nil JsonNode json) {
		Objects.requireNonNull(target, "target is null");
		if(json == null) return;
		JsonNode volumeCapacityNode = json.get("volume");
		if(volumeCapacityNode != null) target.setMaxVolume(volumeCapacityNode.asDouble(0));
		JsonNode slotCapacityNode = json.get("slots");
		if(slotCapacityNode != null) target.setMaxSlots(slotCapacityNode.asInt(0));
		JsonNode contentsNode = json.get("contents");
		if(contentsNode != null) {
			target.clear();
			for(JsonNode element: contentsNode) {
				ItemStack itemRecord = ItemStack.loadFromJson(element);
				target.setAmount(itemRecord.item, itemRecord.amount);
			}
		}
	}
	public static ObjectNode saveInventory(@NN ItemHandler inventory) {
		Objects.requireNonNull(inventory, "inventory is null");
		ObjectNode result = JsonTool.newObjectNode();
		double volumeCapacity = inventory.capacity();
		double slotCapacity = inventory.maxSlots();
		result.put("volume", volumeCapacity);
		result.put("slots", slotCapacity);
		ArrayNode stacksArray = JsonTool.newArrayNode();
		Object2IntMap<ItemEntry> map = new Object2IntOpenHashMap<>();
		inventory.dumpContents(map);
		for(Object2IntMap.Entry<ItemEntry> entry: map.object2IntEntrySet()) {
			ItemEntry item = entry.getKey();
			int amount = entry.getIntValue();
			ItemStack itemStack = new ItemStack(item, amount);
			stacksArray.add(ItemStack.saveToJson(itemStack));
		}
		return result;
	}
}
