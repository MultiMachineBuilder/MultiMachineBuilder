/**
 * 
 */
package monniasza.collects.alloc;

import java.util.EventListener;

import mmb.Nil;

/**
 * @author oskar
 * Defines an event listener for allocation events
 * @param <T> type of objects listened to
 */
public interface AllocationListener<T> extends EventListener {
	/**
	 * Invoked when an object is deallocated
	 * @param index integer identifier of allocation
	 * @param obj the object allocated
	 */
	public void allocated(int index, @Nil T obj);
	/**
	 * @param index integer identifier of deallocated object
	 * @param obj the object in the slot before deallocation
	 */
	public void deallocated(int index, @Nil T obj);
}
