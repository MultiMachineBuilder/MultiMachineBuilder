/**
 * 
 */
package mmb.WORLD.crafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.ainslec.picocog.PicoWriter;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.items.ItemEntry;

/**
 * An item stack or item entry
 * @author oskar
 */
public interface SingleItem extends RecipeOutput {

	@Override
	default void produceResults(InventoryWriter tgt, int amount) {
		tgt.write(item(), amount*amount());
	}

	@Override
	default void represent(PicoWriter out) {
		out.write(amount() +"� "+item().title());
	}

	@Override
	default double outVolume() {
		return item().volume(amount());
	}

	@Override
	default Object2IntMap<ItemEntry> getContents() {
		return Object2IntMaps.singleton(item(), amount());
	}

	@Override
	default boolean contains(@Nullable ItemEntry entry) {
		return item().equals(entry);
	}

	@Override
	default int get(ItemEntry entry) {
		if(item().equals(entry)) return amount();
		return 0;
	}

	@Override
	default int getOrDefault(ItemEntry entry, int value) {
		if(item().equals(entry)) return amount();
		return value;
	}
	
	/** @return this item or this stack's corresponding items*/
	@Nonnull public ItemEntry item();
	
	/** @return number of items */
	public int amount();
	
}
