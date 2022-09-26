/**
 * 
 */
package monniasza.collects.selfset;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public class TreeSelfSet<K, V extends Identifiable<K>> implements SortedSelfSet<K, V> {
	private final TreeMap<K, V> map = new TreeMap<>();
	private final Collection<V> vals = map.values();
	private final Set<K> keys = map.keySet();

	@SuppressWarnings("null")
	@Override
	public boolean add(@Nonnull V e) {
		return map.put(e.id(), e) == null;
	}

	@SuppressWarnings("null")
	@Override
	public boolean addAll(@Nullable Collection<? extends V> c) {
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
	public boolean containsAll(@Nullable Collection<?> c) {
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
	public boolean removeAll(@Nullable Collection<?> c) {
		return keys.removeAll(c) || vals.removeAll(c);
	}

	private class Bool{boolean val = false;}
	@SuppressWarnings("null")
	@Override
	public boolean retainAll(@Nullable Collection<?> c) {
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
	public NavigableSet<K> keys() {
		return new NavigableSet<K>(){

			@Override
			public boolean add(@Nullable K e) {
				throw new UnsupportedOperationException("add() by key not supported");
			}

			@SuppressWarnings("null")
			@Override
			public boolean addAll(@Nullable Collection<? extends K> c) {
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
			public boolean removeAll(@Nullable Collection<?> c) {
				return keys.removeAll(c);
			}

			@SuppressWarnings("null")
			@Override
			public boolean retainAll(@Nullable Collection<?> c) {
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

			@Override
			public Comparator<? super K> comparator() {
				return map.comparator();
			}

			@Override
			public K first() {
				return map.firstKey();
			}

			@Override
			public K last() {
				return map.lastKey();
			}

			@Override
			public K ceiling(@Nullable K e) {
				return map.ceilingKey(e);
			}

			@Override
			public Iterator<K> descendingIterator() {
				return map.descendingKeySet().iterator();
			}

			@Override
			public NavigableSet<K> descendingSet() {
				return map.descendingKeySet();
			}

			@Override
			public K floor(@Nullable K e) {
				return map.floorKey(e);
			}

			@Override
			public SortedSet<K> headSet(@Nullable K toElement) {
				return headSet(toElement, false);
			}

			@Override
			public NavigableSet<K> headSet(@Nullable K toElement, boolean inclusive) {
				return map.headMap(toElement, false).navigableKeySet();
			}

			@Override
			public K higher(@Nullable K e) {
				return map.higherKey(e);
			}

			@Override
			public K lower(@Nullable K e) {
				return map.lowerKey(e);
			}

			@Override
			public K pollFirst() {
				return map.pollFirstEntry().getKey();
			}

			@Override
			public K pollLast() {
				return map.pollLastEntry().getKey();
			}

			@Override
			public SortedSet<K> subSet(@Nullable K fromElement, @Nullable K toElement) {
				return subSet(fromElement, true, toElement, false);
			}

			@Override
			public NavigableSet<K> subSet(@Nullable K fromElement, boolean fromInclusive, @Nullable K toElement, boolean toInclusive) {
				return map.subMap(fromElement, fromInclusive, toElement, toInclusive).navigableKeySet();
			}

			@Override
			public SortedSet<K> tailSet(@Nullable K fromElement) {
				return tailSet(fromElement, true);
			}

			@Override
			public NavigableSet<K> tailSet(@Nullable K fromElement, boolean inclusive) {
				return map.tailMap(fromElement, inclusive).navigableKeySet();
			}
			
		};
	}

	private final TreeSelfSet<K, V> that = this;
	@Override
	public Set<V> values() {
		return new Set<V>(){

			@Override
			public boolean add(@SuppressWarnings("null") V e) {
				map.put(e.id(), e);
				return true;
			}

			@Override
			public boolean addAll(@SuppressWarnings("null")Collection<? extends V> c) {
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
			public boolean containsAll(@SuppressWarnings("null") @Nullable Collection<?> c) {
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
			public boolean removeAll(@Nullable Collection<?> c) {
				return vals.removeAll(c);
			}

			@Override
			public boolean retainAll(@Nullable Collection<?> c) {
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
	public V get(@Nullable K key) {
		return map.get(key);
	}

	@Override
	public V getOrDefault(@Nullable K key, V defalt) {
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
