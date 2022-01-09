/**
 * 
 */
package mmb.WORLD.crafting;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.ainslec.picocog.PicoWriter;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import mmb.WORLD.block.Drop;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 * Represents a recipe output.
 * All implementations of this interface must be immutable, and may have builders.
 */
public interface RecipeOutput extends Drop {
	@Override
	default boolean drop(@Nullable InventoryWriter inv, World map, int x, int y) {
		@Nonnull InventoryWriter dropper = map.createDropper(x, y);
		InventoryWriter priority = (inv == null)? dropper :new InventoryWriter.Priority(inv, dropper);
		produceResults(priority, 1);
		return true;
	}
	/**
	 * Produces {@code amount} units of this recipe output
	 * @param tgt inventory to move items to
	 * @param amount number of items
	 */
	public void produceResults(InventoryWriter tgt, int amount);
	/**
	 * Produces one unit of recipe output
	 * @param tgt
	 */
	public default void produceResults(InventoryWriter tgt) {
		produceResults(tgt, 1);
	}
	/**
	 * Represents this recipe output as text
	 * @param out
	 */
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
	@Nonnull public Object2IntMap<ItemEntry> getContents();
	
	/**
	 * Checks if the item list contains the selected item
	 * @param entry item to check
	 * @return does this item list contain selected item?
	 */
	public boolean contains(ItemEntry entry);
	
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
		public boolean contains(ItemEntry entry) {
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
}
