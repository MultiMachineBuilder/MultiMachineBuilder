/**
 * 
 */
package mmb.WORLD.blocks.entries;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * @author oskar
 *
 */
public enum BlockEntryLoader {
	
	SIMPLE{
		public BlockEntrySimple load(JsonElement e) {
			
		}
	};
	
	
	
	/**
	 * FORMAT SPECIFICATION FOR BLOCK ENTRIES:
	 * 
	 * @param je
	 */
	public static BlockEntry loadBlockEntry(JsonElement je){
		BlockEntryLoader bl;
		if(je.isJsonArray()) {
			//as array
			JsonArray a = je.getAsJsonArray();
			JsonElement first = a.get(0);
			if(first.isJsonPrimitive()) {
				JsonPrimitive p = first.getAsJsonPrimitive();
				
			}
		}else if(je.isJsonPrimitive()) {
			
		}
	}
}
