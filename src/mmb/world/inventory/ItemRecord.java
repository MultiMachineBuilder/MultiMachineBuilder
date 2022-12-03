/**
 * 
 */
package mmb.world.inventory;

import javax.annotation.Nonnull;

import mmb.world.item.ItemEntry;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public interface ItemRecord extends Identifiable<ItemEntry> {
	//Conversion
	/** @return the UI string for this item record */
	public default String toRecipeOutputString() {
		return item().title() + "� "+ amount();
	}
	/** @return an item stack for this item record*/
	@Nonnull public default ItemStack toItemStack() {
		return new ItemStack(item(), amount());
	}
	
	//Stack definition
	@Override
	@Nonnull default ItemEntry id() {
		return item();
	}
	/** @return number of items stored */
	public int amount();
	/** @return inventory, which stores this item record */
	@Nonnull public Inventory inventory();
	/** @return the item stroed in this item record */
	@Nonnull public ItemEntry item();
	/** @return total volume of items in this item record*/
	public default double volume() {
		return item().volume()*amount();
	}
	
	//Item I/O
	/**
	 * Insert specific amount of items
	 * @param amount amount of items to insert
	 * @throws IllegalArgumentException if amount < 0
	 * @return amount of items actually inserted
	 */
	public int insert(int amount);
	/**
	 * extract specific amount of items
	 * @param amount amount of items to extract
	 * @throws IllegalArgumentException if amount < 0
	 * @return amount of items actually extracted
	 */
	public int extract(int amount);
	
	//I/O testing
	public default boolean exists() {
		return inventory().exists();
	}
	public default boolean canExtract() {
		return true;
	}
	public default boolean canInsert() {
		return true;
	}
	
	//I/O restrictions
	@Nonnull public default ItemRecord lockInsertions() {
		return ExtractionsOnlyItemRecord.decorate(this);
	}
	@Nonnull public default ItemRecord lockExtractions() {
		return InsertionsOnlyItemRecord.decorate(this);
	}
	@Nonnull public default ItemRecord lockInsertions(Inventory inv) {
		return ExtractionsOnlyItemRecord.decorate(this, inv);
	}
	@Nonnull public default ItemRecord lockExtractions(Inventory inv) {
		return InsertionsOnlyItemRecord.decorate(this, inv);
	}
	@Nonnull public default ItemRecord readOnly() {
		return ReadOnlyItemRecord.decorate(this);
	}
}
