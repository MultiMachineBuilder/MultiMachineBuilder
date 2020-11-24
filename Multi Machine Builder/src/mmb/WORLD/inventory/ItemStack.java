/**
 * 
 */
package mmb.WORLD.inventory;

import mmb.WORLD.tileworld.BlockDrawer;

/**
 * @author oskar
 *
 */
public class ItemStack {
	public Item item;
	public int amount;
	public double volume() {
		return amount * item.getVolume();
	}
	public int fitsInto(double volume) {
		return Math.min(amount, (int)(volume / item.getVolume()));
	}
	public static int fitsInto(Item item, int amount, double volume) {
		return Math.max(0, Math.min(amount, (int)(volume / item.getVolume())));
	}
	public ItemStack(Item item, int amount) {
		super();
		this.item = item;
		this.amount = amount;
	}
	public ItemStack(Item item) {
		super();
		this.item = item;
		this.amount = 1;
	}
	/**
	 * @return
	 */
	public BlockDrawer getTexture() {
		return item.getTexture();
	}
}
