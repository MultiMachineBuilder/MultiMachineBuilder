/**
 * 
 */
package mmb.WORLD.inventory.items;

import mmb.WORLD.tileworld.BlockDrawer;

/**
 * @author oskar
 *
 */
public interface ItemDefinition {
	public BlockDrawer getTexture();
	public String getName();
	public String getID();
}
