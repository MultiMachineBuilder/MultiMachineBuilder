/**
 * 
 */
package mmbbase.data.variables;

import java.util.HashSet;
import java.util.Set;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;

/**
 * @author oskar
 *
 */
public class ListenerBooleanVariable extends BooleanVariable{
	private Set<BooleanConsumer> listeners = new HashSet<>();
	
	/**
	 * @param arg0
	 * @return was new consume added?
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean add(BooleanConsumer arg0) {
		return listeners.add(arg0);
	}
	/**
	 * @param arg0
	 * @return is consumer registered?
	 * @see java.util.Set#contains(java.lang.Object)
	 */
	public boolean contains(BooleanConsumer arg0) {
		return listeners.contains(arg0);
	}
	/**
	 * @param arg0
	 * @return was consumer removed?
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	public boolean remove(BooleanConsumer arg0) {
		return listeners.remove(arg0);
	}
	@Override
	public void setValue(boolean value) {
		if(this.value == value) return;
		this.value = value;
		for(BooleanConsumer c: listeners) c.accept(value);
	}
}
