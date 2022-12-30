/**
 * 
 */
package mmb.content.stn.attachment;

import java.awt.Component;
import java.util.function.Supplier;

/**
 * A base interface for all STN attachments
 * @author oskar
 * @param <T> type of the attachment
 * @param <U> type of the GUI component (it must also implement a get() method, which provides a new state
 */
public interface STNattachment<T, U extends Component&Supplier<T>> {
	public T state();
	
	/**
	 * Replaces a state. This method does not have any side effects
	 * @param newState
	 * @return a new attachment
	 */
	public STNattachment<T, U> replaceState(T newState);
	
	/**
	 * Create a GUI component
	 * @return a GUI component
	 */
	public U createGUI();
}
