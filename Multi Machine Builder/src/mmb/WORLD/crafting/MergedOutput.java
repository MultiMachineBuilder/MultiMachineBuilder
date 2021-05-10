/**
 * 
 */
package mmb.WORLD.crafting;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.ainslec.picocog.PicoWriter;
import org.joml.Vector3d;

import mmb.WORLD.inventory.Inventory;

/**
 * @author oskar
 *
 */
public class MergedOutput implements RecipeOutput{
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
	public MergedOutput(RecipeOutput... outputs) {
		out = Arrays.copyOf(outputs, outputs.length);
	}
	public MergedOutput(Collection<? extends RecipeOutput> c) {
		out = c.toArray(new RecipeOutput[c.size()]);
	}
	
	@Override
	public void produceResults(Inventory tgt, int amount) {
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
	public void calcVolumes(Vector3d out) {
		for(RecipeOutput rout: this.out) {
			rout.calcVolumes(out);
		}
	}

}
