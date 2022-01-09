/**
 * 
 */
package mmb.WORLD.recipe_old;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mmb.WORLD.crafting.Recipe;
import mmb.WORLD.crafting.RecipeGroup;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.selfset.SelfSet;

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

	@Override
	public void getRecipes(Inventory inv, Collection<Recipe> out) {
		for(ItemRecord record: inv) {
			if(recipes.containsKey(record.item())) {
				out.add(new BasicRecipe(recipes.get(record.item()), record.item()));
			}
		}
	}
}
