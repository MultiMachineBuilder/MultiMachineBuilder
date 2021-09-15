/**
 * 
 */
package mmb.WORLD.crafting;

import java.util.Objects;

import org.apache.commons.collections4.Bag;
import org.joml.Vector3d;

import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ItemEntry;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Craftings {
	private static final Debugger debug = new Debugger("CRAFTING PROCESSOR");
	/**
	 * @param in ingredients
	 * @param out recipe output
	 * @param from source inventory
	 * @param to target inventory
	 */
	public static void transact(Bag<ItemEntry> in, RecipeOutput out, Inventory from, Inventory to) {
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
		transact(new ItemStack(in, 1), out, from, to);
	}
	public static void transact(ItemStack in, RecipeOutput out, Inventory from, Inventory to) {
		Objects.requireNonNull(from, "from is null");
		if(from.get(in.item).amount() < in.amount) return;
		//Calculate capacity
		Vector3d calcvec = new Vector3d();
		out.outVolume();
		double remainCapacity = to.capacity() - to.volume();
		if(calcvec.x > remainCapacity) {
			debug.printl("Required "+calcvec.x+" capacity, got "+remainCapacity);
			return;
		} //Not enough space in the output
		//Withdraw from input
		from.extract(in.item, in.amount);
		out.produceResults(to.createWriter());
	}
}
