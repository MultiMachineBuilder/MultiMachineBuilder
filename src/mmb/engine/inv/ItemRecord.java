/**
 * 
 */
package mmb.engine.inv;

import mmb.NN;
import mmb.engine.craft.ItemStack;
import mmb.engine.item.ItemEntry;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public interface ItemRecord extends Identifiable<@NN ItemEntry> {
	//Conversion
	/** @return the UI string for this item record */
	public default String toRecipeOutputString() {
		return item().title() + "� "+ amount();
	}
	/** @return an item stack for this item record*/
	@NN public default ItemStack toItemStack() {
		return new ItemStack(item(), amount());
	}
	
	//Stack definition
	@Override
	@NN default ItemEntry id() {
		return item();
	}
	/** @return number of items stored */
	public int amount();
	/** @return inventory, which stores this item record */
	@NN public Inventory inventory();
	/** @return the item stroed in this item record */
	@NN public ItemEntry item();
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
	@NN public default ItemRecord lockInsertions() {
		return ExtractionsOnlyItemRecord.decorate(this);
	}
	@NN public default ItemRecord lockExtractions() {
		return InsertionsOnlyItemRecord.decorate(this);
	}
	@NN public default ItemRecord lockInsertions(Inventory inv) {
		return ExtractionsOnlyItemRecord.decorate(this, inv);
	}
	@NN public default ItemRecord lockExtractions(Inventory inv) {
		return InsertionsOnlyItemRecord.decorate(this, inv);
	}
	@NN public default ItemRecord readOnly() {
		return ReadOnlyItemRecord.decorate(this);
	}
}
