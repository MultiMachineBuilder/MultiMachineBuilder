/**
 * 
 */
package mmb.WORLD.recipe_old;

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
		SingleItemInventory tmp = new SingleItemInventory();
		tmp.setContents(item);
		items = tmp.readOnly();
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

	private final Inventory items;
	@Override
	public RecipeOutput output() {
		return output;
	}
	@Override
	public Inventory inputs() {
		return items;
	}

}
