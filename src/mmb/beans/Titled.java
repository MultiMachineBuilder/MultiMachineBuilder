/**
 * 
 */
package mmb.beans;

import mmb.annotations.NN;

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
