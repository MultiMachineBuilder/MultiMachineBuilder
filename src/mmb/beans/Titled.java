/**
 * 
 */
package mmb.beans;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
@FunctionalInterface
public interface Titled {
	/**
	 * @return the GUI title
	 */
	public @Nonnull String title();
}
