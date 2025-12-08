/**
 * 
 */
package mmb.content.electric;

import mmb.annotations.NN;

/**
 * An iterface bean which provides main electrical connection.
 * @author oskar
 * @see mmb.content.electric.machines.BlockPowerTower
 */
public interface Electric {
	/** @return the main electrical connection */
	@NN public Electricity getElectricity();
}
