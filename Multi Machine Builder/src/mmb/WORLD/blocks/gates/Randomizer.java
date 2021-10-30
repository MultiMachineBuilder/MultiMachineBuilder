/**
 * 
 */
package mmb.WORLD.blocks.gates;

import mmb.WORLD.block.Block;
import mmb.WORLD.rotate.Side;

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