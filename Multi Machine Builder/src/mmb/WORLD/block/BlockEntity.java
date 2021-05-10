/**
 * 
 */
package mmb.WORLD.block;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.GameObject;
import mmb.BEANS.Loader;
import mmb.BEANS.RunOnTick;
import mmb.WORLD.worlds.world.BlockArrayProvider;

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
	
	public default void onStartup(BlockArrayProvider map) {}
	public default void onPlace(BlockArrayProvider map, @Nullable GameObject obj) {}
	//onTick()
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
