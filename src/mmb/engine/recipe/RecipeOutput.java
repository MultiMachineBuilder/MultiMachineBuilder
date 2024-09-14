/**
 * 
 */
package mmb.engine.recipe;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.ainslec.picocog.PicoWriter;

import com.google.common.collect.Iterators;

import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.NN;
import mmb.Nil;
import mmb.engine.chance.Chance;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.ItemEntry;
import mmb.engine.worlds.world.World;
import monniasza.collects.Collects;

/**
 * Represents a deterministic recipe output.
 * All implementations of this interface must be immutable, and may have builders.
 * @author oskar
 */
public interface RecipeOutput extends Chance, Iterable<ItemStack>{
	@Override
	default boolean drop(@Nil InventoryWriter inv, @Nil World map, int x, int y) { //NOSONAR the items will always be dropped
		if(map == null) {
			if(inv == null) return false;
			produceResults(inv, 1);
		}else {
			@NN InventoryWriter dropper = map.createDropper(x, y);
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
	public default void produceResults(InventoryWriter tgt, int amount) {
		for(Entry<ItemEntry> ent : getContents().object2IntEntrySet()) tgt.insert(ent.getKey(), ent.getIntValue());
	}
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
	/** @return contents of the item list as map. */
	@NN public Object2IntMap<@NN ItemEntry> getContents();
	
	/**
	 * Checks if the item list contains the selected item
	 * @param entry item to check
	 * @return does this item list contain selected item?
	 */
	@Override
	public boolean contains(@Nil ItemEntry entry);
	
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
	public default boolean equiv(Map<@NN ItemEntry, @NN Integer> data) {
		return getContents().equals(data);
	}

	/**
	 * The lists are equal if their contents are equal as specified by {@link Map#equals(Object)}.
	 * The implementations can optimize this method.
	 * @param obj value to compare to
	 * @return true if this item list is equal to the argument; false otherwise.
	 */
	@Override
	boolean equals(@Nil Object obj);
	/**
	 * The list's hash code is the hash code of their contents map as specified by {@link Map#hashCode()}
	 * @return a hash code value for this item list.
	 */
	@Override
	int hashCode();

	/** Represents a recipe output that does nothing */
	@NN public static final RecipeOutput NONE = new RecipeOutput() {

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
		public boolean contains(@Nil ItemEntry entry) {
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
		public boolean equals(@Nil Object obj) {
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
	public default @NN Set<@NN ItemEntry> items(){
		return getContents().keySet();
	}
	
	/**
	 * Prevents the output from becoming null
	 * @param rout recipe output
	 * @return an empty recipe output if null, or else the input recipe output
	 */
	@NN public static RecipeOutput orDefault(@Nil RecipeOutput rout) {
		if(rout == null) return NONE;
		return rout;
	}
	
	//Arithmetic methods
	/**
	 * Adds two recipe outputs together
	 * @param rout the ingredient in the sum
	 * @return sum of two recipe outputs
	 */
	public default SimpleItemList add(RecipeOutput rout) {
		Object2IntOpenHashMap<@NN ItemEntry> map = new Object2IntOpenHashMap<>(getContents());
		for(ItemStack items: rout) 
			map.addTo(items.item, items.amount);
		return new SimpleItemList(map);
	}
	/**
	 * Adds two recipe outputs together
	 * @param rout the ingredient in the sum
	 * @return sum of two recipe outputs
	 */
	public default SimpleItemList add(SingleItem rout) {
		return add(rout.item(), rout.amount());
	}
	/**
	 * Adds two recipe outputs together
	 * @param item the ingredient in the sum
	 * @param amount the amount of the ingredient in the sum
	 * @return sum of two recipe outputs
	 */
	public default SimpleItemList add(ItemEntry item, int amount) {
		Object2IntOpenHashMap<@NN ItemEntry> map = new Object2IntOpenHashMap<>(getContents());
		map.addTo(item, amount);
		return new SimpleItemList(map);
	}
	/**
	 * Mutiplies this recipe output into an item stack stream
	 * @param amount mutiplier
	 * @return product
	 */
	public default Stream<@NN ItemStack> mul2stream(int amount) {
		return getContents()
			.object2IntEntrySet()
			.stream()
			.map(entry -> new ItemStack(entry.getKey(), entry.getIntValue()*amount));
	}
	/**
	 * Mutiplies this recipe output into a map entry stack stream
	 * @param amount mutiplier
	 * @return product of the recipe output by the amount
	 */
	public default Stream<Object2IntMap.Entry<@NN ItemEntry>> mul2entrystream(int amount) {
		return getContents()
			.object2IntEntrySet()
			.stream()
			.map(entry -> new AbstractObject2IntMap.BasicEntry<>(entry.getKey(), entry.getIntValue()*amount));
	}
	/**
	 * Mutiplies this recipe output into an item list
	 * @param amount mutiplier
	 * @return product
	 */
	public default SimpleItemList mul(int amount) {
		return mul2stream(amount).collect(ItemLists.collectToItemList());
	}
	/**
	 * Mutiplies this recipe output into a map
	 * @param <M> type of the map
	 * @param amount mutiplier
	 * @param map produces maps
	 * @return product of the recipe output by an amount
	 */
	@SuppressWarnings("null")
	public default <M extends Object2IntMap<ItemEntry>> M mul2map(int amount, Supplier<M> map) {
		return mul2entrystream(amount).collect(Collects.collectToIntMap(map));
	}
	@SuppressWarnings("null")
	@Override
	default @NN Iterator<ItemStack> iterator() {
		return Iterators.transform(getContents().object2IntEntrySet().iterator(), RecipeOutput::entry2stack);
	}
	
	/**
	 * Converts a map entry into an item stack
	 * @param entry map entry
	 * @return item stack
	 */
	public static ItemStack entry2stack(Entry<@NN ItemEntry> entry){
		return new ItemStack(entry.getKey(), entry.getIntValue());
	}
	
	/**
	 * @return pretty-printed item list
	 */
	public @NN default String prettyPrint() {
		Object2IntMap<ItemEntry> contents = getContents();
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for(Entry<ItemEntry> ir: contents.object2IntEntrySet()) {
			if(i > 0) sb.append("\n");
			i++;
			sb.append(ItemStack.toString(ir));
		}
		return sb.toString();
	}
}
