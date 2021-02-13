/**
 * 
 */
package mmb.BEANS;

/**
 * @author oskar
 * @param <T> input type
 * @param <U> output type
 *
 */
public interface LazyLoader<T, U> extends Loader<T> {
	/**
	 * @return object, which was loaded using this loader
	 */
	public U getLoaded();
}
