/**
 * 
 */
package mmb.WORLD.block;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.BEANS.Saver;
import mmb.WORLD.Side;

/**
 * @author oskar
 * The following methods can be overridden:
 * provideSignal - 
 */
public interface BlockEntry extends Saver<JsonNode> {
	public boolean isBlockEntity();
	public BlockEntity asBlockEntity();
	public BlockType type();
	/**
	 * @param type block type to check
	 * @return does given type match actual type?
	 */
	public default boolean typeof(BlockType type) {
		return type == type();
	}
	public default boolean provideSignal(Side s) {
		return false;
	}
	
	public default void wrenchCW() {}
	public default void wrenchCCW() {}
}
