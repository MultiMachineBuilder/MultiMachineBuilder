/**
 * 
 */
package mmb.content.wireworld;

import mmb.PropertyExtension;
import mmb.engine.block.Block;
import mmb.engine.rotate.Side;

/**
 * This block always emits a signal in all directions, including corners
 * @author oskar
 */
public class EmitTrue extends Block {
	public EmitTrue(String id, PropertyExtension... properties) {
		super(id, properties);
	}

	@Override
	public boolean provideSignal(Side s) {
		return true;
	}
}
