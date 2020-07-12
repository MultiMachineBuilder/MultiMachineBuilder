/**
 * 
 */
package mmb.world.vessels;

import e3d.d3.Size3;
import mmb.world.blockworld.blocks.Block;
import mmb.world.blockworld.blocks.BlockInfo;
import mmb.world.blockworld.events.BlockChange;
import mmb.world.blockworld.maps.BlockMap;

/**
 * @author oskar
 * Block-based vessel, which is much easier to handle than freeform vessels.
 */
public class VesselBlocks implements BlockMap, Vessel {

	/**
	 * 
	 */
	public VesselBlocks() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see mmb.world.blockworld.maps.BlockMap#place(mmb.world.blockworld.blocks.BlockInfo, e3d.d3.Size3)
	 */
	@Override
	public BlockChange place(BlockInfo block, Size3 pos) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mmb.world.blockworld.maps.BlockMap#destroy(mmb.world.blockworld.blocks.BlockInfo)
	 */
	@Override
	public BlockChange destroy(BlockInfo block) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mmb.world.blockworld.maps.BlockMap#destroy(e3d.d3.Size3)
	 */
	@Override
	public BlockChange destroy(Size3 pos) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mmb.world.blockworld.maps.BlockMap#get(e3d.d3.Size3)
	 */
	@Override
	public Block get(Size3 pos) {
		// TODO Auto-generated method stub
		return null;
	}

}
