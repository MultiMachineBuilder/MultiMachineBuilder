/**
 * 
 */
package mmb.BEANS;

import javax.annotation.Nonnull;

/**
 * @author oskar
 * @param <T> saved data type
 *
 */
public interface Saver<T> {
	/**
	 * @return the saved data
	 */
	@Nonnull public T save();
}
