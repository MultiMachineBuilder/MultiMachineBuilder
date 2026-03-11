package mmb.engine.recipe2.processing;

import java.util.Set;

import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;

/**
 * Uniquely identifies a processing recipe
 * @param recipeInputItems distinct recipe inputs
 * @param catalyst all catalysts
 * @param craftWord the "craft-word"
 * @param craftCode the crafting code
 */
public record ProcessingRecipeCriterion(
		Set<ItemEntry> recipeInputItems,
		@Nil ItemEntry catalyst,
		String craftWord,
		int craftCode
) {

}
