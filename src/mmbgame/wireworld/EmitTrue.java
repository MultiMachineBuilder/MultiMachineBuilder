/**
 * 
 */
package mmbgame.wireworld;

import mmbeng.block.Block;
import mmbeng.rotate.Side;

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
