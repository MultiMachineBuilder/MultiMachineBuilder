package mmb.inventory2;

import java.util.Set;
import java.util.function.Predicate;

import io.reactivex.rxjava3.disposables.Disposable;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;

/**
 * A thread-safe mutable item storage capability.
 * <p>
 * An {@code ItemHandler} represents a unit of item storage and transfer, such as:
 * <ul>
 *   <li>a general-purpose inventory,</li>
 *   <li>a single-slot container,</li>
 *   <li>a machine input or output bus,</li>
 *   <li>a filtered or sided inventory view,</li>
 *   <li>or another item-handling abstraction.</li>
 * </ul>
 * <p>
 * Implementations may impose arbitrary restrictions on:
 * <ul>
 *   <li>which items are accepted,</li>
 *   <li>which items may be extracted,</li>
 *   <li>how many distinct item types may be stored,</li>
 *   <li>or how much total volume may be contained.</li>
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
 * The values returned by all query and mutation methods must always correspond to a logically
 * valid handler state.
 *
 * <h2>Check and operation consistency</h2>
 * This interface distinguishes between:
 * <ul>
 *   <li><b>check methods</b>, such as {@link #insertableAmount(int, ItemEntry)} and
 *       {@link #extractableAmount(int, ItemEntry)}, which predict what could be done,</li>
 *   <li><b>operation methods</b>, such as {@link #insert(ItemEntry, int)} and
 *       {@link #extract(ItemEntry, int)}, which actually mutate the handler.</li>
 * </ul>
 * <p>
 * All check and operation methods must return values greater than or equal to {@code 0}.
 * <p>
 * For a stable handler state, every check method must agree with the corresponding operation
 * method if called immediately afterwards with the same arguments and no intervening mutation.
 * For example:
 * <pre>{@code
 * synchronized(handler.lock()) {
 *     int canInsert = handler.insertableRemain(amount, item);
 *     int inserted  = handler.insert(item, amount);
 *     assert inserted == canInsert;
 * }
 * }</pre>
 * The same rule applies to:
 * <ul>
 *   <li>{@link #extractableAmount(int, ItemEntry)} ↔ {@link #extract(ItemEntry, int)}</li>
 *   <li>{@link #insertableAmountBulk(int, ItemList)} ↔ {@link #bulkInsert(ItemList, int)}</li>
 *   <li>{@link #extractableAmountBulk(int, ItemList)} ↔ {@link #bulkExtract(ItemList, int)}</li>
 * </ul>
 *
 * <h2>Thread safety</h2>
 * All {@code ItemHandler} implementations must be fully thread-safe.
 * <p>
 * In particular:
 * <ul>
 *   <li>all public methods must be safe to call concurrently from multiple threads,</li>
 *   <li>all returned values must reflect a valid logical state,</li>
 *   <li>no race condition may cause state corruption or invariant violation.</li>
 * </ul>
 * <p>
 * For multi-step atomic access, callers may synchronize on the object returned by
 * {@link #lock()}.
 * <p>
 * All externally visible state changes must be synchronized consistently with this lock.
 * Code synchronized on {@link #lock()} must observe a stable handler state.
 *
 * <h2>Contents view</h2>
 * The map returned by {@link #dumpContents(Object2IntMap)} is a read-only live view of this handler.
 * It must:
 * <ul>
 *   <li>never be {@code null},</li>
 *   <li>never expose negative counts,</li>
 *   <li>always reflect a valid current or concurrent state of this handler,</li>
 *   <li>not allow mutation by the caller.</li>
 * </ul>
 * <p>
 * Unless otherwise documented by an implementation, iteration over this view should be assumed
 * to require external synchronization using {@link #lock()} if concurrent mutation is possible.
 *
 * <h2>Listeners</h2>
 * Implementations may support item-change listeners via {@link #addItemListener(Set, Predicate, ItemListener)}.
 * Registered listeners are notified whenever the logical contents of this handler change in a way
 * matching their filter.
 * <p>
 * Listener callbacks must observe a state consistent with the already-applied change.
 * Implementations are encouraged to invoke listeners after releasing internal mutation locks,
 * unless stronger guarantees are explicitly documented.
 * <p>
 * Listener equality is semantic: listeners that compare equal via {@link Object#equals(Object)}
 * must be observationally interchangeable. In other words, if two listeners are equal, registering
 * either one must have the same externally visible effect.
 *
 * <h2>Lifecycle</h2>
 * {@link #close()} releases any resources associated with this handler, such as delegated
 * subscriptions or listener infrastructure. Implementations that do not require explicit cleanup
 * may define {@code close()} as a no-op.
 */
public interface ItemHandler extends Disposable {

    /**
     * Returns the synchronization object used to coordinate externally visible state access.
     * <p>
     * Callers may synchronize on this object to perform multi-step atomic operations against
     * this handler.
     * <p>
     * All query and mutation methods must behave consistently with synchronization on this lock.
     *
     * @return the lock object for this handler, never {@code null}
     */
    Object lock();

    /**
     * Returns the total storage capacity of this handler, measured in item volume units.
     *
     * @return the maximum volume this handler can contain, always {@code >= 0}
     */
    double capacity();

    /**
     * Returns the current total occupied volume of this handler.
     *
     * @return the current contained volume, always {@code >= 0} and {@code <= capacity()}
     */
    double volume();

