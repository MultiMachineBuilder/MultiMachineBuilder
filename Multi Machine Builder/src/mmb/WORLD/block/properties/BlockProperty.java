/**
 * 
 */
package mmb.WORLD.block.properties;

import java.util.HashMap;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.BEANS.Identifiable;
import mmb.BEANS.Loader;
import mmb.BEANS.Saver;

/**
 * @author oskar
 * A block property is a modular and reusable value, used to store information about the block.
 * The classes should be annotated:
 * {@code @LoadProperty} - for constructor or static factory which accepts JSON property data
 * {@code @NewProperty} - for constructor or static factory which creates new block property
 */
public interface BlockProperty extends Identifiable<String>, Saver<JsonNode>, Loader<JsonNode> {
	/**
	 * A string identifier
	 * @return String ID
	 */
	public String name();
	
	public final static HashMap<String, Supplier<BlockProperty>> properties = new HashMap<String, Supplier<BlockProperty>>();
	/**
	 * Add 
	 * @param name
	 * @param value a Supplier, which creates new properties
	 */
	@Deprecated
	public static void add(String name, Supplier<BlockProperty> value) {
		properties.put(name, value);
	}
	/**
	 * Create a new block property, or null if not found
	 * @param name block properties identifier
	 * @return a newly created block property
	 */
	@Deprecated
	public static BlockProperty create(String name) {
		return properties.getOrDefault(name, () -> null).get();
	}
	/**
	 * Get a factory for given block entry
	 * @return retrieved 'Supplier'-type factory or null
	 * Returned value can throw {@link NullPointerException}, if called directly, To avoid this situation, use null-safe syntax or {@code getSafe(String)}
	 */
	@Deprecated
	public static Supplier<BlockProperty> get(String name){
		return properties.get(name);
	}
	/**
	 * Get a factory be
	 * @return retrieved 'Supplier'-type factory for(given block property or null)
	 */
	@Deprecated
	public static Supplier<BlockProperty> getSafe(String name){
		return properties.getOrDefault(name, () -> null);
	}
	@Deprecated
	public static BlockProperty with(String name, JsonNode je) {
		BlockProperty prop = create(name);
		if(prop == null) return null;
		prop.load(je);
		return prop;
	}
	@Override
	default String id() {
		return name();
	}
}
