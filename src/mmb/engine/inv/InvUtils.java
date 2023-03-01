/**
 * 
 */
package mmb.engine.inv;

import mmb.Nil;
import mmb.engine.craft.ItemStack;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.item.ItemEntry;
import monniasza.collects.Collects;

/**
 * A set of utilities for inventories
 * @author oskar
 */
public class InvUtils {
	private InvUtils() {}
	
	/**
	 * Returns the only item in an recipe output, or null if not found, ambiguous or mutiple
	 * @param rout recipe output
	 * @return an item entry or null
	 */
	public static @Nil ItemEntry onlyItem(RecipeOutput rout) {
		if(rout.items().size() != 1) return null;
		ItemStack entry = Collects.first(rout);
		if(entry.amount > 1) return null;
		if(entry.amount == 0) return null;
		return entry.item;
	}
	/**
	 * Returns the only item stack in an recipe output, or null if not found, ambiguous
	 * @param rout recipe output
	 * @return an item stack
	 */
	public static @Nil ItemStack onlyItemStack(RecipeOutput rout) {
		if(rout.items().size() != 1) return null;
		ItemStack entry = Collects.first(rout);
		if(entry.amount == 0) return null;
		return entry;
	}
	/**
	 * Returns the only item in an recipe output, or null if not found, ambiguous or above the limit
	 * @param rout recipe output
	 * @return an item entry or null
	 * @param amount maximum amount
	 */
	public static @Nil ItemStack onlyItemStackUpTo(RecipeOutput rout, int amount) {
		if(rout.items().size() != 1) return null;
		ItemStack entry = Collects.first(rout);
		if(entry.amount > amount) return null;
		if(entry.amount == 0) return null;
		return entry;
	}
}
