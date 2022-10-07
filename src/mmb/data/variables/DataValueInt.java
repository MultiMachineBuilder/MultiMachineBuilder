/**
 * 
 */
package mmb.data.variables;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public class DataValueInt implements Variable<@Nonnull Integer> {
	private int value;
	/**
	 * 
	 */
	public DataValueInt(int data) {
		value = data;
	}
	@Override
	public @Nonnull Integer get() {
		return value;
	}
	@Override
	public void set(Integer newValue) {
		value = newValue;
	}
	public int getInt() {
		return value;
	}
	public void set(int newValue) {
		value = newValue;
	}
}
