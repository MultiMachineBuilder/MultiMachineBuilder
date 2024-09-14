/**
 * 
 */
package mmb.engine.recipe;

import org.ainslec.picocog.PicoWriter;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.NN;
import mmb.Nil;
import mmb.engine.item.ItemEntry;
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

	/**
	 * An item stored in the inventory
	 */
	@NN public final ItemEntry item;
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
}
