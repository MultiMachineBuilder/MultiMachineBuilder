/**
 * 
 */
package mmb.WORLD.worlds.universe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.GameObject;
import mmb.BEANS.Loader;
import mmb.BEANS.Saver;
import mmb.COLLECTIONS.Collects;
import mmb.COLLECTIONS.HashSelfSet;
import mmb.COLLECTIONS.SelfSet;
import mmb.DATA.json.JsonTool;
import mmb.RUNTIME.actions.WorldBehavior;
import mmb.WORLD.worlds.DataLayers;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Universe implements GameObject, Saver<JsonNode>, Loader<JsonNode>{
	private Debugger debug = new Debugger("WORLD - ");
	//[start] data
	/**
	 * This hashtable contains additional worlds. When playing the toolbar will say for example:
	 * [hell] Let's play
	 * 
	 * If playing on the main world, the toolbar will display without square brackets:
	 * Let's play
	 */
	public final SelfSet<String, World> maps = new HashSelfSet<>();
	/**
	 * Attached data layers
	 */
	public final SelfSet<String, UniverseDataLayer> data = new HashSelfSet<>();
	/**
	 * Active world behaviors
	 */
	public final List<WorldBehavior> behaviors = new ArrayList<>();
	/** The main world */
	private World main;
	/**@return the main*/
	public World getMain() {
		return main;
	}
	/** @param main the main to set*/
	public void setMain(World main) {
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
	public World getMap(@Nullable String name) {
		if(name == null) return main;
		return maps.get(name);
	}
	//[end]
	//[start] serialization
	//New Jackson methods
	@SuppressWarnings("null")
	@Override
	public void load(JsonNode data) {
		if(data instanceof ObjectNode) {
			ObjectNode on = (ObjectNode)data;
			if(on.has("main")) {//Load as a world	
				//Data
				ObjectNode nodeData = JsonTool.requestObject("data", on);
				@Nonnull Iterator<Entry<String, JsonNode>> iter = nodeData.fields();
				//Create all new data objects
				SelfSet<String, UniverseDataLayer> wdata = DataLayers.createAllWorldDataLayers();
				//Create data objects
				for(Entry<String, JsonNode> node: Collects.iter(iter)) {
					@Nonnull String key = Objects.requireNonNull(node.getKey(),
							"THIS MESSAGE INDICATES MALFUNCTION OF JACKSON API."
							+ " REPORT IMMEDIATELY AT https://github.com/FasterXML/jackson-databind/issues"
					);
					JsonNode value = node.getValue();
					if(value.isNull() || value.isMissingNode()) continue;
					wdata.get(key).load(value);
				}
				//Post-load data layers
				for(UniverseDataLayer d: wdata) {
					d.afterWorldLoaded(this);
				}
				
				//Maps
				ObjectNode nodeMaps = JsonTool.requestObject("maps", on);
				Iterator<Entry<String, JsonNode>> iter2 = nodeMaps.fields();
				for(Entry<String, JsonNode> ent: Collects.iter(iter2)) {
					World map = new World();
					map.saveLoad.load(ent.getValue());
					map.setName(ent.getKey());
					maps.add(map);
				}
				
				//Main map
				ObjectNode nodeMain = JsonTool.requestObject("main", on);
				World map = new World();
				map.setName(name);
				map.saveLoad.load(nodeMain);
				main = map;
			}else{//Load as a map
				World map = new World();
				map.setName(name);
				map.saveLoad.load(on);
				main = map;
			}
		}else debug.printl("Not an ObjectNode");
	}
	@Override
	public JsonNode save() {
		//Save the main map
		debug.printl("Saving main BlockMap");
		JsonNode mainNode = main.saveLoad.save();
		//Save extra maps
		ObjectNode mapsNode = JsonTool.newObjectNode();
		for(World map: maps) {
			debug.printl("Saving BlockMap: "+map.getName());
			mapsNode.set(map.getName(), map.saveLoad.save());
		}
		//Save data layers
		ObjectNode dataNode = JsonTool.newObjectNode();
		for(UniverseDataLayer datalayer: data) {
			debug.printl("Saving DataLayer: "+datalayer.id());
			dataNode.set(datalayer.id(), datalayer.save());
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
	public void setActive(boolean active) {
		main.setRunning(active);;
		maps.values().forEach(map -> map.setRunning(active));
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
	public String id() {
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
