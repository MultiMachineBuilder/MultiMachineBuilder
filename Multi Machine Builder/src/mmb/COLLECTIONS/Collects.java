/**
 * 
 */
package mmb.COLLECTIONS;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import mmb.Identifiable;

/**
 * @author oskar
 *
 */
public class Collects {
	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> downcastIterator(Iterator<? extends T> iter) {
		return (Iterator<T>) iter;
	}
	@SuppressWarnings("unchecked")
	public static <T> Set<T> downcastSet(Set<? extends T> set){
		return (Set<T>) set;
	}
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> downcastCollection(Collection<? extends T> collect){
		return (Collection<T>) collect;
	}
	
	public <T> HashSet<T> newUnion(Collection<? extends T> a, Collection<? extends T> b){
		HashSet<T> set = new HashSet<>(a);
		set.addAll(b);
		return set;
	}
	public <T> HashSet<T> newIntersection(Collection<? extends T> a, Collection<? extends T> b){
		HashSet<T> set = new HashSet<>(a);
		set.retainAll(b);
		return set;
	}
	public <T> HashSet<T> newSubtrSet(Collection<? extends T> a, Collection<? extends T> b){
		HashSet<T> set = new HashSet<>(a);
		set.removeAll(b);
		return set;
	}
	public <T> HashSet<T> newXORSet(Collection<? extends T> a, Collection<? extends T> b){
		HashSet<T> set = new HashSet<>(a);
		set.addAll(b);
		set.removeAll(newIntersection(a, b));
		return set;
	}
	public <T> HashSet<T> newMultiUnion(Collection<? extends T> a, Collection<? extends T>... b){
		HashSet<T> set = new HashSet<>(a);
		for(Collection<? extends T> c: b) {
			set.addAll(c);
		}
		return set;
	}
	public <T> HashSet<T> newMultiIntersection(Collection<? extends T> a, Collection<? extends T>... b){
		HashSet<T> set = new HashSet<>(a);
		for(Collection<? extends T> c: b) {
			set.retainAll(c);
		}
		return set;
	}
	public <T> HashSet<T> newMultiSub(Collection<? extends T> a, Collection<? extends T>... b){
		HashSet<T> set = new HashSet<>(a);
		for(Collection<? extends T> c: b) {
			set.removeAll(c);
		}
		return set;
	}
	public static <K, V extends Identifiable<K>> SelfSet<K, V> unmodifiableSelfSet(SelfSet<K, V> set){
		return new SelfSet<K, V>() {
			@Override
			public boolean add(V e) {
				return false;
			}

			@Override
			public boolean addAll(Collection<? extends V> c) {
				return false;
			}

			@Override
			public void clear() {}

			@Override
			public boolean containsAll(Collection<?> c) {
				return set.containsAll(c);
			}

			@Override
			public boolean isEmpty() {
				return set.isEmpty();
			}

			@Override
			public Iterator<V> iterator() {
				return set.iterator();
			}

			@Override
			public boolean removeAll(Collection<?> c) {
				return false;
			}

			@Override
			public boolean retainAll(Collection<?> c) {
				return false;
			}

			@Override
			public int size() {
				return set.size();
			}

			@Override
			public Object[] toArray() {
				return set.toArray();
			}

			@Override
			public <T> T[] toArray(T[] a) {
				return set.toArray(a);
			}

			@Override
			public Set<K> keys() {
				return Collections.unmodifiableSet(set.keys());
			}

			@Override
			public Set<V> values() {
				return Collections.unmodifiableSet(set.values());
			}

			@Override
			public V get(K key) {
				return set.get(key);
			}

			@Override
			public V getOrDefault(K key, V defalt) {
				return set.getOrDefault(key, defalt);
			}

			@Override
			public boolean removeKey(K key) {
				return false;
			}

			@Override
			public boolean containsKey(K key) {
				return set.containsKey(key);
			}
		};
	}
}
