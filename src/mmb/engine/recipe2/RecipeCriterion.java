package mmb.engine.recipe2;

import java.util.Set;

import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;

/**
 * Uniquely identifies a recipe
 * @param recipeInputItems distinc recipe inputs
 * @param catalyst all catalysts
 * @param craftWord the "craft-word"
 * @param craftCode the crafting code
 */
public record RecipeCriterion(
		Set<ItemEntry> recipeInputItems,
		@Nil ItemEntry catalyst,
		@Nil String craftWord,
		int craftCode
) {

}
