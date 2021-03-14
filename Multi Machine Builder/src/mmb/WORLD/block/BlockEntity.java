/**
 * 
 */
package mmb.WORLD.block;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.GameObject;
import mmb.BEANS.Loader;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.BlockArrayProvider;

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
	
	public default void onStartup(BlockArrayProvider map) {}
	public default void onPlace(BlockArrayProvider map, @Nullable GameObject obj) {}
	public default void onTick(MapProxy map) {}
	public default void onBreak(BlockArrayProvider blockMap, @Nullable GameObject obj) {}
	public default void onShutdown(BlockArrayProvider map) {}

	/**
	 * @return X coordinate of the block entity
	 */
	int posX();
	/**
	 * @return Y coordinate of the block entity
	 */
	int posY();
}
