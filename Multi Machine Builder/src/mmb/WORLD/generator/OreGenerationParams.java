/**
 * 
 */
package mmb.WORLD.generator;

import mmb.WORLD.block.BlockType;
import monniasza.collects.Identifiable;

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
