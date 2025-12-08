/**
 * 
 */
package monniasza.collects.selfset;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import mmb.annotations.NN;
import mmb.annotations.Nil;

/**
 * An basic map-based self-set
 * @author oskar
 * @param <K> 
 * @param <V> 
 * @param <M> the type of map
 */
public class BaseMapSelfSet<K, V, M extends Map<K, V>> implements SelfSet<K, V>{
	@NN private final M map;
	private final boolean nullable;
	@Nil private final Class<V> type;
	@NN private final Function<V, K> fn;

	/**
	 * A complex self-set constructor. Usually not used by the user
	 * @param map 
	 * @param nullable is the self-set nullable
	 * @param type
	 * @param fn
	 */
	public BaseMapSelfSet(@NN M map, boolean nullable, Class<V> type, Function<V, K> fn) {
		this.map = map;
		this.nullable = nullable;
		this.type = type;
		this.fn = fn;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public @NN Iterator<V> iterator() {
		return map.values().iterator();
	}

	@Override
	public Object @NN [] toArray() {
		return map.values().toArray();
	}

	@Override
	public <T> T @NN [] toArray(T[] a) {
		return map.values().toArray(a);
	}

	@Override
	public boolean add(V e) {
		return map.put(id(e), e) == null;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object obj: c) {
			if(!test(obj)) return false;
			Object id = id(obj);
			if(!map.containsKey(id)) return false;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends V> c) {
		boolean result = false;
		for(V obj: c) {
			result |= add(obj);
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		AtomicBoolean changed = new AtomicBoolean();
		map.entrySet().removeIf(e ->{
			if(!c.contains(e.getValue())) return false;
			changed.set(true);
			return true;
		});
		return changed.get();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean removed = false;
		for(Object obj: c) {
			removed |= map.remove(obj) != null;
		}
		return removed;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<K> keys() {
		return map.keySet();
	}

	@Override
	public Set<V> values() {
		return this;
	}

	@Override
	public V get(@Nil Object key) {
		return map.get(key);
	}

	@Override
	public V getOrDefault(@Nil Object key, V defalt) {
		return map.getOrDefault(key, defalt);
	}

	@Override
	public boolean removeKey(K key) {
		return map.remove(key) != null;
	}

	@Override
	public boolean containsKey(@Nil Object key) {
		return map.containsKey(key);
	}

	
	@Override
	public boolean test(@Nil Object o) {
		if(o == null) return nullable;
		Class<V> cls = type;
		if(cls == null) return true;
		return cls.isInstance(o);
	}
	@SuppressWarnings("unchecked")
	@Override
	public K id(@Nil Object value) {
		return fn.apply((V) value);
	}
	@Override
	public boolean nullable() {
		return nullable;
	}
	@Override
	public Class<V> type() {
		return type;
	}	
}
