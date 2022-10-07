/**
 * 
 */
package mmb.data.variables;

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
	 * Adds a listener
	 * @param arg0 listener
	 * @return was new consume added?
	 */
	public boolean add(DoubleConsumer arg0) {
		return listeners.add(arg0);
	}
	/**
	 * Tests for a listener
	 * @param arg0 listener
	 * @return is consumer registered?
	 */
	public boolean contains(DoubleConsumer arg0) {
		return listeners.contains(arg0);
	}
	/**
	 * Removes a listener
	 * @param arg0 listener
	 * @return was consumer removed?
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
