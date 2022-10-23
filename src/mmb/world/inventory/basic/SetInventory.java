/**
 * 
 */
package mmb.world.inventory.basic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Iterators;

import mmb.Bitwise;
import mmb.beans.Saver;
import mmb.data.json.JsonTool;
import mmb.debug.Debugger;
import mmb.world.crafting.RecipeOutput;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.ItemRecord;
import mmb.world.inventory.ItemStack;
import mmb.world.items.ItemEntry;

/**
 * @author oskar
 * A skeletal implementation of inventory using sets.
 * A SetInventory is faster and self-contained implementation of the inventory
 * @implSpec Undefined behavior:
 * <ul>
 * 	<li>Creation with a populated inventory</li>
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
public class SetInventory<T extends ItemEntry> implements Inventory, Saver{
	@Nonnull private static final Debugger debug = new Debugger("INVENTORIES");
	@Nonnull public final Set<T> set;
	/** The class of the items (null if unrestricted)*/
	@Nullable public final Class<T> type;
	private double capacity = 2;
	private double volume = 0;
	
	/**
	 * Creates an inventory using a set
	 * @param set set to base on (should be empty)
	 * @param type type of items (null if unrestricted)
	 */
	public SetInventory(Set<T> set, @Nullable Class<T> type) {
		this.set = set;
		this.type = type;
	}
	/**
	 * Creates an inventory using a set factory
	 * @param supplier set factory
	 * @param type type of items (null if unrestricted)
	 */
	public SetInventory(Supplier<@Nonnull Set<T>> supplier, @Nullable Class<T> type) {
		this.set = supplier.get();
		this.type = type;
	}
	/**
	 * Creates a set inventory (simplest)
	 * @return a new set inventory
	 */
	@Nonnull public static SetInventory<ItemEntry> create() {
		return new SetInventory<>(new HashSet<>(), null);
	}
	/**
	 * Creates a set inventory with a custom set
	 * @param set set to use
	 * @return a new set inventory
	 */
	@Nonnull public static SetInventory<ItemEntry> create(Set<ItemEntry> set) {
		return new SetInventory<>(set, null);
	}
	/**
	 * Creates a set inventory using a set factory
	 * @param supplier set factory
	 * @return a new set inventory
	 */
	@Nonnull public static SetInventory<ItemEntry> create(Supplier<@Nonnull Set<ItemEntry>> supplier) {
		return new SetInventory<>(supplier, null);
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public @Nonnull Iterator<@Nonnull ItemRecord> iterator() {
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
	class SIRecord implements ItemRecord{
		@Nonnull private final ItemEntry entry;
		public SIRecord(ItemEntry entry) {
			this.entry = entry;
		}

		@Override
		public int amount() {
			return Bitwise.bool2int(set.contains(entry));
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
	
	@Override
	public int size() {
		return set.size();
	}

	@Override
	public int insert(ItemEntry ent, int amount) {
		if(amount <= 0 || !test(ent)) return 0;
		@SuppressWarnings("unchecked")
		T casted = (T) ent; //item is already of correct type
		if(remainVolume() < ent.volume()) return 0;
		boolean insert = set.add(casted);
		if(insert) volume += ent.volume();
		return Bitwise.bool2int(insert);
	}

	@Override
	public int extract(ItemEntry ent, int amount) {
		if(amount <= 0) return 0;
		boolean remove = set.remove(ent);
		if(remove) volume -= ent.volume();
		return Bitwise.bool2int(remove);
	}

	@Override
	public int bulkInsert(RecipeOutput ent, int amount) {
		if(amount <= 0) return 0;
		//Test the volume
		if(remainVolume() < ent.outVolume()) return 0;
		//Test the recipe output
		for(ItemStack entry: ent) {
			if(entry.amount > 1) return 0;
			if(!test(entry.item)) return 0;
		}
		for(ItemStack entry: ent) {
			if(entry.amount == 0) continue;
			volume += entry.outVolume();
			@SuppressWarnings("unchecked")
			T casted = (T) entry.item; //item is already of correct type
			set.add(casted);
		}
		return 1;
	}

	@Override
	public double capacity() {
		return capacity;
	}
	/**
	 * Changes capacity of the inventory
	 * @param capacity capacity of the inventory
	 * @return this
	 */
	@Nonnull public SetInventory<T> setCapacity(double capacity) {
		this.capacity = capacity;
		return this;
	}

	@Override
	public double volume() {
		return volume;
	}

	@Override
	public boolean test(@Nullable ItemEntry e) {
		if(type == null) return true;
		return type.isInstance(e);
	}

	@Override
	public JsonNode save() {
		ArrayNode array = JsonTool.newArrayNode();
		array.add(capacity);
		for(ItemEntry item: set) 
			array.add(ItemEntry.saveItem(item));
		return array;
	}

	@Override
	public void load(@Nullable JsonNode data) {
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
				set.add(casted);
				volume += item.volume();
			}
		}
	}
}