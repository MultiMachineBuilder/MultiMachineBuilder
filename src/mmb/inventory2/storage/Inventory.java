package mmb.inventory2.storage;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import io.reactivex.rxjava3.disposables.Disposable;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.Verify;
import mmb.engine.debug.Debugger;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;
import mmb.inventory2.ItemEvent;
import mmb.inventory2.ItemHandler;
import mmb.inventory2.ItemListener;
import mmb.rx.ChannelObservable;
import mmb.rx.Source;

/**
 * A simple thread-safe map-backed item handler with:
 * <ul>
 *   <li>arbitrarily many supported item types,</li>
 *   <li>a configurable maximum number of distinct stored item entries,</li>
 *   <li>a configurable maximum total item volume.</li>
 * </ul>
 * <p>
 * This handler imposes no item-type restrictions beyond slot count and total volume.
 */
public class Inventory implements ItemHandler {
	private static final Debugger debug = new Debugger("mmb.inventory2.storage.Inventory");
	
	private final Object2IntOpenHashMap<ItemEntry> contents = new Object2IntOpenHashMap<>();

	private double maxVolume;
	private int maxSlots;
	private double volume;

	/** Base event source for all item changes. */
	private final Source<ItemEvent> source = new Source<>();
	/** Classified event view by item entry. */
	private final ChannelObservable<ItemEvent, ItemEntry> itemEvent =
		new ChannelObservable<>(source, ItemEvent::item);

	/**
	 * Creates an empty item handler.
	 *
	 * @param maxVolume maximum total item volume
	 * @param maxSlots maximum number of distinct stored item entries
	 * @throws IllegalArgumentException if {@code maxVolume <= 0} or {@code maxSlots < 0}
	 */
	public Inventory(double maxVolume, int maxSlots) {
		contents.defaultReturnValue(0);
		this.volume = 0;
		this.maxVolume = maxVolume;
		this.maxSlots = maxSlots;

		Verify.requirePositive(maxVolume);
		Verify.requireNonNegative(maxSlots);
	}

	/**
	 * Creates an empty item handler with effectively unlimited slot count.
	 *
	 * @param maxVolume maximum total item volume
	 */
	public Inventory(double maxVolume) {
		this(maxVolume, Integer.MAX_VALUE);
	}

	/**
	 * Creates an empty item handler with default capacity of 1 volume unit
	 * and effectively unlimited slot count.
	 */
	public Inventory() {
		this(1.0, Integer.MAX_VALUE);
	}

	@Override
	public synchronized double capacity() {
		return maxVolume;
	}

	@Override
	public synchronized double volume() {
		return volume;
	}

	@Override
	public synchronized int maxSlots() {
		return maxSlots;
	}

	@Override
	public synchronized boolean isEmpty() {
		return contents.isEmpty();
	}

	@Override
	public boolean test(ItemEntry item) {
		return item != null;
	}

	@Override
	public void dumpContents(Object2IntMap<ItemEntry> map) {
		map.clear();
		map.putAll(contents);
	}

	@Override
	public synchronized int insert(ItemEntry item, int amount) {
		Objects.requireNonNull(item, "item");
		Verify.requireNonNegative(amount);
		if(amount == 0) return 0;
		if(!test(item)) return 0;

		int insertable = insertableAmount(amount, item);
		if(insertable == 0) return 0;

		int current = contents.getInt(item);
		contents.put(item, current + insertable);
		volume += item.volume() * insertable;

		emitEvent(item, current);
		return insertable;
	}

	@Override
	public synchronized int extract(ItemEntry item, int amount) {
		Objects.requireNonNull(item, "item");
		Verify.requireNonNegative(amount);
		if(amount == 0) return 0;

		int current = contents.getInt(item);
		if(current == 0 && !contents.containsKey(item)) return 0;

		int extracted = Math.min(amount, current);
		if(extracted == 0) return 0;

		int remaining = current - extracted;
		if(remaining == 0) {
			contents.removeInt(item);
		}else {
			contents.put(item, remaining);
		}

		volume -= item.volume() * extracted;
		if(volume < 0 && volume > -1e-12) volume = 0; //floating point drift clamp

		emitEvent(item, current);
		return extracted;
	}

