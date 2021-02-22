/**
 * 
 */
package mmb.WORLD.inventory;

/**
 * @author oskar
 *
 */
public class CreativeWithdrawalItemToken implements ItemToken {
	private int amt;
	private Inventory inv;
	private final ItemEntry item;
	/**
	 * @param item item to produce
	 */
	public CreativeWithdrawalItemToken(ItemEntry item) {
		this.item = item;
	}

	@Override
	public void directTo(Inventory inv) {
		this.inv = inv;
	}

	@Override
	public void setTransferAmount(int amt) {
		this.amt = amt;
	}

	@Override
	public void run() {
		inv.put(item, amt);
	}
	
}
