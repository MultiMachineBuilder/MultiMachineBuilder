/**
 * 
 */
package mmb.content.event;

import java.util.EventListener;

/**
 * @author oskar
 *
 */
public interface BlockEntityDemolitionListener extends EventListener {
	/**
	 * Invoked when a block entity is demolished
	 * @param e information about demolished block
	 */
	public void blockDemolished(BlockEntityDemolitionEvent e);
}
