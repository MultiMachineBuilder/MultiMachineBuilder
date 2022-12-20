/**
 * 
 */
package mmb.content.electric;

import mmb.NN;

/**
 * @author oskar
 * Allows blocks and items to provide the main electrical connection
 */
public interface Electric {
	/** @return the main electrical connection */
	@NN public Electricity getElectricity();
}
