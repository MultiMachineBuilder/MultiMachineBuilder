/**
 * 
 */
package mmb.engine.inv;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import mmb.NN;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.item.ItemEntry;

/**
 * An inventory, which only allows insertions
 * @author oskar
 */
public class InsertionsOnlyInventory implements Inventory{
	private final @NN Inventory inv;

	@Override
	public Iterator<ItemRecord> iterator() {
		return Iterators.transform(inv.iterator(), ItemRecord::lockExtractions);
	}

	@Override
	public ItemRecord get(ItemEntry entry) {
		return inv.get(entry).lockExtractions();
	}

	@Override
	public int insert(ItemEntry ent, int amount) {
		return inv.insert(ent, amount);
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
	/**
	 * Decorate an inventory, used by the inventories themselves
	 * @param inv inventory to decorate
	 * @return an insertion-only inventory
	 */
	@NN public static Inventory decorate(Inventory inv) {
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

	@Override
	public boolean isEmpty() {
		return inv.isEmpty();
	}

	@Override
	public int size() {
		return inv.size();
	}

	@Override
	public int bulkInsert(RecipeOutput ent, int amount) {
		return inv.bulkInsert(ent, amount);
	}

	@Override
	public boolean test(ItemEntry e) {
		return inv.test(e);
	}

	@Override
	public int insertibleRemainBulk(int amount, RecipeOutput ent) {
		return inv.insertibleRemainBulk(amount, ent);
	}
}
