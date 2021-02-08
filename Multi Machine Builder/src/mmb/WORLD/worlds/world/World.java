/**
 * 
 */
package mmb.WORLD.worlds.world;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;

import mmb.GameObject;
import mmb.RUNTIME.RuntimeManager;
import mmb.RUNTIME.actions.WorldBehavior;
import mmb.WORLD.worlds.map.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class World implements GameObject{
	private Debugger debug = new Debugger("WORLD - ");
	/**
	 * Creates a blank world with only main map and square dimension, starting at (0, 0)
	 * @param d map size in blocks square side
	 * @return newly created map
	 */
	public static World createBlankWorld(Dimension d) {
		World w = new World();
		BlockMap bm = new BlockMap(d.width, d.height);
		w.main = bm;
		bm.setContainer(w);
		return w;
	}
	//[start] data
	/**
	 * This hashtable contains additional worlds. When playing the toolbar will say for example:
	 * [hell] Let's play
	 * 
	 * If playing on the main world, the toolbar will display without square brackets:
	 * Let's play
	 */
	public final Map<String, BlockMap> extras = new HashMap<>();
	/**
	 * Attached data layers
	 */
	public final Map<String, WorldData> data = new HashMap<>();
	/**
	 * Active world behaviors
	 */
	public final List<WorldBehavior> behaviors = new ArrayList<>();
	/** The main world */
	private BlockMap main;
	/**@return the main*/
	public BlockMap getMain() {
		return main;
	}
	/** @param main the main to set*/
	public void setMain(BlockMap main) {
		this.main = main;
	}
	//[end]	
	//[start] Identification
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
	/**
	 * Gets a map with given name.
	 * If name is null, return a main map
	 * @param name map name
	 * @return map with given name
	 */
	public BlockMap getMap(String name) {
		if(name == null) return main;
		return extras.get(name);
	}
	//[end]
	//[start] serialization
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
		extras.forEach((name0, map) -> subMaps.add(name0, map.serialize()));
		jo.add("maps", subMaps);
		JsonObject jsonData = new JsonObject();
		data.forEach((name0, dataobj) -> jsonData.add(name0, dataobj.save()));
		jo.add("data", jsonData);
		return jo;
	}
	/**
	 * @param tree input data
	 * @param name world name
	 * @return deserialized world
	 */
	public static World deserialize(JsonObject tree, String name) {
		if(tree.has("main")) {
			BlockMap mapMain = BlockMap.deserialize(tree.get("main").getAsJsonObject(), null);
			JsonObject objData = tree.get("data").getAsJsonObject();
			JsonObject mapsData = tree.get("maps").getAsJsonObject();
			World result = new World();
			result.name = name;
			result.main = mapMain;
			objData.entrySet().forEach(ent -> {
				WorldData data = WorldData.with(ent.getKey(), ent.getValue());
				result.data.put(ent.getKey(), data);
			});
			mapsData.entrySet().forEach(ent -> {
				BlockMap map = BlockMap.deserialize(ent.getValue(), ent.getKey());
				result.extras.put(ent.getKey(), map);
			});
			return result;
		}
		World result = new World();
		BlockMap map = BlockMap.deserialize(tree, name);
		result.main = map;
		result.name = name;
		return result;
	}
	//[end]
	//[start] activity
	/**
	 * Disable all maps and map behaviors
	 */
	public void close(){
		main.dispose();
		extras.values().forEach(map -> map.dispose());
	}
	/**
	 * Activate all maps and map behaviors
	 */
	public void activate() {
		main.activate();
		extras.values().forEach(map -> map.activate());
	}
	/**
	 * Destroy all associated resources.
	 */
	public void destroy() {
		
		//Shut down all active behaviors
		behaviors.forEach(e -> e.close(this));
		
		//Shut down all maps
		main.destroy();
		extras.values().forEach(map -> map.destroy());
	}
	//[end]
	@Override
	public String identifier() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterator<GameObject> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getUTID() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RuntimeManager getRuntimeManager() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public GameObject getOwner() {
		return null; //TBD in 0.7
	}
	@Override
	public GameObject getContainer() {
		return null;
	}
	@Override
	public void setContainer(GameObject container) {
	}
	@Override
	public boolean add(GameObject obj) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean remove(GameObject obj) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean contains(GameObject obj) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Set<GameObject> contents() {
		// TODO Auto-generated method stub
		return null;
	}
}
