/**
 * 
 */
package mmb.WORLD.worlds;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.COLLECTIONS.HashSelfSet;
import mmb.COLLECTIONS.SelfSet;
import mmb.WORLD.worlds.map.MapDataLayer;
import mmb.WORLD.worlds.world.WorldDataLayer;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class DataLayers {
	private static final Debugger debug = new Debugger("DATA LAYERS");
	//[start] World data layers
	private static Map<String, Class<? extends WorldDataLayer>> wdatas = new HashMap<>();
	public static void registerWorldData(String name, Class<? extends WorldDataLayer> cls) {
		wdatas.put(name, cls);
	}
	public static WorldDataLayer createWorldData(String name) {
		Class<? extends WorldDataLayer> c = wdatas.get(name);
		try {
			return c.newInstance();
		} catch (Exception e) {
			debug.pstm(e, "Failed to create "+c .getCanonicalName());
			return null;
		}
	}
	/**
	 * Create a full set of uninitialized world data layers
	 * @return all world data layers
	 */
	public static SelfSet<String, WorldDataLayer> createAllWorldDataLayers(){
		SelfSet<String,WorldDataLayer> result = new HashSelfSet<>();
		wdatas.forEach((s, c) -> {
			try {
				result.add(c.newInstance());
			} catch (Exception e) {
				debug.pstm(e, "Failed to create "+c .getCanonicalName());
			}
		});
		return result;
	}
	//[end]
	//[start] Map data layers
	private static Map<String, Class<? extends MapDataLayer>> mdatas = new HashMap<>();
	public static void registerMapData(String name, Class<? extends MapDataLayer> cls) {
		mdatas.put(name, cls);
	}
	public static MapDataLayer createMapData(String name) {
		Class<? extends MapDataLayer> c = mdatas.get(name);
		try {
			return c.newInstance();
		} catch (Exception e) {
			debug.pstm(e, "Failed to create "+c .getCanonicalName());
			return null;
		}
	}
	/**
	 * @return the immutable map of data layers
	 */
	public static Map<String, Class<? extends WorldDataLayer>> getWorldDataLayers(){
		return Collections.unmodifiableMap(wdatas);
	}
	/**
	 * Create a full set of uninitialized map data layers
	 * @return all map data layers
	 */
	public static SelfSet<String, MapDataLayer> createAllMapDataLayers(){
		SelfSet<String,MapDataLayer> result = new HashSelfSet<>();
		mdatas.forEach((s, c) -> {
			try {
				result.add(c.newInstance());
			} catch (Exception e) {
				debug.pstm(e, "Failed to create "+c .getCanonicalName());
			}
		});
		return result;
	}
	
	//[end]
	/**
	 * Load and deserialize given JSON data into a data layer
	 * @param name data layer name
	 * @param je deserializer data
	 * @return loaded data layer
	 */
	public static WorldDataLayer deserializeWorldDataLayer(String name, JsonNode je) {
		WorldDataLayer prop = createWorldData(name);
		if(prop == null) return null;
		prop.load(je);
		return prop;
	}
	/**
	 * Load and deserialize given JSON data into a data layer
	 * @param name data layer name
	 * @param je deserializer data
	 * @return loaded data layer
	 */
	public static MapDataLayer deserializeMapDataLayer(String name, JsonNode je) {
		MapDataLayer prop = createMapData(name);
		if(prop == null) return null;
		prop.load(je);
		return prop;
	}
}
