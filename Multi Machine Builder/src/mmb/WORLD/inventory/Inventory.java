/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author oskar
 *
 */
public interface Inventory extends Iterable<ItemRecord> {
	
	@Override
	@Nonnull public Iterator<ItemRecord> iterator();
	/**
	 * Get the item record under given item type
	 * @param entry
	 * @return the item record with given type, or throws if not found
	 */
	@Nonnull public ItemRecord get(ItemEntry entry);
	/**
	 * Get the item record under given item type
	 * @param entry
	 * @return the item record with given type, or null if not found
	 */
	@Nullable public ItemRecord nget(ItemEntry entry);
	public int insert(ItemEntry ent, int amount);
	public int extract(ItemEntry ent, int amount);
	/**
	 * @return capacity of given inventory
	 */
	public double capacity();
	/**
	 * @return used volume of given inventory
	 */
	public double volume();
	/**
	 * @return does given inventory exist?
	 */
	default public boolean exists() {
		return true;
	}
	default public boolean canExtract() {
		return true;
	}
	default public boolean canInsert() {
		return true;
	}

	@Nonnull default public Inventory lockInsertions() {
		return ExtractionsOnlyInventory.decorate(this);
	}
	@Nonnull default public Inventory lockExtractions() {
		return InsertionsOnlyInventory.decorate(this);
	}
	@Nonnull default public Inventory readOnly() {
		return ReadOnlyInventory.decorate(this);
	}
}