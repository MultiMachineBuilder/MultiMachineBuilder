/**
 * 
 */
package mmb.engine.chance;

import java.util.Collections;
import java.util.Set;

import org.ainslec.picocog.PicoWriter;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.ItemEntry;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.worlds.world.World;

/**
 * A potentially randomized item drop
 * @author oskar
 */
public interface Chance {
	//Translation
	@NN static final String str_CHANCE = GlobalSettings.$res("chance-random");
	@NN static final String str_CHANCELSE = GlobalSettings.$res("chance-randomelse");
	@NN static final String str_ELSE = GlobalSettings.$res("chance-else");
	
	/**
	 * Drops an item
	 * @param inv inventory, to which item will be dropped. If it is null, the items are dropped into world
	 * @param map world (optional when using an inventory)
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Were items dropped?
	 * @throws IllegalArgumentException when both world and inventory are absent
	 */
	public boolean drop(@Nil InventoryWriter inv, World map, int x, int y);
	
	/**
	 * Produces {@code amount} units of this chance
	 * @param tgt inventory to move items to
	 * @param amount number of items
	 */
	public void produceResults(InventoryWriter tgt, int amount);
	/**
	 * Produces one unit of chance
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
	 * @param item
	 * @return can this chance produce given item?
	 */
	public boolean contains(ItemEntry item);

	/**
	 * A simple implementation of item drops
	 * @param ent item entry
	 * @param i inventory writer
	 * @param map block map
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return were items dropped
	 */
	public static boolean tryDrop(ItemEntry ent, @Nil InventoryWriter i, @Nil World map, int x, int y) {
		int inserted = 0;
		if(i != null) inserted = i.insert(ent);
		if(inserted == 0 && map != null) map.dropItem(ent, x, y);
		return true;
	}
	
	/**
	 * A chance that drops nothing
	 */
	@NN public static final Chance NONE = new Chance() {

		@Override
		public boolean drop(@Nil InventoryWriter inv, World map, int x, int y) {
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
	@NN public Set<@NN ItemEntry> items();
}
