/**
 * 
 */
package mmb.WORLD.inventory;

/**
 * @author oskar
 *
 */
public interface ItemToken {
	/**
	 * Set the target inventory
	 * @param inv target inventory
	 */
	public void directTo(Inventory inv);
	/**
	 * Set target amount. Use negative value to transfer entire inventory
	 * @param amount
	 */
	public void setTransferAmount(int amount);
	/**
	 * Run the transaction
	 */
	public void run();
}
