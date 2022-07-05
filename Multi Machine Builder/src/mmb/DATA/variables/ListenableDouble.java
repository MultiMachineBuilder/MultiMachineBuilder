/**
 * 
 */
package mmb.DATA.variables;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

/**
 * @author oskar
 *
 */
public class ListenableDouble extends DataValueDouble {
	/**
	 * @param data
	 */
	public ListenableDouble(double data) {
		super(data);
	}
	private Set<DoubleConsumer> listeners = new HashSet<>();
	
	/**
	 * @param arg0
	 * @return was new consume added?
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean add(DoubleConsumer arg0) {
		return listeners.add(arg0);
	}
	/**
	 * @param arg0
	 * @return is consumer registered?
	 * @see java.util.Set#contains(java.lang.Object)
	 */
	public boolean contains(DoubleConsumer arg0) {
		return listeners.contains(arg0);
	}
	/**
	 * @param arg0
	 * @return was consumer removed?
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	public boolean remove(DoubleConsumer arg0) {
		return listeners.remove(arg0);
	}
	@Override
	public void set(double newValue) {
		for(DoubleConsumer c: listeners) c.accept(newValue);
		super.set(newValue);
	}	
}
