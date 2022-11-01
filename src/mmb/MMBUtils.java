/**
 * 
 */
package mmb;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public class MMBUtils {
	private MMBUtils() {}
	/**
	 * Switches displayed class types. The types should be variant of the same class type
	 * @param <R> dest type
	 * @param cls class to cast
	 * @return a casted class
	 */
	@SuppressWarnings("unchecked")
	@Nonnull public static <R> Class<R> classcast(Class<?> cls){
		return (Class<R>) cls;
	}
}
