/**
 * 
 */
package mmb.WORLD.crafting.recipes;

import java.util.Collection;

import mmb.WORLD.inventory.Inventory;

/**
 * @author oskar
 *
 */
public interface RecipeGroup {
	/**
	 * Lists all valid recipes achievable with given inventory. This method is faster, but inventory must match the recipe exactly
	 * @param inv
	 * @return
	 */
	public Recipe getRecipe(Inventory inv);
}
