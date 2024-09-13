/**
 * 
 */
package mmb.engine.inv;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mmb.NN;
import mmb.Nil;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemStack;
import mmb.engine.recipe.RecipeOutput;

/**
 * An inventory is an object, which allows players and machines to store items.
 * @author oskar
 */
public interface Inventory extends Collection<@NN ItemRecord> {
	static final Debugger debug0 = new Debugger("INVENTORIES0");
	//Item records
	/**
	 * Returns an iterator over the item records in this inventory.
	 * There are no guarantees concerning the order in which the item records are returned
	 * (unless this inventory is an instance of some class that provides a guarantee).
	 * @return an {@code Iterator} over the elements in this inventory
	 */
	@Override
	@NN Iterator<@NN ItemRecord> iterator();
	/**
	 * Get the item record under given item type
	 * @param entry item to get
	 * @return the item record with given type, or throws if not found
	 * @throws NullPointerException if {@code entry} is null
	 * @throws IllegalStateException if item does not exist
	 */
	@NN public ItemRecord get(ItemEntry entry);
	/**
	 * Get the item record under given item type
	 * @param entry item to get item record for
	 * @return the item record with given type, or null if not found
	 * @throws NullPointerException if {@code entry} is null
	 */
	@Nil public ItemRecord nget(ItemEntry entry);
	@Override
	default ItemRecord[] toArray() {
		return toArray(new ItemRecord[size()]);
	}
	@Override
	default <T> T[] toArray(T[] arr) {
		int i = 0;
		for(ItemRecord irecord: this) 
			arr[i++] = (T) irecord;
		return arr;
	}
	
