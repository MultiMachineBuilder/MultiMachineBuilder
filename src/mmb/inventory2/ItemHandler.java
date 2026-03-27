package mmb.inventory2;

import java.util.List;

import com.pploder.events.Event;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;

public interface ItemHandler {
    // Basic queries
    double capacity();
    double volume();
    int maxSlots();
    boolean isEmpty();
    boolean test(ItemEntry item);

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
    
    /** Invoked when contents of this item handler change */
	public Observable<ItemEvent> itemEvent();

    /** 
     * Checks if the item handler is a slot. If true, returns its contents
     * @return contents if {@code this} is a slot, {@code null} otherwise
     */
    public @Nil SlotState getSlotContents();
}
