/**
 * 
 */
package monniasza.collects;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class Collects {
	@SuppressWarnings("unchecked")
	@Nonnull public static <T> Iterator<T> downcastIterator(Iterator<? extends T> iter) {
		return (Iterator<T>) iter;
	}

	@Nonnull public static <T> HashSet<T> newUnion(Collection<? extends T> a, Collection<? extends T> b){
		HashSet<T> set = new HashSet<>(a);
		set.addAll(b);
		return set;
	}
	@Nonnull public static <T> HashSet<T> newIntersection(Collection<? extends T> a, Collection<? extends T> b){
		HashSet<T> set = new HashSet<>(a);
		set.retainAll(b);
		return set;
	}
	@Nonnull public static <T> HashSet<T> newSubtrSet(Collection<? extends T> a, Collection<? extends T> b){
		HashSet<T> set = new HashSet<>(a);
		set.removeAll(b);
		return set;
	}
	@Nonnull public static <T> HashSet<T> newXORSet(Collection<? extends T> a, Collection<? extends T> b){
		HashSet<T> set = new HashSet<>(a);
		set.addAll(b);
		set.removeAll(newIntersection(a, b));
		return set;
	}
	@SafeVarargs @Nonnull 
	public static <T> HashSet<T> newMultiUnion(Collection<? extends T> a, Collection<? extends T>... b){
		HashSet<T> set = new HashSet<>(a);
		for(Collection<? extends T> c: b) {
			set.addAll(c);
		}
		return set;
	}
	@SafeVarargs @Nonnull 
	public static <T> HashSet<T> newMultiIntersection(Collection<? extends T> a, Collection<? extends T>... b){
		HashSet<T> set = new HashSet<>(a);
		for(Collection<? extends T> c: b) {
			set.retainAll(c);
		}
		return set;
	}
	@SafeVarargs @Nonnull 
	public static <T> HashSet<T> newMultiSub(Collection<? extends T> a, Collection<? extends T>... b){
		HashSet<T> set = new HashSet<>(a);
		for(Collection<? extends T> c: b) {
			set.removeAll(c);
		}
		return set;
	}
	@Nonnull public static <K, V extends Identifiable<K>> SelfSet<K, V> unmodifiableSelfSet(SelfSet<? extends K, ? extends V> set){
		return new SelfSet<K, V>() {
			@Override
			public boolean add(@SuppressWarnings("null") V e) {
				return false;
			}

			@Override
			public boolean addAll(@SuppressWarnings("null") Collection<? extends V> c) {
				return false;
			}

			@Override
			public void clear() {}

			@Override
			public boolean containsAll(@SuppressWarnings("null") Collection<?> c) {
				return set.containsAll(c);
			}

			@Override
			public boolean isEmpty() {
				return set.isEmpty();
			}

			@Override
			public Iterator<V> iterator() {
				return downcastIterator(set.iterator());
			}

			@Override
			public boolean removeAll(@SuppressWarnings("null") Collection<?> c) {
				return false;
			}

			@Override
			public boolean retainAll(@SuppressWarnings("null") Collection<?> c) {
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
			public <T> T[] toArray(@SuppressWarnings("null") T[] a) {
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
			public V get(@Nullable K key) {
				return ((SelfSet<K, V>)set).get(key);
			}

			@Override
			public V getOrDefault(@Nullable K key, V defalt) {
				return ((SelfSet<K, V>)set).getOrDefault(key, defalt);
			}

			@Override
			public boolean removeKey(K key) {
				return false;
			}

			@Override
			public boolean containsKey(@Nullable Object key) {
				return set.containsKey(key);
			}
		};
	}
	/**
	 * Converts an {@link Iterator} to an {@link Iterable}
	 * @param <T> type of iterator
	 * @param iter iterator to be converted
	 * @return wrapped iterator object
	 */
	@Nonnull public static <T> Iterable<T> iter(Iterator<T> iter){
		return () -> iter;
	}
	/**
	 * Converts an {@link Enumeration} to an {@link Iterable}
	 * @param <T> type of enumeration
	 * @param iter enumeration to be converted
	 * @return wrapped enumeration object
	 */
	public static <T> Iterable<T> iter(Enumeration<T> iter) {
		return iter::asIterator;
	}
	/**
	 * Wraps the {@link ListModel} in a {@link List}, to allow Java Collections Framework operations to be used
	 * @param <T>
	 * @param list the ListModel to be wrapped
	 * @return the wrapped list
	 */
	@Nonnull public static <T> List<T> fromListModel(ListModel<T> list){
		return new AbstractList<T>() {
			@Override public T get(int index) {
				return list.getElementAt(index);
			}
			@Override public int size() {
				return list.getSize();
			}
		};
	}
	@Nonnull public static <T> ListModel<T> toListModel(List<T> list){
		return new ListModel<T>() {
			@Override
			public void addListDataListener(@SuppressWarnings("null") ListDataListener l) {
				throw new UnsupportedOperationException("ListModel from List does not support listeners");
			}
			@Override
			public T getElementAt(int index) {
				return list.get(index);
			}
			@Override
			public int getSize() {
				return list.size();
			}
			@Override
			public void removeListDataListener(@SuppressWarnings("null") ListDataListener l) {
				throw new UnsupportedOperationException("ListModel from List oes not support listeners");
			}
		};
	}
	@Nonnull public static <T> List<T> toWritableList(DefaultListModel<T> list){
		return new AbstractList<T>() {
			@Override
			public T get(int index) {
				return list.get(index);
			}
			@Override
			public int size() {
				return list.getSize();
			}
			@Override
			public void add(int index, @Nullable T element) {
				list.add(index, element);
			}
			@Override
			public boolean add(@Nullable T e) {
				list.addElement(e);
				return true;
			}
			@Override
			public T remove(int index) {
				return list.remove(index);
			}
			@Override
			public T set(int index, @SuppressWarnings("null") T e) {
				return list.set(index, e);
			}
			
		};
	}
	@Nonnull public static <T> DefaultListModel<T> newListModel(Collection<? extends T> list){
		DefaultListModel<T> model = new DefaultListModel<>();
		model.addAll(list);
		return model;
	}
	@Nonnull public static <T> List<T> inplaceAddLists(List<T> list, Collection<T> collect){
		list.addAll(collect);
		return list;
	}
	@Nonnull public static <T> List<T> ooplaceAddLists(Collection<T> a, Collection<T> b, Supplier<List<T>> supplier){
		List<T> list0 = supplier.get();
		list0.addAll(a);
		list0.addAll(b);
		return list0;
	}
	@Nonnull public static <T> BiFunction<Collection<T>, Collection<T>, List<T>> ooplaceListAdder(Supplier<List<T>> supplier){
		return (a, b) -> ooplaceAddLists(a, b, supplier);
	}
}