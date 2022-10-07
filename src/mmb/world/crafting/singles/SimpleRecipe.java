/**
 * 
 */
package mmb.world.crafting.singles;

import mmb.world.crafting.Recipe;
import mmb.world.crafting.SingleItem;

/**
 * @author oskar
 * @param <T> type of recipes
 * A recipe with single input
 */
public interface SimpleRecipe<T extends SimpleRecipe<T>> extends Recipe<T> {

	@Override
	public SimpleRecipeGroup<T> group();

	@Override
	SingleItem inputs();
	
}
