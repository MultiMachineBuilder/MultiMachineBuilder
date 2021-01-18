/**
 * 
 */
package mmb.WORLD_new.worlds.world;

import java.util.HashMap;
import java.util.function.Supplier;

import com.google.gson.JsonElement;

import mmb.WORLD_new.block.properties.BlockProperty;

/**
 * @author oskar
 *
 */
public interface WorldData {
	/**
	 * Load this data layer from given JSON data
	 * @param e
	 */
	public void load(JsonElement e);
	/**
	 * Serialize given data layer
	 * @return
	 */
	public JsonElement save();
	public String id();
	
	public HashMap<String, Supplier<WorldData>> properties = new HashMap<String, Supplier<WorldData>>();
	/**
	 * Create a new world data layer, or null if not found
	 * @param name block properties identifier
	 * @return a newly created block property
	 */
	public static WorldData create(String name) {
		return properties.getOrDefault(name, () -> null).get();
	}
	/**
	 * Get a factory for given world data layer
	 * @return retrieved 'Supplier'-type factory or null
	 * Returned value can throw {@link NullPointerException}, if called directly, To avoid this situation, use null-safe syntax or {@link getSafe(String)}
	 */
	public static Supplier<WorldData> get(String name){
		return properties.get(name);
	}
	/**
	 * Get a factory belonging to a given data layer. This method is slower, because it creates a null factory for eventual use.
	 * @return retrieved 'Supplier'-type factory for(given block property or null)
	 */
	public static Supplier<WorldData> getSafe(String name){
		return properties.getOrDefault(name, () -> null);
	}
	
	/**
	 * Load and deserialize given JSON data into a data layer
	 * @param name data layer name
	 * @param je deserializer data
	 * @return loaded data layer
	 */
	public static WorldData with(String name, JsonElement je) {
		WorldData prop = create(name);
		if(prop == null) return null;
		prop.load(je);
		return prop;
	}
}
