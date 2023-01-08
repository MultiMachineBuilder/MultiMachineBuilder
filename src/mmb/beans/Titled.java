/**
 * 
 */
package mmb.beans;

import mmb.NN;

/**
 * @author oskar
 *
 */
@FunctionalInterface
public interface Titled {
	/**
	 * @return the GUI title
	 */
	public @NN String title();
}
