/**
 * 
 */
package mmb.world.inventory.storage;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

import com.google.common.collect.Iterators;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.Bitwise;
import mmb.debug.Debugger;
import mmb.world.crafting.RecipeOutput;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.ItemRecord;
import mmb.world.items.ItemEntry;

/**
 * An inventory, whose item entry is locked
 * @author oskar
 */
public class DrawerInventory implements Inventory{
	private static final Debugger debug = new Debugger("STACK INVENTORY");
	private double capacity = 2;
	private int amount;
	private ItemEntry entry;
	private class Record implements ItemRecord{
		@Override
		public boolean exists() {
			return DrawerInventory.this.entry == entry;
		}

		private final ItemEntry entry;
		public Record(ItemEntry ent) {
			entry = ent;
		}
		@Override
		public int amount() {
			if(DrawerInventory.this.entry == entry) return amount;
			return 0;
		}

		@Override
		public Inventory inventory() {
			return DrawerInventory.this;
		}

		@Override
		public ItemEntry item() {
			return entry;
		}

		@Override
		public int insert(int amount) {
			if(DrawerInventory.this.entry != entry) return 0;
			return DrawerInventory.this.insert(entry, amount);
		}

		@Override
		public int extract(int amount) {
			if(DrawerInventory.this.entry != entry) return 0;
			return DrawerInventory.this.extract(entry, amount);
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
		if(item.equals(entry)) {
			int tgtamount = Math.min((int)(remainVolume()/ item.volume()), amount);
			return Math.max(0, tgtamount);
		}
		return 0;
	}

	/** @return total items stored */
	public int itemCount() {
		return amount;
	}
	
	@Override
	public boolean isEmpty() {
		return amount==0 || entry==null;
	}
	@Override
	public int size() {
		return Bitwise.bool2int(!isEmpty());
	}

	/** @return the current lock */
	public ItemEntry getEntry() {
		return entry;
	}
	/**
	 * Replaces the item lock
	 * @param entry new lock
	 */
	public void setEntry(ItemEntry entry) {
		this.entry = entry;
	}
	
	@Override
	public int bulkInsert(RecipeOutput block, int amount) {
		if(block.items().size() > 1) return 0;
		if(block.items().isEmpty()) return amount;
		for(Entry<ItemEntry> entry: block.getContents().object2IntEntrySet()) {
			if(amount < 1) return 0;
			if(entry.getIntValue() == 0) return amount;
			ItemEntry ent = entry.getKey();
			if(ent == null) return 0;
			int max = amount;
			double unitVolume = ent.outVolume();
			double roughVolumeLimit = remainVolume()/unitVolume;
			int exactUnits = (int) roughVolumeLimit;
			if(exactUnits == 0) return 0;
			if(max > exactUnits) max = exactUnits;
			return insert(ent, entry.getIntValue()*max); //NOSONAR this loop is required to get the required item entry
		}
		return 0;
	}

	@Override
	public boolean test(ItemEntry e) {
		return Objects.equals(e, entry);
	}
}