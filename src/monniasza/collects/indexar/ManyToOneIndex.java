/**
 * 
 */
package monniasza.collects.indexar;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import mmb.annotations.NN;

/**
 * An index, where property is a set of values and may not repeat between objects
 * @author oskar
 * @param <T> type of indexed objects
 * @param <U> type of indexed properties
 */
public class ManyToOneIndex<T, U> implements Index<T, U, SetMultimap<U, T>> {
	/** Function which defines a property of an object */
	@NN public final Function<T, Set<U>> fn;
	@NN private final Map<U, T> map0 = new HashMap<>();
	@NN public final Map<U, T> map = Collections.unmodifiableMap(map0);
	
	/**
	 * Creates a m-1 index
	 * @param fn property to be indexed
	 */
	public ManyToOneIndex(Function<T, Set<U>> fn) {
		this.fn = fn;
	}

	@Override
	public boolean add(T value) {
		boolean result = false;
		Set<U> set = fn.apply(value);
		for(U key: set) {
			result |= (map.put(key, value) != null);
		}
		return result;
	}
	
	@Override
	public boolean test(T value) {
		return !map.containsKey(fn.apply(value));
	}

	@Override
	public boolean remove(T value) {
		boolean result = false;
		Set<U> set = fn.apply(value);
		for(U key: set) {
			result |= (map.remove(key) != null);
		}
		return result;
	}

	@Override
	public SetMultimap<U, T> multimap() {
		return Multimaps.forMap(map);
	}

	@Override
	public void clear() {
		map0.clear();
	}

	
}
