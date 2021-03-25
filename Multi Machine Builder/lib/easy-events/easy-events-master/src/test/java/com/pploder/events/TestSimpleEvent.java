package com.pploder.events;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class TestSimpleEvent {

    @Test
    public void testTriggerWithNull() {
        Event<Void> event = new SimpleEvent<>();

        AtomicBoolean executed = new AtomicBoolean();
        event.addListener(arg -> executed.set(true));

        event.trigger(null);

        Assert.assertTrue(executed.get());
    }

    @Test
    public void testTriggerNoArg() {
        Event<Void> event = new SimpleEvent<>();

        AtomicBoolean executed = new AtomicBoolean();
        event.addListener(arg -> executed.set(true));

        event.trigger();

        Assert.assertTrue(executed.get());
    }

    @Test
    public void testTriggerWithArg() {
        Event<String> event = new SimpleEvent<>();
        AtomicReference<String> reference = new AtomicReference<>();

        event.addListener(reference::set);
        event.trigger("Hello, World!");

        Assert.assertEquals("Hello, World!", reference.get());
    }

    @Test
    public void testListenerRemoval() {
        Event<Void> event = new SimpleEvent<>();
        AtomicBoolean executed = new AtomicBoolean();
        Consumer<Void> listener = arg -> executed.set(true);

        event.addListener(listener);
        event.trigger();

        Assert.assertTrue(executed.get());

        executed.set(false);

        event.removeListener(listener);
        event.trigger();

        Assert.assertFalse(executed.get());
    }

    @Test(expected = NullPointerException.class)
    public void testAddListenerNull() {
        new SimpleEvent<>().addListener(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllListenersArrayNull() {
        new SimpleEvent<>().addAllListeners((Consumer<Object>[]) null);
    }

    @Test
    public void testAddAllListenersArray() {
        Event<Void> event = new SimpleEvent<>();
        AtomicInteger counter = new AtomicInteger();

        event.addAllListeners(arg -> counter.incrementAndGet(), arg -> counter.incrementAndGet(), arg -> counter.incrementAndGet());

        event.trigger();

        Assert.assertEquals(3, counter.get());
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllListenersArrayNullContained() {
        new SimpleEvent<>().addAllListeners(o -> {}, null, o -> {});
    }

    @Test
    public void testAddAllListenersCollection() {
        Event<Void> event = new SimpleEvent<>();
        AtomicInteger counter = new AtomicInteger();

        event.addAllListeners(Arrays.asList(arg -> counter.incrementAndGet(), arg -> counter.incrementAndGet(), arg -> counter.incrementAndGet()));

        event.trigger();

        Assert.assertEquals(3, counter.get());
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllListenersCollectionNull() {
        new SimpleEvent<>().addAllListeners((Collection<Consumer<Object>>) null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllListenersCollectionNullContained() {
        new SimpleEvent<>().addAllListeners(Arrays.asList(o -> {}, null, o -> {}));
    }

    @Test
    public void testDuplicateListener() {
        Event<Void> event = new SimpleEvent<>();
        AtomicInteger counter = new AtomicInteger();
        Consumer<Void> listener = arg -> counter.incrementAndGet();

        event.addListener(listener);
        event.addListener(listener);

        event.trigger();

        Assert.assertEquals(2, counter.get());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveListenerNull() {
        new SimpleEvent<>().removeListener(null);
    }

    @Test
    public void testRemoveAllOccurrences() {
        Event<Void> event = new SimpleEvent<>();
        AtomicInteger counter = new AtomicInteger();
        Consumer<Void> listener = arg -> counter.incrementAndGet();

        event.addAllListeners(listener, listener, listener);
        event.removeAllOccurrences(listener);

        event.trigger();

        Assert.assertEquals(0, counter.get());
    }

}
