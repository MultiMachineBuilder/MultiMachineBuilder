/**
 * 
 */
package mmb.world.blocks.gates;

import mmb.world.block.BlockType;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class UniformRandom extends AbstractUniformEmitterBase {
	@Override
	public BlockType type() {
		return ContentsBlocks.URANDOM;
	}

	@Override
	public void onTick(MapProxy map) {
		map.later(() -> state = Math.random() < 0.5);
	}

}
