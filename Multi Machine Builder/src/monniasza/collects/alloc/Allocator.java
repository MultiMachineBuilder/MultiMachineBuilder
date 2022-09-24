/**
 * 
 */
package monniasza.collects.alloc;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;
import com.google.common.collect.Iterators;

/**
 * @author oskar
 * @param <T> type of allocated objects
 * An allocator offers fast, reliable and durable allocation of object IDs. The allocations may not be changed except for removing and replacing them.
 */
public interface Allocator<T> extends Iterable<T>{
	/**
	 * Allocates a slot in the allocator
	 * @param data object to be allocated
	 * @return allocated index
	 */
	public int allocate(T data);
	/**
	 * Destroys the allocation at given index and calls the listeners
	 * @param id index of allocation to be destroyed
	 */
	public void destroy(int id);
	/**
	 * Gets the object at given index
	 * @param id index to get from
	 * @return object at given index, or null if absent
	 */
	public T get(int id);
	/**
	 * @param id index to check
	 * @return is given index currently allocateed?
	 */
	public boolean isAllocated(int id);
	/**
	 * @return number of allocation slots (not allocated values)
	 */
	public int size();
	/**
	 * The iterator's remove() method can be used to destroy allocation slots
	 * @return an iterator over valid objects
	 */
	@Override
	public Iterator<T> iterator();
	
	//Global listeners
	/**
	 * Adds an allocation listener
	 * @param listener listener to be added
	 */
	public void addAllocationListener(AllocationListener<T> listener);
	/**
	 * Removes an allocation listener
	 * @param listener listener to be removed
	 */
	public void removeAllocationListener(AllocationListener<T> listener);
	/**
	 * Add an allocation listener to a specific position
	 * @param listener
	 * @param index index to listen to
	 * @throws NoSuchElementException when given index is absent
	 */
	public void addSpecificAllocationListener(AllocationListener<T>  listener, int index);
	/**
	 * Add an allocation listener to a specific position
	 * @param listener
	 * @param index index to listen to
	 * @throws NoSuchElementException when given index is absent
	 */
	public void removeSpecificAllocationListener(AllocationListener<T>  listener, int index);
	
	/**
	 * @return read-only allocator which mirrors this allocator.
	 * The returned allocator does not allow new allocations and changes to the exception handler
	 */
	@Nonnull public default Allocator<T> readonly(){
		final Allocator<T> alloc = this;
		return new Allocator<T>() {

			@Override
			public int allocate(T data) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void destroy(int id) {
				throw new UnsupportedOperationException();
			}

			@Override
			public T get(int id) {
				return alloc.get(id);
			}

			@Override
			public boolean isAllocated(int id) {
				return alloc.isAllocated(id);
			}

			@Override
			public void addAllocationListener(AllocationListener<T> listener) {
				alloc.addAllocationListener(listener);
			}

			@Override
			public void removeAllocationListener(AllocationListener<T> listener) {
				alloc.removeAllocationListener(listener);
			}

			@Override
			public void addSpecificAllocationListener(AllocationListener<T> listener, int index) {
				alloc.addSpecificAllocationListener(listener, index);
			}

			@Override
			public void removeSpecificAllocationListener(AllocationListener<T> listener, int index) {
				alloc.removeSpecificAllocationListener(listener, index);
			}
			
			@Override
			public Allocator<T> readonly(){return this;}

			@Override
			public int size() {
				return alloc.size();
			}

			@SuppressWarnings("null")
			@Override
			public @Nonnull Iterator<T> iterator() {
				return Iterators.unmodifiableIterator(alloc.iterator());
			}
			};
	}
}
