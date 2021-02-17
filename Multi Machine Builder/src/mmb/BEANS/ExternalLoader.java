/**
 * 
 */
package mmb.BEANS;

/**
 * @author oskar
 *
 */
public interface ExternalLoader<T, U> {
	/**
	 * Loads the following data object.
	 * @param data
	 * @return a newly loaded object
	 */
	public U load(T data);
}
