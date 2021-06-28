/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Iterator;

import javax.annotation.Nonnull;

import com.google.common.collect.Iterators;

import mmb.WORLD.items.ItemEntry;
import monniasza.collects.MapIterator;

/**
 * @author oskar
 *
 */
public class ReadOnlyInventory implements Inventory {
	private final Inventory inv;
	@SuppressWarnings("null")
	@Override
	@Nonnull public Iterator<ItemRecord> iterator() {
		return Iterators.unmodifiableIterator(
				new MapIterator<ItemRecord, ItemRecord>(ReadOnlyItemRecord::decorate, inv.iterator())
		);
	}

	@Override
	public ItemRecord get(ItemEntry entry) {
		return inv.get(entry).readOnly();
	}

	@Override
	public int insert(ItemEntry ent, int amount) {
		return 0;
	}

	@Override
	public int extract(ItemEntry ent, int amount) {
		return 0;
	}

	@Override
	public double capacity() {
		return inv.capacity();
	}

	@Override
	public double volume() {
		return inv.volume();
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
		return false;
	}

	@Override
	public Inventory lockInsertions() {
		return this;
	}

	@Override
	public Inventory lockExtractions() {
		return this;
	}

	@Override
	public Inventory readOnly() {
		return this;
	}

	private ReadOnlyInventory(Inventory inv) {
		super();
		this.inv = inv;
	}
	public static @Nonnull Inventory decorate(Inventory inv) {
		if(inv.canExtract() && inv.canInsert()) return new ReadOnlyInventory(inv);
		if(inv.canInsert()) return inv.lockInsertions();
		if(inv.canExtract()) return inv.lockExtractions();
		return inv;
	}

	@Override
	public ItemRecord nget(ItemEntry entry) {
		ItemRecord result = inv.nget(entry);
		if(result == null) return null;
		return result.readOnly();
	}

}
