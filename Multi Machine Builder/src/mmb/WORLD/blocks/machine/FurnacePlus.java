/**
 * 
 */
package mmb.WORLD.blocks.machine;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;

/**
 * @author oskar
 *
 */
public class FurnacePlus extends SkeletalBlockMachine {
	public FurnacePlus() {
		super(SkeletalBlockMachine.SETTING_FLAG_ITEM_INPUT | SkeletalBlockMachine.SETTING_FLAG_ITEM_OUTPUT | SkeletalBlockMachine.SETTING_FLAG_ELEC_INPUT);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.EFURNACE;
	}

}