	@Override
	public synchronized int bulkInsert(ItemList items, int units) {
		Objects.requireNonNull(items, "items");
		Verify.requireNonNegative(units);
		if(units == 0) return 0;

		int insertable = insertableAmountBulk(units, items);
		if(insertable == 0) return 0;

		for(var stack : items) {
			int count = stack.amount * insertable;
			insert(stack.item, count);
		}
		return insertable;
	}

	@Override
	public synchronized int bulkExtract(ItemList items, int units) {
		Objects.requireNonNull(items, "items");
		Verify.requireNonNegative(units);
		if(units == 0) return 0;

		int extractable = extractableAmountBulk(units, items);
		if(extractable == 0) return 0;

		for(var stack : items) {
			int count = stack.amount * extractable;
			extract(stack.item, count);
		}
		return extractable;
	}

	@Override
	public synchronized int insertableAmount(int amount, ItemEntry item) {
		Objects.requireNonNull(item, "item");
		Verify.requireNonNegative(amount);
		if(amount == 0) return 0;
		if(!test(item)) return 0;

		boolean alreadyPresent = contents.containsKey(item);
		if(!alreadyPresent && contents.size() >= maxSlots) return 0;

		double itemVolume = item.volume();
		if(itemVolume <= 0) return amount; //supports zero-volume items safely

		double remainVolume = maxVolume - volume;
		if(remainVolume <= 0) return 0;

		int byVolume = (int) Math.floor(remainVolume / itemVolume);
		return Math.max(0, Math.min(amount, byVolume));
	}

	@Override
	public synchronized int extractableAmount(int amount, ItemEntry item) {
		Objects.requireNonNull(item, "item");
		Verify.requireNonNegative(amount);
		if(amount == 0) return 0;

		return Math.min(amount, contents.getInt(item));
	}

	@Override
	public synchronized int insertableAmountBulk(int amount, ItemList items) {
		Objects.requireNonNull(items, "items");
		Verify.requireNonNegative(amount);
		if(amount == 0) return 0;
		if(items.getContents().isEmpty()) return amount;

		int newTypes = 0;
		for(var stack : items) {
			if(stack.amount <= 0) continue;
			if(!test(stack.item)) return 0;
			if(!contents.containsKey(stack.item)) newTypes++;
		}

		int freeSlots = maxSlots - contents.size();
		if(newTypes > freeSlots) return 0;

		double unitVolume = items.outVolume();
		if(unitVolume <= 0) return amount;

		double remainVolume = maxVolume - volume;
		if(remainVolume <= 0) return 0;

		int byVolume = (int) Math.floor(remainVolume / unitVolume);
		return Math.max(0, Math.min(amount, byVolume));
	}

	@Override
	public synchronized int extractableAmountBulk(int amount, ItemList items) {
		Objects.requireNonNull(items, "items");
		Verify.requireNonNegative(amount);
		if(amount == 0) return 0;
		if(items.getContents().isEmpty()) return amount;

		int result = amount;
		for(var stack : items) {
			int possibleUnits = contents.getInt(stack.item) / stack.amount;
			result = Math.min(result, possibleUnits);
			if(result == 0) return 0;
		}
		return result;
	}

	private boolean disposed = false;
	@Override
	public synchronized void dispose() {
		contents.clear();
		volume = 0;
		disposed = true;
	}

	/**
	 * Returns the maximum total volume of this handler.
	 * Equivalent to {@link #capacity()}.
	 *
	 * @return maximum total volume
	 */
	public synchronized double getMaxVolume() {
		return maxVolume;
	}

	/**
	 * Returns the maximum number of distinct stored item entries.
	 * Equivalent to {@link #maxSlots()}.
	 *
	 * @return maximum slot count
	 */
	public synchronized int getMaxSlots() {
		return maxSlots;
	}

