/**
 * 
 */
package mmb.engine.inv;

import mmb.NN;
import mmb.engine.item.ItemEntry;

/**
 * @author oskar
 *
 */
public class NoSuchItemRecord implements ItemRecord {
	@NN private final Inventory inv;
	@Override
	public ItemRecord lockInsertions() {
		return this;
	}

	/**
	 * @param inv the target inventory
	 */
	public NoSuchItemRecord(Inventory inv) {
		super();
		this.inv = inv;
	}

	@Override
	public ItemRecord lockExtractions() {
		return this;
	}

	@Override
	public int amount() {
		return 0;
	}

	@Override
	public Inventory inventory() {
		return inv;
	}

	@Override
	public ItemEntry item() {
		return null;
	}

	@Override
	public int insert(int amount) {
		return 0;
	}

	@Override
	public int extract(int amount) {
		return 0;
	}

}
