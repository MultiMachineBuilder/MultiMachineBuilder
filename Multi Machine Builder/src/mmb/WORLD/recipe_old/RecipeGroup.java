/**
 * 
 */
package mmb.WORLD.recipe_old;

import java.util.Collection;

import mmb.WORLD.inventory.Inventory;

/**
 * @author oskar
 *
 */
public interface RecipeGroup {
	/**
	 * Lists primary recipe achievable with given inventory. This method is faster, but inventory must match the recipe exactly
	 * @param inv
	 * @return
	 */
	public Recipe getRecipe(Inventory inv);
	
	/**
	 * Lists all avaliable recipes achievable with given inventory. This method is slower.
	 * @param inv inventory to list recipes from
	 * @param out the collection, to which write list of recipes
	 */
	public void getRecipes(Inventory inv, Collection<Recipe> out);
}
