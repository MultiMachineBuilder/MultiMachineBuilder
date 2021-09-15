/**
 * 
 */
package mmb.WORLD.recipe_old;

import java.util.HashMap;
import java.util.Map;

import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 * Represents a group of single-item, no electricity recipes
 */
public class SingleBasicResourceGroup implements RecipeGroup {
	public final Map<ItemEntry, RecipeOutput> recipes = new HashMap<>();

	@Override
	public Recipe getRecipe(Inventory inv) {
		for(ItemRecord record: inv) {
			if(recipes.containsKey(record.item())) {
				return new BasicRecipe(recipes.get(record.item()), record.item());
			}
		}
		return null;
	}
}
