/**
 * 
 */
package mmb.content.wireworld;

import mmb.engine.block.Block;
import mmb.engine.rotate.Side;

/**
 * This block emits eight random signals for each direction, including corners.
 * The signal is computed on demand, so the block runs faster
 * @author oskar
 * @see EmitUniformRandom
 */
public class EmitRandom extends Block {

	@Override
	public boolean provideSignal(Side s) {
		return Math.random() < 0.5;
	}

}
