/**
 * 
 */
package mmb.WORLD.worlds.universe;

import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.vavr.Tuple2;
import mmb.GameEvents;
import mmb.BEANS.Saver;
import mmb.DATA.json.JsonTool;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import monniasza.collects.Collects;
import monniasza.collects.alloc.Allocator;
import monniasza.collects.alloc.Indexable;
import monniasza.collects.alloc.SimpleAllocator;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class Universe implements Saver<JsonNode>, Indexable{
	//Allocator & data layers
	private static SimpleAllocator<Universe> allocator0 = new SimpleAllocator<>();
	/** Allocator for universes */
	public static final Allocator<Universe> allocator = allocator0.readonly();
	private int ordinal; //ordinal, set to -1 to prevent abuse after universe dies
	@Override
	public int ordinal() {
		return ordinal;
	}
	@Override
	public Object index() {
		return allocator;
	}
	
	//Constructor
	/**
	 * Creates a new universe.
	 * Should not be used by mods
	 */
	public Universe() {
		ordinal = allocator0.allocate(this);
		GameEvents.onUniverseCreate.trigger(this);
	}
	
	private Debugger debug = new Debugger("WORLD - ");
	//Data
	/**
	 * This hashtable contains additional worlds. When playing the toolbar will say for example:
	 * [hell] Let's play
	 * 
	 * If playing on the main world, the toolbar will display without square brackets:
	 * Let's play
	 */
	public final SelfSet<String, World> maps = new HashSelfSet<>();
	/** The main world */
	private World main;
	/** @return the main world */
	public World getMain() {
		return main;
	}
	/** @param main new main world*/
	public void setMain(World main) {
		this.main = main;
	}

	//Identification
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

	//Serialization
	@SuppressWarnings("null")
	@Override
	public void load(@Nullable JsonNode data) {
		if(data instanceof ObjectNode) {
			ObjectNode on = (ObjectNode)data;
			if(on.has("main")) {//Load as a world				
				//Maps
				ObjectNode nodeMaps = JsonTool.requestObject("maps", on);
				Iterator<Entry<String, JsonNode>> iter2 = nodeMaps.fields();
				for(Entry<String, JsonNode> ent: Collects.iter(iter2)) {
					World map = World.load(ent.getValue());
					map.setName(ent.getKey());
					maps.add(map);
				}
				
				//Main map
				ObjectNode nodeMain = JsonTool.requestObject("main", on);
				World map = World.load(nodeMain);
				map.setName(name);
				main = map;
			}else{//Load as a map
				World map = World.load(on);
				map.setName(name);
				main = map;
			}
			GameEvents.onUniverseLoad.trigger(new Tuple2<>(this, on));
		}else debug.printl("Not an ObjectNode");
	}
	@Override
	@Nonnull public JsonNode save() {
		//Save the main map
		debug.printl("Saving main BlockMap");
		World main0 = main;
		if(main0 == null) throw new IllegalStateException("The main map is null");
		JsonNode mainNode = World.save(main0);
		
		//Save extra maps
		ObjectNode mapsNode = JsonTool.newObjectNode();
		for(World map: maps) {
			debug.printl("Saving BlockMap: "+map.getName());
			mapsNode.set(map.getName(), World.save(map));
		}
				
		//Node to save to
		ObjectNode master = JsonTool.newObjectNode();
		
		//Run the save event
		GameEvents.onUniverseSave.trigger(new Tuple2<>(this, master));
		
		//Combine all three
		master.set("main", mainNode); //main map
		master.set("maps", mapsNode); //extra maps
		return master;
	}
	
	//Activity
	/**
	 * Starts up the universe
	 */
	public void start() {
		main.startTimer(); //this fails
		maps.values().forEach(World::startTimer);
	}
	/**
	 * Destroy all associated resources.
	 */
	public void destroy() {		
		//Shut down all maps
		main.destroy();
		maps.values().forEach(World::destroy);
		
		//Kill the allocation
		GameEvents.onUniverseDie.trigger(this);
		allocator0.destroy(ordinal); //free up som space
		ordinal = -1; //stop use in data layers
	}
}
