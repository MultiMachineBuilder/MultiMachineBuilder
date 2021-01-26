/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import mmb.HashSelfSet;
import mmb.SelfSet;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.block.properties.BlockProperty;
import mmb.WORLD.block.properties.BlockPropertyInfo;
import mmb.WORLD.worlds.map.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public abstract class BlockEntry {
	private static Debugger debug0 = new Debugger("BLOCK LOADER");
	//Positioning
	private final int x, y;
	public Point getPosition() {
		return new Point(x, y);
	}
	
	//Rendering
	public abstract BlockDrawer getTexture();
	
	public BlockEntry(int x, int y, BlockMap owner2) {
		super();
		this.x = x;
		this.y = y;
		this.owner = owner2;
	}

	public BlockMap owner;
	
	//Block entity
	/**
	 * @return list of block properties
	 */
	abstract public SelfSet<String,BlockProperty> getProperties();

	//Type
	/**
	 * @return type of the block
	 */
	abstract public BlockType getType();
	
	/**
	 * Get the original block
	 * @return the original block entry
	 */
	abstract public BlockEntry original();
	
	public BlockEntry routeToOriginal() {
		int X = x;
		int Y = y;
		BlockEntry target = this;
		
		while(true) {
			target = original();
			if(target.x == X && target.y == Y) return target;
			X = target.x;
			Y = target.y;
		}
	}
	
	/**
	 * Get the offset from the original
	 */
	public Point getOffset() {
		Point orig = getOriginalPosition();
		return new Point(x - orig.x, y - orig.y);
	}
	
	/**
	 * Gets position of original block
	 */
	public Point getOriginalPosition() {
		return original().getPosition();
	}
	
	abstract public JsonElement save();
	
	/**
	 * De-serialize given JSON object into block
	 * @param e source JSON entry
	 * @param x @param y coordinates
	 * @param requester target map
	 * @return loaded block entry
	 */
	public static BlockEntry load(JsonElement e, int x, int y, @NonNull BlockMap requester) {
		Objects.requireNonNull(requester, "The map can't be null");
		//null
		if(e == null || e.isJsonNull()) return null;
		//reserved
		if(e.isJsonArray()) {
			//Array: a reserved entry
			JsonArray ja = e.getAsJsonArray();
			BlockEntry ent = requester.get(ja.get(0).getAsInt(), ja.get(1).getAsInt());
			return new BlockEntryReserved(x, y, requester, ent, ja.get(2).getAsInt());
		}
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
			
			BlockEntryNormal proto = new BlockEntryNormal(x, y, requester, type);
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
	
	public int posX() {
		return x;
	}
	public int posY() {
		return y;
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
	public static BlockEntryNormal defaultProperties(int x, int y, BlockMap requester, BlockType typ) {
		if(typ == null) return null;
		BlockEntryNormal ben = new BlockEntryNormal(x, y, requester, typ);
		BlockPropertyInfo[] props = typ.properties;
		ArrayList<BlockProperty> props0 = new ArrayList<BlockProperty>();
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
	abstract public boolean typeof(BlockType type);

	abstract public BlockProperty getProperty(String string);
	
}
