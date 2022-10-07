/**
 * 
 */
package mmb.world.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.world.crafting.RecipeOutput;
import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.items.ItemEntry;

/**
 * @author oskar
 * This is a common interface for 
 */
public interface Inventory extends Collection<@Nonnull ItemRecord> {	
	/**
	 * Get the item record under given item type
	 * @param entry
	 * @return the item record with given type, or throws if not found
	 * @throws NullPointerException if {@code entry} is null
	 * @throws IllegalStateException if item does not exist
	 */
	@Nonnull public ItemRecord get(ItemEntry entry);
	
	//Collection methods
	@Override
	default boolean add(ItemRecord arg0) {
		return insert(arg0.item(), arg0.amount()) != 0;
	}
	@Override
	default boolean addAll(@SuppressWarnings("null") Collection<? extends ItemRecord> c) {
		boolean changed = false;
		for(ItemRecord record: c) {
			changed |= (insert(record.item(), record.amount())) != 0;
		}
		return changed;
	}
	@Override
	default void clear() {
		for(ItemRecord record: this) {
			record.extract(record.amount());
		}
	}
	@Override
	default boolean contains(@Nullable Object o) {
		if(o instanceof ItemRecord) {
			ItemRecord  got = nget(((ItemRecord) o).item());
			if(got == null) return false;
			return got.amount() >= ((ItemRecord) o).amount();
		}
		return false;
	}
	@Override
	default boolean containsAll(@SuppressWarnings("null") Collection<?> c) {
		for(ItemRecord record: this) {
			if(!c.contains(record)) return false;
		}
		return true;
	}
	@Override
	default boolean remove(@Nullable Object o) {
		if(!(o instanceof ItemRecord)) return false;
		ItemRecord record = nget(((ItemRecord)o).item());
		if(record == null) return false;
		return record.extract(record.amount()) != 0;
	}
	@Override
	default boolean removeAll(@SuppressWarnings("null") Collection<?> c) {
		boolean changed = false;
		for(ItemRecord record: this) {
			if(c.contains(record)) {
				changed |= (0 != record.extract(record.amount()));
			}
		}
		return changed;// TODO Auto-generated method stub
	}
	@Override
	default boolean retainAll(@SuppressWarnings("null") Collection<?> c) {
		boolean changed = false;
		for(ItemRecord record: this) {
			if(!c.contains(record)) {
				changed |= (0 != record.extract(record.amount()));
			}
		}
		return changed;
	}
	/** @return number of unique cached item records */
	@Override
	int size();
	@Override
	default ItemRecord[] toArray() {
		return toArray(new ItemRecord[size()]);
	}
	@Override
	default <T> T[] toArray(@SuppressWarnings("null") T[] arr) {
		List<ItemRecord> list = new ArrayList<>();
		for(ItemRecord record: this) {
			list.add(record);
		}
		return list.toArray(arr);
	}
	/**
	 * Get the item record under given item type
	 * @param entry
	 * @return the item record with given type, or null if not found
	 * @throws NullPointerException if {@code entry} is null
	 */
	@Nullable public ItemRecord nget(ItemEntry entry);
	public int insert(ItemEntry ent, int amount);
	public int extract(ItemEntry ent, int amount);
	public int bulkInsert(RecipeOutput ent, int amount);
	/**
	 * @return capacity of given inventory
	 */
	public double capacity();
	/**
	 * @return used volume of given inventory
	 */
	public double volume();
	/**
	 * @return does given inventory exist?
	 */
	public default boolean exists() {
		return true;
	}
	public default boolean canExtract() {
		return true;
	}
	public default boolean canInsert() {
		return true;
	}

	@Nonnull public default Inventory lockInsertions() {
		return ExtractionsOnlyInventory.decorate(this);
	}
	@Nonnull public default Inventory lockExtractions() {
		return InsertionsOnlyInventory.decorate(this);
	}
	@Nonnull public default Inventory readOnly() {
		return ReadOnlyInventory.decorate(this);
	}

	@Nonnull public default InventoryReader createReader() {
		return new InventoryReader() {
			private final Iterator<ItemRecord> records = iterator();
			private ItemRecord current = records.hasNext() ? records.next() : null;
			
			@Override
			public int currentAmount() {
				if(current == null) return 0;
				return current.amount();
			}

			@Override
			public ItemEntry currentItem() {
				if(current == null) return null;
				return current.item();
			}

			@Override
			public int extract(int amount) {
				if(current == null) return 0;
				return current.extract(amount);
			}

			@Override
			public void skip() {
				current = null;
				records.next();
			}

			@Override
			public int extract(ItemEntry entry, int amount) {
				return Inventory.this.extract(entry, amount);
			}

			@Override
			public void next() {
				current = records.next();
			}

			@Override
			public boolean hasNext() {
				return records.hasNext();
			}

			@Override
			public boolean hasCurrent() {
				if(current == null) return false;
				return current.amount() > 0;
			}

			@Override
			public ExtractionLevel level() {
				return ExtractionLevel.RANDOM;
			}
		};
	}
	@Nonnull public default InventoryWriter createWriter() {
		return new InventoryWriter() {
			@Override
			public int write(ItemEntry ent, int amount) {
				return insert(ent, amount);
			}

			@Override
			public int bulkInsert(RecipeOutput block, int amount) {
				return Inventory.this.bulkInsert(block, amount);
			}
		};
	}
	/** @return Remaining volume in this inventory */
	public default double remainVolume() {
		return capacity() - volume();
	}
	/** @return Insertible volume in this inventory */
	public default double iremainVolume() {
		if(!canInsert()) return 0;
		return capacity() - volume();
	}
	/**
	 * 
	 * @param amount
	 * @param ivolume
	 * @return
	 */
	public default int insertibleRemain(int amount, double ivolume) {
		double tvolume = ivolume * amount;
		tvolume = Math.min(iremainVolume(), tvolume) / ivolume;
		return (int) Math.floor(tvolume);
	}
	/**
	 * @param amount mount of items
	 * @param item item to check
	 * @return number of items that may be inserted. May vary by inventory
	 */
	public default int insertibleRemain(int amount, ItemEntry item) {
		return insertibleRemain(amount, item.volume());
	}
	
	/**
	 * Returns how many copies of {@code sub} exist in {@code main}.
	 * @param main inventory to check
	 * @param sub copies to count
	 * @return amount of {@code sub} ins {@code main}
	 */
	public static int howManyTimesThisContainsThat(Inventory main, Inventory sub) {
		int result = Integer.MAX_VALUE;
		for(ItemRecord record: sub) {
			int small = record.amount(); //the sub contains null records
			ItemRecord mrecord = main.nget(record.item());
			if(mrecord == null) return 0;
			int big = mrecord.amount();
			int units = big/small;
			if(result > units) result = units;
			if(result == 0) return 0;
		}
		return result;
	}
	/**
	 * Returns how many copies of {@code sub} exist in {@code main}.
	 * @param main inventory to check
	 * @param sub copies to count
	 * @return amount of {@code sub} ins {@code main}
	 */
	public static int howManyTimesThisContainsThat(Inventory main, RecipeOutput sub) {
		int result = Integer.MAX_VALUE;
		for(Entry<ItemEntry> record: sub.getContents().object2IntEntrySet()) {
			int small = record.getIntValue(); //the sub contains null records
			ItemRecord mrecord = main.nget(record.getKey());
			if(mrecord == null) return 0;
			int big = mrecord.amount();
			int units = big/small;
			if(result > units) result = units;
			if(result == 0) return 0;
		}
		return result;
	}
}