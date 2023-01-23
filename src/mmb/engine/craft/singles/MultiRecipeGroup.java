/**
 * 
 */
package mmb.engine.craft.singles;

import java.util.Set;

import mmb.Nil;
import mmb.engine.craft.Recipe;
import mmb.engine.craft.RecipeGroup;
import mmb.engine.item.ItemEntry;
import monniasza.collects.Collects;

/**
 *
 * @author oskar
 *
 */
public interface MultiRecipeGroup<T extends Recipe<?>> extends RecipeGroup<T> {
	/**
	 * Finds recipe(s) which meets criteria
	 * @param in required input item
	 * @return a matching recipe or null if not found
	 */
	@Nil public Set<T> findPlausible(ItemEntry in);
	/**
	 * Finds a single recipe which meets criteria
	 * @param catalyst required catalyst (null if none)
	 * @param in required inputs
	 * @return a matching recipe or null if not found
	 */
	@Nil public default T findExact(Set<ItemEntry> in, @Nil ItemEntry catalyst) {
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
