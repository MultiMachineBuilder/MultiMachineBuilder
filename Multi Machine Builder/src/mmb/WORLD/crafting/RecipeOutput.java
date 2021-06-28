/**
 * 
 */
package mmb.WORLD.crafting;

import org.ainslec.picocog.PicoWriter;
import org.joml.Vector3d;

import mmb.WORLD.block.Drop;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 *
 */
public interface RecipeOutput extends Drop {
	@Override
	default boolean drop(InventoryWriter inv, BlockMap map, int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * Produces {@code amount} units of this recipe output
	 * @param tgt
	 * @param amount
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
	 * X coordinate: min
	 * Y coordinate: avg
	 * Z coordinate: max
	 * @param out target volume output
	 * @return maximum volume
	 */
	public void calcVolumes(Vector3d out);
}
