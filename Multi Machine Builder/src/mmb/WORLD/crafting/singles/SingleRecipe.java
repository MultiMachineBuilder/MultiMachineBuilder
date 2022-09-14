/**
 * 
 */
package mmb.WORLD.crafting.singles;

import mmb.WORLD.crafting.Recipe;
import mmb.WORLD.crafting.SingleItem;

/**
 * @author oskar
 * @param <T> type of recipes
 * A recipe with single input
 */
public interface SingleRecipe<T extends SingleRecipe<T>> extends Recipe<T> {

	@Override
	public SingleItem output();

	@Override
	public SingleRecipeGroup<T> group();
	
}
