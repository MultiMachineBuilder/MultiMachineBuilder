/**
 * 
 */
package mmb.COLLECTIONS;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author oskar
 *
 */
public class TRArrayList<E> extends ArrayList<E> {
	private static final long serialVersionUID = -5742533698759352210L;
	public final Class<E> clas;
	public TRArrayList(Class<E> clazz) {
		super();
		clas = clazz;
	}

	public TRArrayList(Collection<? extends E> c, Class<E> clazz) {
		super(c);
		clas = clazz;
	}

	public TRArrayList(int initialCapacity, Class<E> clazz) {
		super(initialCapacity);
		clas = clazz;
	}
	@SuppressWarnings("unchecked")
	public TRArrayList(E[] c) {
		this(Arrays.asList(c), (Class<E>) c.getClass().getComponentType());
	}

	@Override
	public Object[] toArray() {
		@SuppressWarnings("unchecked")
		E[] data = (E[]) Array.newInstance(clas, size());
		return toArray(data);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((clas == null) ? 0 : clas.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		TRArrayList<E> other = (TRArrayList<E>) obj;
		if (clas == null) {
			if (other.clas != null)
				return false;
		} else if (!clas.equals(other.clas))
			return false;
		return true;
	}

}
