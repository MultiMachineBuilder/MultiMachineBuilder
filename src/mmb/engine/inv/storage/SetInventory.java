/**
 * 
 */
package mmb.engine.inv.storage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Iterators;

import mmb.NN;
import mmb.Nil;
import mmb.engine.MMBUtils;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.inv.SaveInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.json.JsonTool;
import mmb.engine.recipe.ItemStack;
import mmb.engine.recipe.RecipeOutput;

/**
 * @author oskar
 * A skeletal implementation of inventory using sets.
 * A SetInventory is faster and self-contained implementation of the inventory
 * @implSpec Undefined behavior:
 * <ul>
 * 	<li>Creation with a populated set</li>
 *  <li>Modification of a set outside of the inventory</li>
 *  <li>Bulk insertion, if the set imposes restrictions on items beyond the type</li>
 *  <li>Using an unmodifiable set (if inventory will be modified)</li>
 * </ul>
 * @implNote The bulk insertion is implemented as follows:
 * <ol>
 * 	<li>Test all items using the {@link #test} method</li>
 * 	<li>If any item does not pass, reject the insertion</li>
 *  <li>Bulk insert as normal</li>
 * </ol> 
 * @param <T> type of items 
 */
public class SetInventory<@NN T extends ItemEntry> implements SaveInventory{
	//Debug
	@NN private static final Debugger debug = new Debugger("INVENTORIES");
	
	//Inventory definition
	/** The underlying set*/
	@NN public final Set<T> set;
	/** The class of the items (null if unrestricted)*/
	@Nil public final Class<T> type;
	private double capacity = 2;
	private double volume = 0;
	
	//Constructors
	/**
	 * Creates an inventory using a set
	 * @param set set to base on (should be empty)
	 * @param type type of items (null if unrestricted)
	 */
	public SetInventory(Set<@NN T> set, @Nil Class<T> type) {
		this.set = set;
		this.type = type;
		for(ItemEntry item: set) {
			volume += item.volume();
		}
	}
	/**
	 * Creates an inventory using a set factory
	 * @param supplier set factory
	 * @param type type of items (null if unrestricted)
	 */
	public SetInventory(Supplier<@NN Set<T>> supplier, @Nil Class<T> type) {
		this(supplier.get(), type);
	}
	/**
	 * Creates an unrestricted set inventory (simplest)
	 * @return a new set inventory
	 */
	@NN public static SetInventory<@NN ItemEntry> create() {
		return new SetInventory<>(new HashSet<>(), null);
	}
	/**
	 * Creates an unrestricted set inventory with a custom set
	 * @param set set to use
	 * @return a new set inventory
	 */
	@NN public static SetInventory<@NN ItemEntry> create(Set<@NN ItemEntry> set) {
		return new SetInventory<>(set, null);
	}
	/**
	 * Creates an unrestricted set inventory using a set factory
	 * @param supplier set factory
	 * @return a new set inventory
	 */
	@NN public static SetInventory<@NN ItemEntry> create(Supplier<@NN Set<@NN ItemEntry>> supplier) {
		return new SetInventory<>(supplier, null);
	}

