/**
 * 
 */
package mmbeng.craft;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.ainslec.picocog.PicoWriter;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmbeng.inv.Inventory;
import mmbeng.inv.ItemRecord;
import mmbeng.inv.io.InventoryWriter;
import mmbeng.item.ItemEntry;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * @author oskar
 * A mutable implementation of item list
 */
public final class SimpleItemList implements RecipeOutput {
	@Nonnull public static final SimpleItemList EMPTY = new SimpleItemList();
	
	/**
	 * The data array
	 */
	@Nonnull private final Object2IntMap<ItemEntry> data = new Object2IntOpenHashMap<>();
	
	/**
	 * Creates an empty item list
	 */
	private SimpleItemList() {}
			
	/**
	 * Creates an item list from a map
	 * @param items item map to copy
	 */
	public SimpleItemList(Map<ItemEntry, Integer> items) {
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
	public SimpleItemList(Collection<? extends SingleItem> stacks) {
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
			data.put(irecord.item(), irecord.amount());
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

	@SuppressWarnings("null")
	@Override
	public Object2IntMap<ItemEntry> getContents() {
		return Object2IntMaps.unmodifiable(data);
	}

	@Override
	public boolean contains(ItemEntry entry) {
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
	
	/**
	 * Creates an inventory writer which adds items into this item list
	 * @return an inventory writer
	 */
	@Nonnull InventoryWriter createWriter() {
		return new InventoryWriter() {
			@Override
			public int insert(ItemEntry ent, int amount) {
				data.put(ent, get(ent));
				return amount;
			}
			@Override
			public int bulkInsert(RecipeOutput block, int amount) {
				for(Entry<ItemEntry> ent: block.getContents().object2IntEntrySet()) {
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

	@Override
	public void produceResults(InventoryWriter tgt, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void represent(PicoWriter out) {
		// TODO Auto-generated method stub
		
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
	
	private Integer hash = null;

	@Override
	public int hashCode() {
		if(hash == null) hash = Integer.valueOf(data.hashCode());
		return hash.intValue();
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof RecipeOutput)
			return data.equals(((RecipeOutput) obj).getContents());
		return false;
	}
	
}
