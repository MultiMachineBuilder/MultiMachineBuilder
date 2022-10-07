/**
 * 
 */
package mmb.world.chance;

import java.util.Collections;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.ainslec.picocog.PicoWriter;

import mmb.world.inventory.io.InventoryWriter;
import mmb.world.items.ItemEntry;
import mmb.world.worlds.world.World;

/**
 * @author oskar
 * Represents a processing result with random chance
 */
public interface Chance {
	/**
	 * Drops an item
	 * @param inv inventory, to which item will be dropped. If it is null, the items are dropped into world
	 * @param map world (optional when using an inventory)
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Were items dropped?
	 * @throws IllegalArgumentException when both world and inventory are absent
	 */
	public boolean drop(@Nullable InventoryWriter inv, World map, int x, int y);
	
	/**
	 * Produces {@code amount} units of this chance
	 * @param tgt inventory to move items to
	 * @param amount number of items
	 */
	public void produceResults(@Nonnull InventoryWriter tgt, int amount);
	/**
	 * Produces one unit of chance
	 * @param tgt
	 */
	public default void produceResults(@Nonnull InventoryWriter tgt) {
		produceResults(tgt, 1);
	}
	/**
	 * Represents this recipe output as text
	 * @param out
	 */
	public void represent(@Nonnull PicoWriter out);
	
	/**
	 * @param item
	 * @return can this chance produce given item?
	 */
	public boolean contains(ItemEntry item);

	public static boolean tryDrop(ItemEntry ent, @Nullable InventoryWriter i, World map, int x, int y) {
		int inserted = 0;
		if(i != null) inserted = i.write(ent);
		if(inserted == 0) map.dropItem(ent, x, y);
		return true;
	}
	
	/**
	 * A chance that drops nothing
	 */
	@Nonnull public static final Chance NONE = new Chance() {

		@Override
		public boolean drop(InventoryWriter inv, World map, int x, int y) {
			return true;
		}

		@Override
		public void produceResults(InventoryWriter tgt, int amount) {
			//does not produce anything
		}

		@Override
		public void represent(PicoWriter out) {
			out.write("(none)");
		}

		@Override
		public boolean contains(ItemEntry item) {
			return false;
		}

		@Override
		public Set<ItemEntry> items() {
			return Collections.emptySet();
		}
	};
	
	/**
	 * @return unique items in this random chance
	 */
	@Nonnull public Set<@Nonnull ItemEntry> items();
}
