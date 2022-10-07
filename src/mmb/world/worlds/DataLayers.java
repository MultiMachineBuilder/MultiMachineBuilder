/**
 * 
 */
package mmb.world.worlds;

import java.util.function.Function;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import mmb.GameEvents;
import mmb.debug.Debugger;
import mmb.world.worlds.universe.Universe;
import mmb.world.worlds.world.DataLayer;
import mmb.world.worlds.world.World;
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
			debug.printl("Loading world data layer: "+nodeName);
			JsonNode node = tuple._2.get(nodeName);
			T datalayer = databinder.get(tuple._1);
			if(node != null) datalayer.load(node);
		});
		GameEvents.onWorldSave.addListener(tuple -> {
			debug.printl("Saving world data layer: "+nodeName);
			T datalayer = databinder.get(tuple._1);
			JsonNode save = datalayer.save();
			tuple._2.set(nodeName, save);
		});
	}
	/**
	 * Creates a world data layer for given JSON node
	 * @param <T> type of the data layer
	 * @param nodeName name of JSON node used
	 * @param populator populator for data layer
	 * @return a new data layer
	 */
	@Nonnull public static <T extends DataLayer<World>> IndexedDatalayerMap<World, T> createWorldDataLayerUsingNode
	(String nodeName, Function<World, T> populator){
		IndexedDatalayerMap<World, T> datalayer = new IndexedDatalayerMap<>(World.allocator, populator);
		registerWorldDataLayerUsingNode(nodeName, datalayer);
		return datalayer;
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
