/**
 * 
 */
package mmb.WORLD.blocks.defs;

import com.google.gson.JsonElement;

/**
 * @author oskar
 *
 */
public interface BlockModule<T> {
	public T load(JsonElement e);
	public JsonElement save(T e);
	public BlockModuleData<T> createNew();
	default public BlockModuleData<T> loadNew(JsonElement e){
		BlockModuleData<T> bmd = createNew();
		bmd.load(e);
		return bmd;
	}
}
