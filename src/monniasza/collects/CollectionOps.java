/**
 * 
 */
package monniasza.collects;

import java.util.function.Predicate;

/**
 * Functional-style operations on collections
 * @author oskar
 */
public class CollectionOps {
	private CollectionOps() {}
	
	//Collection to boolean
	/**
	 * Checks if all values meet a predicate
	 * @param <T> type of values
	 * @param collection collection to test
	 * @param predicate condition
	 * @return do all items meet a criterion?
	 */
	public static <T> boolean isAll(Iterable<? extends T> collection, Predicate<T> predicate) {
		for(T value: collection) 
			if(!predicate.test(value)) return false;
		return true;
	}
	/**
	 * Checks if any value meets a predicate
	 * @param <T> type of values
	 * @param collection collection to test
	 * @param predicate condition
	 * @return does any items meet a criterion?
	 */
	public static <T> boolean isAny(Iterable<? extends T> collection, Predicate<T> predicate) {
		for(T value: collection) 
			if(predicate.test(value)) return true;
		return false;
	}
}
