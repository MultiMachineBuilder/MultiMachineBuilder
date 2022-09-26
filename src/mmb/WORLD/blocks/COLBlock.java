/**
 * 
 */
package mmb.WORLD.blocks;

import java.util.Arrays;

import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntry;

/**
 * @author oskar
 *
 */
public class COLBlock extends Block {
	@Override
	public BlockEntry createBlock() {
		final boolean disable = true;
		if(disable) return this;
		Error err = new InternalError("Crash it!");
		String stk = Arrays.deepToString(err.getStackTrace());
		if(stk.contains("mmb.WORLD.worlds.world.World.load")) throw err;
		return this;
	}
}
