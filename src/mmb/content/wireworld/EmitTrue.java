/**
 * 
 */
package mmb.content.wireworld;

import mmb.engine.block.Block;
import mmb.engine.rotate.Side;

/**
 * This block always emits a signal in all directions, including corners
 * @author oskar
 */
public class EmitTrue extends Block {
	@Override
	public boolean provideSignal(Side s) {
		return true;
	}
}
