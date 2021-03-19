/**
 * 
 */
package mmb.BEANS;

import javax.annotation.Nullable;

import mmb.WORLD.block.BlockType;

/**
 * @author oskar
 *
 */
public interface BlockSetting {
	/**
	 * @return currently selected block
	 */
	public BlockType getBlockSetting();
	
	/**
	 * Set the block setting
	 * @param setting new block
	 */
	public void setBlockSetting(@Nullable BlockType setting);
}
