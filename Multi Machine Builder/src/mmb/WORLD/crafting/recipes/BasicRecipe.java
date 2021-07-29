/**
 * 
 */
package mmb.WORLD.crafting.recipes;

import javax.annotation.Nonnull;

import org.joml.Vector3d;

import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.item.ItemType;
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
	
	Vector3d volumes;
	
	@SuppressWarnings("null")
	private @Nonnull Vector3d getVolumes() {
		if(volumes == null) {
			volumes = new Vector3d();
			output.calcVolumes(volumes);
		}
		return volumes;
	}
	@Override
	public int maxCraftable(Inventory src, int amount) {
		Vector3d vols = getVolumes();
		double maxVol = vols.z * amount;
		double remainingVolume = src.iremainVolume();
		//If max volume < space, then craft all
		if(maxVol < remainingVolume) return amount;
		//Else craft less
		return (int) ((remainingVolume/maxVol)*amount);
	}

	@Override
	public int consume(Inventory inv, int amount) {
		return inv.extract(item, amount);
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

}
