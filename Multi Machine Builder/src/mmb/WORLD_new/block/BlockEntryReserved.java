/**
 * 
 */
package mmb.WORLD_new.block;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import mmb.WORLD.tileworld.BlockDrawer;
import mmb.WORLD_new.block.properties.BlockProperty;
import mmb.WORLD_new.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public class BlockEntryReserved extends BlockEntry {

	public BlockEntry target;
	public int partNumber = 0;
	/**
	 * @param x
	 * @param y
	 */
	public BlockEntryReserved(int x, int y, BlockMap owner, BlockEntry newTarget) {
		super(x, y, owner);
		target = newTarget;
	}

	@Override
	public List<BlockProperty> getProperties() {
		return target.getProperties();
	}

	@Override
	public BlockType getType() {
		return original().getType();
	}

	@Override
	public BlockEntry original() {
		return target;
	}

	@Override
	public BlockDrawer getTexture() {
		return null;
	}

	@Override
	public JsonElement save() {
		JsonArray a = new JsonArray();
		a.add(target.posX());
		a.add(target.posY());
		a.add(partNumber);
		return a;
	}

	public BlockEntryReserved(int x, int y, BlockMap owner2, BlockEntry target, int partNumber) {
		super(x, y, owner2);
		this.target = target;
		this.partNumber = partNumber;
	}

}
