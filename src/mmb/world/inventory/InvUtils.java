/**
 * 
 */
package mmb.world.inventory;

import javax.annotation.Nullable;

import mmb.world.crafting.RecipeOutput;
import mmb.world.item.ItemEntry;
import monniasza.collects.Collects;

/**
 * A set of utilities for inventories
 * @author oskar
 */
public class InvUtils {
	public static @Nullable ItemEntry onlyItem(RecipeOutput rout) {
		if(rout.items().size() != 1) return null;
		ItemStack entry = Collects.first(rout);
		if(entry.amount > 1) return null;
		if(entry.amount == 0) return null;
		return entry.item;
	}
	public static @Nullable ItemStack onlyItemStack(RecipeOutput rout) {
		if(rout.items().size() != 1) return null;
		ItemStack entry = Collects.first(rout);
		if(entry.amount == 0) return null;
		return entry;
	}
	public static @Nullable ItemStack onlyItemStackUpTo(RecipeOutput rout, int amount) {
		if(rout.items().size() != 1) return null;
		ItemStack entry = Collects.first(rout);
		if(entry.amount > amount) return null;
		if(entry.amount == 0) return null;
		return entry;
	}
}
