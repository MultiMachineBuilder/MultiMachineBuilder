/**
 * 
 */
package mmb.WORLD.inventory;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public class ExtractionsOnlyItemRecord implements ItemRecord{
	@Nonnull private final ItemRecord record;
	@Nonnull private final Inventory inv;
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
	@Nonnull public static ItemRecord decorate(ItemRecord rec, Inventory inv) {
		if(!rec.canInsert()) return rec;
		return new ExtractionsOnlyItemRecord(rec, ExtractionsOnlyInventory.decorate(inv));
	}
	@Nonnull public static ItemRecord decorate(ItemRecord rec) {
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