    /**
     * Returns the maximum number of distinct item entries this handler may contain simultaneously.
     *
     * @return the slot/type capacity of this handler, always {@code >= 0}
     */
    int maxSlots();

    /**
     * Returns whether this handler currently contains no items.
     *
     * @return {@code true} if and only if {@link #dumpContents(Object2IntMap)} is empty
     */
    boolean isEmpty();

    /**
     * Tests whether this handler is capable of accepting at least some amount of the given item
     * under its current structural rules.
     * <p>
     * This method does not guarantee that insertion would succeed at the moment of use, since
     * available space may change concurrently.
     *
     * @param item the item to test
     * @return {@code true} if the item is structurally accepted by this handler
     * @throws NullPointerException if {@code item} is {@code null}
     */
    boolean test(ItemEntry item);

    /**
     * Dumps contents of this inventory into a map
     * <p>
     * The returned map is backed by this handler and reflects subsequent changes.
     * It must not permit mutation by the caller.
     * <p>
     * Unless externally synchronized via {@link #lock()}, concurrent iteration should be assumed
     * to observe a changing view.
     * @param target TODO
     */
    void dumpContents(Object2IntMap<ItemEntry> target);
    default Object2IntMap<ItemEntry> copyContents(){
    	Object2IntMap<ItemEntry> result = new Object2IntOpenHashMap<ItemEntry>();
    	dumpContents(result);
    	return result;
    }

    /**
     * Attempts to insert up to {@code amount} units of the given item into this handler.
     *
     * @param item the item to insert
     * @param amount the maximum amount to insert
     * @return the amount actually inserted, always {@code >= 0}
     * @throws NullPointerException if {@code item} is {@code null}
     */
    int insert(ItemEntry item, int amount);

    /**
     * Attempts to extract up to {@code amount} units of the given item from this handler.
     *
     * @param item the item to extract
     * @param amount the maximum amount to extract
     * @return the amount actually extracted, always {@code >= 0}
     * @throws NullPointerException if {@code item} is {@code null}
     */
    int extract(ItemEntry item, int amount);

    /**
     * Attempts to insert up to {@code units} copies of the given item list into this handler.
     * <p>
     * One unit represents one full application of the provided recipe/list.
     *
     * @param items the item list to insert
     * @param units the maximum number of units to insert
     * @return the number of whole units actually inserted, always {@code >= 0}
     * @throws NullPointerException if {@code items} is {@code null}
     */
    int bulkInsert(ItemList items, int units);

    /**
     * Attempts to extract up to {@code units} copies of the given item list from this handler.
     * <p>
     * One unit represents one full application of the provided recipe/list.
     *
     * @param items the item list to extract
     * @param units the maximum number of units to extract
     * @return the number of whole units actually extracted, always {@code >= 0}
     * @throws NullPointerException if {@code items} is {@code null}
     */
    int bulkExtract(ItemList items, int units);

    /**
     * Returns how much of the given item could currently be inserted into this handler.
     *
     * @param amount the maximum amount to test
     * @param item the item to test
     * @return the amount that could currently be inserted, always {@code >= 0}
     * @throws NullPointerException if {@code item} is {@code null}
     */
    int insertableAmount(int amount, ItemEntry item);

    /**
     * Returns how much of the given item could currently be extracted from this handler.
     *
     * @param amount the maximum amount to test
     * @param item the item to test
     * @return the amount that could currently be extracted, always {@code >= 0}
     * @throws NullPointerException if {@code item} is {@code null}
     */
    int extractableAmount(int amount, ItemEntry item);

    /**
     * Returns how many whole units of the given item list could currently be inserted into
     * this handler.
     *
     * @param amount the maximum number of units to test
     * @param items the item list to test
     * @return the number of whole units that could currently be inserted, always {@code >= 0}
     * @throws NullPointerException if {@code items} is {@code null}
     */
    int insertableAmountBulk(int amount, ItemList items);

    /**
     * Returns how many whole units of the given item list could currently be extracted from
     * this handler.
     *
     * @param amount the maximum number of units to test
     * @param items the item list to test
     * @return the number of whole units that could currently be extracted, always {@code >= 0}
     * @throws NullPointerException if {@code items} is {@code null}
     */
    int extractableAmountBulk(int amount, ItemList items);

    /**
     * Registers a listener to be notified when matching item contents of this handler change.
     * <p>
     * The listener is invoked only for changes affecting items that match both:
     * <ul>
     *   <li>the optional {@code itemsToWatch} set, if non-{@code null}, and</li>
     *   <li>the optional {@code filterRefinement} predicate, if non-{@code null}.</li>
     * </ul>
     * <p>
     * If both filters are {@code null}, the listener observes all logical item changes.
     * <p>
     * Implementations may internally deduplicate listeners using {@link Object#equals(Object)}.
     * Therefore, listeners that compare equal must be observationally interchangeable.
     *
     * @param itemsToWatch optional set of items to watch, or {@code null} to watch all items
     * @param filterRefinement optional additional filter predicate, or {@code null} for none
     * @param listener the listener to register
     * @return a handle whose disposal unregisters the listener
     * @throws NullPointerException if {@code listener} is {@code null}
     */
    Disposable addItemListener(
        @Nil Set<ItemEntry> itemsToWatch,
        @Nil Predicate<ItemEntry> filterRefinement,
        ItemListener listener
    );
}