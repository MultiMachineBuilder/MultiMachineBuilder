/**
 * 
 */
package monniasza.collects;

/**
 * Defines an element with an unique identifier
 * @author oskar
 * @param <T> type of identifier
 */
public interface Identifiable<T> {
	/**
	 * This method returns object's unique identifier
	 * @return object's identifier
	 */
	public T id();
}
