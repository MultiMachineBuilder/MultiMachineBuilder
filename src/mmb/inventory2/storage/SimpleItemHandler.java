package mmb.inventory2.storage;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.annotations.NN;
import mmb.engine.Verify;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;
import mmb.inventory2.ItemEvent;
import mmb.inventory2.ItemHandler;
import mmb.rx.ChannelObservable;

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
public class SimpleItemHandler implements ItemHandler {
    private final Object2IntOpenHashMap<ItemEntry> contents = new Object2IntOpenHashMap<>();
    private final Object2IntMap<ItemEntry> readOnlyContents = Object2IntMaps.unmodifiable(contents);

    private double maxVolume;
    private int maxSlots;
    private double volume;

    private final ChannelObservable<ItemEvent, ItemEntry> itemEvent = new ChannelObservable<>();

    /**
     * Creates an empty item handler.
     *
     * @param maxVolume maximum total item volume
     * @param maxSlots maximum number of distinct stored item entries
     * @throws IllegalArgumentException if {@code maxVolume <= 0} or {@code maxSlots < 0}
     */
    public SimpleItemHandler(double maxVolume, int maxSlots) {
        setMaxVolume(maxVolume);
        setMaxSlots(maxSlots);
        this.volume = 0;
        contents.defaultReturnValue(0);
    }

    /**
     * Creates an empty item handler with effectively unlimited slot count.
     *
     * @param maxVolume maximum total item volume
     */
    public SimpleItemHandler(double maxVolume) {
        this(maxVolume, Integer.MAX_VALUE);
    }

    /**
     * Creates an empty item handler with default capacity of 1 volume unit
     * and effectively unlimited slot count.
     */
    public SimpleItemHandler() {
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
    public synchronized Object2IntMap<ItemEntry> contents() {
        return readOnlyContents;
    }

    @Override
    public synchronized int insert(ItemEntry item, int amount) {
        Verify.requireNonNull(item, "item");
        Verify.requireNonNegative(amount);
        if(amount == 0) return 0;
        if(!test(item)) return 0;

        int current = contents.getInt(item);
        boolean isNew = current == 0 && !contents.containsKey(item);

        if(isNew && contents.size() >= maxSlots) return 0;

        int insertable = insertableRemain(amount, item);
        if(insertable == 0) return 0;

        contents.put(item, current + insertable);
        volume += item.volume() * insertable;

        // TODO emit event(s) once event model is finalized
        return insertable;
    }

    @Override
    public synchronized int extract(ItemEntry item, int amount) {
        Verify.requireNonNull(item, "item");
        Verify.requireNonNegative(amount);
        if(amount == 0) return 0;

        int current = contents.getInt(item);
        if(current == 0 && !contents.containsKey(item)) return 0;

        int extracted = Math.min(amount, current);
        if(extracted == 0) return 0;

        int remaining = current - extracted;
        if(remaining == 0) {
            contents.removeInt(item);
        } else {
            contents.put(item, remaining);
        }

        volume -= item.volume() * extracted;

        // defend against tiny floating-point drift
        if(volume < 0 && volume > -1e-12) volume = 0;

        // TODO emit event(s) once event model is finalized
        return extracted;
    }

    @Override
    public synchronized int bulkInsert(ItemList items, int units) {
        Verify.requireNonNull(items, "items");
        Verify.requireNonNegative(units);
        if(units == 0) return 0;

        int insertable = insertableRemainBulk(units, items);
        if(insertable == 0) return 0;

        items.forEach(stack -> {
            int count = stack.amount * insertable;
            insert(stack.item, count);
        });

        return insertable;
    }

    @Override
    public synchronized int bulkExtract(ItemList items, int units) {
        Verify.requireNonNull(items, "items");
        Verify.requireNonNegative(units);
        if(units == 0) return 0;

        int extractable = extractableRemainBulk(units, items);
        if(extractable == 0) return 0;

        items.forEach(stack -> {
            int count = stack.amount * extractable;
            extract(stack.item, count);
        });

        return extractable;
    }

    @Override
    public synchronized int insertableRemain(int amount, ItemEntry item) {
        Verify.requireNonNull(item, "item");
        Verify.requireNonNegative(amount);
        if(amount == 0) return 0;
        if(!test(item)) return 0;

        int current = contents.getInt(item);
        boolean alreadyPresent = current > 0 || contents.containsKey(item);

        if(!alreadyPresent && contents.size() >= maxSlots) return 0;

        double itemVolume = item.volume();
        if(itemVolume <= 0) return amount; // degenerate zero-volume item support

        double remainVolume = maxVolume - volume;
        if(remainVolume <= 0) return 0;

        int byVolume = (int) Math.floor(remainVolume / itemVolume);
        return Math.max(0, Math.min(amount, byVolume));
    }

    @Override
    public synchronized int extractableRemain(int amount, ItemEntry item) {
        Verify.requireNonNull(item, "item");
        Verify.requireNonNegative(amount);
        if(amount == 0) return 0;

        int current = contents.getInt(item);
        return Math.min(amount, current);
    }

    @Override
    public synchronized int insertableRemainBulk(int amount, ItemList items) {
        Verify.requireNonNull(items, "items");
        Verify.requireNonNegative(amount);
        if(amount == 0) return 0;
        if(items.isEmpty()) return amount;

        // check how many new distinct item types would be introduced
        int newTypes = 0;
        for(var stack : items) {
            ItemEntry item = stack.item;
            if(!test(item)) return 0;
            if(stack.amount <= 0) continue;

            if(contents.getInt(item) == 0 && !contents.containsKey(item)) {
                newTypes++;
            }
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
    public synchronized int extractableRemainBulk(int amount, ItemList items) {
        Verify.requireNonNull(items, "items");
        Verify.requireNonNegative(amount);
        if(amount == 0) return 0;
        if(items.isEmpty()) return amount;

        int result = amount;
        for(var stack : items) {
            int stored = contents.getInt(stack.item);
            int possibleUnits = stored / stack.amount;
            result = Math.min(result, possibleUnits);
            if(result == 0) return 0;
        }
        return result;
    }

    @Override
    public ChannelObservable<ItemEvent, ItemEntry> itemEvent() {
        return itemEvent;
    }

    @Override
    public synchronized void close() {
        contents.clear();
        volume = 0;
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
        if(volume > maxVolume) {
            throw new IllegalStateException("Current contents exceed new max volume");
        }
        this.maxVolume = maxVolume;
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
     * Sets the maximum number of distinct stored item entries.
     *
     * @param maxSlots new maximum slot count
     * @throws IllegalArgumentException if {@code maxSlots < 0}
     * @throws IllegalStateException if current contents exceed the new limit
     */
    public synchronized void setMaxSlots(int maxSlots) {
        Verify.requireNonNegative(maxSlots);
        if(contents.size() > maxSlots) {
            throw new IllegalStateException("Current contents exceed new max slot count");
        }
        this.maxSlots = maxSlots;
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
}