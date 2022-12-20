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
public class InsertionsOnlyItemRecord implements ItemRecord {
	@NN private final ItemRecord ir;
	@NN private final Inventory inv;
	private InsertionsOnlyItemRecord(ItemRecord ir, Inventory inv) {
		super();
		this.ir = ir;
		this.inv = inv;
	}

	/**
	 * @param rec 
	 * @param inv 
	 * @return
	 */
	@NN public static ItemRecord decorate(ItemRecord rec, Inventory inv) {
		if(!rec.canExtract()) return rec;
		return new InsertionsOnlyItemRecord(rec, inv.lockExtractions());
	}
	/**
	 * @param itemRecord
	 * @return
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
