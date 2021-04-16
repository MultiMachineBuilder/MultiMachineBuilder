/**
 * 
 */
package mmb.COLLECTIONS;

import java.util.Set;

import javax.annotation.Nullable;

import mmb.BEANS.Identifiable;

/**
 * @author oskar
 * A SelfSet is a set, which uses object's identifiers to locate given object within a set
 * @param <K> the key type
 * @param <V> the value type
 */
public interface SelfSet<K, V extends Identifiable<K>> extends Set<V>{
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
	public V get(K key);
	/**
	 * Get the value under a given key
	 * @param key key
	 * @param defalt default value
	 * @return value under the key, or default if absent
	 */
	public V getOrDefault(K key, V defalt);
	
	/**
	 * Remove given key from the self-set
	 * @param key
	 * @return did the self-set change?
	 */
	public boolean removeKey(K key);
	
	public boolean containsKey(@Nullable K key);

	@SuppressWarnings({"unchecked", "sexy"})
	@Override
	/**
	 * Checks if given self-set contains given key
	 * The input must be of type <V>, because it is casted to {@link Identifiable} internally
	 */
	default boolean contains(@Nullable Object arg0) {
		if(arg0 == null) return containsKey(null);
		return arg0 instanceof Identifiable && containsKey(((V)arg0).id());
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * Removes given value from the self-set.
	 * The input must be of type <V>, because it is casted to {@link Identifiable} internally
	 */
	default boolean remove(@Nullable Object arg0) {
		return arg0 instanceof Identifiable && removeKey(((V)arg0).id());
	}
}
