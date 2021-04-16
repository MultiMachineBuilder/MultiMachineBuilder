/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Iterator;

import javax.annotation.Nonnull;

import mmb.COLLECTIONS.MapIterator;

/**
 * @author oskar
 *
 */
public class InsertionsOnlyInventory implements Inventory{
	private final @Nonnull Inventory inv;

	@Override
	public Iterator<ItemRecord> iterator() {
		return new MapIterator<ItemRecord, ItemRecord>(i -> InsertionsOnlyItemRecord.decorate(i, this), inv.iterator());
	}

	@Override
	public ItemRecord get(ItemEntry entry) {
		return inv.get(entry).lockExtractions();
	}

	@Override
	public int insert(ItemEntry ent, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int extract(ItemEntry ent, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double capacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean exists() {
		return inv.exists();
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public boolean canInsert() {
		return inv.canInsert();
	}

	@Override
	public Inventory lockInsertions() {
		return ReadOnlyInventory.decorate(inv);
	}

	@Override
	public Inventory lockExtractions() {
		return this;
	}

	@Override
	public double volume() {
		return inv.volume();
	}
	@Nonnull public static Inventory decorate(Inventory inv) {
		if(!inv.canExtract()) return inv;
		return new InsertionsOnlyInventory(inv);
	}

	private InsertionsOnlyInventory(Inventory inv) {
		super();
		this.inv = inv;
	}

	@Override
	public ItemRecord nget(ItemEntry entry) {
		ItemRecord result = inv.nget(entry);
		if(result == null) return null;
		return result.lockExtractions();
	}
}
