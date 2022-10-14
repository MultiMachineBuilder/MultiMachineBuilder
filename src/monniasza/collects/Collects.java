/**
 * 
 */
package monniasza.collects;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;
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
	private Collects() {}
	
	
	/**
	 * Down-casts the iterator
	 * @param <T> type of the output iterator
	 * @param iter iterator
	 * @return downcasted iterator
	 */
	@SuppressWarnings("unchecked")
	@Nonnull public static <T> Iterator<T> downcastIterator(Iterator<? extends T> iter) {
		return (Iterator<T>) iter;
	}
	/**
	 * @param <K> type of keys of the output set
	 * @param <V> type of values of the output set
	 * @param set self-set to wrap
	 * @return an unmodifiable wrapper around the self-set
	 */
	@Nonnull public static <K, V extends Identifiable<K>> SelfSet<K, V> unmodifiableSelfSet(SelfSet<? extends K, ? extends V> set){
		return new SelfSet<K, V>() {
			@Override
			public boolean add(@SuppressWarnings("null") V e) {
				return false;
			}

			@Override
			public boolean addAll(Collection<? extends V> c) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void clear() {
				throw new UnsupportedOperationException();
			}

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
				return downcastIterator(set.iterator());
			}

			@Override
			public boolean removeAll(Collection<?> c) {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean retainAll(Collection<?> c) {
				throw new UnsupportedOperationException();
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
			public V get(@Nullable Object  key) {
				return set.get(key);
			}

			@SuppressWarnings("unchecked") //cast is required to accept a supertype, it will never fail
			@Override
			public V getOrDefault(@Nullable Object key, V defalt) {
				return ((SelfSet<K, V>)set).getOrDefault(key, defalt);
			}

			@Override
			public boolean removeKey(K key) {
				throw new UnsupportedOperationException();
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
	/**
	 * Wraps the list to a list model
	 * @param <T> type of the list
	 * @param list list to wrap
	 * @return a list model
	 * @apiNote The list model does not support listeners
	 */
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
	/**
	 * Wraps a list model
	 * @param <T> type of the list model
	 * @param list list model
	 * @return a list wrapper
	 */
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
			@Override
			public boolean isEmpty() {
				return list.isEmpty();
			}
			@Override
			public boolean contains(@Nullable Object o) {
				return list.contains(list);
			}
			@Override
			public @Nonnull Iterator<T> iterator() {
				return list.elements().asIterator();
			}
			@Override
			public Object @Nonnull [] toArray() {
				return list.toArray();
			}
			@Override
			public <U> U @Nonnull [] toArray(U[] a) {
				list.copyInto(a);
				return a;
			}
			@Override
			public boolean remove(Object o) {
				return list.removeElement(o);
			}
			@Override
			public boolean containsAll(Collection<?> c) {
				for(Object e: c) {
					if(!list.contains(e)) return false;
				}
				return true;
			}
			@Override
			public boolean addAll(Collection<? extends T> c) {
				list.addAll(c);
				return true;
			}
			@Override
			public boolean addAll(int index, Collection<? extends T> c) {
				list.addAll(index, c);
				return true;
			}
			@Override
			public boolean removeAll(Collection<?> c) {
				boolean result = false;
				for(Object e: c) {
					result |= list.removeElement(e);
				}
				return result;
			}
			@Override
			public boolean retainAll(Collection<?> c) {
				List<T> copy = new ArrayList<>(this);
				boolean result = copy.retainAll(c);
				list.addAll(copy);
				return result;
			}
			@Override
			public void clear() {
				list.removeAllElements();
			}
			@Override
			public int indexOf(@Nullable Object o) {
				return list.indexOf(o);
			}
			@Override
			public int lastIndexOf(@Nullable Object o) {
				return list.lastIndexOf(o);
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