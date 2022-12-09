/**
 * 
 */
package mmb.data.variables;

/**
 * @author oskar
 * @param <T> type of data
 *
 */
public class DataValue<T> implements Variable<T> {
	private T value;
	/**
	 * Creates a variable
	 * @param data initial value
	 */
	public DataValue(T data) {
		value = data;
	}
	@Override
	public T get() {
		return value;
	}
	@Override
	public void set(T newValue) {
		value = newValue;
	}

}
