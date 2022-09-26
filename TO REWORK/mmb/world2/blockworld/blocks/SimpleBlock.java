/**
 * 
 */
package mmb.world2.blockworld.blocks;

import e3d.d3.Size3;
import mmb.world2.blockworld.maps.WorldMap;

/**
 * @author oskar
 * This is simple version of block. Does not have events, orientation and processing. Works best for building blocks
 */
public class SimpleBlock {
	//Simple block functionality
	WorldMap map;
	Size3 pos = new Size3(0, 0, 0);
	SimpleType type;
	/**
	 * 
	 */
	public SimpleBlock() {
		// TODO Auto-generated constructor stub
	}

}
