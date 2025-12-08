/**
 * 
 */
package mmb.engine.inv;

import mmb.annotations.NN;
import mmb.engine.item.ItemEntry;

/**
 * An item record, which does not allow I/O
 * @author oskar
 */
public class ReadOnlyItemRecord implements ItemRecord{

	/**
	 * Decorates an item record
	 * @param itemRecord item record to decorate
	 * @return a read-only item record
	 */
	public static @NN ItemRecord decorate(ItemRecord itemRecord) {
		return new ReadOnlyItemRecord(itemRecord);
	}

	private final ItemRecord rec;

	private ReadOnlyItemRecord(ItemRecord rec) {
		super();
		this.rec = rec;
	}

	@Override
	public int amount() {
		return rec.amount();
	}

	@Override
	public Inventory inventory() {
		return rec.inventory().readOnly();
	}

	@Override
	public ItemEntry item() {
		return rec.item();
	}

	@Override
	public int insert(int amount) {
		return 0;
	}

	@Override
	public int extract(int amount) {
		return 0;
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public boolean canInsert() {
		return false;
	}

	@Override
	public ItemRecord lockInsertions() {
		return this;
	}

	@Override
	public ItemRecord lockExtractions() {
		return this;
	}

	@Override
	public ItemRecord readOnly() {
		return this;
	}

}
