/**
 * 
 */
package monniasza.collects.indexar;

import java.util.function.Function;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import mmb.annotations.NN;

/**
 * An index, where property is a single value and may repeat between objects
 * @param <T> type of indexed objects
 * @param <U> type of indexed properties
 * @author oskar
 */
public class OneToManyIndex<T, U> implements Index<T, U, SetMultimap<U, T>> {
	/** Function which defines a property of an object */
	@NN public final Function<T, U> fn;
	@NN private final SetMultimap<U, T> multimap = HashMultimap.create();
	@NN private final SetMultimap<U, T> pmultimap = Multimaps.unmodifiableSetMultimap(multimap);
	
	public OneToManyIndex(Function<T, U> fn) {
		this.fn = fn;
	}
	
	@Override
	public boolean add(T value) {
		U key = fn.apply(value);
		return multimap.put(key, value);
	}

	@Override
	public boolean remove(T value) {
		U key = fn.apply(value);
		return multimap.remove(key, value);
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
