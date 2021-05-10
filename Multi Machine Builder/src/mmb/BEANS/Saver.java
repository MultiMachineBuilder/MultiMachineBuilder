/**
 * 
 */
package mmb.BEANS;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
