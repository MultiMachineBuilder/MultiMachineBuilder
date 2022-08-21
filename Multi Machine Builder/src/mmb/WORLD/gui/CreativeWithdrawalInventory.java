/**
 * 
 */
package mmb.WORLD.gui;

import java.util.Iterator;

import javax.annotation.Nonnull;

import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.item.Items;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.MapIterator;

/**
 * @author oskar
 *
 */
public class CreativeWithdrawalInventory implements Inventory {
	/**
	 * The singleton instance of CreativeWithdrawalInventory
	 */
	@Nonnull public static final CreativeWithdrawalInventory INSTANCE = new CreativeWithdrawalInventory();
	
	@SuppressWarnings("null")
	@Override
	public Iterator<ItemRecord> iterator() {
		return new MapIterator<>(
				CreativeWithdrawalItemRecord::new, //create new copies of the item
				Items.items.iterator() //list item types
				);
	}

	@Override
	public ItemRecord get(ItemEntry entry) {
		return new CreativeWithdrawalItemRecord(entry);
	}

	@Override
	public int insert(ItemEntry ent, int amount) {
		return 0;
	}

	@Override
	public int extract(ItemEntry ent, int amount) {
		return amount;
	}

	@Override
	public double capacity() {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public double volume() {
		return Double.POSITIVE_INFINITY;
	}

	private class CreativeWithdrawalItemRecord implements ItemRecord{
		private final ItemEntry item;

		public CreativeWithdrawalItemRecord(ItemType item) {
			this.item = item.create();
		}
		public CreativeWithdrawalItemRecord(ItemEntry item) {
			this.item = item;
		}
		@Override
		public int amount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public Inventory inventory() {
			return INSTANCE;
		}

		@Override
		public ItemEntry item() {
			return item;
		}

		@Override
		public int insert(int amount) {
			return 0;
		}

		@Override
		public int extract(int amount) {
			return amount;
		}

		@Override
		public boolean canInsert() {
			return false;
		}

		@Override
		public ItemRecord lockInsertions() {
			return this;
		}
		
	}

	@Override
	public ItemRecord nget(ItemEntry entry) {
		return get(entry);
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public int size() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int bulkInsert(RecipeOutput ent, int amount) {
		return 0;
	}
}
