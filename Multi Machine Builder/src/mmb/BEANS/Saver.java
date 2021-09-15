/**
 * 
 */
package mmb.BEANS;

import javax.annotation.Nullable;

/**
 * @author oskar
 * Represents an object which can be saved
 * @param <T> saved data type
 *
 */
public interface Saver<@Nullable T> {
	/**
	 * @return the saved data
	 */
	public T save();
	/**
	 * Loads the following data object.
	 * @param data
	 */
	public void load(T data);
}
