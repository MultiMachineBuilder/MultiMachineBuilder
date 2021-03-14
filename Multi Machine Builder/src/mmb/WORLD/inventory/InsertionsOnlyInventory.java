/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Iterator;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public class InsertionsOnlyInventory implements Inventory{
	private final Inventory inv;

	@Override
	public Iterator<ItemRecord> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemRecord get(ItemEntry entry) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return Inventory.super.lockInsertions();
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
}
