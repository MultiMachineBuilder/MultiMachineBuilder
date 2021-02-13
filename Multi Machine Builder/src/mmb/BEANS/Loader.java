/**
 * 
 */
package mmb.BEANS;

/**
 * @author oskar
 * This interface defines an object which can load data of one type and transform them into other, runtime type
 * @param <T> accepted type
 */
public interface Loader<T> {
	/**
	 * Loads the following data object.
	 * @param data
	 */
	public void load(T data);
}
