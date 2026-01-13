/**
 * 
 */
package mmb.engine.recipe;

import org.ainslec.picocog.PicoWriter;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.ItemEntry;

/**
 * An item stack or an item entry
 * @author oskar
 */
public interface SingleItem extends ItemList {

	@Override
	default void produceResults(InventoryWriter tgt, int amount) {
		tgt.insert(item(), amount*amount());
	}
	
	@Override
	default void represent(PicoWriter out) {
		out.write(amount() +"� "+item().title());
	}

	@Override
	default double outVolume() {
		return item().volume()*amount();
	}

	@Override
	default Object2IntMap<ItemEntry> getContents() {
		return Object2IntMaps.singleton(item(), amount());
	}

	@Override
	default boolean contains(@Nil ItemEntry entry) {
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
	@NN public ItemEntry item();
	
	/** @return number of items */
	public int amount();	
}
