/**
 * 
 */
package mmb.WORLD.inventory;

import mmb.BEANS.Identifiable;

/**
 * @author oskar
 * An item entry is intended for use in inventories.
 */
public class ItemStack implements Identifiable<ItemEntry> {
	/**
	 * An item stored in the inventory
	 */
	public ItemEntry item;
	/**
	 * Number of items in the entry
	 */
	public int amount;
	
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
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass())
			return false;
		ItemStack other = (ItemStack) obj;
		if (amount != other.amount)
			return false;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		return true;
	}
	
	public boolean equalsType(ItemStack other) {
		return equalsType(other.item);
	}

	public boolean equalsType(ItemEntry item) {
		if(item == this.item) return true;
		if(item == null) return false;
		return item.equals(this.item);
	}

	@Override
	public ItemEntry id() {
		return item;
	}
}
