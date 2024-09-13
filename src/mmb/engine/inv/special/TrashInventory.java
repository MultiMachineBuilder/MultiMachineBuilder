package mmb.engine.inv.special;

import java.util.Collections;
import java.util.Iterator;

import mmb.NN;
import mmb.Nil;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.inv.NoSuchInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.RecipeOutput;

/** Destroys all incoming items */
public class TrashInventory implements Inventory {
	private TrashInventory() {}
	/** The singleton trash inventory */
	public static final @NN TrashInventory INSTANCE = new TrashInventory();

	@Override
	public @NN Iterator<@NN ItemRecord> iterator() {
		return Collections.emptyIterator();
	}

	@Override
	public @NN ItemRecord get(ItemEntry entry) {
		return NoSuchInventory.INSTANCE.get(entry);
	}

	@Override
	public @Nil ItemRecord nget(ItemEntry entry) {
		return null;
	}

	@Override
	public int insertibleRemainBulk(int amount, RecipeOutput ent) {
		return amount;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean test(ItemEntry e) {
		return true;
	}

	@Override
	public double volume() {
		return 0;
	}

	@Override
	public int insert(ItemEntry ent, int amount) {
		return amount;
	}

	@Override
	public int extract(ItemEntry ent, int amount) {
		return 0;
	}

	@Override
	public int bulkInsert(RecipeOutput ent, int amount) {
		return amount;
	}

	@Override
	public double capacity() {
		return Double.POSITIVE_INFINITY;
	}

}
