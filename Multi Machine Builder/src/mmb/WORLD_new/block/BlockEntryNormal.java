/**
 * 
 */
package mmb.WORLD_new.block;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import mmb.WORLD.tileworld.BlockDrawer;
import mmb.WORLD_new.block.properties.BlockProperty;
import mmb.WORLD_new.worlds.map.BlockMap;

/**
 * @author oskar
 * 
 * This class represents a normal block entry
 */
public class BlockEntryNormal extends BlockEntry {
	public BlockType type;
	public List<BlockProperty> properties = new ArrayList<BlockProperty>();
	/**
	 * @param x @param y coordinates
	 * @param owner2 block owner
	 */
	public BlockEntryNormal(int x, int y, BlockMap owner2, BlockType typ) {
		super(x, y, owner2);
		type = typ;
	}

	@Override
	public BlockDrawer getTexture() {
		return type.drawer;
	}

	@Override
	public List<BlockProperty> getProperties() {
		return properties;
	}

	@Override
	public BlockType getType() {
		return type;
	}

	@Override
	public BlockEntry original() {
		return this;
	}

	@Override
	public BlockEntry routeToOriginal() {
		return this;
	}

	@Override
	public Point getOffset() {
		return new Point();
	}

	@Override
	public Point getOriginalPosition() {
		return getPosition();
	}

	@Override
	public JsonElement save() {
		if(properties.isEmpty()) return new JsonPrimitive(type.id); //if block has no data
		JsonObject data = new JsonObject();
		data.addProperty("blocktype", type.id);
		for(BlockProperty property: properties) {
			data.add(property.name(), property.save());
		}
		return data;
	}
}
