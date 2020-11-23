/**
 * 
 */
package mmb.WORLD.inventory;

import mmb.WORLD.tileworld.BlockDrawer;

/**
 * @author oskar
 *
 */
public interface Item {
	public double getVolume();
	public ItemType getType();
	public String getName();
	public String getID();
	public BlockDrawer getTexture();
}
