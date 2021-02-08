/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Point;
import java.util.ArrayList;
import javax.annotation.Nonnull;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import mmb.COLLECTIONS.HashSelfSet;
import mmb.COLLECTIONS.SelfSet;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.block.properties.BlockProperty;
import mmb.WORLD.block.properties.BlockPropertyInfo;
import mmb.WORLD.worlds.map.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 * To be removed in 0.5.
 * @deprecated For machines, use {@link mmb.WORLD.machine.Machine}. For blocks, use {@link BlockType}
 */
public class BlockEntry {
	private static Debugger debug0 = new Debugger("BLOCK LOADER");
	
	public SelfSet<String, BlockProperty> properties = new HashSelfSet<>();
	
	//Positioning
	public final int x, y;
	public Point getPosition() {
		return new Point(x, y);
	}
	
	//Rendering
	public BlockDrawer getTexture() {
		return type.drawer;
	}
	
	public BlockEntry(int x, int y, BlockMap owner2) {
		super();
		this.x = x;
		this.y = y;
		this.owner = owner2;
	}

	public BlockMap owner;
	
	//Block entity

	//Type
	public BlockType type;

	/**
	 * De-serialize given JSON object into block
	 * @param e source JSON entry
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param requester target map
	 * @return loaded block entry
	 */
	public static BlockEntry load(JsonElement e, int x, int y, @NonNull BlockMap requester) {
		Objects.requireNonNull(requester, "The map can't be null");
		//null
		if(e == null || e.isJsonNull()) return null;
		//normal (without properties)
		if(e.isJsonPrimitive()) {
			//String: a normal entry without properties
			JsonPrimitive jp = e.getAsJsonPrimitive();
			if(jp != null && jp.isString()) {
				String typeName = jp.getAsString();
				if(typeName == null) {
					debug0.printl("The block type is null or of incorrect type");
					return null;
				}
				BlockType type = Blocks.get(typeName);
				if(type == null) {
					debug0.printl("Block type "+typeName+" was not found");
					return null;
				}
				return defaultProperties(x, y, requester, type);
			}
		}
		//normal (with properties)
		if(e.isJsonObject()) {
			//Object: a normal entry with properties
			JsonObject jo = e.getAsJsonObject();
			String typeName = null;
			JsonElement tmp = jo.get("blocktype");
			if(tmp == null) {
				debug0.printl("The block type is not correcly specified");
				return null;
			}
			if(tmp.isJsonPrimitive() && tmp.getAsJsonPrimitive().isString()) typeName = tmp.getAsString();
			if(typeName == null) {
				debug0.printl("The block type is null or of incorrect type");
				return null;
			}
			BlockType type = Blocks.get(tmp.getAsString());
			if(type == null) {
				debug0.printl("Block type "+typeName+" was not found");
				return null;
			}
			
			BlockEntry proto = new BlockEntry(x, y, requester, type);
			//TODO decode
			BlockPropertyInfo[] props = type.properties;
			SelfSet<String, BlockProperty> result = new HashSelfSet<>();
			for(int i = 0; i < props.length; i++) {
				BlockPropertyInfo prop = props[i];
				if(prop == null) continue;
				JsonElement element = jo.get(prop.identifier());
				if(element == null) {
					result.add(prop.createNew());
				}else {
					result.add(prop.load(element));
				}
			}
			proto.properties = result;
			return proto;
		}
		return null;
	}

	private static void loaderProperties(int x, int y, BlockMap requester, BlockType typ) {
		
	}
	/**
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param requester target map
	 * @param typ block type
	 * @return newly created block entry, ready to be set into map.
	 */
	public static BlockEntry defaultProperties(int x, int y, BlockMap requester, BlockType typ) {
		if(typ == null) return null;
		BlockEntry ben = new BlockEntry(x, y, requester, typ);
		BlockPropertyInfo[] props = typ.properties;
		ArrayList<BlockProperty> props0 = new ArrayList<>();
		for(int i = 0; i < props.length; i++) {
			BlockProperty item = props[i].createNew();
			if(item == null) debug0.printl("The block property "+props[i]+" was not found");
			else props0.add(item);
		}
		ben.properties.addAll(props0);
		return ben;
	}

	/**
	 * @return
	 */
	public BlockEntry[] getNeighbors4() {
		return new BlockEntry[] {
				owner.get(x, y-1),
				owner.get(x, y+1),
				owner.get(x-1, y),
				owner.get(x+1, y)
		};
	}
	public BlockEntry[] getNeighbors8() {
		return new BlockEntry[] {
				owner.get(x, y-1),
				owner.get(x, y+1),
				owner.get(x-1, y),
				owner.get(x+1, y),
				owner.get(x-1, y-1),
				owner.get(x-1, y+1),
				owner.get(x+1, y-1),
				owner.get(x+1, y+1)
		};
	}
	
	//[start] Rotation
	public Rotation rotation;
	public void wrenchCW() {
		rotation = type.rotations.cw(rotation);
	}

	
	public void wrenchCCW() {
		rotation = type.rotations.ccw(rotation);
	}

	
	public Rotation orientation() {
		return rotation;
	}
	//[end]
	
	/**
	 * @param x @param y coordinates
	 * @param owner2 block owner
	 */
	public BlockEntry(int x, int y, @Nonnull BlockMap owner2, @Nonnull BlockType typ) {
		this(x, y, owner2);
		type = typ;
	}

	public JsonElement save() {
		if(properties.isEmpty()) return new JsonPrimitive(type.id); //if block has no data
		JsonObject data = new JsonObject();
		data.addProperty("blocktype", type.id);
		for(BlockProperty property: properties) {
			data.add(property.name(), property.save());
		}
		return data;
	}

	public boolean typeof(BlockType type) {
		return type == this.type;
	}

	public BlockProperty getProperty(@Nonnull String string) {
		return properties.get(string);
	}
	
	
}
