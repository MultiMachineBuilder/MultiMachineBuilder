package mmb.engine.recipe2;

import mmb.annotations.Nil;
import mmb.content.electric.VoltageTier;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;
import monniasza.collects.Identifiable;

/**
 * The full definition of any non-grid recipe
 * @param inputs input items
 * @param catalyst the catalyst required. Must match exactly
 * @param craftWord the craft-word. Must match exactly
 * @param craftCode the craft-code. Must match exactly
 * @param voltage the voltage tier. The machine tier must be equal or greater.
 * @param out output items.
 */
public record ProcessingRecipeData(
		ItemList inputs,
		@Nil ItemEntry catalyst,
		@Nil String craftWord,
		int craftCode,
		VoltageTier voltage,
		RecipeOutput out
) implements Identifiable<RecipeCriterion>{
	@Override
	public RecipeCriterion id() {
		return new RecipeCriterion(inputs.items(), catalyst, craftWord, craftCode);
	}
}
