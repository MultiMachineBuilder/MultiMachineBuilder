/**
 * 
 */
package mmbeng.block;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmbeng.debug.Debugger;
import mmbeng.item.Items;
import mmbeng.worlds.world.World;

/**
 * A class to help load blocks
 * @author oskar
 */
public class BlockLoader{
	private BlockLoader() {}
	private static final Debugger debug = new Debugger("BLOCK LOADER");

	/**
	 * Loads a block
	 * @param data block data node
	 * @param x X coordinate of the block
	 * @param y Y coordinaye of the block
	 * @param map world which holds the block
	 * @return a loaded block
	 */
	public static BlockEntry load(@Nullable JsonNode data, int x, int y, World map) {
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
		if(data.isObject()) return doLoadEnhanced((ObjectNode) data, x, y); //with properties
		String text = data.asText(); //without properties
		if(text != null) return doLoadBasic(text);
		debug.printl("Unknown block data: "+data);
		return null;
	}
	private static BlockEntry doLoadBasic(String text) {
		BlockType type = Items.getExpectType(text, BlockType.class);
				if(type == null) {
					debug.printl("Unknown block type: \""+text+"\"");
					return Blocks.grass;
				}
		return type.createBlock();
	}
	private static BlockEntry doLoadEnhanced(ObjectNode on, int x, int y) {
		//find block type
		String blockName = on.get("blocktype").asText();
		BlockType typ = Items.getExpectType(blockName, BlockType.class);
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
