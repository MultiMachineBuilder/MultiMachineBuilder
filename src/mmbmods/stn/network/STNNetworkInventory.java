/**
 * 
 */
package mmbmods.stn.network;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterators;
import com.google.common.collect.SetMultimap;

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import mmb.debug.Debugger;
import mmb.world.crafting.RecipeOutput;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.ItemRecord;
import mmb.world.inventory.storage.SimpleInventory;
import mmb.world.item.ItemEntry;

/**
 * The implementation of the Storage component of the STN
 * @author oskar
 */
public class STNNetworkInventory implements Inventory{
	private static final Debugger debug = new Debugger("STN-inventory");
	
	//Outgoing items
	/** The STN storage queue. Items are going to get moved to various inventories from here */
	@Nonnull public final SimpleInventory storageQueue = new SimpleInventory().setCapacity(128);

	/**
	 * Creates a network inventory. This is an implementation of inventory for the STN
	 * @param dataLayerSTN
	 */
	protected STNNetworkInventory(DataLayerSTN dataLayerSTN) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isEmpty() {
		reindex();
		return volume == 0;
	}

	@Override
	public @Nonnull Iterator<@Nonnull ItemRecord> iterator() {
		return Iterators.transform(storageIndex.keySet().iterator(), this::get);
	}

	@Override
	public ItemRecord get(ItemEntry entry) {
		return nget(entry);
	}

	@Override
	public int size() {
		reindex();
		return storageIndex.size();
	}

	@Override
	@Nonnull public ItemRecord nget(ItemEntry entry) {
		reindex();
		return new STNNetworkItemNode(entry);
	}

	@Override
	public int insert(ItemEntry ent, int amount) {
		return networkInsert(ent, amount);
	}

	@Override
	public int extract(ItemEntry ent, int amount) {
		return networkExtract(ent, amount);
	}

	@Override
	public int bulkInsert(RecipeOutput ent, int amount) {
		reindex();
		int remain = amount;
		double attempts0 = 2 + (ent.outVolume(amount)/storageQueue.capacity());
		int attempts = (int) attempts0;
		
		for(int i = 0; i < attempts; i++) {
			int insertion = storageQueue.bulkInsert(ent, remain);
			remain -= insertion;
			if(remain == 0) break;
			if(remain > 0) flushQueue();
		}
		
		return amount-remain;
	}

	/** Flushes the insertion queue */
	public void flushQueue() { //fails to move items
		for(ItemRecord irecord: storageQueue) {
			ItemEntry item = irecord.item();
			int amount = irecord.amount();
			int move = networkInsert(item, amount);
			irecord.extract(move);
		}
	}
	
	//network methods
	//network insert items
	private int networkInsert(ItemEntry item, int amount) {
		reindex();
		int remain = amount;
		for(Inventory inv: nodes0) {
			int insertion = inv.insert(item, remain);
			remain -= insertion;
			if(insertion != 0)
				itemInvIndex.put(item, inv);
			if(remain == 0) break;
		}
		int inserted = amount - remain;
		if(inserted != 0) storageIndex.addTo(item, inserted);
		return inserted;
	}
	private int networkExtract(ItemEntry item, int amount) {
		reindex();
		long indexed = storageIndex.getLong(item);
		//the expected amount of items
		int expected = (int)Math.min(indexed, amount);
		Set<@Nonnull Inventory> candidates = itemInvIndex.get(item);
		int remain = expected;
		int extractedTotal = 0;
		for(Inventory inv: candidates) {
			int extracted = inv.extract(item, remain);
			remain -= extracted;
			extractedTotal += extracted;
			if(remain == 0) break;
		}
		if(extractedTotal >= indexed) {
			//All extracted, remove from index
			storageIndex.removeLong(item);
		}else {
			storageIndex.addTo(item, -extractedTotal);
		}
		if(extractedTotal != expected) {
			//items are missing, rebuild
			dirty = true;
		}
		return extractedTotal;
	}
	
	//storage index
	@Nonnull private final Set<@Nonnull Inventory> nodes0 = new DirtySet();
	/** Inventories connected to the network */
	@Nonnull public final Set<@Nonnull Inventory> nodes = Collections.unmodifiableSet(nodes0);
	@Nonnull private final Object2LongOpenHashMap<mmb.world.item.ItemEntry> storageIndex = new Object2LongOpenHashMap<>();
	@Nonnull private final SetMultimap<mmb.world.item.ItemEntry, @Nonnull Inventory> itemInvIndex = HashMultimap.create();
	private double volume;
	private double capacity;
	private boolean dirty;
	/** Marks the inventory as dirty (the indexes will be rebuilt on next use) */
	public void dirty() {
		dirty = true;
	}
	/** Rebuilds the index if there are inconsistencies */
	public void reindex() {
		if(!dirty) return;
		rebuild();
	}
	/** Force rebuild the index */
	public void rebuild() {
		debug.printl("Index rebuild");
		//Force rebuild the index
		storageIndex.clear();
		itemInvIndex.clear();
		volume = 0;
		capacity = 0;
		for(Inventory inv: nodes0) {
			volume += inv.volume();
			capacity += inv.capacity();
			for(ItemRecord irecord: inv) {
				itemInvIndex.put(irecord.item(), inv);
				storageIndex.addTo(irecord.item(), irecord.amount());
			}
		}
		dirty = false;
	}
	
	//Storage volume
	@Override
	public double capacity() {
		reindex();
		return capacity;
	}

	@Override
	public double volume() {
		reindex();
		return volume;
	}
	
	//Storage network
	/**
	 * Adds inventory to the network. Rejects STN networks
	 * @param inv inventory to add
	 * @return was the inventory added
	 */
	public boolean addInv(@Nullable Inventory inv) {
		if(inv == null || inv instanceof STNNetworkInventory) return false;
		return nodes0.add(inv);
	}
	/**
	 * Removes inventory from the network
	 * @param inv inventory to remove
	 * @return was the inventory removed?
	 */
	public boolean removeInv(@Nullable Inventory inv) {
		return nodes0.remove(inv);
	}
	private class DirtySet extends HashSet<@Nonnull Inventory>{
		private static final long serialVersionUID = -3126410334552219394L;

		@Override
		public boolean add(Inventory e) {
			boolean result = super.add(e);
			dirty |= result;
			return result;
		}

		@Override
		public boolean remove(Object o) {
			boolean result = super.remove(o);
			dirty |= result;
			return result;
		}

		@Override
		public void clear() {
			dirty = true;
			super.clear();
		}
		
	}
	private class STNNetworkItemNode implements ItemRecord{

		@Nonnull private final ItemEntry item;
		private long cache = -1;
		public STNNetworkItemNode(ItemEntry entry) {
			item = entry;
		}

		@Override
		public int amount() {
			if(dirty || cache < 0) {
				reindex();
				cache = storageIndex.getLong(item);
			}
			return (int) Math.max(0, cache);
		}

		@Override
		public Inventory inventory() {
			return STNNetworkInventory.this;
		}

		@Override
		public ItemEntry item() {
			return item;
		}

		@Override
		public int insert(int amount) {
			return STNNetworkInventory.this.insert(item, amount);
		}

		@Override
		public int extract(int amount) {
			return STNNetworkInventory.this.extract(item, amount);
		}
		
	}
	@Override
	public boolean test(ItemEntry e) {
		return true;
	}

	@Override
	public int insertibleRemainBulk(int amount, RecipeOutput ent) {
		return storageQueue.insertibleRemainBulk(amount, ent);
	}
}
