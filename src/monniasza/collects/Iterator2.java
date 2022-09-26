/**
 * 
 */
package monniasza.collects;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author oskar
 * An iterator which iterates over all of its collections
 * @param <T> type of iterator
 */
public class Iterator2<T> implements Iterator<T> {
	public static <U> Iterator2<U> iterIterCol(Iterator<? extends Collection<? extends U>> ic) {
		return iterIterIter(new MapIterator<>(Collection::iterator, ic));
	}
	public static <U> Iterator2<U> iterIterIter(Iterator<? extends Iterator<? extends U>> ii) {
		return new Iterator2<>(ii);
	}
	public static <U> Iterator2<U> iterColCol(Collection<? extends Collection<? extends U>> cc) {
		return iterIterCol(cc.iterator());
	}
	public static <U> Iterator2<U> iterColIter(Collection<? extends Iterator<? extends U>> ci) {
		return iterIterIter(ci.iterator());
	}
	private Iterator2(Iterator<? extends Iterator<? extends T>> iter) {
		this.iter = iter;
	}

	private final Iterator<? extends Iterator<? extends T>> iter;
	private Iterator<? extends T> subiter;
	private boolean state;
	@Override
	public void remove() {
		if(!state) throw new IllegalStateException("Item not selected");
		if(subiter.hasNext()) subiter.remove();
		if(iter.hasNext()) {
			subiter = iter.next();
			remove();
		}
		state = false;
	}

	@Override
	public boolean hasNext() {
		if(subiter.hasNext()) return true;
		if(iter.hasNext()) {
			subiter = iter.next();
			return hasNext();
		}
		return false;
	}

	@Override
	public T next() {
		state = true;
		if(subiter.hasNext()) subiter.next();
		if(iter.hasNext()) {
			subiter = iter.next();
			return next();
		}
		state = false;
		throw new NoSuchElementException("No more iterators");
	}}
