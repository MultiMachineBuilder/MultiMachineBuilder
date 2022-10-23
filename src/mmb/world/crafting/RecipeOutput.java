/**
 * 
 */
package mmb.world.crafting;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.ainslec.picocog.PicoWriter;
import com.google.common.collect.Iterators;

import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.world.chance.Chance;
import mmb.world.inventory.ItemStack;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.items.ItemEntry;
import mmb.world.worlds.world.World;
import monniasza.collects.Collects;

/**
 * @author oskar
 * Represents a recipe output.
 * All implementations of this interface must be immutable, and may have builders.
 */
public interface RecipeOutput extends Chance, Iterable<ItemStack>{
	@Override
	default boolean drop(@Nullable InventoryWriter inv, @Nullable World map, int x, int y) {
		if(map == null) {
			if(inv == null) return false;
			produceResults(inv, 1);
		}else {
			@Nonnull InventoryWriter dropper = map.createDropper(x, y);
			InventoryWriter priority = (inv == null)? dropper :new InventoryWriter.Priority(inv, dropper);
			produceResults(priority, 1);
		}
		
		return true;
	}
	/**
	 * Produces {@code amount} units of this recipe output
	 * @param tgt inventory to move items to
	 * @param amount number of items
	 */
	@Override
	public void produceResults(InventoryWriter tgt, int amount);
	/**
	 * Produces one unit of recipe output
	 * @param tgt
	 */
	@Override
	public default void produceResults(InventoryWriter tgt) {
		produceResults(tgt, 1);
	}
	/**
	 * Represents this recipe output as text
	 * @param out
	 */
	@Override
	public void represent(PicoWriter out);
	/**
	 * Re-calculate the maximum volume of this recipe output.
	 * @return output volume
	 */
	public double outVolume();
	public default double outVolume(int amount) {
		return outVolume() * amount;
	}
	/**
	 * @return contents of the item list as map.
	 */
	@Nonnull public Object2IntMap<@Nonnull ItemEntry> getContents();
	
	/**
	 * Checks if the item list contains the selected item
	 * @param entry item to check
	 * @return does this item list contain selected item?
	 */
	@Override
	public boolean contains(@Nullable ItemEntry entry);
	
	/**
	 * @param entry item to get amount
	 * @return amount of items of given type, or 0 if not found
	 */
	public int get(ItemEntry entry);
	
	/**
	 * @param entry item to get amount
	 * @param value default value if item is not found
	 * @return amount of items of given type, or {@code value} if not found
	 */
	public int getOrDefault(ItemEntry entry, int value);
	
	/**
	 * Checks if contents of this item list are equal to the map
	 * @param data item data to compare to
	 * @return are contents of this item list equal to the map?
	 */
	public default boolean equiv(Map<ItemEntry, Integer> data) {
		return getContents().equals(data);
	}

	/**
	 * The lists are equal if their contents are equal as specified by {@link Map#equals(Object)}.
	 * The implementations can optimize this method.
	 * @param obj value to compare to
	 * @return true if this item list is equal to the argument; false otherwise.
	 */
	@Override
	boolean equals(@Nullable Object obj);

	/**
	 * The list's hash code is the hash code of their contents map as specified by {@link Map#hashCode()}
	 * @return a hash code value for this item list.
	 */
	@Override
	int hashCode();

	/** Represents a recipe output that does nothing */
	@Nonnull public static final RecipeOutput NONE = new RecipeOutput() {

		@Override
		public void produceResults(InventoryWriter tgt, int amount) {
			//no items to insert
		}

		@Override
		public void represent(PicoWriter out) {
			out.write("Nothing");
		}

		@Override
		public double outVolume() {
			return 0;
		}

		
		@SuppressWarnings("null")
		@Override
		public Object2IntMap<ItemEntry> getContents() {
			return Object2IntMaps.emptyMap();
		}

		@Override
		public boolean contains(@Nullable ItemEntry entry) {
			return false;
		}

		@Override
		public int get(ItemEntry entry) {
			return 0;
		}

		@Override
		public int getOrDefault(ItemEntry entry, int value) {
			return value;
		}
		@Override
		public boolean equals(@Nullable Object obj) {
			if(obj == this) return true;
			if(obj instanceof RecipeOutput) {
				RecipeOutput other = (RecipeOutput) obj;
				return other.getContents().isEmpty();
			}
			return false;
		}
		@Override
		public int hashCode() {
			return 0;
		}
	};

	/**
	 * @return unique items in this recipe output
	 */
	@Override
	public default Set<@Nonnull ItemEntry> items(){
		return getContents().keySet();
	}
	
	//Arithmetic methods
	public default SimpleItemList add(RecipeOutput rout) {
		Object2IntOpenHashMap<ItemEntry> map = new Object2IntOpenHashMap<>(getContents());
		for(ItemStack items: rout) 
			map.addTo(items.item, items.amount);
		return new SimpleItemList(map);
	}
	public default SimpleItemList add(SingleItem rout) {
		return add(rout.item(), rout.amount());
	}
	public default SimpleItemList add(ItemEntry item, int amount) {
		Object2IntOpenHashMap<ItemEntry> map = new Object2IntOpenHashMap<>(getContents());
		map.addTo(item, amount);
		return new SimpleItemList(map);
	}
	public default Stream<ItemStack> mul2stream(int amount) {
		return getContents()
			.object2IntEntrySet()
			.stream()
			.map(entry -> new ItemStack(entry.getKey(), entry.getIntValue()*amount));
	}
	public default Stream<Object2IntMap.Entry<ItemEntry>> mul2entrystream(int amount) {
		return getContents()
			.object2IntEntrySet()
			.stream()
			.map(entry -> new AbstractObject2IntMap.BasicEntry<ItemEntry>(entry.getKey(), entry.getIntValue()*amount));
	}
	public default SimpleItemList mul(int amount) {
		return mul2stream(amount).collect(ItemLists.collectToItemList());
	}
	public default <M extends Object2IntMap<ItemEntry>> M mul2map(int amount, Supplier<M> map) {
		return mul2entrystream(amount).collect(Collects.collectToIntMap(map));
	}
	@Override
	default @Nonnull Iterator<ItemStack> iterator() {
		return Iterators.transform(getContents().object2IntEntrySet().iterator(), entry -> new ItemStack(entry.getKey(), entry.getIntValue()));
	}
}
