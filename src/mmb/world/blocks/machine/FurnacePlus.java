/**
 * 
 */
package mmb.world.blocks.machine;

import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
import mmb.world.blocks.ContentsBlocks;

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

	@Override
	public BlockEntry blockCopy() {
		FurnacePlus copy = new FurnacePlus();
		copy.cfgInElec.set(cfgInElec);
		copy.cfgInItems.set(cfgInItems);
		copy.cfgOutItems.set(cfgOutItems);
		return copy;
	}

}
