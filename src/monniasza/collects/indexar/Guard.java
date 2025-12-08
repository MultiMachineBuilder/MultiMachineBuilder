/**
 * 
 */
package monniasza.collects.indexar;

import java.util.function.Predicate;

import com.google.common.collect.SetMultimap;

import mmb.annotations.NN;
import monniasza.collects.Collects;

/**
 * Imposes restrictions on contents of a database. Does not index values.
 * The predicate may throw exceptions if desired
 * @author oskar
 * @param <T> type of values
 */
public class Guard<T> implements Index<T, Object, SetMultimap<Object, T>> {

	/** Condition required for new objects in the database*/
	@NN public final Predicate<T> pred;
	/**
	 * Creates a guard
	 * @param pred condition required for new objects in the database
	 */
	public Guard(Predicate<T> pred) {
		this.pred = pred;
	}
	
	@Override
	public boolean add(T value) {
		return false;
	}

	@Override
	public boolean remove(Object value) {
		return false;
	}

	@Override
	public boolean test(T value) {
		return pred.test(value);
	}

	@Override
	public SetMultimap<Object, T> multimap() {
		return Collects.emptyMultimap();
	}

	
	@Override
	public void clear() {
		//does nothing
	}

	

}
