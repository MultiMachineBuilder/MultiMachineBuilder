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
public class Randomizer extends Block {

	@Override
	public boolean provideSignal(Side s) {
		return Math.random() < 0.5;
	}

}
