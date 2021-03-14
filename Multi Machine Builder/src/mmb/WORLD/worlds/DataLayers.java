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
import mmb.WORLD.worlds.universe.UniverseDataLayer;
import mmb.WORLD.worlds.world.WorldDataLayer;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class DataLayers {
	private static final Debugger debug = new Debugger("DATA LAYERS");
	//[start] Universe data layers
	private static Map<String, Class<? extends UniverseDataLayer>> udatas = new HashMap<>();
	public static void registerUniverseData(String name, Class<? extends UniverseDataLayer> cls) {
		udatas.put(name, cls);
	}
	public static UniverseDataLayer createUniverseData(String name) {
		Class<? extends UniverseDataLayer> c = udatas.get(name);
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
	public static SelfSet<String, UniverseDataLayer> createAllWorldDataLayers(){
		SelfSet<String,UniverseDataLayer> result = new HashSelfSet<>();
		udatas.forEach((s, c) -> {
			try {
				result.add(c.newInstance());
			} catch (Exception e) {
				debug.pstm(e, "Failed to create "+c .getCanonicalName());
			}
		});
		return result;
	}
	/**
	 * Load and deserialize given JSON data into a data layer
	 * @param name data layer name
	 * @param je deserializer data
	 * @return loaded data layer
	 */
	public static UniverseDataLayer deserializeUniverseDataLayer(String name, JsonNode je) {
		UniverseDataLayer prop = createUniverseData(name);
		if(prop == null) return null;
		prop.load(je);
		return prop;
	}
	//[end]
	//[start] Map data layers
	private static Map<String, Class<? extends WorldDataLayer>> mdatas = new HashMap<>();
	public static void registerMapData(String name, Class<? extends WorldDataLayer> cls) {
		mdatas.put(name, cls);
	}
	public static WorldDataLayer createMapData(String name) {
		Class<? extends WorldDataLayer> c = mdatas.get(name);
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
	public static Map<String, Class<? extends UniverseDataLayer>> getWorldDataLayers(){
		return Collections.unmodifiableMap(udatas);
	}
	/**
	 * Create a full set of uninitialized map data layers
	 * @return all map data layers
	 */
	public static SelfSet<String, WorldDataLayer> createAllMapDataLayers(){
		SelfSet<String,WorldDataLayer> result = new HashSelfSet<>();
		mdatas.forEach((s, c) -> {
			try {
				result.add(c.newInstance());
			} catch (Exception e) {
				debug.pstm(e, "Failed to create "+c .getCanonicalName());
			}
		});
		return result;
	}
	/**
	 * Load and deserialize given JSON data into a data layer
	 * @param name data layer name
	 * @param je deserializer data
	 * @return loaded data layer
	 */
	public static WorldDataLayer deserializeMapDataLayer(String name, JsonNode je) {
		WorldDataLayer prop = createMapData(name);
		if(prop == null) return null;
		prop.load(je);
		return prop;
	}
	//[end]
}
