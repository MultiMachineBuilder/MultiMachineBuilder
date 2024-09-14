/**
 * 
 */
package mmb.engine.recipe;

import java.util.Collection;
import java.util.Map;

import org.ainslec.picocog.PicoWriter;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.NN;
import mmb.Nil;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.ItemEntry;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * An arbitrary, multi-item item list
 * @author oskar
 */
public final class SimpleItemList implements RecipeOutput {
	/** Item list with no items */
	@NN public static final SimpleItemList EMPTY = new SimpleItemList();
	
	/**
	 * The data array
	 */
	@NN private final Object2IntMap<@NN ItemEntry> data = new Object2IntOpenHashMap<>();
	
	/**
	 * Creates an empty item list
	 */
	private SimpleItemList() {}
	/**
	 * Creates an item list from a map
	 * @param items item map to copy
	 */
	public SimpleItemList(Map<@NN ItemEntry, Integer> items) {
		data.putAll(items);
	}
	/**
	 * Creates an item list with one unit of the item
	 * @param item the item in the new item list
	 */
	public SimpleItemList(ItemEntry item) {
		data.put(item, 1);
	}
	/**
	 * Creates an item list with one unit of the item
	 * @param item the item in the new item list
	 * @param amount item amount
	 */
	public SimpleItemList(ItemEntry item, int amount) {
		data.put(item, amount);
	}
	/**
	 * Creates an item list with items from the stack
	 * @param stack item stack
	 */
	public SimpleItemList(SingleItem stack) {
		data.put(stack.item(), stack.amount());
	}	
	/**
	 * Creates an item list with items from the stack
	 * @param stacks item stacks
	 */
	public SimpleItemList(SingleItem... stacks) {
		for(SingleItem stack: stacks) {
			data.put(stack.item(), stack.amount());
		}
	}	
	/**
	 * Creates an item list with items from the stack
	 * @param stacks item stacks
	 */
	public SimpleItemList(Collection<@NN ? extends SingleItem> stacks) {
		for(SingleItem stack: stacks) {
			data.put(stack.item(), stack.amount());
		}
	}	
	/**
	 * Creates an item list with the given recipe output
	 * @param out recipe output
	 */
	public SimpleItemList(RecipeOutput out) {
		out.produceResults(createWriter());		
	}	
	/**
	 * Creates an item list with the given recipe output
	 * @param out recipe output
	 * @param amount amount of given recipe output
	 */
	public SimpleItemList(RecipeOutput out, int amount) {
		out.produceResults(createWriter(), amount);		
	}	
	/**
	 * Creates an item list from an inventory
	 * @param inv inventory to copy
	 */
	public SimpleItemList(Inventory inv) {
		for(ItemRecord irecord: inv) {
			if(irecord.amount() > 0) data.put(irecord.item(), irecord.amount());
		}
	}
	/**
	 * Creates an item list with items
	 * @param items items to use
	 */
	public SimpleItemList(ItemEntry... items) {
		for(ItemEntry item: items) {
			data.put(item, 1);
		}
	}
	/**
	 * Creates an inventory writer which adds items into this item list
	 * @return an inventory writer
	 */
	@NN InventoryWriter createWriter() {
		return new InventoryWriter() {
			@Override
			public int insert(ItemEntry ent, int amount) {
				data.put(ent, amount);
				return amount;
			}
			@Override
			public int bulkInsert(RecipeOutput block, int amount) {
				for(Entry<@NN ItemEntry> ent: block.getContents().object2IntEntrySet()) {
					insert(ent.getKey(), amount*ent.getIntValue());
				}
				return amount;
			}
			@Override
			public int toInsertBulk(RecipeOutput block, int amount) {
				return amount;
			}

			@Override
			public int toInsert(ItemEntry item, int amount) {
				return amount;
			}
		};
	}
	
	@SuppressWarnings("null")
	@Override
	public Object2IntMap<ItemEntry> getContents() {
		return Object2IntMaps.unmodifiable(data);
	}

	@Override
	public boolean contains(@Nil ItemEntry entry) {
		return data.containsKey(entry);
	}

	@Override
	public int get(ItemEntry entry) {
		return data.getInt(entry);
	}

	@Override
	public int getOrDefault(ItemEntry entry, int value) {
		return data.getOrDefault(entry, value);
	}

	@Override
	public void represent(PicoWriter out) {
		for(Entry<ItemEntry> entry: data.object2IntEntrySet()) {
			out.writeln(ItemStack.toString(entry));
		}
	}

	private double vol = -1;
	@Override
	public double outVolume() {
		if(vol < 0) {
			vol = 0;
			for(Entry<ItemEntry> ent : data.object2IntEntrySet()) {
				vol += ent.getKey().volume()*ent.getIntValue();
			}
		}
		return vol;
	}
	
	//Equality
	private Integer hash = null;
	@Override
	public int hashCode() {
		if(hash == null) hash = Integer.valueOf(data.hashCode());
		return hash.intValue();
	}
	@Override
	public boolean equals(@Nil Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof RecipeOutput)
			return data.equals(((RecipeOutput) obj).getContents());
		return false;
	}
	@Override
	public @NN String toString() {
		PicoWriter writer = new PicoWriter();
		represent(writer);
		return writer.toString();
	}
}
