/**
 * 
 */
package monniasza.collects.indexar;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import mmb.annotations.NN;
import mmb.annotations.Nil;

/**
 * A database is a collection of values allowing values to be indexed, stored and retrieved.
 * Indexes may impose restrictions (guards and to-one indexes)
 * @author oskar
 * @param <T> type of values
 */
public class Database<@NN T> extends AbstractSet<@NN T>{ //NOSONAR the method is a contract
	/** A list of indexes */
	@NN public final Set<@NN Index<T, ?, ?>> indexes0 = new HashSet<>();
	/** Type of objects */
	@NN public final Class<T> cls;
	
	/**
	 * Creates a database
	 * @param cls type of values
	 */
	public Database(Class<T> cls) {
		this.set = new HashSet<>();
		this.cls = cls;
	}
	/**
	 * Creates a database using a set
	 * @param cls type of values
	 * @param set set to use
	 */
	public Database(Class<T> cls, Set<T> set) {
		this.set = set;
		this.cls = cls;
	}
	
	/**
	 * Adds an index. Returns this database for easy chaining
	 * The index should be empty
	 * @param index index to add
	 * @return this
	 */
	@NN public Database<T> addIndex(Index<T, ?, ?> index){
		indexes0.add(index);
		removeIf(val -> !index.add(val)); //add and filter both at once
		return this;
	}
	/**
	 * Removes an index
	 * @param index index to rmove
	 */
	public void removeIndex(Index<T, ?, ?> index) {
		indexes0.remove(index);
	}

	@NN private final Set<T> set;
	
	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public boolean contains(@Nil Object o) {
		return set.contains(o);
	}

	@Override
	public @NN Iterator<T> iterator() {
		Iterator<T> iter = set.iterator();
		return new Iterator<T>(){
			@SuppressWarnings("null")
			private T value;
			private boolean valid;
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public T next() {
				value = iter.next();
				valid = true;
				return value;
			}

			@Override
			public void remove() {
				if(!valid) throw new IllegalStateException("remove() without next()");
				for(Index<T, ?, ?> idx: indexes0) {
					idx.remove(value);
				}
				iter.remove();
			}
			
		};
	}

	@Override
	public Object @NN [] toArray() {
		return set.toArray();
	}

	@Override
	public <U> U @NN [] toArray(U[] a) {
		return set.toArray(a);
	}

	@Override
	public boolean add(T e) {
		cls.cast(e); //type check
		for(Index<T, ?, ?> idx: indexes0) {
			if(!idx.test(e)) return false;
		}
		for(Index<T, ?, ?> idx: indexes0) idx.add(e);
		return true;
	}

	@Override
	public boolean remove(@Nil Object o) {
		T value = cls.cast(o); //type check
		if(o == null) return false;
		for(Index<T, ?, ?> idx: indexes0) idx.remove(value);
		return set.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return set.containsAll(c);
	}

	@Override
	public void clear() {
		for(Index<T, ?, ?> idx: indexes0) idx.clear();
		set.clear();
	}
}
