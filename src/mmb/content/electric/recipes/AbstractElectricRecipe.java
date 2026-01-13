/**
 * 
 */
package mmb.content.electric.recipes;

import mmb.annotations.NN;
import mmb.content.electric.VoltageTier;
import mmb.engine.chance.Chance;
import mmb.engine.recipe.Recipe;
import mmb.engine.recipe.ItemList;

/**
 * A shared implementation of electric recipes
 * @author oskar
 * @param <T> type of the recipe
 */
public abstract class AbstractElectricRecipe<@NN T extends Recipe<T>> implements Recipe<T> {
	/** Energy required for completion in joules */
	public final double energy;
	/** Voltage tier required for this recipe */
	public final VoltageTier voltage;
	/** Deterministic output of this recipe */
	public final ItemList output;
	/** Randomized output of this recipe */
	public final Chance luck;
	
	/**
	 * Base constructor for recipes
	 * @param energy energy required for completion in joules
	 * @param voltage voltage tier required for this recipe
	 * @param output deterministic output of this recipe
	 * @param luck randomized output of this recipe
	 */
	protected AbstractElectricRecipe(double energy, VoltageTier voltage, ItemList output, Chance luck) {
		this.energy = energy;
		this.voltage = voltage;
		this.output = output;
		this.luck = luck;
	}

	@Override
	public final ItemList output() {
		return output;
	}

	@Override
	public final Chance luck() {
		return luck;
	}

	@Override
	public final double energy() {
		return energy;
	}

	@Override
	public final VoltageTier voltTier() {
		return voltage;
	}
}
