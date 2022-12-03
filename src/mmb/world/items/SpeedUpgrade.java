/**
 * 
 */
package mmb.world.items;

import javax.annotation.Nullable;

import mmb.world.inventory.storage.SingleItemInventory;
import mmb.world.item.Item;
import mmb.world.item.ItemEntry;

/**
 * @author oskar
 *
 */
public class SpeedUpgrade extends Item {
	/** Speed mutiplier */
	public final double speed;

	/**
	 * Creates a speed upgrade
	 * @param speed speed mutiplier
	 */
	public SpeedUpgrade(double speed) {
		this.speed = speed;
	}
	
	/**
	 * Gets a speed mutiplier
	 * @param inv inventory with speed upgrade
	 * @return speed mutiplier for the inventory
	 */
	public static double speedup(SingleItemInventory inv) {
		return speedup(inv.getContents());
	}
	/**
	 * Gets a speed mutiplier
	 * @param ent speed upgrade
	 * @return speed mutiplier for the item
	 */
	public static double speedup(@Nullable ItemEntry ent) {
		if(ent instanceof SpeedUpgrade) return ((SpeedUpgrade) ent).speed;
		return 1;
	}
	/**
	 * Gets a speed mutiplier (optimized)
	 * @param ent speed upgrade
	 * @return speed mutiplier for the item
	 */
	public static double speedup(@Nullable SpeedUpgrade ent) {
		if(ent != null) return ent.speed;
		return 1;
	}
}
