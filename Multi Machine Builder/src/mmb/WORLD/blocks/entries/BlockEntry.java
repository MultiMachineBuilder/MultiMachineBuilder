/**
 * 
 */
package mmb.WORLD.blocks.entries;

import com.google.gson.JsonElement;

/**
 * @author oskar
 *
 */
public interface BlockEntry {
	public void load(JsonElement e);
	public JsonElement save(JsonElement e);
}
