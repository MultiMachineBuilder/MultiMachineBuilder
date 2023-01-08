/**
 * 
 */
package mmb.engine.craft;

import java.awt.Component;

import mmb.NN;
import mmb.Nil;
import mmb.content.electric.VoltageTier;
import mmb.engine.chance.Chance;
import mmb.engine.inv.Inventory;
import mmb.engine.item.ItemEntry;
import mmb.menu.world.craft.RecipeView;

/**
 * @author oskar
 * Represents a recipe
 * @param <T> the type of recipe
 */
public interface Recipe<T extends Recipe<T>>{
	
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
		
	//CRAFT methods
	/**
	 * Crafts at most {@code amount} units, within ingredient limits of {@code src} inventory and capacity limits of {@code tgt} inventory
	 * @param src inventory with source items
	 * @param tgt inventory to which to place items
	 * @param amount number of recipes to craft.
	 * @return amount of recipes crafted
	 */
	public default int craft(Inventory src, Inventory tgt, int amount) {
		return Craftings.transact(inputs(), src, output(), tgt, amount);
	}
	/**
	 * Crafts at most one unit, within ingredient limits of {@code src} inventory and capacity limits of {@code tgt} inventory
	 * @param src inventory with source items
	 * @param tgt inventory to which to place items
	 * @return were items crafted
	 */
	public default boolean craft(Inventory src, Inventory tgt) {
		return craft(src, tgt, 1) == 1;
	}
	
	//CONTENTS
	/** @return the recipe output of a single craft */
	@NN public RecipeOutput output();
	/**
	 * @return the required input items
	 * @implNote the returned value should be read only
	 */
	@NN public RecipeOutput inputs();
	/** @return the catalyst. If the method returns {@code null}, it means there is no catalyst */
	@Nil public ItemEntry catalyst();
	/**
	 * @return the parent recipe group
	 * @throws UnsupportedOperationException if the implementation is a stencil, or something similar (optional operation)
	 */
	@NN public RecipeGroup<T> group();
	/**
	 * @return randomly chanced items
	 */
	@NN public Chance luck();
	/**
	 * @return amount of electricity required for the recipe
	 */
	public double energy();
	/**
	 * @return voltage required for the recipe
	 */
	@NN public VoltageTier voltTier();
	/**
	 * @return this object
	 * @apiNote Used to enforce type safety in recipes
	 */
	@NN public T that();
	/** @return the AWT or Swing component for this recipe */
	@NN public default Component createComponent() {
		RecipeView<T> rv = group().createView();
		rv.set(that());
		return rv;
	}
	
	@NN public static RecipeOutput out(@Nil Recipe<?> recipe) {
		if(recipe == null) return RecipeOutput.NONE;
		return recipe.output();
	}
	@NN public static RecipeOutput in(@Nil Recipe<?> recipe) {
		if(recipe == null) return RecipeOutput.NONE;
		return recipe.inputs();
	}
}
