/**
 * 
 */
package mmb.world.blockworld.blocks;

import e3d.d3.Size3;
import mmb.world.blockworld.direction.Direction;
import mmb.world.blockworld.maps.WorldMap;

/**
 * @author oskar
 *
 */
public interface Block {
	public WorldMap getWorld();
	public void setPos(Size3 p);
	public Size3 getPos();
	public Direction getDir();
	
}
