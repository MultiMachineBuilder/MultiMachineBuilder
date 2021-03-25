/**
 * 
 */
package mmb.COLLECTIONS;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * @author oskar
 * A list which does not preserve order. Most useful for event oriented programming
 */
public class UnorderedList<E> implements List<E> {
	
	public UnorderedList() {
		clear();
	}
	
	private int size;
	private E[] array;
	
	@Override
	public boolean add(E e) {
		size++;
		requireCapacity(size);
		array[size - 1] = e;
		return true;
	}

	@Override
	public void add(int index, E element) {
		if(index > size) add(size, element);
		else if(index == size) {
			//Add at end
			add(element);
		}
		else {
			//Add not on end
			size++;
			requireCapacity(size);
			//Swap target and end
			E temp = array[index];
			array[index] = element;
			array[size-1] = temp;
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		array = (E[]) new Object[16];
		size = 0;
	}

	@Override
	public boolean contains(@Nullable Object o) {
		if(o == null) {
			for(int i = 0; i < size; i++) {
				if(array[i] == null) return true;
			}
		}else {
			for(int i = 0; i < size; i++) {
				if(o.equals(array[i])) return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(@SuppressWarnings("null") Collection<?> c) {
		int counter = 0;
		Set<?> set;
		if(c instanceof HashSet) set = (Set<?>) c;
		else set = new HashSet<>(c);
		int max = set.size();
		for(int i = 0; i < size; i++) {
			if(set.contains(array[i])) counter++;
		}
		return counter == max;
	}

	@Override
	public E get(int index) {
		if(index >= size) throw new ArrayIndexOutOfBoundsException("Index: "+index+", size: "+size);
		return array[index];
	}

	@Override
	public int indexOf(@Nullable Object o) {
		if(o == null) {
			for(int i = 0; i < size; i++) {
				if(array[i] == null) return i;
			}
		}else {
			for(int i = 0; i < size; i++) {
				if(o.equals(array[i])) return i;
			}
		}
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			int i = 0;
			@Override
			public boolean hasNext() {
				return i < size;
			}

			@Override
			public E next() {
				return get(i);
			}
			
		};
	}

	@Override
	public int lastIndexOf(@Nullable Object o) {
		if(o == null) {
			for(int i = size - 1; i >= 0; i--) {
				if(array[i] == null) return i;
			}
		}else {
			for(int i = size - 1; i >= 0; i--) {
				if(o.equals(array[i])) return i;
			}
		}
		return -1;
	}

	@Override
	public ListIterator<E> listIterator() {
		return new ListIterator<E>() {
			int i = 0;
			@Override
			public void add(E e) {
				add(e);
			}

			@Override
			public boolean hasNext() {
				return i < size;
			}

			@Override
			public boolean hasPrevious() {
				return i > 0;
			}

			@Override
			public E next() {
				
			}

			@Override
			public int nextIndex() {
				if(i == size) return size;
				return i + 1;
			}

			@Override
			public E previous() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int previousIndex() {
				if(i == 0) return 0;
				return i - 1;
			}

			@Override
			public void remove() {
				UnorderedList.this.remove(i);
			}

			@Override
			public void set(E e) {
				UnorderedList.this.set(i, e);
			}
			
		};
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E set(int index, E element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	private void requireCapacity(int size) {
		if(array.length < size) {
			array = Arrays.copyOf(array, size*2);
		}else if(array.length >  2*size) {
			array = Arrays.copyOf(array, size*2);
		}
	}
}
