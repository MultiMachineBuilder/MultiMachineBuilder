/**
 * 
 */
package monniasza.collects.datalayer;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Iterators;

import monniasza.collects.alloc.AllocationListener;
import monniasza.collects.alloc.Allocator;
import monniasza.collects.alloc.Indexable;

/**
 * A specialized map used by data layers
 * @author oskar
 * @param <K> type of keys
 * @param <V> type of values
 */                                                                //not overriding equals(), because the existing implementation meets a contract
public class IndexedDatalayerMap<@Nonnull K extends Indexable, V> extends AbstractMap<K, V>{//NOSONAR ^^^
	private final ArrayList<V> values0;
	private final Function<K, V> populator;
	private final Allocator<K> allocator;
	
	public IndexedDatalayerMap(Allocator<K> allocator, Function<K, V> populator){
		values0 = new ArrayList<>(allocator.size());
		this.populator = populator;
		AllocationListener<K> listener = new Listener();
		this.allocator = allocator;
		allocator.addAllocationListener(listener);
		
		//Populate the array list
		for(int i = 0; i < allocator.size(); i++) {
			K key = allocator.get(i);
			V value = key==null?populator.apply(key):null;
			values0.add(value);
		}
	}
	
	private class Listener implements AllocationListener<@Nonnull K>{
		@Override
		public void allocated(int index, K obj) {
			if(index == values0.size()) 
				values0.add(populator.apply(obj));
			else
				values0.set(index, populator.apply(obj));
		}

		@Override
		public void deallocated(int index, K obj) {
			//unused
		}
	}
	private class KeySet extends AbstractSet<K>{
		@SuppressWarnings("null")
		@Override
		public @Nonnull Iterator<K> iterator() {
			return Iterators.unmodifiableIterator(allocator.iterator());
		}

		@Override
		public int size() {
			return allocator.size();
		}

		@Override
		public boolean contains(@Nullable Object o) {
			return containsKey(o);
		}
	}
	private class EntrySet extends AbstractSet<Entry<K, V>>{
		@SuppressWarnings("null")
		@Override
		public @Nonnull Iterator<Entry<K, V>> iterator() {
			return Iterators.transform(allocator.iterator(), key -> new AbstractMap.SimpleImmutableEntry<>(key, get(key)));
		}

		@Override
		public int size() {
			return allocator.size();
		}

		@Override
		public boolean contains(@Nullable Object o) {
			return containsKey(o);
		}
	}
	
	@Override
	public Set<Entry<K, V>> entrySet() {
		return new EntrySet();
	}

	/**
	 * Checks if this map contains a key.
	 */
	@Override
	public boolean containsKey(@Nullable Object key) {
		if(!(key instanceof Indexable)) return false;
		Indexable k0 = (Indexable) key;
		if(k0.index() != allocator) return false;
		return allocator.isAllocated(k0.ordinal());
	}

	/**
	 * @param key key to check
	 * @return index of given key, or -1 if not found
	 */
	public int indexof(@Nullable Object key) {
		if(!(key instanceof Indexable)) return -1;
		Indexable k0 = (Indexable) key;
		if(k0.index() != allocator) return -1;
		return k0.ordinal();
	}
	
	@Override
	@Nullable public V get(@Nullable Object key) {
		int index = indexof(key);
		if(index >= 0) return values0.get(index);
		return null;
	}

	@Override
	public V put(K key, V value) {
		int idx = indexof(key);
		if(idx < 0) return null;
		V old = values0.get(idx);
		values0.set(idx, value);
		return old;	
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for(Entry<? extends K, ? extends V> e : m.entrySet()) {
			int idx = indexof(e.getKey());
			if(idx < 0) continue;
			values0.set(idx, e.getValue());
		}
	}

	@Override
	public Set<K> keySet() {
		return new KeySet();
	}
	
}
