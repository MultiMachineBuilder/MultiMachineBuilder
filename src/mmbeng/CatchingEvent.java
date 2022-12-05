/**
 * 
 */
package mmbeng;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import com.pploder.events.Event;

import mmbeng.debug.Debugger;

/**
 * An implementation of an event, which catches and reports exceptions thrown by listeners
 * @author oskar (catch exceptions, nullability)
 * @author Philipp Ploder (original implementation)
 * @param <T> The argument type to be passed to the listeners. (copied from {@link Event})
 * @version 1.0.0
 * @since 1.1.0
 */
@SuppressWarnings({"null"})
public class CatchingEvent<T> implements Event<T> {
	private Debugger debug;
	private String msg;
	public CatchingEvent(Debugger debug, String msg) {
		this.debug = debug;
		this.msg = msg;
	}
    private final List<Consumer<T>> listeners = new CopyOnWriteArrayList<>();

	@Override
    public void addListener(Consumer<T> listener) throws NullPointerException {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public void addAllListeners(Consumer<T>... listeners) throws NullPointerException {
        addAllListeners(Arrays.asList(listeners));
    }

    @Override
    public void addAllListeners(Collection<Consumer<T>> listeners) throws NullPointerException {
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
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    @Override
    public void removeAllOccurrences(Consumer<T> listener) throws NullPointerException {
        synchronized (listeners) {
            listeners.removeIf(listener::equals);
        }
    }

    @Override
    public void trigger(T t) {
        listeners.forEach(listener -> {
        	try {
        		listener.accept(t);
        	}catch(Exception e) {
        		debug.pstm(e, msg);
        	}
        });
    }
    
    public void clear() {
    	listeners.clear();
    }
}
