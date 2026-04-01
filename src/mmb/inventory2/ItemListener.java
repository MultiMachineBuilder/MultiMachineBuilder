package mmb.inventory2;

import mmb.engine.item.ItemEntry;

/**
 * Listener for logical item-count changes in an {@link ItemHandler}.
 * <p>
 * An {@code ItemListener} is notified whenever the stored amount of a particular
 * {@link ItemEntry} changes within a handler.
 * <p>
 * Each callback represents a single logical transition for one item entry:
 * <pre>{@code
 * before -> after
 * }</pre>
 * where:
 * <ul>
 *   <li>{@code before >= 0}</li>
 *   <li>{@code after >= 0}</li>
 *   <li>{@code before != after}</li>
 * </ul>
 * <p>
 * Special cases:
 * <ul>
 *   <li>{@code before == 0} means the item entry was newly introduced,</li>
 *   <li>{@code after == 0} means the item entry was fully removed.</li>
 * </ul>
 *
 * <h2>Equality contract</h2>
 * Listener equality is semantic.
 * If two listeners compare equal via {@link Object#equals(Object)}, they must be
 * observationally interchangeable.
 * In other words, registering either one must have the same externally visible effect.
 * <p>
 * This allows implementations to deduplicate or index listeners using equality.
 *
 * <h2>Threading</h2>
 * Listener callbacks may occur on any thread unless an implementation documents
 * stronger guarantees.
 * <p>
 * Callbacks must observe a state consistent with the already-applied change.
 * Implementations are encouraged to invoke listeners after releasing internal locks.
 */
@FunctionalInterface
public interface ItemListener {
    /**
     * Called when the stored amount of an item entry changes in an item handler.
     *
     * @param handler the handler whose logical contents changed
     * @param item the item entry whose stored amount changed
     * @param before the stored amount before the change, always {@code >= 0}
     * @param after the stored amount after the change, always {@code >= 0}
     * @throws NullPointerException if {@code handler} or {@code item} is {@code null}
     */
    void stackModified(ItemHandler handler, ItemEntry item, int before, int after);
}