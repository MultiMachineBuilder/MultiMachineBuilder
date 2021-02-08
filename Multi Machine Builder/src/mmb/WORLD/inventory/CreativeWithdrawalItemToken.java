/**
 * 
 */
package mmb.WORLD.inventory;

import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 *
 */
public class CreativeWithdrawalItemToken implements ItemToken {
	private int amt;
	private Inventory inv;
	private final ItemType item;
	/**
	 * @param item item to produce
	 */
	public CreativeWithdrawalItemToken(ItemType item) {
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