	//Insertion calculation
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
	 * Checks how many items by volume may be inserted.
	 * The inventory may impose restrictions on the items, so this method should not be used to check insertability. 
	 * Use {@link #insertibleRemain(int, ItemEntry)} instead.
	 * @apiNote This method does not test if item is allowed.
	 * @param amount max number of items
	 * @param ivolume volume of an item
	 * @return how many items can be inserted
	 */
	public default int insertibleRemain(int amount, double ivolume) {
		if(ivolume == 0) return amount;
		double tvolume = ivolume * amount;
		tvolume = Math.min(iremainVolume(), tvolume) / ivolume;
		return (int) Math.floor(tvolume);
	}
	/**
	 * Counts how many units of the item may be inserted.
	 * The inventory may impose restrictions on the items and extraction, so this method should be used to check insertability.
	 * @param amount amount of items
	 * @param item item to check
	 * @return number of items that may be inserted. May vary by inventory
	 */
	public default int insertibleRemain(int amount, ItemEntry item) {
		if(!test(item)) return 0;
		return insertibleRemain(amount, item.volume());
	}
	/**
	 * Counts how many units of the item may be extracted.
	 * @param amount amount of items
	 * @param ent insertion unit to check
	 * @return number of units that may be inserted. May vary by inventory
	 */
	public int insertibleRemainBulk(int amount, RecipeOutput ent);
	
	//Extraction calculation
	/**
	 * Counts how many units of the item may be extracted.
	 * The inventory may impose restrictions on the extraction, so this method should be used to check extractability.
	 * @param amount amount of items
	 * @param item item to check
	 * @return number of items that may be extracted. May vary by inventory
	 * @implNote The implementation is suitable for most inventories.
	 * If inventory restricts extractions beyond blocking them all, this method should be overridden
	 */
 	public default int extractRemain(int amount, ItemEntry item) {
		if(!canExtract()) return 0;
		ItemRecord ir = nget(item);
		if(ir == null) return 0;
		int stored = ir.amount();
		return Math.min(amount, stored);
	}
	/**
	 * Counts how many units may be extracted, without breaking extraction units up.
	 * The inventory may impose restrictions on the extraction, so this method should be used to check bulk extractability.
	 * @param amount amount of items
	 * @param ent extraction unit to check
	 * @return number of items that may be extracted. May vary by inventory
	 * @implNote The implementation is suitable for most inventories.
	 * If inventory restricts extractions beyond blocking them all, this method should be overridden
	 */
	public default int extractRemainBulk(int amount, RecipeOutput ent) {
		int maxUnits = amount;
		for(ItemStack stk: ent) {
			int largerAmount = stk.amount * maxUnits;
			int maxUnitsSub = extractRemain(largerAmount, stk.item);
			maxUnits = maxUnitsSub / stk.amount;
		}
		return maxUnits;
	}
	
	//Inventory calculation
	/** @return is this inventory empty? (no cached items) */
	@Override
	boolean isEmpty();
	/** @return number of unique cached item records */
	@Override
	int size();
	/**
	 * Checks if the inventory allows given items
	 * @param e item to test
	 * @return does the inventory allow given item
	 */
	public boolean test(ItemEntry e);
	/** @return used volume of given inventory */
	public double volume();
	@Override
	default boolean contains(@Nil Object o) {
		if(o instanceof ItemRecord) {
			ItemRecord got = nget(((ItemRecord) o).item());
			if(got == null) return false;
			return got.amount() >= ((ItemRecord) o).amount();
		}
		return false;
	}
	@Override
	default boolean containsAll(Collection<?> c) {
		for(ItemRecord irecord: this) {
			if(!c.contains(irecord)) return false;
		}
		return true;
	}
	
	//Item manipulation
	/**
	 * Adds items to an inventory.
	 * The inventory may impose restrictions on the items and extraction, so users should check beforehand,
	 * preferrably with {@link #insertibleRemain(int, ItemEntry)}
	 * @param ent item to insert
	 * @param amount number of items to insert
	 * @throws IllegalArgumentException when negative items are inserted.
	 * @return number of items inserted
	 */
	public int insert(ItemEntry ent, int amount);
	/**
	 * Removes items from an inventory
	 * The inventory may impose restrictions on the extraction, so users should check beforehand
	 * @param ent item to extract
	 * @param amount number of items to extract
	 * @throws IllegalArgumentException when negative items are extracted.
	 * @return number of items extracted
	 */
	public int extract(ItemEntry ent, int amount);
	/**
	 * Adds units to an inventory, without breaking up the units
	 * The inventory may impose restrictions on the items and insertion, so users should check beforehand
	 * @param ent insertion unit
	 * @param amount number of units to insert
	 * @throws IllegalArgumentException when negative units are inserted.
	 * @return number of units inserted
	 */
	public int bulkInsert(RecipeOutput ent, int amount);
	/**
	 * Extract units from an inventory, without breaking up the units
	 * The inventory may impose restrictions on the extraction, so users should check beforehand
	 * @param ent extraction unit
	 * @param amount number of units to extract
	 * @throws IllegalArgumentException when negative units are extracted.
	 * @return number of units extracted
	 * @implNote The implementation is suitable for most inventories.
	 * If inventory restricts extractions beyond blocking them all, the {@link #extractRemainBulk(int, RecipeOutput)} should be overridden
	 */
	public default int bulkExtract(RecipeOutput ent, int amount) {
		int actual = extractRemainBulk(amount, ent);
		debug0.printl("Extract attempt with "+actual+" of "+amount+" blocks");
		if(actual == 0) return 0;
		for(ItemStack stk: ent) {
			int count = stk.amount * actual;
			int ex = extract(stk.item, count);
			debug0.printl("Extracted "+ex+" of "+stk.item+", expected "+ count);
		}
		return actual;
	}
	/** 
	 * Converts this inventory to an inventory reader
	 * If this inventory does not allow extractions, the reader will be useless.
	 * @return a new inventory reader
	 * @apiNote The inventory reader is random access and iterative.
	 * If inventory is modified during iteration, this inventory reader will fail.
	 */
	@NN public default InventoryReader createReader() {
		return new InventoryReader() {
			private final Iterator<ItemRecord> records = iterator();
			private ItemRecord current = null;
			
			@Override
			public int currentAmount() {
				if(current == null) throw new NoSuchElementException();
				return current.amount();
			}

			@Override
			public ItemEntry currentItem() {
				if(current == null) throw new NoSuchElementException();
				return current.item();
			}

			@Override
			public int extract(int amount) {
				if(current == null) return 0;
				return current.extract(amount);
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
			public ExtractionLevel level() {
				return ExtractionLevel.RANDOM;
			}

			@Override
			public int toBeExtracted(ItemEntry item, int amount) {
				return extractRemain(amount, item);
			}

			@Override
			public int toBeExtractedBulk(RecipeOutput item, int amount) {
				return extractRemainBulk(amount, item);
			}

			@Override
			public int extractBulk(RecipeOutput block, int amount) {
				return Inventory.this.bulkExtract(block, amount);
			}
		};
	}
	/**
	 * Converts this inventory to an inventory writer
	 * If this inventory does not allow insertions, the writer will be useless.
	 * @return a new inventory writer
	 */
	@NN public default InventoryWriter createWriter() {
		return new InventoryWriter() {
			@Override
			public int insert(ItemEntry ent, int amount) {
				return Inventory.this.insert(ent, amount);
			}

			@Override
			public int bulkInsert(RecipeOutput block, int amount) {
				return Inventory.this.bulkInsert(block, amount);
			}

			@Override
			public int toInsertBulk(RecipeOutput block, int amount) {
				return insertibleRemainBulk(amount, block);
			}

			@Override
			public int toInsert(ItemEntry item, int amount) {
				return insertibleRemain(amount, item);
			}
		};
	}
	
	//(quasi)direct method - this interface doesn't provide direct modification
	/** @return capacity of given inventory */
	public double capacity();
	@Override
	default boolean add(ItemRecord arg0) {
		return insert(arg0.item(), arg0.amount()) != 0;
	}
	@Override
	default boolean addAll(Collection<@NN ? extends ItemRecord> c) {
		boolean changed = false;
		for(@NN ItemRecord irecord: c) {
			changed |= (insert(irecord.item(), irecord.amount())) != 0;
		}
		return changed;
	}
	@Override
	default void clear() {
		for(ItemRecord irecord: this) {
			irecord.extract(irecord.amount());
		}
	}
	
	@Override
	default boolean remove(@Nil Object o) {
		if(!(o instanceof ItemRecord)) return false;
		ItemRecord iecord = nget(((ItemRecord)o).item());
		if(iecord == null) return false;
		return iecord.extract(iecord.amount()) != 0;
	}
	@Override
	default boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for(ItemRecord iecord: this) {
			if(c.contains(iecord)) {
				changed |= (0 != iecord.extract(iecord.amount()));
			}
		}
		return changed;
	}
	@Override
	default boolean retainAll(Collection<?> c) {
		boolean changed = false;
		for(ItemRecord irecord: this) {
			if(!c.contains(irecord)) {
				changed |= (0 != irecord.extract(irecord.amount()));
			}
		}
		return changed;
	}
	
	//I/O checking
	/** @return does given inventory exist? */
	public default boolean exists() {
		return true;
	}
	/** @return is extraction allowed? */
	public default boolean canExtract() {
		return true;
	}
	/** @return is insertion allowed? */
	public default boolean canInsert() {
		return true;
	}
	
	//I/O restrictions
	/** @return an inventory without insertions allowed */
	@NN public default Inventory lockInsertions() {
		return ExtractionsOnlyInventory.decorate(this);
	}
	/** @return an inventory without extractions allowed */
	@NN public default Inventory lockExtractions() {
		return InsertionsOnlyInventory.decorate(this);
	}
	/** @return an inventory without I/O allowed */
	@NN public default Inventory readOnly() {
		return ReadOnlyInventory.decorate(this);
	}

	/**
	 * Returns how many copies of {@code sub} exist in {@code main}.
	 * @param main inventory to check
	 * @param sub copies to count
	 * @param amount maximum reported amount
	 * @return amount of {@code sub} ins {@code main}
	 */
	public static int howManyTimesThisContainsThat(Inventory main, RecipeOutput sub, int amount) {
		int result = amount;
		for(ItemStack irecord: sub) {
			int small = irecord.amount; //the sub contains null records
			ItemRecord mrecord = main.nget(irecord.item);
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
		return howManyTimesThisContainsThat(main, sub, Integer.MAX_VALUE);
	}
	/**
	 * Inserts contents of this inventory into a map
	 * @param map
	 */
	public default void contents(Object2IntMap<ItemEntry> map) {
		for(ItemRecord irecord: this) {
			map.put(irecord.item(), irecord.amount());
		}
	}
}