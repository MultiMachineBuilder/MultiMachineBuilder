/**
 * 
 */
package mmb.WORLD.crafting;

import java.util.Random;

import org.joml.Vector3d;

import mmb.WORLD.inventory.Inventory;

/**
 * @author oskar
 *
 */
public abstract class RandomizerOutput implements RecipeOutput {
	private RecipeOutput output;
	protected double minValue;
	protected double avgValue;
	protected double maxValue;
	protected RandomizerOutput(RecipeOutput output, double minValue, double avgValue, double maxValue) {
		this.output = output;
		this.minValue = minValue;
		this.avgValue = avgValue;
		this.maxValue = maxValue;
	}
	protected static final Random random = new Random();
	@Override
	public void produceResults(Inventory tgt, int amount) {
		double mul = randomize(amount);
		output.produceResults(tgt, (int) Math.round(mul));
	}
	abstract public double randomize(int nthrows);
	@Override
	public void calcVolumes(Vector3d out) {
		Vector3d results = new Vector3d();
		output.calcVolumes(results);
		results.mul(minValue, avgValue, maxValue);
	}
	/**
	 * @return the output
	 */
	public RecipeOutput getOutput() {
		return output;
	}
	/**
	 * @param output the output to set
	 */
	public void setOutput(RecipeOutput output) {
		this.output = output;
	}

	public static double exprandom(int thrOws) {
		int exp = (2 * thrOws)-1;
		double randomized = random.nextDouble();
		randomized -= 0.5;
		randomized *= 2;
		randomized = Math.pow(randomized, 1.0/exp);
		return randomized;
	}
	public static double aexprandom(int thrOws) {
		int exp = (2 * thrOws)-1;
		double randomized = random.nextDouble();
		randomized = Math.pow(randomized, 1.0/exp);
		return randomized;
	}
	public static double exprandom(double min, double max, int thrOws) {
		double result = aexprandom(thrOws);
		return (max * result)-(min * result)+min;
	}
	
}
