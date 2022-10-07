/**
 * 
 */
package mmb.world.blocks.gates;

import mmb.world.block.Block;
import mmb.world.rotate.Side;

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
