/**
 * 
 */
package mmb.beans;

import javax.annotation.Nullable;

import mmb.data.variables.ListenableValue;
import mmbeng.block.BlockType;

/**
 * A block with replaceable block configuration
 * @author oskar
 */
public interface BlockSetting {
	/** @return the variable used to set block settings */
	public ListenableValue<@Nullable BlockType> getBlockVariable();
	/** @return currently selected block */
	public default BlockType blockSetting() {
		return getBlockVariable().get();
	}
	/**
	 * Set the block setting
	 * @param setting new block
	 */
	public default void setBlockSetting(@Nullable BlockType setting) {
		getBlockVariable().set(setting);
	}
}
