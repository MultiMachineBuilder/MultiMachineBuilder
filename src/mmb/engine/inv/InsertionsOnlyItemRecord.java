/**
 * 
 */
package mmb.engine.inv;

import mmb.NN;
import mmb.engine.item.ItemEntry;

/**
 * An item record, which allows only insertion
 * @author oskar
 */
public class InsertionsOnlyItemRecord implements ItemRecord {
	@NN private final ItemRecord ir;
	@NN private final Inventory inv;
	private InsertionsOnlyItemRecord(ItemRecord ir, Inventory inv) {
		super();
		this.ir = ir;
		this.inv = inv;
	}

	/**
	 * Create an extract-only item record for given item record.
	 * @param rec target item record
	 * @param inv inventory
	 * @return the insertion only item record
	 */
	@NN public static ItemRecord decorate(ItemRecord rec, Inventory inv) {
		if(!rec.canExtract()) return rec;
		return new InsertionsOnlyItemRecord(rec, inv.lockExtractions());
	}
	/**
	 * Decorates an item record. Used internally by item records
	 * @param itemRecord item record to decorate
	 * @return an extraction only item record
	 */
	public static @NN ItemRecord decorate(ItemRecord itemRecord) {
		return decorate(itemRecord, itemRecord.inventory());
	}

	@Override
	public int amount() {
		return ir.amount();
	}

	@Override
	public Inventory inventory() {
		return inv;
	}

	@Override
	public ItemEntry item() {
		return ir.item();
	}

	@Override
	public int insert(int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int extract(int amount) {
		// No extration allowed
		return 0;
	}

}
