/**
 * 
 */
package monniasza.collects.selfset;

import java.util.Set;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import monniasza.collects.Identifiable;

/**
 * The SelfSet is a collection of unique elements where each element is identified either by its {@link Identifiable#id() id()} function, or by a function specified in the constructor.
 * Items must have distinct identifiers, since they are considered equal here if they share the identifier.
 * Also, the SelfSet is reified because objects can be identified if the ID method supports the type.
 * @param <K> the key type
 * @param <V> the value type
 */
public interface SelfSet<K, V> extends Set<V>{
	/**
	 * @return keys contained in the self-set
	 */
	@NN public Set<K> keys();
	
	/**
	 * @return values contained in the self-set
	 */
	@NN public Set<V> values();
	
	/**
	 * Get the value under a given key
	 * @param key key
	 * @return value under the key, or null if absent
	 */
	@Nil public V get(@Nil Object key);
	/**
	 * Get the value under a given key
	 * @param key key
	 * @param defalt default value
	 * @return value under the key, or default if absent
	 */
	public V getOrDefault(@Nil Object key, V defalt);
	
	/**
	 * Remove given key from the self-set
	 * @param key
	 * @return did the self-set change?
	 */
	public boolean removeKey(K key);
	
	public boolean containsKey(@Nil Object key);

	@Override
	/**
	 * Checks if given self-set contains given key
	 * The input must be of type <V>, because it is casted to {@link Identifiable} internally
	 */
	default boolean contains(@Nil Object arg0) {
		if(arg0 == null) return containsKey(null);
		return test(arg0) && containsKey(id(arg0));
	}

	@Override
	/**
	 * Removes given value from the self-set.
	 * The input must be of type <V>, because it is casted to {@link Identifiable} internally
	 */
	default boolean remove(@Nil Object arg0) {
		return test(arg0) && removeKey(id(arg0));
	}
	
	/**
	 * Checks if the object is supported
	 * @param o
	 * @return is the provided object a supported value
	 */
	public boolean test(@Nil Object o);
	/**
	 * @param value
	 * @return identifier for purposes of this self-set
	 */
	public K id(@Nil Object value);
	/**
	 * @return is this self-set nullable?
	 */
	public boolean nullable();
	/**
	 * @return the type of this self-set, or null if unrestricted
	 */
	@Nil public Class<V> type();
}
