/**
 * 
 */
package mmb.WORLD.crafting;

import org.ainslec.picocog.PicoWriter;
import mmb.WORLD.inventory.io.InventoryWriter;

/**
 * @author oskar
 *
 */
public class RecipeOutputMutiple implements RecipeOutput {
	private RecipeOutput toMutiply;
	private int mutiplier;
	@Override
	public void produceResults(InventoryWriter tgt, int amount) {
		toMutiply.produceResults(tgt, mutiplier * amount);
	}
	public RecipeOutputMutiple(RecipeOutput toMutiply, int mutiplier) {
		this.toMutiply = toMutiply;
		this.mutiplier = mutiplier;
	}
	@Override
	public void represent(PicoWriter out) {
		out.writeln(mutiplier+"×:");
		out.indentRight();
		toMutiply.represent(out);
		out.indentLeft();
	}
	@Override
	public double outVolume() {
		return toMutiply.outVolume()*mutiplier;
	}
	/**
	 * @return the toMutiply
	 */
	public RecipeOutput getToMutiply() {
		return toMutiply;
	}
	/**
	 * @param toMutiply the toMutiply to set
	 */
	public void setToMutiply(RecipeOutput toMutiply) {
		this.toMutiply = toMutiply;
	}
	/**
	 * @return the mutiplier
	 */
	public int getMutiplier() {
		return mutiplier;
	}
	/**
	 * @param mutiplier the mutiplier to set
	 */
	public void setMutiplier(int mutiplier) {
		this.mutiplier = mutiplier;
	}

}
