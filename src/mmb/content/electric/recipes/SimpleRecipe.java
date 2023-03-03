/**
 * 
 */
package mmb.content.electric.recipes;

import mmb.NN;
import mmb.engine.craft.Recipe;
import mmb.engine.craft.SingleItem;

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
