/**
 * 
 */
package mmb.WORLD.crafting;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.ainslec.picocog.PicoWriter;

import mmb.WORLD.inventory.io.InventoryWriter;

/**
 * @author oskar
 *
 */
public class RecipeOutputCombined implements RecipeOutput{
	private RecipeOutput[] out;
	public List<RecipeOutput> getOutputs(){
		return Arrays.asList(out);
	}
	public void setOutput(RecipeOutput... outputs) {
		out = Arrays.copyOf(outputs, outputs.length);
	}
	public void setOutput(Collection<? extends RecipeOutput> c) {
		out = c.toArray(new RecipeOutput[c.size()]);
	}
	public void bindOutput(RecipeOutput[] outputArray) {
		out = outputArray;
	}
	/**
	 * 
	 */
	public RecipeOutputCombined(RecipeOutput... outputs) {
		out = Arrays.copyOf(outputs, outputs.length);
	}
	public RecipeOutputCombined(Collection<? extends RecipeOutput> c) {
		out = c.toArray(new RecipeOutput[c.size()]);
	}
	
	@Override
	public void produceResults(InventoryWriter tgt, int amount) {
		for(RecipeOutput rout: out) {
			rout.produceResults(tgt, amount);
		}
	}
	@Override
	public void represent(PicoWriter writer) {
		for(RecipeOutput rout: out) {
			rout.represent(writer);
		}
	}
	@Override
	public double outVolume() {
		return Arrays.stream(out).mapToDouble(i -> i.outVolume()).sum();
	}

}
