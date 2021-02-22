/**
 * 
 */
package mmb.WORLD.blocks.gates;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public class UniformRandom extends AbstractUniformEmitterBase {
	public UniformRandom(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.URANDOM;
	}

	@Override
	public void onTick(MapProxy map) {
		map.later(() -> state = Math.random() < 0.5);
	}

}
