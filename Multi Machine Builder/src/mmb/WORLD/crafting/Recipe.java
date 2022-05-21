/**
 * 
 */
package mmb.WORLD.crafting;

import java.awt.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import mmb.WORLD.chance.Chance;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.items.ItemEntry;

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
	
	//CONTENTS
	/** @return the recipe output of a single craft */
	@Nonnull public RecipeOutput output();
	/**
	 * @return the required input items
	 * @implNote the returned value should be read only
	 */
	@Nonnull public RecipeOutput inputs();
	/** @return the catalyst. If the method returns {@code null}, it means there is no catalyst */
	@Nullable public ItemEntry catalyst();
	/**
	 * @return the parent recipe group
	 * @throws UnsupportedOperationException if the implementation is a stencil, or something similar (optional operation)
	 */
	@Nonnull public RecipeGroup group();
	@Nonnull public default Chance luck() {
		return Chance.NONE;
	}
	/**
	 * @return amount of electricity required for the recipe
	 */
	public double energy();
	/**
	 * @return voltage required for the recipe
	 */
	@Nonnull public VoltageTier voltTier();
	/**
	 * @return this object
	 * @apiNote Used to enforce type safety in recipes
	 */
	@Nonnull public T that();
	/** @return the AWT or Swing component for this recipe */
	@Nonnull public Component createComponent();
}
