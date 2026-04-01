package mmb.inventory2.storage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;
import mmb.inventory2.ItemEvent;
import mmb.inventory2.ItemHandler;
import mmb.inventory2.ItemListener;

/**
 * A wrapper around an {@link ItemHandler} that applies optional filtering
 * on insertion, extraction, and view operations.
 *
 * <p>
 * Filtering rules:
 * <ul>
 *   <li>Null predicate or null set → no filtering for that operation.</li>
 *   <li>Empty set → block all items for that operation.</li>
 *   <li>Bulk operations are rejected entirely if any item fails the filter.</li>
 * </ul>
 * </p>
 *
 * <p>
 * {@link #contents()} returns a dynamically filtered view of the underlying
 * handler contents. Changes in the base handler are immediately visible.
 * </p>
 */
public class FilteredItemHandler implements ItemHandler {

    private final ItemHandler base;

    @Nil private final Predicate<ItemEntry> insertPredicate;
    @Nil private final Set<ItemEntry> insertSet;

    @Nil private final Predicate<ItemEntry> extractPredicate;
    @Nil private final Set<ItemEntry> extractSet;

    @Nil private final Predicate<ItemEntry> viewPredicate;
    @Nil private final Set<ItemEntry> viewSet;

    /**
     * Wraps an existing {@link ItemHandler} with optional filtering.
     *
     * @param base the underlying handler (non-null)
     * @param insertPredicate predicate for insertable items (null = no filter)
     * @param insertSet set of allowed insertable items (null = no filter)
     * @param extractPredicate predicate for extractable items (null = no filter)
     * @param extractSet set of allowed extractable items (null = no filter)
     * @param viewPredicate predicate for visible items (null = no filter)
     * @param viewSet set of allowed visible items (null = no filter)
     */
    public FilteredItemHandler(
            ItemHandler base,
            @Nil Predicate<ItemEntry> insertPredicate, @Nil Set<ItemEntry> insertSet,
            @Nil Predicate<ItemEntry> extractPredicate, @Nil Set<ItemEntry> extractSet,
            @Nil Predicate<ItemEntry> viewPredicate, @Nil Set<ItemEntry> viewSet) {
        this.base = Objects.requireNonNull(base, "base handler");
        this.insertPredicate = insertPredicate;
        this.insertSet = insertSet == null ? null : Collections.unmodifiableSet(Set.copyOf(insertSet));
        this.extractPredicate = extractPredicate;
        this.extractSet = extractSet == null ? null : Collections.unmodifiableSet(Set.copyOf(extractSet));
        this.viewPredicate = viewPredicate;
        this.viewSet = viewSet == null ? null : Collections.unmodifiableSet(Set.copyOf(viewSet));
    }

    private static boolean allows(Predicate<ItemEntry> pred, Set<ItemEntry> set, ItemEntry item) {
        if (set != null && set.isEmpty()) return false;
        boolean predOk = pred == null || pred.test(item);
        boolean setOk = set == null || set.contains(item);
        return predOk && setOk;
    }

    private boolean allowsInsert(ItemEntry item) {
        return allows(insertPredicate, insertSet, item);
    }

    private boolean allowsExtract(ItemEntry item) {
        return allows(extractPredicate, extractSet, item);
    }

    private boolean allowsView(ItemEntry item) {
        return allows(viewPredicate, viewSet, item);
    }

    @Override
    public int insert(ItemEntry item, int amount) {
        if (!allowsInsert(item)) return 0;
        synchronized (base.lock()) {
            return base.insert(item, amount);
        }
    }

    @Override
    public int extract(ItemEntry item, int amount) {
        if (!allowsExtract(item)) return 0;
        synchronized (base.lock()) {
            return base.extract(item, amount);
        }
    }

    @Override
    public int bulkInsert(ItemList items, int units) {
        for (var e : items) if (!allowsInsert(e.item())) return 0;
        synchronized (base.lock()) {
            return base.bulkInsert(items, units);
        }
    }

    @Override
    public int bulkExtract(ItemList items, int units) {
        for (var e : items) if (!allowsExtract(e.item())) return 0;
        synchronized (base.lock()) {
            return base.bulkExtract(items, units);
        }
    }

    @Override
    public boolean test(ItemEntry item) {
        return allowsInsert(item) && base.test(item);
    }

    @Override
    public double capacity() {
        synchronized (base.lock()) {
            return base.capacity();
        }
    }

    @Override
    public double volume() {
        synchronized (base.lock()) {
            return base.volume();
        }
    }

    @Override
    public int maxSlots() {
        synchronized (base.lock()) {
            return base.maxSlots();
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (base.lock()) {
            return base.isEmpty();
        }
    }

    @Override
    public void dumpContents(Object2IntMap<ItemEntry> target) {
    	base.dumpContents(target);
    	target.keySet().removeIf(x -> !allowsView(x));
    }

    @Override
    public int insertableAmount(int amount, ItemEntry item) {
        if (!allowsInsert(item)) return 0;
        synchronized (base.lock()) {
            return base.insertableAmount(amount, item);
        }
    }

    @Override
    public int extractableAmount(int amount, ItemEntry item) {
        if (!allowsExtract(item)) return 0;
        synchronized (base.lock()) {
            return base.extractableAmount(amount, item);
        }
    }

    @Override
    public int insertableAmountBulk(int amount, ItemList items) {
        for (var e : items) if (!allowsInsert(e.item())) return 0;
        synchronized (base.lock()) {
            return base.insertableAmountBulk(amount, items);
        }
    }

    @Override
    public int extractableAmountBulk(int amount, ItemList items) {
        for (var e : items) if (!allowsExtract(e.item())) return 0;
        synchronized (base.lock()) {
            return base.extractableAmountBulk(amount, items);
        }
    }

    @Override
    public boolean isDisposed() {
        synchronized (base.lock()) {
            return base.isDisposed();
        }
    }

    @Override
    public void dispose() {
        synchronized (base.lock()) {
            base.dispose();
        }
    }

    @Override
    public Object lock() {
        return base.lock();
    }

    /**
     * Adds a filtered listener: only receives events for items allowed by
     * this handler's filtering. The source reported to the listener will be
     * this wrapper, not the underlying handler.
     */
    @Override
    public Disposable addItemListener(@Nil Set<ItemEntry> itemsToWatch,
                                      @Nil Predicate<ItemEntry> filterRefinement,
                                      ItemListener listener) {
    	
        Predicate<ItemEntry> combinedPredicate = combinePredicate(filterRefinement);
        Set<ItemEntry> combinedSet = combineSet(itemsToWatch);
        
        return base.addItemListener(combinedSet, combinedPredicate, event -> {
        	ItemEvent newEvent = event.with(this);
        	listener.stackModified(event);
        });
    }

	private Set<ItemEntry> combineSet(@Nil Set<ItemEntry> itemsToWatch) {
		if(viewSet == null) return itemsToWatch;
		if(itemsToWatch == null) return viewSet;
		Set<ItemEntry> combo = new HashSet<ItemEntry>(viewSet);
		combo.retainAll(itemsToWatch);
		return Collections.unmodifiableSet(combo);
	}

	private Predicate<ItemEntry> combinePredicate(@Nil Predicate<ItemEntry> filterRefinement) {
		if(filterRefinement == null) return viewPredicate;
		if(viewPredicate == null) return filterRefinement;
		return viewPredicate.and(filterRefinement);
	}
}