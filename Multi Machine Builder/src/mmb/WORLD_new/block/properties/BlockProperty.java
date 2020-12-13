/**
 * 
 */
package mmb.WORLD_new.block.properties;

import com.google.gson.JsonElement;

/**
 * @author oskar
 *
 */
public interface BlockProperty {
	public void load(JsonElement e);
	public void save(JsonElement e);
	public String name();
}
