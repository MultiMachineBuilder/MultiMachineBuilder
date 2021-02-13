/**
 * 
 */
package mmb.BEANS;

/**
 * @author oskar
 * @param <T> saved data type
 *
 */
public interface Saver<T> {
	/**
	 * @return the saved data
	 */
	public T save();
}
