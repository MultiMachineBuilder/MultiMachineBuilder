/**
 * 
 */
package monniasza.collects.indexar;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

/**
 * An index, where property is a single value and may not repeat between objects
 * @author oskar
 * @param <T> type of indexed objects
 * @param <U> type of indexed properties
 */
public class OneToOneIndex<T, U> implements Index<T, U, SetMultimap<U, T>> {
	/** Function which defines a property of an object */
	@Nonnull public final Function<T, U> fn;
	@Nonnull private final Map<U, T> map0 = new HashMap<>();
	@Nonnull public final Map<U, T> map = Collections.unmodifiableMap(map0);
	
	/**
	 * Creates a 1-1 index
	 * @param fn property to be indexed
	 */
	public OneToOneIndex(Function<T, U> fn) {
		this.fn = fn;
	}

	@Override
	public boolean add(T value) {
		return map.putIfAbsent(fn.apply(value), value) == null;
	}
	
	@Override
	public boolean test(T value) {
		return !map.containsKey(fn.apply(value));
	}

	@Override
	public boolean remove(T value) {
		return map.remove(fn.apply(value)) != null;
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
