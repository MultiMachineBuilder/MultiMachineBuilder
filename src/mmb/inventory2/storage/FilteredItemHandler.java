package mmb.inventory2.storage;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import io.reactivex.rxjava3.disposables.Disposable;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;
import mmb.inventory2.ItemHandler;
import mmb.inventory2.ItemListener;

public class FilteredItemHandler implements ItemHandler {
    private final ItemHandler base;

    @Nil private final Predicate<ItemEntry> insertPredicate;
    @Nil private final Set<ItemEntry> insertSet;

    @Nil private final Predicate<ItemEntry> extractPredicate;
    @Nil private final Set<ItemEntry> extractSet;

    @Nil private final Predicate<ItemEntry> viewPredicate;
    @Nil private final Set<ItemEntry> viewSet;

    public FilteredItemHandler(
        ItemHandler base,
        @Nil Predicate<ItemEntry> insertPredicate, @Nil Set<ItemEntry> insertSet,
        @Nil Predicate<ItemEntry> extractPredicate, @Nil Set<ItemEntry> extractSet,
        @Nil Predicate<ItemEntry> viewPredicate, @Nil Set<ItemEntry> viewSet
    ) {
        this.base = Objects.requireNonNull(base, "base handler");
        this.insertPredicate = insertPredicate;
        this.insertSet = insertSet == null ? null : Collections.unmodifiableSet(Set.copyOf(insertSet));
        this.extractPredicate = extractPredicate;
        this.extractSet = extractSet == null ? null : Collections.unmodifiableSet(Set.copyOf(extractSet));
        this.viewPredicate = viewPredicate;
        this.viewSet = viewSet == null ? null : Collections.unmodifiableSet(Set.copyOf(viewSet));
    }

    private static boolean allows(Predicate<ItemEntry> pred, Set<ItemEntry> set, ItemEntry item) {
        // null predicate/set = no filtering, empty set = block all
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
        for (var e : items) {
            if (!allowsInsert(e.item())) return 0; // reject whole bulk
        }
        synchronized (base.lock()) {
            return base.bulkInsert(items, units);
        }
    }

    @Override
    public int bulkExtract(ItemList items, int units) {
        for (var e : items) {
            if (!allowsExtract(e.item())) return 0; // reject whole bulk
        }
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
    public Object2IntMap<ItemEntry> contents() {
        synchronized (base.lock()) {
            Object2IntMap<ItemEntry> original = base.contents();
            if (viewPredicate == null && viewSet == null) return original;

            var filtered = new it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap<ItemEntry>();
            original.forEach((item, count) -> {
                if (allowsView(item)) filtered.put(item, count);
            });
            return it.unimi.dsi.fastutil.objects.Object2IntMaps.unmodifiable(filtered);
        }
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

	@Override
	public Disposable addItemListener(@Nil Set<ItemEntry> itemsToWatch, @Nil Predicate<ItemEntry> filterRefinement,
			ItemListener listener) {
		// TODO Auto-generated method stub
		return null;
	}
}