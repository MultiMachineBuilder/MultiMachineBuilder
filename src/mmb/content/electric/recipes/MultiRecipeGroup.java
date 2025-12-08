/**
 * 
 */
package mmb.content.electric.recipes;

import java.util.Set;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.Recipe;
import mmb.engine.recipe.RecipeGroup;
import monniasza.collects.Collects;

/**
 * An interface to a multi-item recipe group
 * @author oskar
 * @param <T> type of recipes
 */
public interface MultiRecipeGroup<@NN T extends Recipe<?>> extends RecipeGroup<T> {
	/**
	 * Finds recipe(s) which meets criteria
	 * @param in required input item
	 * @return a set of matching recipes
	 */
	@Nil public Set<T> findPlausible(ItemEntry in);
	/**
	 * Finds a single recipe which meets criteria
	 * @param catalyst required catalyst (null if none)
	 * @param in required inputs
	 * @return a matching recipe or null if not found
	 */
	@Nil public default T findExact(Set<@NN ItemEntry> in, @Nil ItemEntry catalyst) {
		Set<T> candidates = findPlausible(Collects.first(in));
		if(candidates == null) return null;
		for(T candidate: candidates) {
			if(candidate.catalyst() == catalyst && candidate.inputs().items().equals(in)) return candidate;
		}
		return null;
	}
	/** @return minimum ingredients */
	public int minIngredients();
}
