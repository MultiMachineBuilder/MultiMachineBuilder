/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import mmb.HashSelfSet;
import mmb.SelfSet;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.block.properties.BlockProperty;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 * 
 * This class represents a normal block entry
 */
public class BlockEntryNormal extends BlockEntry {
	public BlockType type;
	public SelfSet<String, BlockProperty> properties = new HashSelfSet<>();
	/**
	 * @param x @param y coordinates
	 * @param owner2 block owner
	 */
	public BlockEntryNormal(int x, int y, @NonNull BlockMap owner2, @NonNull BlockType typ) {
		super(x, y, owner2);
		type = typ;
	}

	@Override
	public BlockDrawer getTexture() {
		return type.drawer;
	}

	@Override
	public SelfSet<String, BlockProperty> getProperties() {
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

	@Override
	public boolean typeof(BlockType type) {
		return type == this.type;
	}

	@Override
	public BlockProperty getProperty(@NonNull String string) {
		return properties.get(string);
	}
}
