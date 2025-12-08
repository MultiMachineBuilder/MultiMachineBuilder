/**
 * 
 */
package monniasza.collects.indexar;

import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import mmb.annotations.NN;

/**
 * An index, where property is a set of values and may repeat between objects
 * @param <T> type of indexed objects
 * @param <U> type of indexed properties
 * @author oskar
 */
public class ManyToManyIndex<T, U> implements Index<T, U, SetMultimap<U, T>> {
	/** Function which defines a property of an object */
	@NN public final Function<T, Set<U>> fn;
	@NN private final SetMultimap<U, T> multimap = HashMultimap.create();
	@NN private final SetMultimap<U, T> pmultimap = Multimaps.unmodifiableSetMultimap(multimap);
	
	public ManyToManyIndex(Function<T, Set<U>> fn) {
		this.fn = fn;
	}
	
	@Override
	public boolean add(T value) {
		Set<U> set = fn.apply(value);
		boolean result = false;
		for(U key: set) {
			result |= multimap.put(key, value);
		}
		return result;
	}

	@Override
	public boolean remove(T value) {
		Set<U> set = fn.apply(value);
		boolean result = false;
		for(U key: set) {
			result |= multimap.remove(key, value);
		}
		return result;
	}

	@Override
	public boolean test(T value) {
		return true;
	}

	@Override
	public SetMultimap<U, T> multimap() {
		return pmultimap;
	}

	@Override
	public void clear() {
		multimap.clear();
	}

}
