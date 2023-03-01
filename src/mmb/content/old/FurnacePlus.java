/**
 * 
 */
package mmb.content.old;

import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;

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
		return ContentsBlocks.old_EFURNACE;
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
