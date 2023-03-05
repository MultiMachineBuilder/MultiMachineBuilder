/**
 * 
 */
package monniasza.collects;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A list which can't be resized
 * @author oskar
 * @param <E> type of contents
 */
public class StaticList<E> extends AbstractList<E> { //NOSONAR the equals() is implemented by AbstractList
	private final Object[] array;
	/**
	 * Creates a fixed size list by copying an array
	 * @param array source array
	 */
	@SafeVarargs
	public StaticList(E... array) {
		this.array = Arrays.copyOf(array, array.length, Object[].class);
	}
	/** 
	 * Creates a fixed size list of a given size
	 * @param size size of the listt
	 */
	public StaticList(int size) {
		array = new Object[size];
	}
	/**
	 * Creates a fixed size list from a collection
	 * @param c source collection
	 */
	public StaticList(Collection<? extends E> c) {
		array = c.toArray();
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
