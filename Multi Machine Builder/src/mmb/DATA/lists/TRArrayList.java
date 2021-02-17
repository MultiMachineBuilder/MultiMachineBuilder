/**
 * 
 */
package mmb.DATA.lists;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author oskar
 * @param <T> data type
 *
 */
public class TRArrayList<T> implements List<T>{
	public final Class<T> type;
	private T[] data;
	private int size;
	
	private void resizeData(int newSize) {
			data = Arrays.copyOf(data, newSize);
	}
	
	/** Generate a type retaining array list with given type*/
	@SuppressWarnings("unchecked")
	public TRArrayList(Class<T> type) {
		super();
		this.type = type;
		data = (T[]) Array.newInstance(type, 20);
	}

	@Override
	public boolean add(T val) {
		if(size == data.length) {
			resizeData(size * 2);
		}
		int pos = size++;
		data[pos] = val;
		return true;
	}

	@Override
	public void add(int arg0, T arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		int newSize = arg0.size() + size;
		if(newSize > data.length) {
			resizeData(Math.max(data.length * 2, newSize));
		}
		System.arraycopy(arg0.toArray(), 0, data, size, arg0.size());
		size = newSize;
		return true;
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(Object arg0) {
		return false;
		
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public T get(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int lastIndexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<T> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<T> listIterator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public T remove(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public T set(int arg0, T arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<T> subList(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T[] toArray() {
		return Arrays.copyOf(data, size);
	}

	@Override
	public <U> U[] toArray(U[] array) {
		System.arraycopy(data, 0, array, 0, size);
		return array;
	}
}
