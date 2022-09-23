/**
 * 
 */
package mmb.WORLD.inventory.storage;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Iterators;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.Bitwise;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 *
 */
public class SingleItemInventory implements Inventory {
	@Nullable private ItemEntry contents;
	private double capacity = 2;
	
	/** @return is there an item here? */
	public boolean containsItems() {
		return contents != null;
	}
	/** @return contents of this inventory */
	public ItemEntry getContents() {
		return contents;
	}
	/**  @param contents new contents of this inventory */
	public void setContents(@Nullable ItemEntry contents) {
		this.contents = contents;
	}

	private class Record implements ItemRecord{
		private final @Nonnull ItemEntry item0;
		public Record(ItemEntry item) {
			item0 = item;
		}
		@Override
		public int amount() {
			return Bitwise.bool2int(item0 == contents);
		}

		@Override
		public Inventory inventory() {
			return SingleItemInventory.this;
		}

		@Override
		public ItemEntry item() {
			return item0;
		}

		@Override
		public int insert(int amount) {
			if(amount <= 0) return 0;
			if(contents == null) {
				contents = item0;
				return 1;
			}
			return 0;
		}

		@Override
		public int extract(int amount) {
			if(amount <= 0) return 0;
			if(contents == null) return 0;
			if(contents != item0) return 0;
			contents = null;
			return 1;
		}
		
	}
	@SuppressWarnings("null")
	@Override
	public Iterator<ItemRecord> iterator() {
		if(contents == null) return Collections.emptyIterator();
		return Iterators.singletonIterator(get());
	}

	@SuppressWarnings("null")
	@Override
	public ItemRecord get(ItemEntry entry) {
		Objects.requireNonNull(entry, "Selection is null");
		return new Record(entry);
	}
	@Override
	public ItemRecord nget(ItemEntry entry) {
		Objects.requireNonNull(entry, "Selection is null");
		if(Objects.equals(entry, contents)) return new Record(entry);
		return null;
	}
	/**
	 * @return item record of this inventory
	 */
	@SuppressWarnings("null")
	@Nonnull public ItemRecord get() {
		if(contents == null) throw new IllegalStateException("This inventory is empty");
		return new Record(contents);
	}
	@SuppressWarnings("null")
	public ItemRecord nget() {
		if(contents == null) return null;
		return new Record(contents);
	}

	@Override
	public int insert(ItemEntry ent, int amount) {
		if(contents != null) return 0;
		if(amount <= 0) return 0;
		contents = ent;
		return 1;
	}

	@Override
	public int extract(ItemEntry ent, int amount) {
		if(contents == null) return 0;
		if(amount <= 0) return 0;
		if(Objects.equals(contents, ent)) {
			contents = null;
			return 1;
		}
		return 0;
	}

	@Override
	public double capacity() {
		return capacity;
	}
	/** @param cap new capacity */
	public void setCapacity(double cap) {
		capacity = cap;
	}

	@Override
	public double volume() {
		final ItemEntry contents2 = contents;
		if (contents2 != null) 
			return contents2.volume();
		return 0;
	}
	@Override
	public boolean isEmpty() {
		return contents == null;
	}
	@Override
	public int size() {
		return Bitwise.bool2int(!isEmpty());
	}
	@Override
	public int bulkInsert(RecipeOutput block, int amount) {
		if(block.items().size() > 1) return 0;
		if(block.items().isEmpty()) return amount;
		for(Entry<ItemEntry> entry: block.getContents().object2IntEntrySet()) {
			if(amount < 1) return 0;
			if(entry.getIntValue() > 1) return 0;
			if(entry.getIntValue() == 0) return amount;
			ItemEntry ent = entry.getKey();
			if(ent == null) return 0;
			return insert(ent, 1); //NOSONAR this loop is required to get the required item entry
		}
		return 0;
	}

}