	//Item calculation
	@Override
	public int insertibleRemain(int amount, ItemEntry item) {
		if(amount <= 0) return 0;
		if(set.contains(item)) return 0;
		if(!test(item)) return 0;
		return 1;
	}
	@Override
	public int insertibleRemainBulk(int amount, RecipeOutput ent) {
		if(amount <= 0) return 0;
		//Test the volume
		if(remainVolume() < ent.outVolume()) return 0;
		//Test the recipe output
		for(ItemStack entry: ent) {
			if(entry.amount > 1) return 0;
			if(!test(entry.item)) return 0;
		}
		return 1;
	}
	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}
	@Override
	public int size() {
		return set.size();
	}
	@Override
	@EnsuresNonNullIf(result = true, expression = {"e"})
	public boolean test(@Nil  ItemEntry e) {
		Class<T> cls = type;
		return cls == null || cls.isInstance(e);
	}
	@Override
	public double volume() {
		return volume;
	}

	//Item manipulation
	@Override
	public int insert(ItemEntry ent, int amount) {
		if(amount <= 0) return 0;
		if(!test(ent)) return 0;
		@SuppressWarnings("unchecked")
		T casted = (T) ent; //item is already of correct type
		if(remainVolume() < ent.volume()) return 0;
		boolean insert = set.add(casted);
		if(insert) volume += ent.volume();
		return MMBUtils.bool2int(insert);
	}
	@Override
	public int extract(ItemEntry ent, int amount) {
		if(amount <= 0) return 0;
		boolean remove = set.remove(ent);
		if(remove) volume -= ent.volume();
		return MMBUtils.bool2int(remove);
	}
	@Override
	public int bulkInsert(RecipeOutput ent, int amount) {
		int canInsert = insertibleRemainBulk(amount, ent);
		if(canInsert == 0) return 0;
		for(ItemStack entry: ent) {
			if(entry.amount == 0) continue;
			volume += entry.outVolume();
			@SuppressWarnings("unchecked")
			T casted = (T) entry.item; //item is already of correct type
			set.add(casted);
		}
		return 1;
	}

	//Item records
	class SIRecord implements ItemRecord{
		@NN private final ItemEntry entry;
		public SIRecord(ItemEntry entry) {
			this.entry = entry;
		}

		@Override
		public int amount() {
			return MMBUtils.bool2int(set.contains(entry));
		}

		@Override
		public Inventory inventory() {
			return SetInventory.this;
		}

		@Override
		public ItemEntry item() {
			return entry;
		}

		@Override
		public int insert(int amount) {
			return SetInventory.this.insert(entry, amount);
		}

		@Override
		public int extract(int amount) {
			return SetInventory.this.extract(entry, amount);
		}
		
	}
	@SuppressWarnings("null")
	@Override
	public @NN Iterator<@NN ItemRecord> iterator() {
		return Iterators.transform(set.iterator(), this::get);
	}
	@Override
	public ItemRecord get(ItemEntry entry) {
		return new SIRecord(entry);
	}
	@Override
	public ItemRecord nget(ItemEntry entry) {
		if(!set.contains(entry)) return null;
		return get(entry);
	}
	
	//Direct modification
	@Override
	public double capacity() {
		return capacity;
	}
	/**
	 * Changes capacity of the inventory
	 * @param capacity capacity of the inventory
	 * @return this
	 */
	@Override
	@NN public SetInventory<T> setCapacity(double capacity) {
		this.capacity = capacity;
		return this;
	}
	/**
	 * Directly sets the contents of the inventory (no capacity checks)
	 * @param items contents to set
	 */
	public void setContents(Set<@NN ItemEntry> items) {
		set.clear();
		addAllUnvolumed(items);
	}
	/**
	 * Directly adds items (no capacity checks)
	 * @param c items to add
	 * @return were any items added?
	 */
	public boolean addAllUnvolumed(Collection<@NN ItemEntry>  c) {
		boolean result = false;
		for(ItemEntry item: c) if(addUnvolumed(item)) result = true;
		return result;
	}
	/**
	 * Directly adds an item (no capacity checks)
	 * @param ent item to add
	 * @return was the item added?
	 */
	public boolean addUnvolumed(ItemEntry ent) {
		if(!test(ent)) return false;
		@SuppressWarnings("unchecked")
		T casted = (T) ent; //item is already of correct type
		boolean insert = set.add(casted);
		if(insert) volume += ent.volume();
		return insert;
	}
	/**
	 * Replaces the contents of this inventory with the one of the other inventory
	 * @param inv source inventory
	 */
	public void setContents(SetInventory<? extends T> inv) {
		set.clear();
		capacity = inv.capacity();
		volume = 0;
		for(T item: inv.set) {
			boolean added = set.add(item);
			if(added) volume += item.volume();
		}
	}
	
	//Serialization
	@Override
	public JsonNode save() {
		ArrayNode array = JsonTool.newArrayNode();
		array.add(capacity);
		for(ItemEntry item: set) 
			array.add(ItemEntry.saveItem(item));
		return array;
	}
	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		for(JsonNode node: data) {
			if(node.isNumber()) {
				//number nodes - capacity
				capacity = data.asDouble(2);
			} else {
				//array nodes - items
				ItemEntry item = ItemEntry.loadFromJson(node);
				if(!test(item)) {
					if(item == null) 
						debug.printl("Unsupported null item");
					else
						debug.printl("Unsupported item class: "+item.getClass()+" for: "+item);
					continue;
				}
				@SuppressWarnings("unchecked")
				T casted = (T) item; //item is already of the correct type
				if(item != null) {
					set.add(casted);
					volume += item.volume();
				}
			}
		}
	}
}