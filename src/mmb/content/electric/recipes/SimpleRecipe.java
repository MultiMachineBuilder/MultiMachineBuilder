/**
 * 
 */
package mmb.content.electric.recipes;

import mmb.annotations.NN;
import mmb.engine.recipe.Recipe;
import mmb.engine.recipe.SingleItem;

/**
 * A recipe with single input
 * @author oskar
 * @param <T> type of the recipe
 */
public interface SimpleRecipe<@NN T extends SimpleRecipe<T>> extends Recipe<T> {
	@Override
	public SimpleRecipeGroup<T> group();
	@Override
	SingleItem inputs();
	
}
