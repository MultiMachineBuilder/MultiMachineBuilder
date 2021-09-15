/**
 * 
 */
package monniasza.collects.map;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author oskar
 * A map with unmodifiable keys, but modifiable values
 */
public class UnmodifiableKeysMap<K, V> implements Map<K, V> {
	
	private final Map<K, V> map;
	public UnmodifiableKeysMap(Map<? extends K, ? extends V> map) {
		this.map = new HashMap<>(map);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Umodifiable keys");
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return Collections.unmodifiableSet(map.entrySet());
	}

	@Override
	public V get(Object key) {
		return map.get(key);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return Collections.unmodifiableSet(map.keySet());
	}

	/**
	Associated the specified value with the specified key only if it already exists. 
	@param key key with which the specified value is to be associated
	@param value - value to be associated with the specified key
	@return the previous value associated with key, or null if there was no mapping for key.
	 */
	@Override
	public V put(K key, V value) {
		return map.replace(key, value);
	}

	/*
	 * Copies all of the mappings from the specified map to this map
	Associated the specified values with the specified key only if it already exists. 
	@param m
	@return the previous value associated with key, or null if there was no mapping for key.
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for(Entry<? extends K, ? extends V> ent: m.entrySet()) {
			put(ent.getKey(), ent.getValue());
		}
	}

	@Override
	public V remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<V> values() {
		return Collections.unmodifiableCollection(map.values());
	}

	@Override
	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		throw new UnsupportedOperationException("Umodifiable keys");
	}

	@Override
	public void forEach(BiConsumer<? super K, ? super V> action) {
		map.forEach(action);
	}

	@Override
	public V getOrDefault(Object key, V defaultValue) {
		return map.getOrDefault(key, defaultValue);
	}

	@Override
	public V putIfAbsent(K key, V value) {
		throw new UnsupportedOperationException("Umodifiable keys");
	}

	@Override
	public boolean remove(Object key, Object value) {
		throw new UnsupportedOperationException("Umodifiable keys");
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		return map.replace(key, oldValue, newValue);
	}

	@Override
	public V replace(K key, V value) {
		return map.replace(key, value);
	}

	@Override
	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
		// TODO Auto-generated method stub
		Map.super.replaceAll(function);
	}

}
