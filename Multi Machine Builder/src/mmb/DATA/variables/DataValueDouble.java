/**
 * 
 */
package mmb.DATA.variables;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public class DataValueDouble implements Variable<@Nonnull Double> {
	private double value;
	/**
	 * 
	 */
	public DataValueDouble(double data) {
		value = data;
	}
	@Override
	public Double get() {
		return value;
	}
	@Override
	public void set(Double newValue) {
		value = newValue;
	}
	public double getDouble() {
		return value;
	}
	public void set(double newValue) {
		value = newValue;
	}

}
