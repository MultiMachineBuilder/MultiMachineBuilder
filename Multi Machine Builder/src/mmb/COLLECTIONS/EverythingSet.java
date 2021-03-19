/**
 * 
 */
package mmb.COLLECTIONS;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * The {@code EverythingSet}
 *  is an immutable {@link Set},
 *   which contains every possible {@link Object}, even ones that weren't created
 * @author oskar
 * @param <T> the generic type of this {@code EverythingSet}
 * 
 */
public final class EverythingSet<T> implements Set<T> {
	public static final EverythingSet<?> INSTANCE = new EverythingSet<>();
	private EverythingSet() {}
	@SuppressWarnings("unchecked")
	public static <T> EverythingSet<T> of(){
		return (EverythingSet<T>)INSTANCE;
	}
	@Override
	public void clear() {
	}

	@Override
	public boolean contains(@SuppressWarnings("null") Object o) {
		return true;
	}

	@Override
	public boolean containsAll(@SuppressWarnings("null") Collection<?> c) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		throw new UnsupportedOperationException("Unable to create that many objects!");
	}

	@Override
	public boolean remove(@SuppressWarnings("null") Object o) {
		return false;
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
		return Integer.MAX_VALUE;
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException("Unable to create that many objects!");
	}

	@Override
	public <U> U[] toArray(@SuppressWarnings("null") U[] a) {
		throw new UnsupportedOperationException("Unable to create that many objects!");
	}

	@Override
	public boolean add(@SuppressWarnings("null") T e) {
		return false;
	}

	@Override
	public boolean addAll(@SuppressWarnings("null") Collection<? extends T> c) {
		return false;
	}

}
