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
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
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

import com.google.common.collect.Iterators;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.SetMultimap;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.item.ItemEntry;
import monniasza.collects.grid.Grid;
import monniasza.collects.selfset.SelfSet;

/**
 * A set of operations on collections used in MultiMachineBuilder
 * @author oskar
 */
public class Collects {
	private Collects() {}
	
	//Iterators
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
	@SuppressWarnings("null")
	public static <T> Iterable<T> iter(Enumeration<T> iter) {
		return iter::asIterator;
	}
	
	//List models
	/**
	 * Wraps the {@link ListModel} in a {@link List}, to allow Java Collections Framework operations to be used
	 * @param <T>
	 * @param list the ListModel to be wrapped
	 * @return the wrapped list
	 */
	@Nonnull public static <T> List<T> fromListModel(ListModel<T> list){
		return new AbstractList<>() {
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
		return new ListModel<>() {
			@Override
			public void addListDataListener(@Nullable ListDataListener l) {
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
			public void removeListDataListener(@Nullable ListDataListener l) {
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
		return new AbstractList<>() {
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
			public T set(int index, @SuppressWarnings("null") @Nullable T e) {
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
			@SuppressWarnings("null")
			@Override
			public @Nonnull Iterator<T> iterator() {
				return list.elements().asIterator();
			}
			@SuppressWarnings("null")
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
			public boolean remove(@Nullable Object o) {
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
	
	//Self-sets
	/**
	 * @param <K> type of keys of the output set
	 * @param <V> type of values of the output set
	 * @param set self-set to wrap
	 * @return an unmodifiable wrapper around the self-set
	 */
	@Nonnull public static <K, V> SelfSet<K, V> unmodifiableSelfSet(SelfSet<? extends K, ? extends V> set){
		return new SelfSet<>() {
			@Override
			public boolean add(@Nullable V e) {
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

			@Override
			public boolean test(@Nullable Object o) {
				return set.test(o);
			}

			@Override
			public K id(Object value) {
				return set.id(value);
			}

			@Override
			public boolean nullable() {
				return set.nullable();
			}

			@SuppressWarnings("unchecked")
			@Override
			public Class<V> type() {
				return (Class<V>) set.type();
			}
		};
	}
	
	//Lists
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
	@Nonnull public static <T> BiFunction<@Nonnull Collection<T>, @Nonnull Collection<T>, List<T>> ooplaceListAdder(Supplier<List<T>> supplier){
		return (a, b) -> ooplaceAddLists(a, b, supplier);
	}
	
	//Multimaps
	/**
	 * Creates an empty multimap
	 * @param <K> type of keys
	 * @param <V> type of values
	 * @return an empty, immutable multimap
	 */
	@SuppressWarnings("unchecked")
	@Nonnull public static <K, V> SetMultimap<K, V> emptyMultimap(){
		return (SetMultimap<K, V>) emptyMultiMap;
	}
	@Nonnull private static final SetMultimap<?, ?> emptyMultiMap = new EmptySetMultimap();
	/**
	 * Creates an empty multiset
	 * @param <T> type of values
	 * @return an empty, immutable multiset
	 */
	@SuppressWarnings("unchecked")
	@Nonnull public static <T> Multiset<T> emptyMultiset(){
		return (Multiset<T>) emptyMultiSet;
	}
	@Nonnull private static final Multiset<?> emptyMultiSet = new EmptyMultiSet();
	
	//Maps
	@Nonnull public static <T, M extends Object2IntMap<T>> Collector<Object2IntMap.Entry<T>, Object2IntMap<T>, M> collectToIntMap(Supplier<M> mapsup){
		return new IntMapCollector<>(mapsup);
	}
	@Nonnull public static <T, M extends Object2IntMap<T>> M inplaceAddIntMaps(M list, Object2IntMap<T> collect){
		list.putAll(collect);
		return list;
	}

	//Grids
	/**
	 * Prevents changes to a grid
	 * @param grid grid to prevent changes to
	 * @return an unmodifiable view of the input grid
	 */
	public static <T> Grid<T> unmodifiableGrid(Grid<T> grid) {
		return new Grid<>() {
			@Override
			public void set(int x, int y, T data) {
				throw new UnsupportedOperationException();
			}
			@Override
			public T get(int x, int y) {
				return grid.get(x, y);
			}
			@Override
			public int width() {
				return grid.width();
			}
			@Override
			public int height() {
				return grid.height();
			}
			@SuppressWarnings("null")
			@Override
			public Iterator<T> iterator() {
				return Iterators.unmodifiableIterator(grid.iterator());
			}	
		};
	}
	/**
	 * @param <Tin> type of the input grid
	 * @param <Tout> type of the output grid
	 * @param forward forward mapper
	 * @param backward backward mapper
	 * @param grid input grid
	 * @return a transformed view of the grid. The view is backed by the grid, so any changes made to the original are reflected in the view and vice versa
	 */
	public static <Tin, Tout> Grid<Tout> mapGrid(Function<? super Tin, ? extends Tout> forward, @Nullable Function<? super Tout, ? extends Tin> backward, Grid<Tin> grid){
		return new Grid<>() {
			@SuppressWarnings("null")
			@Override
			public void set(int x, int y, Tout data) {
				if(backward == null) throw new UnsupportedOperationException("no backwards function");
				grid.set(x, y, backward.apply(data));
			}
			@Override
			public Tout get(int x, int y) {
				return forward.apply(grid.get(x, y));
			}
			@Override
			public int width() {
				return grid.width();
			}
			@Override
			public int height() {
				return grid.height();
			}
			@SuppressWarnings("null")
			@Override
			public Iterator<Tout> iterator() {
				Iterator<Tin> iter = grid.iterator();
				//return Iterators.transform(iter, forward);
				return new Iterator<>() {

					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public Tout next() {
						return forward.apply(iter.next());
					}

					@Override
					public void remove() {
						iter.remove();
					}
				};
			}	
		};
	}


	//Iteration
	/**
	 * Gets the first item using iteration
	 * @param c collection to get items from
	 * @return the first item
	 * @throws NoSuchElementException when there is no requested item
	 */
	public static <T> T first(Iterable<T> c) {
		return c.iterator().next();
	}
}
class IntMapCollector<T, M extends Object2IntMap<T>> implements Collector<Object2IntMap.Entry<T>, Object2IntMap<T>, M>{
	@Nonnull private final Supplier<M> mapsup;

	public IntMapCollector(Supplier<M> mapsup) {
		this.mapsup = mapsup;
	}

	@Override
	public Supplier<Object2IntMap<T>> supplier() {
		return Object2IntOpenHashMap::new;
	}

	@Override
	public BiConsumer<Object2IntMap<T>, Object2IntMap.Entry<T>> accumulator() {
		return (map, entry) -> map.put(entry.getKey(), entry.getIntValue());
	}

	@Override
	public BinaryOperator<Object2IntMap<T>> combiner() {
		return Collects::inplaceAddIntMaps;
	}

	@SuppressWarnings("null")
	@Override
	public Function<Object2IntMap<T>, M> finisher() {
		return map -> {
			M result = mapsup.get();
			return Collects.inplaceAddIntMaps(result, map);
		};
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Set.of(Characteristics.UNORDERED);
	}
}
class EmptyMultiSet implements Multiset<Object>{

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public Object @Nonnull [] toArray() {
		return new Object[0];
	}

	@Override
	public <T> T @Nonnull [] toArray(T[] a) {
		return a;
	}

	@Override
	public boolean addAll(Collection<? extends Object> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public int count(Object element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int add(Object element, int occurrences) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(@Nullable Object element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int remove(Object element, int occurrences) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(@Nullable Object element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int setCount(Object element, int count) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setCount(Object element, int oldCount, int newCount) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Object> elementSet() {
		return Collections.emptySet();
	}

	@Override
	public Set<Entry<Object>> entrySet() {
		return Collections.emptySet();
	}

	@Override
	public Iterator<Object> iterator() {
		return Collections.emptyIterator();
	}

	@Override
	public boolean contains(@Nullable Object element) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> elements) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
}
class EmptySetMultimap implements SetMultimap<Object, Object>{

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public boolean containsEntry(Object key, Object value) {
		return false;
	}

	@Override
	public boolean put(Object key, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object key, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean putAll(Object key, Iterable<? extends Object> values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean putAll(Multimap<? extends Object, ? extends Object> multimap) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Object> replaceValues(Object key, Iterable<? extends Object> values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Object> removeAll(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Object> get(Object key) {
		return Collections.emptySet();
	}

	@Override
	public Set<Object> keySet() {
		return Collections.emptySet();
	}

	@Override
	public Multiset<Object> keys() {
		return Collects.emptyMultiset();
	}

	@Override
	public Collection<Object> values() {
		return Collections.emptyList();
	}

	@Override
	public Set<Entry<Object, Object>> entries() {
		return Collections.emptySet();
	}

	@Override
	public Map<Object, Collection<Object>> asMap() {
		return Collections.emptyMap();
	}
	
}