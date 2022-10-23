/**
 * 
 */
package monniasza.collects.indexar;

import java.util.Set;

import com.google.common.collect.SetMultimap;

/**
 * An abstract implementation of the index
 * @author oskar
 * @param <T> type of the indexed objects
 * @param <U> type of the property
 * @param <M> type of the multimap. Allows to provide more sorted multimaps for optional sorting
 */
public interface Index<T, U, M extends SetMultimap<U, T>> {
	/**
	 * Adds value to the index
	 * @param value value to add
	 * @return was the value added?
	 */
	public boolean add(T value);
	/**
	 * Removed the value from the index
	 * @param value value to remove
	 * @return was the value removed?
	 */
	public boolean remove(T value);
	/**
	 * Checks the value if it may be placed
	 * @param value value to check
	 * @return is value allowed to be placed
	 */
	public boolean test(T value);
	/**
	 * Clears the index
	 */
	public void clear();
	/**
	 * @return read-only representation of indexed values
	 */
	public M multimap();
	/**
	 * Tests if the property value is present
	 * @param value value to test
	 * @return is the property value present?
	 */
	public default boolean containsTo(U value) {
		Set<T> set = multimap().get(value);
		return set != null && !set.isEmpty();
	}
}
