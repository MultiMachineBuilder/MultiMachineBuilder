/**
 * 
 */
package mmb.WORLD.blocks.entries;

import mmb.WORLD.blocks.defs.BlockDef;

/**
 * @author oskar
 *
 */
public abstract class BlockEntryAbstract implements BlockEntry{
	public BlockDef block;
	@Override
	public BlockDef getBlock() {
		return block;
	}
}
