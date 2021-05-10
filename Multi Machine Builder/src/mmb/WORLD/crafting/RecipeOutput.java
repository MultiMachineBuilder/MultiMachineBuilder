/**
 * 
 */
package mmb.WORLD.crafting;

import org.ainslec.picocog.PicoWriter;
import org.joml.Vector3d;

import mmb.WORLD.inventory.Inventory;

/**
 * @author oskar
 *
 */
public interface RecipeOutput {
	/**
	 * Produces {@code amount} units of this recipe output
	 * @param tgt
	 * @param amount
	 */
	public void produceResults(Inventory tgt, int amount);
	/**
	 * Produces one unit of recipe output
	 * @param tgt
	 */
	public default void produceResults(Inventory tgt) {
		produceResults(tgt, 1);
	}
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
