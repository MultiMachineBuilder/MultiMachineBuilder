/**
 * 
 */
package mmb.content.electric.recipes;

import mmb.NN;
import mmb.Nil;
import mmb.engine.craft.RecipeGroup;
import mmb.engine.item.ItemEntry;

/**
 * @author oskar
 * A recipe group optimized for single-item recipes.
 * @param <T> type of recipes
 */
public interface SimpleRecipeGroup<T extends SimpleRecipe<@NN T>> extends RecipeGroup<@NN T> {
	/**
	 * Finds a recipe which meets criteria
	 * @param catalyst required catalyst (null if none)
	 * @param in required input item
	 * @return a matching recipe or null if not found
	 */
	@Nil public T findRecipe(@Nil ItemEntry catalyst, ItemEntry in);
}
