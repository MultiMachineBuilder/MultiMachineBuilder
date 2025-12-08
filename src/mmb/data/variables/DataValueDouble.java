/**
 * 
 */
package mmb.data.variables;

import mmb.annotations.NN;

/**
 * @author oskar
 *
 */
public class DataValueDouble implements Variable<@NN Double> {
	private double value;
	/**
	 * Creates a double-specialized variable
	 * @param data value of the variable
	 */
	public DataValueDouble(double data) {
		value = data;
	}
	
	/**
	 * @deprecated This function boxes the result. Use {@link #getDouble()} for better performance
	 * @return boxed contents of this variable
	 */
	@Override
	public @Deprecated(forRemoval = false) Double get() {
		return Double.valueOf(value);
	}
	/**
	 * @deprecated This function unboxes the input. Use {@link #set(double)} for better performance
	 * @param newValue new value
	 */
	@Override
	public @Deprecated(forRemoval = false) void set(Double newValue) {
		value = newValue.doubleValue();
	}
	/** @return contents of this variable */
	public double getDouble() {
		return value;
	}
	/**
	 * Sets the variable
	 * @param newValue new value
	 */
	public void set(double newValue) {
		value = newValue;
	}

}
