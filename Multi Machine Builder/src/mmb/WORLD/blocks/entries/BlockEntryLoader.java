/**
 * 
 */
package mmb.WORLD.blocks.entries;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import mmb.ERRORS.WorldLoadException;

/**
 * @author oskar
 *
 */
public enum BlockEntryLoader {
	
	SIMPLE{
		@Override
		public BlockEntrySimple load(JsonElement e) {
			return new BlockEntrySimple(BlockEntries.getBlock(e.getAsString()));
		}
	},
	RESERVED{
		@Override                                       
		public BlockEntryReserved load(JsonElement e) {
			BlockEntryReserved ber = new BlockEntryReserved();
			JsonArray a = e.getAsJsonArray();
			ber.location.x = a.get(0).getAsInt();
			ber.location.y = a.get(1).getAsInt();
			return ber;
		}
	}, 
	LISTED{
		@Override
		public BlockEntry load(JsonElement e) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	public abstract BlockEntry load(JsonElement e);
	
	
	/**
	 * FORMAT SPECIFICATION FOR BLOCK ENTRIES:
	 * 
	 * @param je
	 * @throws WorldLoadException 
	 */
	public static BlockEntry loadBlockEntry(JsonElement je) throws WorldLoadException{
		if(je == null) return null;
		BlockEntryLoader bl = null;
		if(je.isJsonArray()) {
			//as array
			JsonArray a = je.getAsJsonArray();
			JsonElement first = a.get(0);
			if(first.isJsonPrimitive()) {
				JsonPrimitive p = first.getAsJsonPrimitive();
				if(p.isNumber()) {
					bl = RESERVED;
				}else{
					bl = LISTED;
				}
			}else{
				throw new WorldLoadException("The firts item in array block data must be a primitive");
			}
		}else if(je.isJsonPrimitive()) {
			bl = SIMPLE;
		}else if(je.isJsonObject()) {
			
		}else if(je.isJsonNull()) {
			return null;
		}else throw new WorldLoadException("The firts item in array block data must be a valid JSON value");
		return bl.load(je);
	}
}
