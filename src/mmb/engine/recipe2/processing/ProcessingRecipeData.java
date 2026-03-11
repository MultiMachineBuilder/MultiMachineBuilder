package mmb.engine.recipe2.processing;

import mmb.annotations.Nil;
import mmb.content.electric.VoltageTier;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;
import mmb.engine.recipe2.Recipe;
import mmb.engine.recipe2.RecipeOutput;
import mmb.engine.recipe2.RecipeSetup;
import monniasza.collects.Identifiable;

/**
 * The entire list of inputs and outputs for any recipe. It is also the definition of a processing recipe
 * @param inputs input items
 * @param catalyst the catalyst required. Must match exactly
 * @param craftWord the craft-word. Must match exactly
 * @param craftCode the craft-code. Must match exactly
 * @param voltage the voltage tier. The machine tier must be equal or greater.
 * @param out output items.
 * @param energyCost energy required to complete the recipe
 * @param energyReward energy granted after completion
 */
public record ProcessingRecipeData(
		ItemList inputs,
		@Nil ItemEntry catalyst,
		String craftWord,
		int craftCode,
		VoltageTier voltage,
		RecipeOutput out,
		double energyCost,
		double energyReward
) implements Identifiable<ProcessingRecipeCriterion>, Recipe<RecipeSetup>{
	@Override
	public ProcessingRecipeCriterion id() {
		return new ProcessingRecipeCriterion(inputs.items(), catalyst, craftWord, craftCode);
	}

	@Override
	public ProcessingRecipeData getProcessData() {
		return this;
	}
	
	@Override
	public RecipeSetup getConfiguration() {
		return new RecipeSetup(catalyst, craftWord, craftCode);
	}
}
