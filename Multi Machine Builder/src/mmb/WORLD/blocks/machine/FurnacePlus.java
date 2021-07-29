/**
 * 
 */
package mmb.WORLD.blocks.machine;

import javax.annotation.Nonnull;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 *
 */
public class FurnacePlus extends SkeletalBlockMachine {

	/**
	 * @param x X position
	 * @param y Y position
	 * @param owner2 container block map
	 */
	public FurnacePlus(int x, int y, @Nonnull BlockMap owner2) {
		super(x, y, owner2, SkeletalBlockMachine.SETTING_FLAG_ITEM_INPUT | SkeletalBlockMachine.SETTING_FLAG_ITEM_OUTPUT | SkeletalBlockMachine.SETTING_FLAG_ELEC_INPUT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.EFURNACE;
	}

}
