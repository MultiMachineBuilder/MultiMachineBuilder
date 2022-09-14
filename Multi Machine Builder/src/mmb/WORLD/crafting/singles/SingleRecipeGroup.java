/**
 * 
 */
package mmb.WORLD.crafting.singles;

import mmb.WORLD.crafting.RecipeGroup;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 * A recipe group optimized for single-item recipes.
 * @param <T> type of recipes
 */
public interface SingleRecipeGroup<T extends SingleRecipe<?>> extends RecipeGroup<T> {
	/**
	 * Finds a recipe which meets criteria
	 * @param catalyst
	 * @param in
	 * @return a matching recipe or null if not found
	 */
	public T findRecipe(ItemEntry catalyst, ItemEntry in);
}
