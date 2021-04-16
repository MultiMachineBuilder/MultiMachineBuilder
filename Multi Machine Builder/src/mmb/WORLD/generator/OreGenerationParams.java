/**
 * 
 */
package mmb.WORLD.generator;

import mmb.BEANS.Identifiable;
import mmb.WORLD.block.BlockType;

/**
 * @author oskar
 *
 */
public class OreGenerationParams implements Identifiable<BlockType> {
	private BlockType block;
	private double weight = 1;

	@Override
	public BlockType id() {
		return block;
	}
	
}
