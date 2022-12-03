/**
 * 
 */
package mmb.world.inventory;

import javax.annotation.Nonnull;

import mmb.world.item.ItemEntry;

/**
 * @author oskar
 *
 */
public class ReadOnlyItemRecord implements ItemRecord{

	/**
	 * @param itemRecord
	 * @return
	 */
	public static @Nonnull ItemRecord decorate(ItemRecord itemRecord) {
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
