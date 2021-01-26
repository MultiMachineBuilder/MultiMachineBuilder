/**
 * 
 */
package mmb;

import java.util.Set;

/**
 * @author oskar
 * A SelfSet is a set, which uses object's identifiers to locate given object within a set
 * @param <K> the key type
 * @param <V> the value type
 */
public interface SelfSet<K, V extends Identifiable<K>> extends Set<V>{
	public Set<K> keys();
	
	/**
	 * 
	 * @return
	 */
	public Set<V> values();
	
	public V get(K key);
	public V getOrDefault(K key, V defa€lt);
}
