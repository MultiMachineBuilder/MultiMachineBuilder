/**
 * 
 */
package monniasza.collects;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author oskar
 * A list which can't be resized
 * @param <E> type of contents
 */
public class StaticList<E> extends AbstractList<E> {
	private final Object[] array;
	/**
	 * Creates a fixed size list by copying an array
	 * @param array
	 */
	@SafeVarargs
	public StaticList(E... array) {
		this.array = Arrays.copyOf(array, 0);
	}
	public StaticList(int size) {
		array = new Object[size];
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int i) {
		return (E) array[i];
	}

	@Override
	public E set(int index, E element) {
		E value = get(index);
		array[index] = element;
		return value;
	}
	@Override
	public int size() {
		return array.length;
	}

}
