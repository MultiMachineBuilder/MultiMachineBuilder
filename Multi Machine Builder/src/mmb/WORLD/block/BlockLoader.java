/**
 * 
 */
package mmb.WORLD.block;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.ExternalLoader;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class BlockLoader implements ExternalLoader<JsonNode, BlockEntry> {
	public int x, y;
	public World map;
	private static final Debugger debug = new Debugger("BLOCK LOADER");

	@Override
	public BlockEntry load(@Nullable JsonNode data) {
		if(data == null || data.isNull() || data.isMissingNode()) return null;
		if(data.isTextual()) return doLoadBasic(data.asText()); //without properties
		if(data.isObject()) return doLoadEnhanced((ObjectNode) data); //with properties
		return null;
	}
	private BlockEntry doLoadBasic(String text) {
		BlockType type = Blocks.get(text);
				if(type == null) return ContentsBlocks.grass;
		return type.create(x, y, map);
	}
	private BlockEntry doLoadEnhanced(ObjectNode on) {
		//find block type
		String blockName = on.get("blocktype").asText();
		BlockType typ = Blocks.get(blockName);
		if(typ == null) {
			debug.printl("Block type "+blockName+" was not found");
			return null;
		}
		if(typ.isBlockEntity()) {
			BlockEntity block = typ.create(x, y, map).asBlockEntity();
			try {
				block.load(on);
				block.onStartup(map);
			}catch(Exception e) {
				debug.pstm(e, "Failed to load a block "+blockName+" at ["+x+","+y+"]");
			}
			return block;
		}
		return typ.create(x, y, map);
	}
}
