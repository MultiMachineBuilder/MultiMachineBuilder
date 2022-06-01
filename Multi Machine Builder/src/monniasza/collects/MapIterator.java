/**
 * 
 */
package monniasza.collects;

import java.util.Iterator;
import java.util.function.Function;

/**
 * @author oskar
 * the {@code MapIterator} is an {@link Iterator}, which converts all incoming values
 * @param <T> the input type
 * @param <U> the output type
 */
public class MapIterator<T, U> implements Iterator<U> {
	private final Function<T, U> map;
	private final Iterator<? extends T> iter;
	@Override
	public boolean hasNext() {
		return iter.hasNext();
	}

	@Override
	public U next() {
		return map.apply(iter.next());
	}

	public MapIterator(Function<T, U> map, Iterator<? extends T> iter) {
		super();
		this.map = map;
		this.iter = iter;
	}

}
