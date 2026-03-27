package mmb.inventory2;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;
import mmb.rx.ChannelObservable;

/**
 * A thread-safe handler for storing, inserting and extracting items.
 * <p>
 * An {@code ItemHandler} represents a mutable item storage unit, such as:
 * <ul>
 *   <li>a generic item inventory,</li>
 *   <li>a single-item storage,</li>
 *   <li>a machine input or output bus,</li>
 *   <li>or a single slot.</li>
 * </ul>
 * <p>
 * Implementations may impose restrictions on:
 * <ul>
 *   <li>which items are accepted,</li>
 *   <li>which items may be extracted,</li>
 *   <li>how many distinct item types may be stored,</li>
 *   <li>or how much total item volume may be contained.</li>
 * </ul>
 *
 * <h2>State invariants</h2>
 * At all times, every {@code ItemHandler} implementation must satisfy:
 * <ul>
 *   <li>{@code capacity() >= 0}</li>
 *   <li>{@code volume() >= 0}</li>
 *   <li>{@code capacity() >= volume()}</li>
 *   <li>{@code contents() != null}</li>
 *   <li>{@code contents().size() <= maxSlots()}</li>
 *   <li>all item counts in {@code contents()} are non-negative</li>
 *   <li>{@code isEmpty()} if and only if {@code contents().isEmpty()}</li>
 * </ul>
 * <p>
 * Additionally, {@code contents()} must represent the same logical state as the handler's
 * query and operation methods.
 *
 * <h2>Check/operation consistency</h2>
 * This interface distinguishes between:
 * <ul>
 *   <li><b>check methods</b>, such as {@link #insertableRemain(int, ItemEntry)} and
 *       {@link #extractableRemain(int, ItemEntry)}, which predict what could be done,</li>
 *   <li><b>operation methods</b>, such as {@link #insert(ItemEntry, int)} and
 *       {@link #extract(ItemEntry, int)}, which actually mutate the handler.</li>
 * </ul>
 * <p>
 * All check and operation methods must return values not less than {@code 0}.
 * <p>
 * For a stable handler state, every check method must agree with the corresponding operation
 * method if called immediately afterwards with the same arguments.
 * For example:
 * <pre>{@code
 * int canInsert = handler.insertableRemain(amount, item);
 * int inserted  = handler.insert(item, amount);
 * }</pre>
 * then, if the handler state was not modified in between by another thread or code path,
 * {@code inserted == canInsert} must hold.
 * <p>
 * The same rule applies to:
 * <ul>
 *   <li>{@link #extractableRemain(int, ItemEntry)} ↔ {@link #extract(ItemEntry, int)}</li>
 *   <li>{@link #insertableRemainBulk(int, ItemList)} ↔ {@link #bulkInsert(ItemList, int)}</li>
 *   <li>{@link #extractableRemainBulk(int, ItemList)} ↔ {@link #bulkExtract(ItemList, int)}</li>
 * </ul>
 *
 * <h2>Thread safety</h2>
 * All {@code ItemHandler} implementations must be fully thread-safe.
 * <p>
 * In particular:
 * <ul>
 *   <li>all public methods must be safe to call concurrently from multiple threads,</li>
 *   <li>all returned values must reflect a valid state,</li>
 *   <li>no race condition may cause state corruption or invariant violation.</li>
 * </ul>
 * <p>
 * For multi-step atomic access, callers may synchronize on the handler instance:
 * <pre>{@code
 * synchronized(handler) {
 *     int canInsert = handler.insertableRemain(amount, item);
 *     if(canInsert > 0) {
 *         handler.insert(item, amount);
 *     }
 * }
 * }</pre>
 * Implementations must therefore use the handler instance itself ({@code this}) as the
 * synchronization monitor for externally visible state changes.
 * <p>
 * In other words:
 * <ul>
 *   <li>code synchronized on {@code this} must observe a stable handler state,</li>
 *   <li>mutating operations must not bypass this locking contract.</li>
 * </ul>
 *
 * <h2>Contents view</h2>
 * The map returned by {@link #contents()} is a read-only live view of this handler.
 * It must:
 * <ul>
 *   <li>never be {@code null},</li>
 *   <li>never expose negative counts,</li>
 *   <li>always reflect the current contents of this handler,</li>
 *   <li>not allow mutation by the caller.</li>
 * </ul>
 * <p>
 * Unless otherwise documented by an implementation, iteration over this map should be assumed
 * to require external synchronization when used concurrently with mutation.
 *
 * <h2>Events</h2>
 * {@link #itemEvent()} emits notifications whenever the logical contents of this handler change.
 * Implementations should ensure that emitted events are consistent with the already-applied
 * state change.
 *
 * <h2>Lifecycle</h2>
 * {@link #close()} releases resources associated with this handler, such as event subscriptions
 * or delegated backing handlers. After closing, behavior is implementation-defined unless
 * explicitly documented otherwise.
 */
public interface ItemHandler extends AutoCloseable {
    // Basic queries
    double capacity();
    double volume();
    int maxSlots();
    boolean isEmpty();
    boolean test(ItemEntry item);
    /**
     * Returns contents of this item handler as a read-only map.
     * The map is backed by the item handler, so any changes are represented in the map. 
     * @return map view of this item handler
     */
    Object2IntMap<ItemEntry> contents();

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
    
    /** Invoked when contents of this item handler change. */
	public ChannelObservable<ItemEvent, ItemEntry> itemEvent();
	
    @Override
	void close();
}
