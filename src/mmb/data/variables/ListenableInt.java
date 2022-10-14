/**
 * 
 */
package mmb.data.variables;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntConsumer;

/**
 * @author oskar
 *
 */
public class ListenableInt extends DataValueInt {
	/**
	 * @param data
	 */
	public ListenableInt(int data) {
		super(data);
	}
	private Set<IntConsumer> listeners = new HashSet<>();
	
	/**
	 * @param arg0
	 * @return was new consume added?
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean add(IntConsumer arg0) {
		return listeners.add(arg0);
	}
	/**
	 * @param arg0
	 * @return is consumer registered?
	 * @see java.util.Set#contains(java.lang.Object)
	 */
	public boolean contains(IntConsumer arg0) {
		return listeners.contains(arg0);
	}
	/**
	 * @param arg0
	 * @return was consumer removed?
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	public boolean remove(IntConsumer arg0) {
		return listeners.remove(arg0);
	}
	@Override
	public void set(int newValue) {
		for(IntConsumer c: listeners) c.accept(newValue);
		super.set(newValue);
	}	
}
