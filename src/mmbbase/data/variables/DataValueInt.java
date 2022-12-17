/**
 * 
 */
package mmbbase.data.variables;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public class DataValueInt implements Variable<@Nonnull Integer> {
	private int value;
	/**
	 * Creates a new interger variable
	 * @param data 
	 */
	public DataValueInt(int data) {
		value = data;
	}
	@Deprecated @Override
	public @Nonnull Integer get() {
		return Integer.valueOf(value);
	}
	@Deprecated @Override
	public void set(@Nonnull Integer newValue) {
		value = newValue.intValue();
	}
	public int getInt() {
		return value;
	}
	public void set(int newValue) {
		value = newValue;
	}
}
