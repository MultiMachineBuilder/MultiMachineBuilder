/**
 * 
 */
package monniasza.collects.alloc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

import com.google.common.collect.Iterators;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;

/**
 * @author oskar
 * @param <T> 
 * An implementation of the allocator.
 * Exceptions thrown by listeners are forwarded to the {@code exceptionHandler} object in this allocator
 * The {@code exceptionHandler} by default forwards exceptions to the thread's current UncaughtExceptionHandler, where thread is the thread used to run listeners
 */
public class SimpleAllocator<T> implements Allocator<T> {
	
	private Consumer<Exception> exceptionHandler = (ex -> Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex));

	private ArrayList<Node> data = new ArrayList<>();
	private IntList free = new IntArrayList();
	
	private class Node{
		Node(T value) {
			super();
			this.value = value;
		}
		T value() {
			return value;
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
			data.add(new Node(obj));
		}else {
			//Reuse the value
			IntIterator iterator = free.intIterator();
			next = iterator.nextInt();
			iterator.remove();
		}
		Node node = new Node(obj);
		data.add(node);
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
		data.set(id, null);
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
		if(id >= data.size()) return false;
		return data.get(id) != null;
	}

	@Override
	public T get(int id) {
		Node result = data.get(id);
		if(result == null) return null;
		return result.value;
	}

	@Override
	public int size() {
		return data.size();
	}

	@SuppressWarnings("null")
	@Override
	public Iterator<T> iterator() {
		return Iterators.transform(Iterators.filter(data.iterator(), Objects::nonNull), Node::value);
	}

}
