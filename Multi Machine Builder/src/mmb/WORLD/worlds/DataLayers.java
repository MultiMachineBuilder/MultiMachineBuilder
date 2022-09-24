/**
 * 
 */
package mmb.WORLD.worlds;

import com.fasterxml.jackson.databind.JsonNode;
import mmb.GameEvents;
import mmb.WORLD.worlds.universe.Universe;
import mmb.WORLD.worlds.world.DataLayer;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import monniasza.collects.datalayer.IndexedDatalayerMap;

/**
 * @author oskar
 *
 */
public class DataLayers {
	private DataLayers() {}
	private static final Debugger debug = new Debugger("DATA LAYERS");
	
	//Init
	private static boolean inited = false;
	/** Initializes data layer */
	public static void init() {
		if(inited) return;
		inited = true;
		
		//init logic
		
	}
	
	//Universe data layers
	
	//Map data layers
	
	//Map helper
	/**
	 * Sets up a world data layer for given JSON node
	 * @param <T> type of the data layer
	 * @param nodeName name of JSON node used
	 * @param databinder the indexed data layer map
	 */
	public static <T extends DataLayer<World>> void registerWorldDataLayerUsingNode
	(String nodeName, IndexedDatalayerMap<World, T> databinder){
		debug.printl("New world data layer: "+nodeName);
		GameEvents.onWorldLoad.addListener(tuple -> {
			JsonNode node = tuple._2.get(nodeName);
			T datalayer = databinder.get(tuple._1);
			if(node != null) datalayer.load(node);
		});
		GameEvents.onWorldSave.addListener(tuple -> {
			T datalayer = databinder.get(tuple._1);
			JsonNode save = datalayer.save();
			tuple._2.set(nodeName, save);
		});
	}
	
	//Universe helper
	/**
	 * Sets up a world data layer for given JSON node
	 * @param <T> type of the data layer
	 * @param nodeName name of JSON node used
	 * @param databinder the indexed data layer map
	 */
	public static <T extends DataLayer<Universe>> void registerUniverseDataLayerUsingNode
	(String nodeName, IndexedDatalayerMap<Universe, T> databinder){
		debug.printl("New universe data layer: "+nodeName);
		GameEvents.onUniverseLoad.addListener(tuple -> {
			JsonNode node = tuple._2.get(nodeName);
			T datalayer = databinder.get(tuple._1);
			if(node != null) datalayer.load(node);
		});
		GameEvents.onUniverseSave.addListener(tuple -> {
			T datalayer = databinder.get(tuple._1);
			JsonNode save = datalayer.save();
			tuple._2.set(nodeName, save);
		});
	}
}
