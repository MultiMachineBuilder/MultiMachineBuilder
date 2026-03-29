/**
 * 
 */
package mmb.engine.recipe;

import java.util.Objects;

import org.ainslec.picocog.PicoWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;
import mmb.engine.item.Items;
import mmb.engine.json.JsonTool;
import monniasza.collects.Identifiable;

/**
 * Mutiple units of one item entry
 * @author oskar
 */
public final class ItemStack implements Identifiable<ItemEntry>, SingleItem{
	@Override
	public String toString() {
		return "ItemStack " + item + " * " + amount;
	}

	/** An item described by this item stack */
	@NN public final ItemEntry item;
	/** Number of items in the entry */
	public final int amount;
	
	/**
	 * @param item item type in this stack
	 * @param amount number of items in this stack
	 */
	public ItemStack(ItemEntry item, int amount) {
		super();
		this.item = item;
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (amount-1);
		result = prime * result + item.hashCode();
		return result;
	}

	@Override
	public boolean equals(@Nil Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass())
			return false;
		ItemStack other = (ItemStack) obj;
		if (amount != other.amount)
			return false;
		if (!item.equals(other.item))
			return false;
		return true;
	}
	
	/**
	 * Checks equality of item types
	 * @param other item stack to check
	 * @return are item types equal?
	 */
	public boolean equalsType(SingleItem other) {
		return equalsType(other.item());
	}
	/**
	 * Checks equality of item types
	 * @param item1 item to check
	 * @return are item types equal?
	 */
	public boolean equalsType(ItemEntry item1) {
		if(item1 == this.item) return true;
		return item1.equals(this.item);
	}

	@Override
	public ItemEntry id() {
		return item;
	}

	@Override
	public ItemEntry item() {
		return item;
	}

	@Override
	public int amount() {
		return amount;
	}

	public static String toString(Entry<ItemEntry> entry) {
		return entry.getKey() + " * " + entry.getIntValue();
	}

	public static @Nil ItemStack loadFromJson(@Nil JsonNode element) {
		if(element == null) return null;
		int elementSize = element.size();
		if(elementSize < 2) return null;
		JsonNode itemNode = element.get(0);
		JsonNode quantityNode = element.get(1);
		JsonNode itemDataNode = null;
		if(elementSize >= 3) itemDataNode = element.get(2);
		if(itemNode == null || quantityNode == null) return null;
		String itemTypeId = itemNode.asText();
		int quantity = quantityNode.asInt();
		if(itemTypeId == null || quantity <= 0) return null;
		ItemType itemType = Items.get(itemTypeId);
		if(itemType == null) return null;
		ItemEntry itemEntry = itemType.createItem(itemDataNode);
		if(itemEntry == null) return null;
		return new ItemStack(itemEntry, quantity);
	}
	public static JsonNode saveToJson(@Nil ItemStack stack) {
		if(stack == null) return NullNode.instance;
		ItemEntry item = stack.item;
		String itemId = item.itemType().id;
		int quantity = stack.amount;
		JsonNode itemDataNode = item.save();
		ArrayNode result = JsonTool.newArrayNode();
		result.add(itemId);
		result.add(quantity);
		if(itemDataNode != null) result.add(itemDataNode);
		return result;
	}
}
