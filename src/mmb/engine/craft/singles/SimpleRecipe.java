/**
 * 
 */
package mmb.engine.craft.singles;

import mmb.engine.craft.Recipe;
import mmb.engine.craft.SingleItem;
import mmb.engine.inv.Inventory;

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
