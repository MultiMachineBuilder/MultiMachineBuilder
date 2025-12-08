/**
 * 
 */
package mmb.engine.inv;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import mmb.annotations.NN;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.RecipeOutput;

/**
 * An inventory, which only allows to extract items
 * @author oskar
 */
public class ExtractionsOnlyInventory implements Inventory {
	@NN private final Inventory inv;
	private ExtractionsOnlyInventory(Inventory inv) {
		this.inv = inv;
	}

	@Override
	public ItemRecord get(ItemEntry entry) {
		return ExtractionsOnlyItemRecord.decorate(inv.get(entry), this);
	}

	@Override
	public int insert(ItemEntry ent, int amount) {
		return inv.insert(ent, amount);
	}

	@Override
	public boolean exists() {
		return inv.exists();
	}

	@Override
	public boolean canExtract() {
		return inv.canExtract();
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
		return ReadOnlyInventory.decorate(inv);
	}

	@Override
	public Inventory readOnly() {
		return ReadOnlyInventory.decorate(inv);
	}

	@Override
	public Iterator<@NN ItemRecord> iterator() {
		return Iterators.transform(inv.iterator(), ItemRecord::lockInsertions);
	}

	@Override
	public int extract(ItemEntry ent, int amount) {
		return inv.extract(ent, amount);
	}

	@Override
	public double capacity() {
		return inv.capacity();
	}

	@Override
	public double volume() {
		return inv.volume();
	}
	/**
	 * Decorate an inventory, used by the inventories themselves
	 * @param inv inventory to decorate
	 * @return an extraction-only inventory
	 */
	@NN public static Inventory decorate(Inventory inv) {
		if(!inv.canInsert()) return inv;
		return new ExtractionsOnlyInventory(inv);
	}

	@Override
	public ItemRecord nget(ItemEntry entry) {
		ItemRecord result = inv.nget(entry);
		if(result == null) return null;
		return result.lockInsertions();
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
		return 0;
	}

	@Override
	public boolean test(ItemEntry e) {
		return false;
	}

	@Override
	public int insertibleRemainBulk(int amount, RecipeOutput ent) {
		return 0;
	}
}
