/**
 * 
 */
package mmb.WORLD.crafting;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.Bag;
import org.joml.Vector3d;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import mmb.WORLD.crafting.recipes.ComplexProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.SimpleProcessingRecipeGroup;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ItemEntry;
import mmb.debug.Debugger;

/**
 * @author oskar
 * Contains crafting utilities and recipe groups
 */
public class Craftings {
	private static final Debugger debug = new Debugger("CRAFTING PROCESSOR");
	/**
	 * @param in ingredients
	 * @param out recipe output
	 * @param from source inventory
	 * @param to target inventory
	 */
	public static void transact(Bag<@Nonnull ItemEntry> in, RecipeOutput out, Inventory from, Inventory to) {
		//Count
		for(ItemEntry ent: in.uniqueSet()) {
			int amt = in.getCount(ent);
			int act = from.get(ent).amount();
			if(act < amt) {
				//debug.printl("Not enough "+ent+", got "+act+", expected "+amt);
				return;
			} //Not enough items in the source
		}
		//Calculate capacity
		Vector3d calcvec = new Vector3d();
		out.outVolume();
		double remainCapacity = to.capacity() - to.volume();
		if(calcvec.x > remainCapacity) {
			//debug.printl("Required "+calcvec.x+" capacity, got "+remainCapacity);
			return;
		} //Not enough space in the output
		//Withdraw from input
		for(ItemEntry ent: in.uniqueSet()) {
			int amt = in.getCount(ent);
			from.extract(ent, amt);
		}
		out.produceResults(to.createWriter());
	}
	public static void transact(ItemEntry in, RecipeOutput out, Inventory from, Inventory to) {
		transact(in, 1, out, from, to);
	}
	public static void transact(ItemStack in, RecipeOutput out, Inventory from, Inventory to) {
		transact(in.item, in.amount, out, from, to);
	}
	public static void transact(ItemEntry entry, int inAmount, RecipeOutput out, Inventory from, Inventory to) {
		Objects.requireNonNull(from, "from is null");
		if(from.get(entry).amount() < inAmount) return;
		//Calculate capacity
		double volume = out.outVolume();
		double remainCapacity = to.capacity() - to.volume();
		if(volume > remainCapacity) {
			debug.printl("Required "+volume+" capacity, got "+remainCapacity);
			return;
		} //Not enough space in the output
		//Withdraw from input
		from.extract(entry, inAmount);
		out.produceResults(to.createWriter());
	}
	
	//Recipes
	@Nonnull public static final Object2DoubleMap<ItemEntry> furnaceFuels = new Object2DoubleOpenHashMap<>();
	@Nonnull public static final SimpleProcessingRecipeGroup smelting = new SimpleProcessingRecipeGroup("Furnace");
	@Nonnull public static final SimpleProcessingRecipeGroup clusterMill = new SimpleProcessingRecipeGroup("Cluster mill");
	@Nonnull public static final SimpleProcessingRecipeGroup crusher = new SimpleProcessingRecipeGroup("Crusher");
	@Nonnull public static final ComplexProcessingRecipeGroup alloyer = new ComplexProcessingRecipeGroup("Alloyer", true);
}
