/**
 * 
 */
package mmb.WORLD.recipe_old;

import java.util.Collections;
import java.util.Set;

import mmb.WORLD.crafting.Recipe;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.storage.SingleItemInventory;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 *
 */
public class BasicRecipe implements Recipe {
	
	public BasicRecipe(RecipeOutput output, ItemEntry item) {
		super();
		this.output = output;
		this.item = item;
	}

	private final RecipeOutput output;
	private final ItemEntry item;
	
	double volume = Double.NaN;
	
	private double getVolume() {
		if(Double.isFinite(volume)) 
			volume = output.outVolume();
		return volume;
	}
	@Override
	public int maxCraftable(Inventory src, int amount) {
		double vol = getVolume();
		double maxVol = vol * amount;
		double remainingVolume = src.iremainVolume();
		//If max volume < space, then craft all
		if(maxVol < remainingVolume) return amount;
		//Else craft less
		return (int) ((remainingVolume/maxVol)*amount);
	}

	@Override
	public int craft(Inventory src, Inventory tgt, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RecipeOutput output() {
		return output;
	}
	@Override
	public RecipeOutput inputs() {
		return item;
	}
	@Override
	public Set<ItemEntry> id() {
		return Collections.singleton(item);
	}

}
