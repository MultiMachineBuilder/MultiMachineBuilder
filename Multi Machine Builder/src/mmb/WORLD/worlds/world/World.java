/**
 * 
 */
package mmb.WORLD.worlds.world;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.GameObject;
import mmb.BEANS.Loader;
import mmb.BEANS.Saver;
import mmb.COLLECTIONS.HashSelfSet;
import mmb.COLLECTIONS.SelfSet;
import mmb.DATA.json.JsonTool;
import mmb.RUNTIME.RuntimeManager;
import mmb.RUNTIME.actions.WorldBehavior;
import mmb.WORLD.worlds.DataLayers;
import mmb.WORLD.worlds.map.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class World implements GameObject, Saver<JsonNode>, Loader<JsonNode>{
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
		bm.setOwner(w);
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
	public final SelfSet<String, BlockMap> maps = new HashSelfSet<>();
	/**
	 * Attached data layers
	 */
	public final SelfSet<String, WorldDataLayer> data = new HashSelfSet<>();
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
		return maps.get(name);
	}
	//[end]
	//[start] serialization
	//New Jackson methods
	@Override
	public void load(JsonNode data) {
		if(data instanceof ObjectNode) {
			ObjectNode on = (ObjectNode)data;
			if(on.has("main")) {//Load as a world	
				//Data
				ObjectNode nodeData = JsonTool.requestObject("data", on);
				Iterator<Entry<String, JsonNode>> iter = nodeData.fields();
				//Create all new data objects
				SelfSet<String, WorldDataLayer> wdata = DataLayers.createAllWorldDataLayers();
				//Create data objects
				if(iter.hasNext()) for(Entry<String, JsonNode> node = iter.next(); iter.hasNext(); node = iter.next()) {
					wdata.get(node.getKey()).load(node.getValue());
				}
				for(WorldDataLayer d: wdata) {
					d.afterWorldLoaded(this);
				}
				
				//Maps
				ObjectNode nodeMaps = JsonTool.requestObject("maps", on);
				Iterator<Entry<String, JsonNode>> iter2 = nodeMaps.fields();
				if(iter2.hasNext()) for(Entry<String, JsonNode> node = iter2.next(); iter2.hasNext(); node = iter2.next()) {
					BlockMapLoader bml = new BlockMapLoader();
					bml.setName(node.getKey());
					bml.load((ObjectNode) node.getValue());
					BlockMap map = bml.getLoaded();
					maps.add(map);
				}
				
				//Main map
				ObjectNode nodeMain = JsonTool.requestObject("main", on);
				BlockMapLoader bml = new BlockMapLoader();
				bml.setName(name);
				bml.load(nodeMain);
				BlockMap map = bml.getLoaded();
				main = map;
			}else{//Load as a map
				BlockMapLoader bml = new BlockMapLoader();
				bml.setName(name);
				bml.load(on);
				BlockMap map = bml.getLoaded();
				main = map;
			}
		}else debug.printl("Not an ObjectNode");
	}
	@Override
	public JsonNode save() {
		//Save the main map
		debug.printl("Saving main BlockMap");
		JsonNode mainNode = main.save();
		//Save extra maps
		ObjectNode mapsNode = JsonTool.newObjectNode();
		for(BlockMap map: maps) {
			debug.printl("Saving BlockMap: "+map.getName());
			mapsNode.set(map.getName(), map.save());
		}
		//Save data layers
		ObjectNode dataNode = JsonTool.newObjectNode();
		for(WorldDataLayer datalayer: data) {
			debug.printl("Saving DataLayer: "+datalayer.identifier());
			dataNode.set(datalayer.identifier(), datalayer.save());
		}
		
		//Combine all three
		ObjectNode master = JsonTool.newObjectNode();
		master.set("main", mainNode); //main map
		master.set("data", dataNode); //main map
		master.set("maps", mapsNode); //main map
		return master;
	}
	//[end]
	//[start] activity
	/**
	 * Disable all maps and map behaviors
	 */
	public void close(){
		main.dispose();
		maps.values().forEach(map -> map.dispose());
	}
	/**
	 * Activate all maps and map behaviors
	 */
	public void activate() {
		main.activate();
		maps.values().forEach(map -> map.activate());
	}
	/**
	 * Destroy all associated resources.
	 */
	public void destroy() {
		
		//Shut down all active behaviors
		behaviors.forEach(e -> e.close(this));
		
		//Shut down all maps
		main.destroy();
		maps.values().forEach(map -> map.destroy());
	}
	//[end]
	//[start] GameObject
	@Override
	public String identifier() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getUTID() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public GameObject getOwner() {
		return null; //TBD in 0.7
	}
	//[end]
	

}
