/**
 * 
 */
package mmb.WORLD.blocks.defs;

import java.util.HashMap;
import java.util.Map;

import mmb.WORLD.blocks.VariantHandler;

/**
 * @author oskar
 *
 */
public class VariantGroup<T extends Enum<T>> {
	/**
	 * The default state. This is also used to determine type of retrieved values due to the type erasure
	 */
	public T defaultOption;
	public Map<T, VariantHandler<T>> possibilities = new HashMap<T, VariantHandler<T>>();
	
	public T getByName(String name) {
		return Enum.valueOf(defaultOption.getDeclaringClass(), name);
	}
	public VariantHandler<T> getHandlerByName(String name) {
		return possibilities.get(getByName(name));
	}
}
