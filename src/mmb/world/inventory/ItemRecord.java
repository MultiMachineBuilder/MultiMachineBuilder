/**
 * 
 */
package mmb.world.inventory;

import javax.annotation.Nonnull;

import mmb.world.items.ItemEntry;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public interface ItemRecord extends Identifiable<ItemEntry> {
	public default String toRecipeOutputString() {
		return item().title() + "� "+ amount();
	}
	@Override
	@Nonnull default ItemEntry id() {
		return item();
	}
	public int amount();
	@Nonnull public Inventory inventory();
	@Nonnull public ItemEntry item();
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
	@Nonnull default public ItemStack toItemStack() {
		return new ItemStack(item(), amount());
	}
	default public boolean canExtract() {
		return true;
	}
	default public boolean canInsert() {
		return true;
	}

	default public @Nonnull ItemRecord lockInsertions() {
		return ExtractionsOnlyItemRecord.decorate(this);
	}
	default public @Nonnull ItemRecord lockExtractions() {
		return InsertionsOnlyItemRecord.decorate(this);
	}
	default public @Nonnull ItemRecord lockInsertions(Inventory inv) {
		return ExtractionsOnlyItemRecord.decorate(this, inv);
	}
	default public @Nonnull ItemRecord lockExtractions(Inventory inv) {
		return InsertionsOnlyItemRecord.decorate(this, inv);
	}
	default public @Nonnull ItemRecord readOnly() {
		return ReadOnlyItemRecord.decorate(this);
	}
	default public boolean exists() {
		return inventory().exists();
	}
	default public double volume() {
		return item().volume(amount());
	}
}
