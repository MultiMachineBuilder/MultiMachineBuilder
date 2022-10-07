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
public class Randomizer extends Block {

	@Override
	public boolean provideSignal(Side s) {
		return Math.random() < 0.5;
	}

}
