/**
 * 
 */
package mmb.WORLD.blocks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mmb.WORLD.block.properties.BlockProperty;

/**
 * @author oskar
 *
 */
public class BlockProperties implements Iterable<Class<? extends BlockProperty>>{
	public static final Map<Class<? extends BlockProperty>, String> props = new HashMap<>();
	/**
	 * @param prop the {@link BlockProperty}'s class literal
	 * @param name the {@code BlockProperty}'s ID
	 */
	public static void register(Class<? extends BlockProperty> prop, String name) {
		props.put(prop, name);
	}
	public static final BlockProperties INSTANCE = new BlockProperties();
	@Override
	public Iterator<Class<? extends BlockProperty>> iterator() {
		return props.keySet().iterator();
	}
}
