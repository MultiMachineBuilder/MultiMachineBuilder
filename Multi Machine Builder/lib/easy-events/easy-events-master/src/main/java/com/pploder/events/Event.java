package com.pploder.events;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * A minimalistic event.
 * Listeners can be added and removed. When the event is triggered all listeners will be executed
 * with the argument passed to {@link #trigger(Object)}. Duplicate listeners are supported.
 *
 * @param <T> The argument type to be passed to the listeners.
 * @author Philipp Ploder
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Event<T> {

    /**
     * Subscribes a listener to this event.
     * Duplicate listeners are supported.
     *
     * @param listener The listener to add.
     * @throws NullPointerException If the given reference is {@code null}.
     */
    void addListener(Consumer<T> listener) throws NullPointerException;

    /**
     * Subscribes all given listeners to this event.
     * Duplicate listeners are supported.
     *
     * @param listeners The listeners to add.
     * @throws NullPointerException If the given array or any of its contained references is {@code null}.
     */
    void addAllListeners(Consumer<T>... listeners) throws NullPointerException;

    /**
     * Subscribes all given listeners to this event.
     * Duplicate listeners are supported.
     *
     * @param listeners The listeners to add.
     * @throws NullPointerException If the given collection or any of its contained references is {@code null}.
     */
    void addAllListeners(Collection<Consumer<T>> listeners) throws NullPointerException;

    /**
     * Unsubscribes the first occurrence of the given listener from this event.
     * If the listener is not found nothing happens.
     *
     * @param listener The listener to remove.
     * @throws NullPointerException If the given reference is {@code null}.
     */
    void removeListener(Consumer<T> listener) throws NullPointerException;

    /**
     * Unsubscribes all occurrences of the given listener from this event.
     * If the listener is not found nothing happens.
     *
     * @param listener The listener to remove.
     * @throws NullPointerException If the given reference is {@code null}.
     */
    void removeAllOccurrences(Consumer<T> listener) throws NullPointerException;

    /**
     * Triggers the event.
     * The order of execution is implementation dependant.
     *
     * @param t The event argument for the listeners (may be {@code null}).
     */
    void trigger(T t);

    /**
     * Triggers the event with the argument {@code null}.
     * The effect should be identical to calling {@link #trigger(Object)} with the argument {@code null}.
     */
    default void trigger() {
        trigger(null);
    }

}
