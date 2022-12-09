/**
 * 
 */
package mmb.content.wireworld;

import mmb.engine.block.Block;
import mmb.engine.rotate.Side;

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
