/**
 * 
 */
package monniasza.collects.alloc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import monniasza.collects.Allocator;

/**
 * @author oskar
 * @param <T> 
 * An implementation of the allocator.
 * Exceptions thrown by listeners are forwarded to the {@code exceptionHandler} object in this allocator
 * The {@code exceptionHandler} by default forwards exceptions to the thread's current UncaughtExceptionHandler, where thread is the thread used to run listeners
 */
public class SimpleAllocator<T extends AllocationListener<T>> implements Allocator<T> {
	
	private Consumer<Exception> exceptionHandler = (ex -> Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex));

	private Int2ObjectMap<Node> data = new Int2ObjectOpenHashMap<>();
	private IntSet free = new IntOpenHashSet();
	
	private class Node{
		Node(T value) {
			super();
			this.value = value;
			listeners.add(value);
		}
		final T value;
		List<AllocationListener<T>> listeners = new ArrayList<>();
	}
	
	@Override
	public int allocate(T obj) {
		int next;
		if(free.isEmpty()) {
			//Generate new values
			next = data.size();
		}else {
			//Reuse the value
			IntIterator iterator = free.intIterator();
			next = iterator.nextInt();
		}
		Node node = new Node(obj);
		data.put(next, node);
		for(AllocationListener<T> listener: listeners) {
			try {
				listener.allocated(next, obj);
			}catch(Exception e) {
				exceptionHandler.accept(e);
			}
		}
		return next;
	}

	@Override
	public void destroy(int id) {
		if(!isAllocated(id)) throw new NoSuchElementException("Index "+id+" does not exist");
		Node result = data.get(id);
		T value = null;
		if(result != null) value = result.value;
		
		for(AllocationListener<T> listener: listeners) {
			try {
				listener.deallocated(id, value);
			}catch(Exception e) {
				exceptionHandler.accept(e);
			}
		}
		if(result != null) for(AllocationListener<T> listener: result.listeners) {
			try {
				listener.deallocated(id, value);
			}catch(Exception e) {
				exceptionHandler.accept(e);
			}
		}
	}

	private List<AllocationListener<T>> listeners = new ArrayList<>();
	@Override
	public void addAllocationListener(AllocationListener<T> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeAllocationListener(AllocationListener<T> listener) {
		listeners.remove(listener);
	}

	@Override
	public void addSpecificAllocationListener(AllocationListener<T> listener, int index) {
		Node result = data.get(index);
		if(result != null) result.listeners.add(listener);
	}

	@Override
	public void removeSpecificAllocationListener(AllocationListener<T> listener, int index) {
		Node result = data.get(index);
		if(result != null) result.listeners.remove(listener);
	}

	/**
	 * @return current exception handler
	 */
	public Consumer<Exception> getExceptionHandler() {
		return exceptionHandler;
	}

	/**
	 * @param exceptionHandler new exception handler
	 */
	public void setExceptionHandler(Consumer<Exception> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public boolean isAllocated(int id) {
		return data.containsKey(id);
	}

	@Override
	public T get(int id) {
		Node result = data.get(id);
		if(result == null) return null;
		return result.value;
	}

}
