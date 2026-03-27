package mmb.inventory2;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;
import mmb.rx.ChannelObservable;

/**
 * 
 */
public interface ItemHandler extends AutoCloseable {
    // Basic queries
    double capacity();
    double volume();
    int maxSlots();
    boolean isEmpty();
    boolean test(ItemEntry item);
    /**
     * Returns contents of this item handler as a read-only map.
     * The map is backed by the item handler, so any changes are represented in the map. 
     * @return map view of this item handler
     */
    Object2IntMap<ItemEntry> contents();

    // Single item insert/extract
    int insert(ItemEntry item, int amount);
    int extract(ItemEntry item, int amount);

    // Bulk insert/extract
    int bulkInsert(ItemList items, int units);
    int bulkExtract(ItemList items, int units);

    // Remaining space / extractable
    int insertableRemain(int amount, ItemEntry item);
    int extractableRemain(int amount, ItemEntry item);
    int insertableRemainBulk(int amount, ItemList items);
    int extractableRemainBulk(int amount, ItemList items);
    
    /** Invoked when contents of this item handler change. */
	public ChannelObservable<ItemEvent, ItemEntry> itemEvent();
	
    @Override
	void close();
    
	/** 
     * Checks if the item handler is a slot. If true, returns its contents
     * @return contents if {@code this} is a slot, {@code null} otherwise
     */
    public @Nil SlotState getSlotContents();
}
