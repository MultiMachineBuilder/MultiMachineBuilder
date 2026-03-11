/**
 * 
 */
package mmb.beans;

import mmb.annotations.NN;
import mmb.engine.settings.GlobalSettings;

/**
 * @author oskar
 *
 */
@FunctionalInterface
public interface ITitle {
	
	/** @return the GUI title */
	public @NN String title();
	/**
	 * Sets the title. If the target object does not override this method, it will fail.
	 * @param descr new title
	 * @throws UnsupportedOperationException when the target object does not implement this method
	 */
	public default void setTitle(String descr) {
		throw new UnsupportedOperationException(this.getClass().getCanonicalName() + " does not implement title setting");
	}
	/**
	 * Sets the title from a translation key
	 * @param title title translation string
	 */
	public default void translateTitle(String title) {
		setTitle(GlobalSettings.$res1(title));
	}
}
