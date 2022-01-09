/**
 * 
 */
package mmb.WORLD.crafting;

import java.util.Set;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 * Represents a recipe
 */
public interface Recipe extends Identifiable<Set<ItemEntry>> {
	
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
	
	/**
	 * Crafts items according to a recipe
	 * @param recipeOutput items to be consumed
	 * @param rout items to be crafted
	 * @param tgt target inventory
	 * @param src source inventory
	 * @param amount number of recipes to craft
	 * @return number of recipes crafted
	 */
	public static int transact(RecipeOutput recipeOutput, RecipeOutput rout, Inventory tgt, Inventory src, int amount) {
		if(!tgt.exists()) return 0;
		if(!tgt.canInsert()) return 0;
		if(!src.exists()) return 0;
		if(!src.canExtract()) return 0;
		double volume = rout.outVolume(); //Per unit
		int fitsInOut = (int) (tgt.iremainVolume()/volume);
		int ingrsInIn = Inventory.howManyTimesThisContainsThat(tgt, recipeOutput);
		int craftable = Math.min(fitsInOut, ingrsInIn);
		//Remove ingredients from input
		for(Entry<ItemEntry> irecord: recipeOutput.getContents().object2IntEntrySet()) {
			int amt = irecord.getIntValue()*craftable;
			src.extract(irecord.getKey(), amt);
		}
		//Insert output
		rout.produceResults(tgt.createWriter(), craftable);
		return craftable;
	}
}
