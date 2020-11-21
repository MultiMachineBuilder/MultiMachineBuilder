/**
 * 
 */
package mmb.WORLD.inventory;

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
		return Math.min(amount, (int)(volume / item.getVolume()));
	}
	public ItemStack(Item item, int amount) {
		super();
		this.item = item;
		this.amount = amount;
	}
}
