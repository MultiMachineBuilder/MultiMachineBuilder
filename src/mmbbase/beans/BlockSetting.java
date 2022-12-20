/**
 * 
 */
package mmbbase.beans;

import mmb.Nil;
import mmb.engine.block.BlockType;
import mmbbase.data.variables.ListenableValue;

/**
 * A block with replaceable block configuration
 * @author oskar
 */
public interface BlockSetting {
	/** @return the variable used to set block settings */
	public ListenableValue<@Nil BlockType> getBlockVariable();
	/** @return currently selected block */
	public default BlockType blockSetting() {
		return getBlockVariable().get();
	}
	/**
	 * Set the block setting
	 * @param setting new block
	 */
	public default void setBlockSetting(@Nil BlockType setting) {
		getBlockVariable().set(setting);
	}
}
