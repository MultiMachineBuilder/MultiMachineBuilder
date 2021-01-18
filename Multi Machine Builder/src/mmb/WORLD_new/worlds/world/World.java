/**
 * 
 */
package mmb.WORLD_new.worlds.world;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.HashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mmb.WORLD_new.block.BlockType;
import mmb.WORLD_new.worlds.map.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class World implements AutoCloseable{
	private Debugger debug = new Debugger("WORLD - ");
	private static Debugger sdebug = new Debugger("WORLDS");
	/** The main world */
	public BlockMap main;
	
	/**
	 * This hashtable contains additional worlds. When playing the toolbar will say for example:
	 * [hell] Let's play
	 * 
	 * If playing on the main world, the toolbar will display without square brackets:
	 * Let's play
	 */
	public HashMap<String, BlockMap> extras = new HashMap<String, BlockMap>();
	
	public HashMap<String, WorldData> data = new HashMap<String, WorldData>();
	
	/** The world name. */
	private String name;
	
	/** @return the name */
	public String getName() {
		return name;
	}

	/** @param name the name to set */
	public void setName(String name) {
		this.name = name;
		debug.id = "WORLD - "+name;
	}

	public BlockMap getMap(String name) {
		if(name == null) return main;
		return extras.get(name);
	}
	
	/**
	 * 
	 */
	public void destroy() {
		
	}
	
	
	public static World createBlankWorld(Dimension d) {
		World w = new World();
		BlockMap bm = new BlockMap(d.width, d.height);
		w.main = bm;
		bm.owner = w;
		return w;
	}

	/**
	 * Properties:
	 * main - main map
	 * maps - submaps
	 * data - world data layers
	 * @return serialized data
	 */
	public JsonObject serialize() {
		JsonObject jo = new JsonObject();
		jo.add("main", main.serialize());
		JsonObject subMaps = new JsonObject();
		extras.forEach((name, map) -> {
			subMaps.add(name, map.serialize());
		});
		jo.add("maps", subMaps);
		JsonObject jsonData = new JsonObject();
		data.forEach((name, dataobj) -> {
			jsonData.add(name, dataobj.save());
		});
		jo.add("data", jsonData);
		return jo;
	}

	/**
	 * @param tree
	 * @return
	 */
	public static World deserialize(JsonObject tree, String name) {
		if(tree.has("main")) {
			BlockMap mapMain = BlockMap.deserialize(tree.get("main").getAsJsonObject(), null);
			JsonObject objData = tree.get("data").getAsJsonObject();
			JsonObject mapsData = tree.get("maps").getAsJsonObject();
			World result = new World();
			result.main = mapMain;
			objData.entrySet().forEach((ent) -> {
				WorldData data = WorldData.with(ent.getKey(), ent.getValue());
				result.data.put(ent.getKey(), data);
			});
			mapsData.entrySet().forEach((ent) -> {
				BlockMap map = BlockMap.deserialize(ent.getValue(), ent.getKey());
				result.extras.put(ent.getKey(), map);
			});
			return result;
		}
		World result = new World();
		BlockMap map = BlockMap.deserialize(tree, name);
		result.main = map;
		return result;
	}

	@Override
	public void close(){
		main.close();
		extras.values().forEach((map) -> map.close());
	}
}
