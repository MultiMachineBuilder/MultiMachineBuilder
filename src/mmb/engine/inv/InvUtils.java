/**
 * 
 */
package mmb.engine.inv;

import mmb.Nil;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.item.ItemEntry;
import monniasza.collects.Collects;

/**
 * A set of utilities for inventories
 * @author oskar
 */
public class InvUtils {
	public static @Nil ItemEntry onlyItem(RecipeOutput rout) {
		if(rout.items().size() != 1) return null;
		ItemStack entry = Collects.first(rout);
		if(entry.amount > 1) return null;
		if(entry.amount == 0) return null;
		return entry.item;
	}
	public static @Nil ItemStack onlyItemStack(RecipeOutput rout) {
		if(rout.items().size() != 1) return null;
		ItemStack entry = Collects.first(rout);
		if(entry.amount == 0) return null;
		return entry;
	}
	public static @Nil ItemStack onlyItemStackUpTo(RecipeOutput rout, int amount) {
		if(rout.items().size() != 1) return null;
		ItemStack entry = Collects.first(rout);
		if(entry.amount > amount) return null;
		if(entry.amount == 0) return null;
		return entry;
	}
}
