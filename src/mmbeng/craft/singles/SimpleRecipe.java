/**
 * 
 */
package mmbeng.craft.singles;

import mmbeng.craft.Recipe;
import mmbeng.craft.SingleItem;

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
