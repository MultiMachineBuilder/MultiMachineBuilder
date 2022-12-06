/**
 * 
 */
package mmbeng.craft.rgroups;

import javax.annotation.Nonnull;

import mmbeng.chance.Chance;
import mmbeng.craft.Recipe;
import mmbeng.craft.RecipeOutput;
import mmbgame.electric.VoltageTier;

/**
 * @author oskar
 * @param <T> type of the recipe
 * A shared implementation of electric recipes
 */
public abstract class BaseElectricRecipe<T extends Recipe<T>> implements Recipe<T> {
	/** Energy required for completion in joules */
	public final double energy;
	/** Voltage tier required for this recipe */
	@Nonnull public final VoltageTier voltage;
	/** Deterministic output of this recipe */
	@Nonnull public final RecipeOutput output;
	/** Randomized output of this recipe */
	@Nonnull public final Chance luck;
	
	/**
	 * Base constructor for recipes
	 * @param energy energy required for completion in joules
	 * @param voltage voltage tier required for this recipe
	 * @param output deterministic output of this recipe
	 * @param luck randomized output of this recipe
	 */
	protected BaseElectricRecipe(double energy, VoltageTier voltage, RecipeOutput output, Chance luck) {
		this.energy = energy;
		this.voltage = voltage;
		this.output = output;
		this.luck = luck;
	}

	
	@Override
	public final RecipeOutput output() {
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