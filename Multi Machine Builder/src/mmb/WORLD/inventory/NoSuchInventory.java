/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Collections;
import java.util.Iterator;
import javax.annotation.Nonnull;

import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 *
 */
public class NoSuchInventory implements Inventory{
	@Nonnull public static final NoSuchInventory INSTANCE = new NoSuchInventory();
	@Nonnull private static final NoSuchItemRecord nsir = new NoSuchItemRecord(INSTANCE);
	private NoSuchInventory() {}
	@SuppressWarnings("null")
	@Override
	public Iterator<ItemRecord> iterator() {
		return Collections.emptyIterator();
	}
	@Override
	public boolean exists() {
		return false;
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
	public ItemRecord get(ItemEntry entry) {
		return nsir;
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
		return 0;
	}
	@Override
	public double volume() {
		return 0;
	}
	@Override
	public ItemRecord nget(ItemEntry entry) {
		return nsir;
	}
	@Override
	public boolean isEmpty() {
		return true;
	}
	@Override
	public int size() {
		return 0;
	}
}
