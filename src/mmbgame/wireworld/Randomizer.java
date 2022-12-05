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
public class Randomizer extends Block {

	@Override
	public boolean provideSignal(Side s) {
		return Math.random() < 0.5;
	}

}
