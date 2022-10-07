/**
 * 
 */
package mmb.beans;

/**
 * @author oskar
 *
 */
public interface ExternalSaver<T, U> {
	/**
	 * Loads the following data object.
	 * @param data
	 * @return a newly loaded object
	 */
	public U load(T data);
}
