/**
 * 
 */
package mmb.WORLD.blocks.defs;

import com.google.gson.JsonElement;

/**
 * @author oskar
 *
 */
public interface BlockModuleData<T> {
	public void load(JsonElement e);
}
