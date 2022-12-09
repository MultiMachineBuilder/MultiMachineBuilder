/**
 * 
 */
package mmbeng.worlds;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;

import mmbeng.GameEvents;
import mmbeng.debug.Debugger;
import mmbeng.worlds.universe.Universe;
import mmbeng.worlds.world.DataLayer;
import mmbeng.worlds.world.World;
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
	
	
	//Map data layers
	@SuppressWarnings("null")
	@Nonnull private static final Set<IndexedDatalayerMap<World, ? extends DataLayer<World>>> layersWorld0 =
		Collections.newSetFromMap(new IdentityHashMap<>());
	/** An unmodifiable set of world data layers */
	@Nonnull public static final Set<IndexedDatalayerMap<World, ? extends DataLayer<World>>> layersWorld =
		Collections.unmodifiableSet(layersWorld0);
	/**
	 * Sets up a world data layer for given JSON node
	 * @param <T> type of the data layer
	 * @param nodeName name of JSON node used
	 * @param databinder the indexed data layer map
	 */
	public static <T extends DataLayer<World>> void registerWorldDataLayerUsingNode
	(String nodeName, IndexedDatalayerMap<World, T> databinder){
		debug.printl("New world data layer: "+nodeName);
		boolean add = layersWorld0.add(databinder);
		if(!add) throw new IllegalStateException("Data layer "+nodeName+" already exists");
		GameEvents.onWorldLoad.addListener(tuple -> {
			debug.printl("Loading world data layer: "+nodeName);
			JsonNode node = tuple._2.get(nodeName);
			T datalayer = databinder.get(tuple._1);
			if(datalayer == null) throw new InternalError("Data layer found must not be null for a valid world");
			if(node != null) datalayer.load(node);
			datalayer.startup();
		});
		GameEvents.onWorldSave.addListener(tuple -> {
			debug.printl("Saving world data layer: "+nodeName);
			T datalayer = databinder.get(tuple._1);
			if(datalayer == null) throw new InternalError("Data layer found must not be null for a valid world");
			datalayer.shutdown();
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
	
	//Universe data layers
	@SuppressWarnings("null")
	@Nonnull private static final Set<IndexedDatalayerMap<Universe, ? extends DataLayer<Universe>>> layersUniverse0 =
		Collections.newSetFromMap(new IdentityHashMap<>());
	/** An unmodifiable set of universe data layers */
	@Nonnull public static final Set<IndexedDatalayerMap<Universe, ? extends DataLayer<Universe>>> layersUniverse =
			Collections.unmodifiableSet(layersUniverse0);
	/**
	 * Sets up a world data layer for given JSON node
	 * @param <T> type of the data layer
	 * @param nodeName name of JSON node used
	 * @param databinder the indexed data layer map
	 */
	public static <T extends DataLayer<Universe>> void registerUniverseDataLayerUsingNode
	(String nodeName, IndexedDatalayerMap<Universe, T> databinder){
		boolean add = layersUniverse0.add(databinder);
		if(!add) throw new IllegalStateException("Data layer "+nodeName+" already exists");
		debug.printl("New universe data layer: "+nodeName);
		GameEvents.onUniverseLoad.addListener(tuple -> {
			JsonNode node = tuple._2.get(nodeName);
			T datalayer = databinder.get(tuple._1);
			if(datalayer == null) throw new InternalError("Data layer found must not be null for a valid universe");
			if(node != null) datalayer.load(node);
			datalayer.startup();
		});
		GameEvents.onUniverseSave.addListener(tuple -> {
			T datalayer = databinder.get(tuple._1);
			if(datalayer == null) throw new InternalError("Data layer found must not be null for a valid universe");
			datalayer.shutdown();
			JsonNode save = datalayer.save();
			tuple._2.set(nodeName, save);
		});
	}
}
