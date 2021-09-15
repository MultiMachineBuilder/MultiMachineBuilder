/**
 * 
 */
package monniasza.collects;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nullable;

/**
 * @author oskar
 * @param <T> type of stored elements
 *
 */
public class SetProxy<T> {
	public Set<T> set;
	public final Set<T> proxy = new ProxySet();
	private class ProxySet implements Set<T>{
		@Override
		public boolean add(@SuppressWarnings("null") T e) {
			return set.add(e);
		}
		@Override
		public boolean addAll(@SuppressWarnings("null") Collection<? extends T> c) {
			return set.addAll(c);
		}
		@Override
		public void clear() {
			set.clear();
		}
		@Override
		public boolean contains(@Nullable Object o) {
			return set.contains(o);
		}
		@Override
		public boolean containsAll(@SuppressWarnings("null") Collection<?> c) {
			return set.containsAll(c);
		}
		@Override
		public boolean equals(@Nullable Object o) {
			return set.equals(o);
		}
		@Override
		public void forEach(@SuppressWarnings("null") Consumer<? super T> action) {
			set.forEach(action);
		}
		@Override
		public int hashCode() {
			return set.hashCode();
		}
		@Override
		public boolean isEmpty() {
			return set.isEmpty();
		}
		@Override
		public Iterator<T> iterator() {
			return set.iterator();
		}
		@Override
		public Stream<T> parallelStream() {
			return set.parallelStream();
		}
		@Override
		public boolean remove(@Nullable Object o) {
			return set.remove(o);
		}
		@Override
		public boolean removeAll(@SuppressWarnings("null") Collection<?> c) {
			return set.removeAll(c);
		}
		@Override
		public boolean removeIf(@SuppressWarnings("null") Predicate<? super T> filter) {
			return set.removeIf(filter);
		}
		@Override
		public boolean retainAll(@SuppressWarnings("null") Collection<?> c) {
			return set.retainAll(c);
		}
		@Override
		public int size() {
			return set.size();
		}
		@Override
		public Spliterator<T> spliterator() {
			return set.spliterator();
		}
		@Override
		public Stream<T> stream() {
			return set.stream();
		}
		@Override
		public Object[] toArray() {
			return set.toArray();
		}
		@Override
		public <U> U[] toArray(@SuppressWarnings("null") U[] a) {
			return set.toArray(a);
		}
	}
}
