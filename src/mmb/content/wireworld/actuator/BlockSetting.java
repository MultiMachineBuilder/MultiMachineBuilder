/**
 * 
 */
package mmb.content.wireworld.actuator;

import mmb.Nil;
import mmb.data.variables.ListenableValue;
import mmb.engine.block.BlockType;

/**
 * A bean interface used by the creative block placer.
 * The bean provides two methods, {@link #blockSetting()} and {@link #setBlockSetting(BlockType)} to get and set the block respectively.
 * A block with replaceable block configuration
 * @author oskar
 * @see SelectBlock
 * @see ActuatorPlaceBlock
 */
public interface BlockSetting {
	/** @return the variable used to set block settings */
	public ListenableValue<@Nil BlockType> getBlockVariable();
	/** @return currently selected block */
	public default @Nil BlockType blockSetting() {
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