	/**
	 * Emits a coarse item-change event for the specified item.
	 */
	protected void emitEvent(@NN ItemEntry item, int before) {
		ItemEvent event = new ItemEvent(this, item, before, contents.getInt(item));
		forSet(globalHandlers, event);
		if(setHandlers != null) forSet(setHandlers.get(item), event);
	}
	private static void forSet(@Nil Set<Registration> registration, ItemEvent event) {
		if(registration != null) for(Registration listener: registration)
			try {
				if(listener.filter == null || listener.filter.test(event.item()))
					listener.listener.stackModified(event);
			}catch(Exception e) {
				debug.stacktraceError(e, "Failed to process item event");
			}
			
	}
	
	//Direct mutation methods. They do not respect volume and slot limits.
	/**
	 * Sets an amount of an item directly without respecting max slots or max volume.
	 * @param item item to set
	 * @param amount new amount of an item
	 */
	public synchronized void setAmount(@NN ItemEntry item, int amount) {
		Objects.requireNonNull(item, "item is null");
		Verify.requireNonNegative(amount);
		
		int currentAmount = contents.getInt(item);
		if(currentAmount == amount) return;
		int delta = amount - currentAmount;
		volume += delta * item.volume();
		if(amount == 0) {
			contents.removeInt(item);
		}else{
			contents.put(item, amount);
		}
		
		emitEvent(item, amount);
	}
	/**
	 * Sets the maximum number of distinct stored item entries.
	 *
	 * @param maxSlots new maximum slot count
	 * @throws IllegalArgumentException if {@code maxSlots < 0}
	 * @throws IllegalStateException if current contents exceed the new limit
	 */
	public synchronized void setMaxSlots(int maxSlots) {
		Verify.requireNonNegative(maxSlots);
		this.maxSlots = maxSlots;
	}
	
	/**
	 * Sets the maximum total volume of this handler.
	 *
	 * @param maxVolume new maximum volume
	 * @throws IllegalArgumentException if {@code maxVolume <= 0}
	 * @throws IllegalStateException if current contents exceed the new limit
	 */
	public synchronized void setMaxVolume(double maxVolume) {
		Verify.requirePositive(maxVolume);
		this.maxVolume = maxVolume;
	}
	/**
	 * Deletes all items in this inventory.
	 * @return have any items been deleted?
	 */
	public synchronized boolean clear() {
		if(isEmpty()) return false;
		ItemEntry[] data = contents.keySet().toArray(ItemEntry[]::new);
		for(int i = 0; i < data.length; i++) {
			setAmount(data[i], 0);
		}
		
		contents.clear();
		volume = 0;
		
		return true;
	}

	@Override
	public synchronized boolean isDisposed() {
		return disposed;
	}

	private final Object lock = new Object();
	@Override
	public Object lock() {
		return lock;
	}
	
	//Item registration handling
	private class Registration implements Disposable{
		public final ItemListener listener;
		@Nil public final Set<ItemEntry> items;
		@Nil public final Predicate<ItemEntry> filter;
		public boolean dead = false;
		public Registration(ItemListener listener, @Nil Set<ItemEntry> items, @Nil Predicate<ItemEntry> filter) {
			super();
			this.listener = listener;
			this.items = items;
			this.filter = filter;
		}
		@Override
		public void dispose() {
			if(dead) return;
			if(items == null) globalHandlers.remove(this);
			else for(ItemEntry item: items) {
				setHandlers.get(item).remove(this);
			}
			dead = true;
		}
		@Override
		public boolean isDisposed() {
			return dead;
		}		
	}
	
	private Set<Registration> globalHandlers;
	private SetMultimap<ItemEntry, Registration> setHandlers;

	@Override
	public Disposable addItemListener(@Nil Set<ItemEntry> itemsToWatch, @Nil Predicate<ItemEntry> filterRefinement,
			ItemListener listener) {
		Registration node = new Registration(listener, itemsToWatch, filterRefinement);
		if(itemsToWatch == null) {
			if(globalHandlers == null) globalHandlers = new HashSet<>();
			globalHandlers.add(node);
		}else{
			if(setHandlers == null) setHandlers = HashMultimap.create();
			for(ItemEntry item: itemsToWatch) {
				setHandlers.get(item).add(node);
			}
		}
		return node;
	}
}