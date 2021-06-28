/**
 * 
 */
package mmb.WORLD.block;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.BEANS.Loader;
import mmb.BEANS.RunOnTick;

/**
 * @author oskar
 *
 */
public interface BlockEntity extends BlockEntry, Loader<JsonNode>, RunOnTick{
	@Override
	default boolean isBlockEntity() {
		return true;
	}

	@Override
	default BlockEntity asBlockEntity() {
		return this;
	}
	
	/**
	 * @return X coordinate of the block entity
	 */
	int posX();
	/**
	 * @return Y coordinate of the block entity
	 */
	int posY();
}
