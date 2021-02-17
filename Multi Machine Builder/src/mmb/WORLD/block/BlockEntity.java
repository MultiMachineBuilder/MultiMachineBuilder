/**
 * 
 */
package mmb.WORLD.block;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.GameObject;
import mmb.BEANS.Loader;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public interface BlockEntity extends BlockEntry, Loader<JsonNode>{
	@Override
	default boolean isBlockEntity() {
		return false;
	}

	@Override
	default BlockEntity asBlockEntity() {
		return null;
	}
	
	public default void onStartup(BlockMap map) {}
	public default void onPlace(BlockMap map, GameObject obj) {}
	public default void onTick(MapProxy map) {}
	public default void onBreak(BlockMap map, GameObject obj) {}
	public default void onShutdown(BlockMap map) {}

	/**
	 * @return X coordinate of the block entity
	 */
	int posX();
	/**
	 * @return Y coordinate of the block entity
	 */
	int posY();
}
