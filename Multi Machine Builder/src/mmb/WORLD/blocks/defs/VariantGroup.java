/**
 * 
 */
package mmb.WORLD.blocks.defs;

import java.util.HashMap;
import java.util.Map;

import mmb.WORLD.blocks.VariantHandler;

/**
 * @author oskar
 * @param <T> enum type
 *
 */
public class VariantGroup<T extends Enum<T>> {
	/**
	 * The default state. This is also used to determine type of retrieved values due to the type erasure
	 */
	public T defaultOption;
	public Map<T, VariantHandler<T>> possibilities = new HashMap<T, VariantHandler<T>>();
	
	@SuppressWarnings("unchecked")
	public T getByName(String name) {
		return (T) Enum.valueOf(defaultOption.getClass(), name);
	}
	public VariantHandler<T> getHandlerByName(String name) {
		return possibilities.get(getByName(name));
	}
}
