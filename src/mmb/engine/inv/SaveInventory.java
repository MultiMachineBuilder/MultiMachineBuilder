/**
 * 
 */
package mmb.engine.inv;

import mmbbase.beans.Saver;

/**
 * An inventory with essential save and load capabilities
 * @author oskar
 */
public interface SaveInventory extends Inventory, Saver{
	public Inventory setCapacity(double capacity);
}
