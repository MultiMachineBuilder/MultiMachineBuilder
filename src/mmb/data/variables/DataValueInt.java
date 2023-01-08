/**
 * 
 */
package mmb.data.variables;

import mmb.NN;

/**
 * @author oskar
 *
 */
public class DataValueInt implements Variable<@NN Integer> {
	private int value;
	/**
	 * Creates a new interger variable
	 * @param data 
	 */
	public DataValueInt(int data) {
		value = data;
	}
	@Deprecated @Override
	public @NN Integer get() {
		return Integer.valueOf(value);
	}
	@Deprecated @Override
	public void set(@NN Integer newValue) {
		value = newValue.intValue();
	}
	public int getInt() {
		return value;
	}
	public void set(int newValue) {
		value = newValue;
	}
}
