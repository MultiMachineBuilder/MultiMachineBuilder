/**
 * 
 */
package mmb.engine.inv;

import org.checkerframework.common.returnsreceiver.qual.This;

import mmb.beans.Saver;

/**
 * An inventory with essential save and load capabilities
 * @author oskar
 */
public interface SaveInventory extends Inventory, Saver{
	/**
	 * Sets capacity of this inventory
	 * @param capacity item capacity
	 * @return this
	 */
	@This
	public Inventory setCapacity(double capacity);
}
