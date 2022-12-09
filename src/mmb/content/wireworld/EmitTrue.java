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
public class EmitTrue extends Block {
	@Override
	public boolean provideSignal(Side s) {
		return true;
	}
}
