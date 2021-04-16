/**
 * 
 */
package mmb.COLLECTIONS;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author oskar
 *
 */
public class SetProxy<T> {
	public Set<T> set;
	public final Set<T> proxy = new ProxySet();
	private class ProxySet implements Set<T>{
		@Override
		public boolean add(T e) {
			return set.add(e);
		}
		@Override
		public boolean addAll(Collection<? extends T> c) {
			return set.addAll(c);
		}
		@Override
		public void clear() {
			set.clear();
		}
		@Override
		public boolean contains(Object o) {
			return set.contains(o);
		}
		@Override
		public boolean containsAll(Collection<?> c) {
			return set.containsAll(c);
		}
		@Override
		public boolean equals(Object o) {
			return set.equals(o);
		}
		@Override
		public void forEach(Consumer<? super T> action) {
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
		public boolean remove(Object o) {
			return set.remove(o);
		}
		@Override
		public boolean removeAll(Collection<?> c) {
			return set.removeAll(c);
		}
		@Override
		public boolean removeIf(Predicate<? super T> filter) {
			return set.removeIf(filter);
		}
		@Override
		public boolean retainAll(Collection<?> c) {
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
		public <U> U[] toArray(U[] a) {
			return set.toArray(a);
		}
	}
}
