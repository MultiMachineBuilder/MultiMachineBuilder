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
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import com.google.common.collect.Iterators;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.SetMultimap;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;
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
	@NN public static <T> Iterator<T> downcastIterator(Iterator<? extends T> iter) {
		return (Iterator<T>) iter;
	}
	/**
	 * Converts an {@link Iterator} to an {@link Iterable}
	 * @param <T> type of iterator
	 * @param iter iterator to be converted
	 * @return wrapped iterator object
	 */
	@NN public static <T> Iterable<T> iter(Iterator<T> iter){
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
	@NN public static <T> List<T> fromListModel(ListModel<T> list){
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
	@NN public static <T> ListModel<T> toListModel(List<T> list){
		return new ListModel<>() {
			@Override
			public void addListDataListener(@Nil ListDataListener l) {
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
			public void removeListDataListener(@Nil ListDataListener l) {
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
	@NN public static <T> List<T> toWritableList(DefaultListModel<T> list){
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
			public void add(int index, @Nil T element) {
				list.add(index, element);
			}
			@Override
			public boolean add(@Nil T e) {
				list.addElement(e);
				return true;
			}
			@Override
			public T remove(int index) {
				return list.remove(index);
			}
			@Override
			public T set(int index, @SuppressWarnings("null") @Nil T e) {
				return list.set(index, e);
			}
			@Override
			public boolean isEmpty() {
				return list.isEmpty();
			}
			@Override
			public boolean contains(@Nil Object o) {
				return list.contains(list);
			}
			@SuppressWarnings("null")
			@Override
			public @NN Iterator<T> iterator() {
				return list.elements().asIterator();
			}
			@SuppressWarnings("null")
			@Override
			public Object @NN [] toArray() {
				return list.toArray();
			}
			@Override
			public <U> U @NN [] toArray(U[] a) {
				list.copyInto(a);
				return a;
			}
			@Override
			public boolean remove(@Nil Object o) {
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
			public int indexOf(@Nil Object o) {
				return list.indexOf(o);
			}
			@Override
			public int lastIndexOf(@Nil Object o) {
				return list.lastIndexOf(o);
			}
			
		};
	}
	@NN public static <T> DefaultListModel<T> newListModel(Collection<? extends T> list){
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
	@NN public static <K, V> SelfSet<K, V> unmodifiableSelfSet(SelfSet<? extends K, ? extends V> set){
		return new SelfSet<>() {
			@Override
			public boolean add(@Nil V e) {
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
			public V get(@Nil Object  key) {
				return set.get(key);
			}

			@SuppressWarnings("unchecked") //cast is required to accept a supertype, it will never fail
			@Override
			public V getOrDefault(@Nil Object key, V defalt) {
				return ((SelfSet<K, V>)set).getOrDefault(key, defalt);
			}

			@Override
			public boolean removeKey(K key) {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean containsKey(@Nil Object key) {
				return set.containsKey(key);
			}

			@Override
			public boolean test(@Nil Object o) {
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
	/**
	 * Adds the second input to the first one in-place
	 * @param <T> type of elements
	 * @param list list to add to
	 * @param collect elements to be added
	 * @return first input
	 */
	@NN public static <T> List<T> inplaceAddLists(List<T> list, Collection<? extends T> collect){
		list.addAll(collect);
		return list;
	}
	/**
	 * Adds two inputs out of place
	 * @param <T>
	 * @param a first half of the list
	 * @param b second half of the list
	 * @param supplier creates lists
	 * @return a new list
	 */
	@NN public static <T> List<T> ooplaceAddLists(Collection<? extends T> a, Collection<? extends T> b, Supplier<List<T>> supplier){
		List<T> list0 = supplier.get();
		list0.addAll(a);
		list0.addAll(b);
		return list0;
	}
	/**
	 * Creates an out-of-place list added
	 * @param <T> type of values
	 * @param supplier creates lists
	 * @return a list addition function
	 */
	@NN public static <T> BiFunction<@NN Collection<? extends T>, @NN Collection<? extends T>, List<T>> ooplaceListAdder(Supplier<List<T>> supplier){
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
	@NN public static <K, V> SetMultimap<K, V> emptyMultimap(){
		return (SetMultimap<K, V>) emptyMultiMap;
	}
	@NN private static final SetMultimap<?, ?> emptyMultiMap = new EmptySetMultimap();
	/**
	 * Creates an empty multiset
	 * @param <T> type of values
	 * @return an empty, immutable multiset
	 */
	@SuppressWarnings("unchecked")
	@NN public static <T> Multiset<T> emptyMultiset(){
		return (Multiset<T>) emptyMultiSet;
	}
	@NN private static final Multiset<?> emptyMultiSet = new EmptyMultiSet();
	
	//Maps
	/**
	 * Creates an int-to-any map from a stream
	 * @param <T> type of values
	 * @param <M> type of maps
	 * @param mapsup creates maps
	 * @return an int/obj map collector
	 */
	@NN public static <T, M extends Object2IntMap<T>> Collector<Object2IntMap.Entry<T>, Object2IntMap<T>, M> collectToIntMap(Supplier<M> mapsup){
		return new IntMapCollector<>(mapsup);
	}
	/**
	 * Adds the second input to the first input
	 * @param <T> type of values
	 * @param <M> type of the modified map
	 * @param list map to be modified
	 * @param collect map to be added to the first input
	 * @return first input
	 */
	@NN public static <T, @NN M extends Object2IntMap<T>> M inplaceAddIntMaps(M list, Object2IntMap<? extends T> collect){
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
	 * Creates a dynamically-mapped view of a grid
	 * @param <Tin> type of the input grid
	 * @param <Tout> type of the output grid
	 * @param forward forward mapper
	 * @param backward backward mapper
	 * @param grid input grid
	 * @return a transformed view of the grid. The view is backed by the grid, so any changes made to the original are reflected in the view and vice versa
	 */
	public static <Tin, Tout> Grid<Tout> mapGrid(Function<? super Tin, ? extends Tout> forward, @Nil Function<? super Tout, ? extends Tin> backward, Grid<Tin> grid){
		return new Grid<>() {
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
			@Override
			public Iterator<Tout> iterator() {
				Iterator<Tin> iter = grid.iterator();
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

	//All/any boolean operations
	/**
	 * Checks if all values meet a predicate
	 * @param <T> type of values
	 * @param collection collection to test
	 * @param predicate condition
	 * @return do all items meet a criterion?
	 */
	public static <T> boolean isAll(Iterable<? extends T> collection, Predicate<T> predicate) {
		for(T value: collection) 
			if(!predicate.test(value)) return false;
		return true;
	}
	/**
	 * Checks if any value meets a predicate
	 * @param <T> type of values
	 * @param collection collection to test
	 * @param predicate condition
	 * @return does any items meet a criterion?
	 */
	public static <T> boolean isAny(Iterable<? extends T> collection, Predicate<T> predicate) {
		for(T value: collection) 
			if(predicate.test(value)) return true;
		return false;
	}
}
class IntMapCollector<T, M extends Object2IntMap<T>> implements Collector<Object2IntMap.Entry<T>, Object2IntMap<T>, M>{
	@NN private final Supplier<M> mapsup;

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
	public Object @NN [] toArray() {
		return new Object[0];
	}

	@Override
	public <T> T @NN [] toArray(T[] a) {
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
	public int add(@Nil Object element, int occurrences) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(@Nil Object element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int remove(Object element, int occurrences) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(@Nil Object element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int setCount(@Nil Object element, int count) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setCount(@Nil Object element, int oldCount, int newCount) {
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
	public boolean contains(@Nil Object element) {
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
	public boolean put(@Nil Object key, @Nil Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object key, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean putAll(@Nil Object key, Iterable<? extends Object> values) {
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