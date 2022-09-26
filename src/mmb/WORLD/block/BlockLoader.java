/**
 * 
 */
package mmb.WORLD.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.ExternalSaver;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class BlockLoader implements ExternalSaver<JsonNode, BlockEntry> {
	public int x, y;
	@Nonnull public final World map;
	/**
	 * Constructs a BlockLoader with given map
	 * @param map the map to use to load the blocks
	 */
	public BlockLoader(World map) {
		super();
		this.map = map;
	}
	private static final Debugger debug = new Debugger("BLOCK LOADER");

	@Override
	public BlockEntry load(@Nullable JsonNode data) {
		if(data == null) {
			debug.printl("The block data is null");
			return null;
		}
		if(data.isNull()) {
			debug.printl("The block data is null node");
			return null;
		}
		if(data.isMissingNode()) {
			debug.printl("The block data is a virtual node");
			return null;
		}
		if(data.isObject()) return doLoadEnhanced((ObjectNode) data); //with properties
		String text = data.asText(); //without properties
		if(text != null) return doLoadBasic(text);
		debug.printl("Unknown block data: "+data);
		return null;
	}
	private static BlockEntry doLoadBasic(String text) {
		BlockType type = Blocks.get(text);
				if(type == null) {
					debug.printl("Unknown block type: \""+text+"\"");
					return ContentsBlocks.grass;
				}
		return type.createBlock();
	}
	private BlockEntry doLoadEnhanced(ObjectNode on) {
		//find block type
		String blockName = on.get("blocktype").asText();
		BlockType typ = Blocks.get(blockName);
		if(typ == null) {
			debug.printl("Block type "+blockName+" was not found");
			return null;
		}
		BlockEntry block = typ.createBlock();
		try {
			block.load(on);
		}catch(Exception e) {
			debug.pstm(e, "Failed to load a block "+blockName+" at ["+x+","+y+"]");
		}
		return block;
	}
}
