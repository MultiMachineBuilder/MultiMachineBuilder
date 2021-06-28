/**
 * 
 */
package mmb.WORLD.blocks.gates;

import mmb.WORLD.Side;
import mmb.WORLD.block.Block;

/**
 * @author oskar
 *
 */
public class AlwaysTrue extends Block {
	@Override
	public boolean provideSignal(Side s) {
		return true;
	}
}
