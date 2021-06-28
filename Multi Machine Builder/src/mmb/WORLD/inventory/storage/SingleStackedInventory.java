/**
 * 
 */
package mmb.WORLD.inventory.storage;

import java.util.Collections;
import java.util.Iterator;

import com.google.common.collect.Iterators;

import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.items.ItemEntry;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class SingleStackedInventory implements Inventory{
	private static final Debugger debug = new Debugger("STACK INVENTORY");
	private double capacity = 2;
	private int amount;
	private ItemEntry entry;
	private class Record implements ItemRecord{
		@Override
		public boolean exists() {
			return SingleStackedInventory.this.entry == entry;
		}

		private final ItemEntry entry;
		public Record(ItemEntry ent) {
			entry = ent;
		}
		@Override
		public int amount() {
			if(SingleStackedInventory.this.entry == entry) return amount;
			return 0;
		}

		@Override
		public Inventory inventory() {
			return SingleStackedInventory.this;
		}

		@Override
		public ItemEntry item() {
			return entry;
		}

		@Override
		public int insert(int amount) {
			if(SingleStackedInventory.this.entry != entry) return 0;
			return SingleStackedInventory.this.insert(entry, amount);
		}

		@Override
		public int extract(int amount) {
			if(SingleStackedInventory.this.entry != entry) return 0;
			return SingleStackedInventory.this.extract(entry, amount);
		}
	}
	
	@SuppressWarnings("null")
	@Override
	public Iterator<ItemRecord> iterator() {
		if(entry == null) return Collections.emptyIterator();
		return Iterators.singletonIterator(new Record(entry));
	}

	@Override
	public ItemRecord get(ItemEntry entry) {
		if(this.entry == null) throw new IllegalStateException("This inventory is empty");
		if(entry.equals(this.entry)) 
			return new Record(this.entry);
		throw new IllegalStateException("Given item does not exist");
	}

	@Override
	public ItemRecord nget(ItemEntry entry) {
		if(this.entry == null) return null;
		if(entry.equals(this.entry)) 
			return new Record(this.entry);
		return null;
	}

	@Override
	public int insert(ItemEntry ent, int amount) {
		int amt = insertibleRemain(amount, ent);
		debug.printl("Amount: "+amt);
		if(entry == null) {
			entry = ent;
		}
		this.amount += amt;
		return amt;
	}

	@Override
	public int extract(ItemEntry ent, int amount) {
		if(entry == null) return 0;
		if(entry.equals(ent)) {
			int result = Math.min(this.amount, amount);
			this.amount -= result;
			if(this.amount == 0) entry = null;
			return result;
		}
		return 0;
	}

	@Override
	public double capacity() {
		return capacity;
	}
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	@Override
	public double volume() {
		if(entry == null) return 0;
		return entry.volume(amount);
	}

	@Override
	public int insertibleRemain(int amount, ItemEntry item) {
		if(entry == null || item.equals(entry)) {
			int tgtamount = Math.min((int)(remainVolume()/ item.volume()), amount);
			return Math.max(0, tgtamount);
		}
		debug.printl("Differing items, rejecting");
		return 0;
	}

	public int itemCount() {
		return amount;
	}
}
