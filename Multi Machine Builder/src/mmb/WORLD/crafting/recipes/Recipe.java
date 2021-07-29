/**
 * 
 */
package mmb.WORLD.crafting.recipes;

import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.Inventory;

/**
 * @author oskar
 * Represents a recipe
 */
public interface Recipe {
	
	//CONTAINS methods
	/**
	 * @param src inventory with source items
	 * @return can be this recipe crafted with unlimited output capacity and items from 
	 */
	public default boolean containsRequiredIngredients(Inventory src) {
		return maxCraftable(src, 1) == 1;
	}
	/**
	 * 
	 * @param src inventory with source items
	 * @param amount number of recipes to craft
	 * @return number of craftable recipes
	 */
	public int maxCraftable(Inventory src, int amount);
	
	//CONSUME methods
	/**
	 * Consumes maximum number of craftable items
	 * @param inv item source
	 * @return number of crafted recipe outputs
	 */
	public int consume(Inventory inv, int amount);
	/**
	 * 
	 * @param inv item source
	 * @return were items consumed to craft one recipe output?
	 */
	public default boolean consume(Inventory inv) {
		return consume(inv, 1) == 1;
	}
	
	//CRAFT methods
	/**
	 * Crafts at most {@code amount} units, within ingredient limits of {@code src} inventory and capacity limits of {@code tgt} inventory
	 * @param src inventory with source items
	 * @param tgt inventory to which to place items
	 * @param amount number of recipes to craft.
	 * @return amount of recipes crafted
	 */
	public int craft(Inventory src, Inventory tgt, int amount);
	/**
	 * Crafts at most one unit, within ingredient limits of {@code src} inventory and capacity limits of {@code tgt} inventory
	 * @param src inventory with source items
	 * @param tgt inventory to which to place items
	 * @return were items crafted
	 */
	public default boolean craft(Inventory src, Inventory tgt) {
		return craft(src, tgt, 1) == 1;
	}
	/** @return the recipe output of a single craft */
	public RecipeOutput output();
}
