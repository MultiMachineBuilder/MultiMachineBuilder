/**
 * 
 */
package mmb.DATA.variables;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author oskar
 *
 */
public class ListenableValue<T> extends DataValue<T> {
	/**
	 * @param data
	 */
	public ListenableValue(T data) {
		super(data);
	}
	private Set<Consumer<T>> listeners = new HashSet<>();
	
	/**
	 * @param arg0
	 * @return was new consume added?
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean add(Consumer<T> arg0) {
		return listeners.add(arg0);
	}
	/**
	 * @param arg0
	 * @return is consumer registered?
	 * @see java.util.Set#contains(java.lang.Object)
	 */
	public boolean contains(Consumer<T> arg0) {
		return listeners.contains(arg0);
	}
	/**
	 * @param arg0
	 * @return was consumer removed?
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	public boolean remove(Consumer<T> arg0) {
		return listeners.remove(arg0);
	}
	@Override
	public void set(T newValue) {
		for(Consumer<T> c: listeners) c.accept(newValue);
		super.set(newValue);
	}	
}
