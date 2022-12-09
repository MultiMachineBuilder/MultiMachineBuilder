/**
 * 
 */
package mmb.data.variables;

/**
 * An object with a value
 * @author oskar
 * @param <T> type of values
 */
public interface Property<T> {
	/** @return the value of this property */
	public T get();
}
