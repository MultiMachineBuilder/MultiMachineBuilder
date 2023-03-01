/**
 * 
 */
package mmb.engine.inv;

import mmb.NN;
import mmb.engine.item.ItemEntry;

/**
 * An item record, which allows only extraction
 * @author oskar
 */
public class ExtractionsOnlyItemRecord implements ItemRecord{
	@NN private final ItemRecord irecord;
	@NN private final Inventory inv;
	/**
	 * @param rec
	 */
	private ExtractionsOnlyItemRecord(ItemRecord rec, Inventory inv) {
		irecord = rec;
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
	/**
	 * Decorates an item record. Used internally  by item  record
	 * @param rec item record to decorate
	 * @return an extraction only item record
	 */
	@NN public static ItemRecord decorate(ItemRecord rec) {
		if(rec instanceof ExtractionsOnlyItemRecord) return rec;
		return new ExtractionsOnlyItemRecord(rec, ExtractionsOnlyInventory.decorate(rec.inventory()));
	}
	@Override
	public int amount() {
		return irecord.amount();
	}
	@Override
	public Inventory inventory() {
		return inv;
	}
	@Override
	public ItemEntry item() {
		return irecord.item();
	}
	@Override
	public int insert(int amount) {
		return 0;
	}
	@Override
	public int extract(int amount) {
		return irecord.extract(amount);
	}
}
