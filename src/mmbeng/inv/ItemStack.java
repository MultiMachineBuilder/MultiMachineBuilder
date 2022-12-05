/**
 * 
 */
package mmbeng.inv;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmbeng.craft.SingleItem;
import mmbeng.item.ItemEntry;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 * An item entry is intended for use in inventories.
 */
public final class ItemStack implements Identifiable<ItemEntry>, SingleItem{
	@Override
	public String toString() {
		return "ItemStack " + item + " � " + amount;
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
	
	public boolean equalsType(SingleItem other) {
		return equalsType(other.item());
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
	public ItemEntry item() {
		return item;
	}

	@Override
	public int amount() {
		return amount;
	}
}
