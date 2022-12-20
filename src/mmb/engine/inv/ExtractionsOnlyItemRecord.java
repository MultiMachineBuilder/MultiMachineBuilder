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
public class ExtractionsOnlyItemRecord implements ItemRecord{
	@NN private final ItemRecord record;
	@NN private final Inventory inv;
	/**
	 * @param rec
	 */
	private ExtractionsOnlyItemRecord(ItemRecord rec, Inventory inv) {
		record = rec;
		this.inv = inv;
	}
	/**
	 * Create an extract-only item record for given item record.
	 * @param rec target item record
	 * @param inv inventory
	 * @return the extraction only item record
	 */
	@NN public static ItemRecord decorate(ItemRecord rec, Inventory inv) {
		if(!rec.canInsert()) return rec;
		return new ExtractionsOnlyItemRecord(rec, ExtractionsOnlyInventory.decorate(inv));
	}
	@NN public static ItemRecord decorate(ItemRecord rec) {
		if(rec instanceof ExtractionsOnlyItemRecord) return rec;
		return new ExtractionsOnlyItemRecord(rec, ExtractionsOnlyInventory.decorate(rec.inventory()));
	}
	@Override
	public int amount() {
		return record.amount();
	}
	@Override
	public Inventory inventory() {
		return inv;
	}
	@Override
	public ItemEntry item() {
		return record.item();
	}
	@Override
	public int insert(int amount) {
		return 0;
	}
	@Override
	public int extract(int amount) {
		return record.extract(amount);
	}
}
