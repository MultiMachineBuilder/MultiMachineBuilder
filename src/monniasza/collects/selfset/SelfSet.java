/**
 * 
 */
package monniasza.collects.selfset;

import java.util.Set;

import javax.annotation.Nullable;

import monniasza.collects.Identifiable;

/**
 * @author oskar
 * A SelfSet is a set, which uses object's identifiers to locate given object within a set
 * @param <K> the key type
 * @param <V> the value type
 */
public interface SelfSet<K, V> extends Set<V>{
	/**
	 * @return keys contained in the self-set
	 */
	public Set<K> keys();
	
	/**
	 * @return values contained in the self-set
	 */
	public Set<V> values();
	
	/**
	 * Get the value under a given key
	 * @param key key
	 * @return value under the key, or null if absent
	 */
	@Nullable public V get(@Nullable Object key);
	/**
	 * Get the value under a given key
	 * @param key key
	 * @param defalt default value
	 * @return value under the key, or default if absent
	 */
	public V getOrDefault(@Nullable Object key, V defalt);
	
	/**
	 * Remove given key from the self-set
	 * @param key
	 * @return did the self-set change?
	 */
	public boolean removeKey(K key);
	
	public boolean containsKey(@Nullable Object key);

	@SuppressWarnings({"unchecked"})
	@Override
	/**
	 * Checks if given self-set contains given key
	 * The input must be of type <V>, because it is casted to {@link Identifiable} internally
	 */
	default boolean contains(@Nullable Object arg0) {
		if(arg0 == null) return containsKey(null);
		return test(arg0) && containsKey(id((V)arg0));
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * Removes given value from the self-set.
	 * The input must be of type <V>, because it is casted to {@link Identifiable} internally
	 */
	default boolean remove(@Nullable Object arg0) {
		return test(arg0) && removeKey(id((V)arg0));
	}
	
	/**
	 * Checks if the object is supported
	 * @param o
	 * @return is the provided object a supported value
	 */
	public boolean test(@Nullable Object o);
	/**
	 * @param value
	 * @return identifier for purposes of this self-set
	 */
	public K id(Object value);
	/**
	 * @return is this self-set nullable?
	 */
	public boolean nullable();
	/**
	 * @return the type of this self-set, or null if unrestricted
	 */
	@Nullable public Class<V> type();
}
