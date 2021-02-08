/**
 * 
 */
package mmb.COLLECTIONS;

import java.util.Set;

import mmb.Identifiable;

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
}
