package com.pploder.events;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * A simple implementation of an event.
 * Duplicate occurrences of listeners are supported.
 *
 * @param <T> The argument type to be passed to the listeners. (copied from {@link Event})
 * @author Philipp Ploder
 * @version 1.0.0
 * @since 1.0.0
 */
public class SimpleEvent<T> implements Event<T> {

    private final List<Consumer<T>> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void addListener(Consumer<T> listener) throws NullPointerException {
        if (listener == null) {
            throw new NullPointerException("The listener to add may not be null");
        }

        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public void addAllListeners(Consumer<T>... listeners) throws NullPointerException {
        if (listeners == null) {
            throw new NullPointerException("The listeners array is a null-reference");
        }

        addAllListeners(Arrays.asList(listeners));
    }

    @Override
    public void addAllListeners(Collection<Consumer<T>> listeners) throws NullPointerException {
        if (listeners == null) {
            throw new NullPointerException("The listeners collection is a null-reference");
        }

        if (!listeners.isEmpty()) {
            if (listeners.stream().anyMatch(Objects::isNull)) {
                throw new NullPointerException("At least one of the given listeners is a null-reference");
            }

            synchronized (this.listeners) {
                this.listeners.addAll(listeners);
            }
        }
    }

    @Override
    public void removeListener(Consumer<T> listener) throws NullPointerException {
        if (listener == null) {
            throw new NullPointerException("The listener to remove may not be null");
        }

        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    @Override
    public void removeAllOccurrences(Consumer<T> listener) throws NullPointerException {
        if (listener == null) {
            throw new NullPointerException("The listener to remove may not be null");
        }

        synchronized (listeners) {
            listeners.removeIf(listener::equals);
        }
    }

    @Override
    public void trigger(T t) {
        listeners.forEach(listener -> listener.accept(t));
    }

}
