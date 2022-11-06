/**
 * 
 */
package mmb.world.inventory.storage;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Iterators;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.MMBUtils;
import mmb.data.json.JsonTool;
import mmb.world.crafting.RecipeOutput;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.ItemRecord;
import mmb.world.inventory.SaveInventory;
import mmb.world.items.ItemEntry;

/**
 * @author oskar
 *
 */
public abstract class BaseSingleItemInventory implements SaveInventory{

	/** @return contents of this inventory */
	public abstract ItemEntry getContents();
	/**
	 * Sets the contents of the inventory
	 * @param contents new contents of this inventory 
	 * @return were items accepted
	 */
	public abstract boolean setContents(@Nullable ItemEntry contents);
	private double capacity = 2;
	/** @return is there an item here? */
	public boolean containsItems() {
		return getContents() != null;
	}
	private class Record implements ItemRecord{
		private final @Nonnull ItemEntry item0;
		public Record(ItemEntry item) {
			item0 = item;
		}
		@Override
		public int amount() {
			return MMBUtils.bool2int(item0 == getContents());
		}

		@Override
		public Inventory inventory() {
			return BaseSingleItemInventory.this;
		}

		@Override
		public ItemEntry item() {
			return item0;
		}

		@Override
		public int insert(int amount) {
			return BaseSingleItemInventory.this.insert(item0, amount);
		}

		@Override
		public int extract(int amount) {
			return BaseSingleItemInventory.this.extract(item0, amount);
		}
		
	}
	@SuppressWarnings("null")
	@Override
	public Iterator<ItemRecord> iterator() {
		ItemEntry item = getContents();
		if(item == null) return Collections.emptyIterator();
		return Iterators.singletonIterator(get(item));
	}

	@Override
	public ItemRecord get(ItemEntry entry) {
		Objects.requireNonNull(entry, "Selection is null");
		return new Record(entry);
	}
	@Override
	public ItemRecord nget(ItemEntry entry) {
		Objects.requireNonNull(entry, "Selection is null");
		if(Objects.equals(entry, getContents())) return new Record(entry);
		return null;
	}
	/** @return item record of this inventory or throw */
	@Nonnull public ItemRecord get() {
		ItemEntry contents = getContents();
		if(contents == null) throw new IllegalStateException("This inventory is empty");
		return new Record(contents);
	}
	/** @return item record of this inventory or null */
	public ItemRecord nget() {
		ItemEntry contents = getContents();
		if(contents == null) return null;
		return new Record(contents);
	}

	@Override
	public int insert(ItemEntry ent, int amount) {
		ItemEntry current = getContents();
		if(current != null) return 0;
		if(amount <= 0) return 0;
		if(capacity < ent.volume()) return 0;
		boolean result = setContents(ent);
		return MMBUtils.bool2int(result);
	}

	@Override
	public int extract(ItemEntry ent, int amount) {
		ItemEntry current = getContents();
		if(current == null) return 0;
		if(amount <= 0) return 0;
		if(Objects.equals(current, ent)) {
			setContents(null);
			return 1;
		}
		return 0;
	}

	@Override
	public double capacity() {
		return capacity;
	}

	@Override
	public BaseSingleItemInventory setCapacity(double cap) {
		capacity = cap;
		return this;
	}

	@Override
	public double volume() {
		final ItemEntry item = getContents();
		if (item != null) return item.volume();
		return 0;
	}
	@Override
	public boolean isEmpty() {
		return getContents() == null;
	}
	@Override
	public int size() {
		return MMBUtils.bool2int(!isEmpty());
	}
	@Override
	public int bulkInsert(RecipeOutput block, int amount) {
		if(block.items().size() > 1) return 0;
		if(block.items().isEmpty()) return amount;
		for(Entry<ItemEntry> entry: block.getContents().object2IntEntrySet()) {
			if(amount < 1) return 0;
			if(entry.getIntValue() > 1) return 0;
			if(entry.getIntValue() == 0) return amount;
			ItemEntry ent = entry.getKey();
			if(ent == null) return 0;
			return insert(ent, 1); //NOSONAR this loop is required to get the required item entry
		}
		return 0;
	}

	/**
	 * A single item inventory with a callback
	 * @author oskar
	 */
	public static class Callback extends SingleItemInventory{
		@Nonnull private final Consumer<@Nullable ItemEntry> handler;
		@Override
		public boolean setContents(@Nullable ItemEntry contents) {
			handler.accept(contents);
			return super.setContents(contents);
		}
		/**
		 * Creates a callback single item inventory
		 * @param handler callback
		 */
		public Callback(Consumer<@Nullable ItemEntry> handler) {
			this.handler = handler;
		}
	}
	@Override
	public boolean test(ItemEntry e) {
		return true;
	}
	public void set(BaseSingleItemInventory inv) {
		setContents(inv.getContents());
		setCapacity(inv.capacity());
	}
	
	//Serialization
	@Override
	public JsonNode save() {
		ArrayNode array = JsonTool.newArrayNode();
		return array.add(capacity()).add(ItemEntry.saveItem(getContents()));
	}
	@Override
	public void load(@Nullable JsonNode data) {
		if(data == null) return;
		JsonNode nodeCapacity = data.get(0);
		setCapacity(nodeCapacity==null ? 2 : nodeCapacity.asDouble(2));
		setContents(ItemEntry.loadFromJson(data.get(1)));
	}
}
