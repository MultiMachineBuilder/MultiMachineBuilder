/**
 * 
 */
package mmb.DATA;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author oskar
 *	A resizable list with limited capacity
 */

public class LimitedArrayList<E> extends ArrayList<E> {
	private static final long serialVersionUID = -7230187565388862552L;
	
	public int maxAmount = Integer.MAX_VALUE;
	public LimitedArrayList() {
		super();
	}

	public LimitedArrayList(Collection<? extends E> c) {
		super(c);
	}
	
	public LimitedArrayList(Collection<? extends E> c, int max) {
		super(c);
		maxAmount = max;
	}

	@Override
	public boolean add(E e) {
		if(size() >= maxAmount) return false;
		return super.add(e);
	}

	@Override
	public void add(int index, E element) {
		if(size() >= maxAmount) return;
		super.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if(size() + c.size() >= maxAmount) return false;
		return super.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if(size() + c.size() >= maxAmount) return false;
		return super.addAll(index, c);
	}

	@Override
	public void ensureCapacity(int minCapacity) {
		if(maxAmount < minCapacity) maxAmount = minCapacity;
		super.ensureCapacity(minCapacity);
	}

	public LimitedArrayList(int initialCapacity) {
		super(initialCapacity);
		maxAmount = initialCapacity;
	}
	
	public LimitedArrayList(int initialCapacity, int max) {
		super(initialCapacity);
		maxAmount = max;
	}
	
}
