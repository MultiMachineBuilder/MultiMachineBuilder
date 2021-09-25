/**
 * 
 */
package mmb.WORLD.crafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.ainslec.picocog.PicoWriter;

import mmb.WORLD.block.Drop;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
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
	
	/** Represents a recipe output that does nothing */
	public static final RecipeOutput NONE = new RecipeOutput() {

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
		
	};
}
