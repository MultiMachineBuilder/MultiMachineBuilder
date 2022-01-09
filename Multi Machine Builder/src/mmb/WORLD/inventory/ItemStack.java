/**
 * 
 */
package mmb.WORLD.inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.ainslec.picocog.PicoWriter;

import com.fasterxml.jackson.databind.JsonNode;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import mmb.BEANS.Saver;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 * An item entry is intended for use in inventories.
 */
public final class ItemStack implements Identifiable<ItemEntry>, RecipeOutput{
	@Override
	public String toString() {
		return "ItemStack " + item + " × " + amount;
	}

	/**
	 * An item stored in the inventory
	 */
	@Nonnull public final ItemEntry item;
	/**
	 * Number of items in the entry
	 */
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

	/**
	 * @return the volume of item entry
	 */
	public double volume() {
		return item.volume() * amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + item.hashCode();
		return result;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
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
	
	public boolean equalsType(ItemStack other) {
		return equalsType(other.item);
	}

	public boolean equalsType(ItemEntry item) {
		if(item == this.item) return true;
		return item.equals(this.item);
	}

	@Override
	public ItemEntry id() {
		return item;
	}

	@Override
	public void produceResults(InventoryWriter tgt, int amount) {
		tgt.write(item, amount*this.amount);
	}

	@Override
	public void represent(PicoWriter out) {
		out.write(amount +"× "+item.title());
	}

	@Override
	public double outVolume() {
		return volume();
	}
		
	@Override
	public Object2IntMap<ItemEntry> getContents() {
		return Object2IntMaps.singleton(item, amount);
	}

	@Override
	public boolean contains(ItemEntry entry) {
		return item.equals(entry);
	}

	@Override
	public int get(ItemEntry entry) {
		return getOrDefault(entry, 0);
	}

	@Override
	public int getOrDefault(ItemEntry entry, int value) {
		if(contains(entry)) return amount;
		return value;
	}
}
