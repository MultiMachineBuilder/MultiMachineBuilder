/**
 * 
 */
package mmb.WORLD.crafting;

import org.ainslec.picocog.PicoWriter;

/**
 * @author oskar
 *
 */
public class GaussianRandomOutput extends RandomizerOutput {

	/**
	 * 
	 */
	public GaussianRandomOutput(double avg, RecipeOutput recipe) {
		super(recipe, 0.0, avg, Double.POSITIVE_INFINITY);
	}

	@Override
	public void represent(PicoWriter out) {
		out.writeln(avgValue + " Gaussian random:");
		out.indentRight();
		getOutput().represent(out);
		out.indentLeft();
	}

	@Override
	public double randomize(int nthrows) {
		return Math.abs(random.nextGaussian())*nthrows*avgValue;
	}
	public void setAverage(double avg) {
		avgValue = avg;
	}
	public double getAverage() {
		return avgValue;
	}

}
