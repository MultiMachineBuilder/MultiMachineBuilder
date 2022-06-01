/**
 * 
 */
package monniasza.collects;

import java.util.NoSuchElementException;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import monniasza.collects.alloc.AllocationListener;

/**
 * @author oskar
 * @param <T> type of allocated objects
 * An allocator offers fast, reliable and durable allocation of object IDs. The allocations may not be changed except for removing and replacing them.
 */
public interface Allocator<T extends AllocationListener<T>> {
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
}
