/**
 * 
 */
package mmb.WORLD.block;

import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.LazyLoader;
import mmb.WORLD.block.properties.BlockProperty;
import mmb.WORLD.worlds.map.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class BlockLoader implements LazyLoader<JsonNode, BlockEntry> {
	private BlockEntry entry;
	public int x, y;
	public BlockMap map;
	private static final Debugger debug = new Debugger("BLOCK LOADER");
	@Override
	public void load(JsonNode data) {
		Objects.requireNonNull(map, "The map can't be null");
		entry = doLoad(data);
	}
	
	private BlockEntry doLoad(JsonNode data) {
		if(data == null || data.isNull() || data.isMissingNode()) return null;
		if(data.isTextual()) return doLoadBasic(data.asText()); //without properties
		if(data.isObject()) return doLoadEnhanced((ObjectNode) data); //with properties
		return null;
	}
	private BlockEntry doLoadBasic(String text) {
		BlockType typ = Blocks.get(text);
		BlockEntry block = new BlockEntry(x, y, map, typ);
		for(Class<? extends BlockProperty> cls: typ.properties) { //load block properties
			try {
				BlockProperty prop = cls.newInstance(); //create block property
				block.properties.add(prop);
			} catch (Exception e) {
				debug.pstm(e, "Failed to create block property: "+cls.getCanonicalName());
			}
		}
		return block;
	}
	private BlockEntry doLoadEnhanced(ObjectNode on) {
		//find block type
		String blockName = on.get("blocktype").asText();
		if(blockName == null) {
			debug.printl("The block type is null, absent or of incorrect type");
			return null;
		}
		BlockType typ = Blocks.get(blockName);
		if(typ == null) {
			debug.printl("Block type "+blockName+" was not found");
			return null;
		}
		BlockEntry block = new BlockEntry(x, y, map, typ);
		for(Class<? extends BlockProperty> cls: typ.properties) { //load block properties
			try {
				BlockProperty prop = cls.newInstance(); //create block property
				block.properties.add(prop);
				JsonNode value = on.get(prop.identifier());
				if(!value.isMissingNode()) prop.load(value);
			} catch (Exception e) {
				debug.pstm(e, "Failed to create block property: "+cls.getCanonicalName());
			}
		}
		return block;
	}

	@Override
	public BlockEntry getLoaded() {
		return entry;
	}

}
