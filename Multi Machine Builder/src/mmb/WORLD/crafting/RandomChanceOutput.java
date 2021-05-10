/**
 * 
 */
package mmb.WORLD.crafting;

import org.ainslec.picocog.PicoWriter;

/**
 * @author oskar
 *
 */
public class RandomChanceOutput extends RandomizerOutput {
	private double chance;
	private RandomChanceOutput(RecipeOutput out, double chance) {
		super(out, 0, chance, 1);
	}

	@Override
	public double randomize(int nthrows) {
		int result = 0;
		for(int i = 0; i < nthrows; i++) {
			if(random.nextDouble() < getChance()) result++;
		}
		return result;
	}

	@Override
	public void represent(PicoWriter out) {
		out.writeln((getChance()*100)+"% chance:");
		out.indentRight();
		getOutput().represent(out);
		out.indentLeft();
	}

	/**
	 * @return the chance
	 */
	private double getChance() {
		return chance;
	}

	/**
	 * @param chance the chance to set
	 */
	private void setChance(double chance) {
		this.chance = chance;
	}
}
