/**
 * 
 */
package mmb.WORLD.inventory;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public class InsertionsOnlyItemRecord implements ItemRecord {
	private final ItemRecord ir;
	private final Inventory inv;
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
	@Nonnull public static ItemRecord decorate(ItemRecord rec, Inventory inv) {
		if(!rec.canExtract()) return rec;
		return new InsertionsOnlyItemRecord(rec, inv.lockExtractions());
	}
	/**
	 * @param itemRecord
	 * @return
	 */
	public static @Nonnull ItemRecord decorate(ItemRecord itemRecord) {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int extract(int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

}
