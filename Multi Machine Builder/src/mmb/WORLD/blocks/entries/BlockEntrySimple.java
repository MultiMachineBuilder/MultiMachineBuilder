/**
 * 
 */
package mmb.WORLD.blocks.entries;

import com.google.gson.JsonElement;

import mmb.WORLD.blocks.defs.BlockDef;

/**
 * @author oskar
 * A simple block entry. 
 */
public class BlockEntrySimple extends BlockEntryAbstract {

	/**
	 * @param block
	 */
	public BlockEntrySimple(BlockDef block) {
		this.block = block;
	}

	@Override
	public JsonElement save(JsonElement e) {
		// TODO Auto-generated method stub
		return null;
	}
}
