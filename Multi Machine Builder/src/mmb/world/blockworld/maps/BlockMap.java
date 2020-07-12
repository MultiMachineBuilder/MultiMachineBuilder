/**
 * 
 */
package mmb.world.blockworld.maps;

import e3d.d3.Size3;
import mmb.world.blockworld.blocks.Block;
import mmb.world.blockworld.blocks.BlockInfo;
import mmb.world.blockworld.events.BlockChange;

/**
 * @author oskar
 *
 */
public interface BlockMap {
	/**
	 * Places a block into given map
	 * @param block Block to place
	 * @param pos Where to place
	 * @return Change report, which can be used for events
	 */
	public BlockChange place(BlockInfo block, Size3 pos);
	public BlockChange destroy(BlockInfo block);
	public BlockChange destroy(Size3 pos);
	public Block get(Size3 pos);
	
}
