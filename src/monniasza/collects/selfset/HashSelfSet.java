/**
 * 
 */
package monniasza.collects.selfset;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import monniasza.collects.Identifiable;

/**
 * @author oskar
 * @param <K> the key type
 * @param <V> the value type
 *
 */
public class HashSelfSet<K, V extends Identifiable<K>> implements SelfSet<K, V> {

	private final HashMap<K, V> map = new HashMap<>();
	private final Collection<V> vals = map.values();
	private final Set<K> keys = map.keySet();

	@SuppressWarnings("null")
	@Override
	public boolean add(@Nonnull V e) {
		return map.put(e.id(), e) == null;
	}

	@SuppressWarnings("null")
	@Override
	public boolean addAll(Collection<? extends V> c) {
		for(V value: c) {
			map.put(value.id(), value);
		}
		return true;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean contains(@Nullable Object o) {
		return map.containsKey(o) || map.containsValue(o);
	}

	@SuppressWarnings("null")
	@Override
	public boolean containsAll(Collection<?> c) {
		return keys.containsAll(c) || vals.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Iterator<V> iterator() {
		return vals.iterator();
	}

	@Override
	public boolean remove(@Nullable Object o) {
		return keys.remove(o) || vals.remove(o);
	}

	@SuppressWarnings("null")
	@Override
	public boolean removeAll(Collection<?> c) {
		return keys.removeAll(c) || vals.removeAll(c);
	}

	private class Bool{boolean val = false;}
	@SuppressWarnings("null")
	@Override
	public boolean retainAll(Collection<?> c) {
		Bool changed = new Bool();
		map.entrySet().removeIf(e ->{
			if(c.contains(e.getKey())) return false;
			if(c.contains(e.getValue())) return false;
			changed.val = true;
			return changed.val;
		});
		return true;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Object[] toArray() {
		return map.values().toArray();
	}

	@SuppressWarnings("null")
	@Override
	public <T> T[] toArray(T[] a) {
		return map.values().toArray(a);
	}

	@Override
	public Set<K> keys() {
		return new Set<K>(){

			@Override
			public boolean add(@Nullable K e) {
				throw new UnsupportedOperationException("add() by key not supported");
			}

			@SuppressWarnings("null")
			@Override
			public boolean addAll(Collection<? extends K> c) {
				throw new UnsupportedOperationException("addAll() by key not supported");
			}

			@Override
			public void clear() {
				map.clear();
			}

			@Override
			public boolean contains(@Nullable Object o) {
				return map.containsKey(o);
			}

			@Override
			public boolean containsAll(@Nullable Collection<?> c) {
				return keys.containsAll(c);
			}

			@Override
			public boolean isEmpty() {
				return map.isEmpty();
			}

			@Override
			public Iterator<K> iterator() {
				return keys.iterator();
			}

			@Override
			public boolean remove(@Nullable Object o) {
				return keys.remove(o);
			}

			@SuppressWarnings("null")
			@Override
			public boolean removeAll(Collection<?> c) {
				return keys.removeAll(c);
			}

			@SuppressWarnings("null")
			@Override
			public boolean retainAll(Collection<?> c) {
				return keys.retainAll(c);
			}

			@Override
			public int size() {
				return map.size();
			}

			@Override
			public Object[] toArray() {
				return keys.toArray();
			}

			@SuppressWarnings("null")
			@Override
			public <T> T[] toArray(T[] a) {
				return keys.toArray(a);
			}
			
		};
	}

	private final HashSelfSet<K, V> that = this;
	@Override
	public Set<V> values() {
		return new Set<V>(){

			@Override
			public boolean add(@SuppressWarnings("null") V e) {
				map.put(e.id(), e);
				return true;
			}

			@Override
			public boolean addAll(@SuppressWarnings("null") Collection<? extends V> c) {
				
				boolean changed = false;
				for(V value: c) {
					if(that.add(value)) changed = true;
				}
				return changed;
			}

			@Override
			public void clear() {
				that.clear();
			}

			@Override
			public boolean contains(@Nullable Object o) {
				return that.contains(o);
			}

			@Override
			public boolean containsAll(@SuppressWarnings("null") Collection<?> c) {
				return that.containsAll(c);
			}

			@Override
			public boolean isEmpty() {
				return map.isEmpty();
			}

			@Override
			public Iterator<V> iterator() {
				return vals.iterator();
			}

			@Override
			public boolean remove(@Nullable Object o) {
				return vals.remove(o);
			}

			@Override
			public boolean removeAll(@SuppressWarnings("null") Collection<?> c) {
				return vals.removeAll(c);
			}

			@Override
			public boolean retainAll(@SuppressWarnings("null") Collection<?> c) {
				return vals.retainAll(c);
			}

			@Override
			public int size() {
				return map.size();
			}

			@Override
			public Object[] toArray() {
				return vals.toArray();
			}

			@Override
			public <T> T[] toArray(@SuppressWarnings("null") T[] a) {
				return vals.toArray(a);
			}
			
		};
	}

	@Override
	public V get(@Nullable Object key) {
		return map.get(key);
	}

	@Override
	public V getOrDefault(@Nullable Object  key, V defalt) {
		return map.getOrDefault(key, defalt);
	}

	@Override
	public boolean removeKey(K key) {
		return map.remove(key) != null;
	}

	@Override
	public boolean containsKey(@Nullable Object key) {
		return map.containsKey(key);
	}

}
