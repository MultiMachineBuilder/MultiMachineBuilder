/**
 * 
 */
package mmb.engine.inv.storage;

import java.util.Collections;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Iterators;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.NN;
import mmb.Nil;
import mmb.engine.MMBUtils;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.craft.SingleItem;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.inv.ItemStack;
import mmb.engine.inv.SaveInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.json.JsonTool;

/**
 * @author oskar
 *
 */
public class SingleStackedInventory implements SaveInventory{
	//Inventory definition
	private double capacity = 2;
	private int storedAmount;
	private ItemEntry storedItem;
	
	//Item records
	private class Record implements ItemRecord{
		@Override
		public boolean exists() {
			return SingleStackedInventory.this.storedItem == entry;
		}

		@NN private final ItemEntry entry;
		public Record(ItemEntry ent) {
			entry = ent;
		}
		@Override
		public int amount() {
			if(SingleStackedInventory.this.storedItem == entry) return storedAmount;
			return 0;
		}

		@Override
		public Inventory inventory() {
			return SingleStackedInventory.this;
		}

		@Override
		public ItemEntry item() {
			return entry;
		}

		@Override
		public int insert(int amount) {
			if(SingleStackedInventory.this.storedItem != entry) return 0;
			return SingleStackedInventory.this.insert(entry, amount);
		}

		@Override
		public int extract(int amount) {
			if(SingleStackedInventory.this.storedItem != entry) return 0;
			return SingleStackedInventory.this.extract(entry, amount);
		}
	}
	@SuppressWarnings("null")
	@Override
	public Iterator<ItemRecord> iterator() {
		if(storedItem == null) return Collections.emptyIterator();
		return Iterators.singletonIterator(new Record(storedItem));
	}
	@Override
	public ItemRecord get(ItemEntry entry) {
		ItemEntry item0 = getItem();
		if(item0 == null) throw new IllegalStateException("This inventory is empty");
		if(entry.equals(item0)) 
			return new Record(item0);
		throw new IllegalStateException("Given item does not exist");
	}
	@Override
	public ItemRecord nget(ItemEntry entry) {
		ItemEntry item0 = getItem();
		if(item0 == null) return null;
		if(entry.equals(item0)) 
			return new Record(item0);
		return null;
	}	

	//Item calculation
	@Override
	public int insertibleRemainBulk(int amount, RecipeOutput block) {
		if(block.items().size() > 1) return 0;
		if(block.items().isEmpty()) return 0;
		for(Entry<ItemEntry> entry: block.getContents().object2IntEntrySet()) {
			if(amount < 1) return 0;
			if(entry.getIntValue() == 0) return 0;
			ItemEntry ent = entry.getKey();
			if(ent == null) return 0;
			return insertibleRemain(amount*entry.getIntValue(), ent); //NOSONAR this loop is required to get the required item entry
		}
		return 0;
	}
	@Override
	public int insertibleRemain(int amount, ItemEntry item) {
		if(storedItem == null || item.equals(storedItem)) {
			int tgtamount = Math.min((int)(remainVolume()/ item.volume()), amount);
			return Math.max(0, tgtamount);
		}
		return 0;
	}
	@Override
	public boolean isEmpty() {
		return storedAmount==0 || storedItem==null;
	}
	@Override
	public int size() {
		return MMBUtils.bool2int(!isEmpty());
	}
	@Override
	public boolean test(ItemEntry e) {
		return true;
	}
	@Override
	public double volume() {
		if(storedItem == null) return 0;
		return storedItem.volume() * storedAmount;
	}
	
	//Item manipulation
	@Override
	public int insert(ItemEntry ent, int amount) {
		int amt = insertibleRemain(amount, ent);
		if(storedItem == null) {
			storedItem = ent;
		}
		this.storedAmount += amt;
		return amt;
	}
	@Override
	public int extract(ItemEntry ent, int amount) {
		if(storedItem == null) return 0;
		if(storedItem.equals(ent)) {
			int result = Math.min(this.storedAmount, amount);
			this.storedAmount -= result;
			if(this.storedAmount == 0) storedItem = null;
			return result;
		}
		return 0;
	}
	@Override
	public int bulkInsert(RecipeOutput block, int amount) {
		if(block.items().size() > 1) return 0;
		if(block.items().isEmpty()) return amount;
		for(Entry<ItemEntry> entry: block.getContents().object2IntEntrySet()) {
			if(amount < 1) return 0;
			if(entry.getIntValue() == 0) return amount;
			ItemEntry ent = entry.getKey();
			if(ent == null) return 0;
			int max = amount;
			double unitVolume = ent.outVolume();
			double roughVolumeLimit = remainVolume()/unitVolume;
			int exactUnits = (int) roughVolumeLimit;
			if(exactUnits == 0) return 0;
			if(max > exactUnits) max = exactUnits;
			return insert(ent, entry.getIntValue()*max); //NOSONAR this loop is required to get the required item entry
		}
		return 0;
	}
	
	//Direct modification
	@Override
	public double capacity() {
		return capacity;
	}
	@Override
	@NN public SingleStackedInventory setCapacity(double capacity) {
		this.capacity = capacity;
		return this;
	}
	/** 
	 * Gets the item count
	 * @return items currently stored
	 */
	public int itemCount() {
		return storedAmount;
	}
	/**
	 * Directly modifies the item count (no capacity checks)
	 * @param count new count
	 */
	public void setCount(int count) {
		if(count == 0) storedItem = null;
		storedAmount = count;
	}
	/**
	 * Gets the current item
	 * @return current item
	 */
	public ItemEntry getItem() {
		return storedItem;
	}
	/**
	 * Sets the current item
	 * @param item new item
	 * @return did the change succeed (always true)
	 */
	public boolean setItem(@Nil ItemEntry item) {
		storedItem = item;
		return true;
	}
	/**
	 * Replaces the configs in this inventory with the one of the other inventory
	 * @param inv source inventory
	 */
	public void set(SingleStackedInventory inv) {
		setCapacity(inv.capacity());
		setCount(inv.itemCount());
		setItem(inv.getItem());
	}
	/**
	 * @return contents of this single stacked inventory as an item stack
	 */
	@Nil public ItemStack getStack() {
		ItemEntry item = getItem();
		if(item == null) return null;
		return new ItemStack(item, itemCount());
	}
	
	//Serialization
	@Override
	public JsonNode save() {
		ArrayNode array = JsonTool.newArrayNode();
		array.add(capacity).add(storedAmount).add(ItemEntry.saveItem(storedItem));
		return array;
	}
	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		JsonNode capacity0 = data.get(0);
		if(capacity0 != null) setCapacity(capacity0.asDouble(2));
		JsonNode amount0 = data.get(1);
		int amount1 = (amount0==null)?0:amount0.asInt();
		JsonNode item0 = data.get(2);
		ItemEntry item = ItemEntry.loadFromJson(item0);
		if(amount1 > 0 && item != null){
			storedAmount = amount1;
			storedItem = item;
		}else {
			storedAmount = 0;
			storedItem = null;
		}
	}	
}
