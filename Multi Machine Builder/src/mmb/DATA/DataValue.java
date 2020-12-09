/**
 * 
 */
package mmb.DATA;

/**
 * @author oskar
 *
 */
public class DataValue<T> implements GetterSetter<T> {
	public T value;
	/**
	 * 
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
