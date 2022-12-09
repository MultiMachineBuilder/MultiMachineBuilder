/**
 * 
 */
package mmb.content.wireworld;

import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockType;
import mmb.engine.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class EmitUniformRandom extends EmitBase {
	@Override
	public BlockType type() {
		return ContentsBlocks.URANDOM;
	}

	@Override
	public void onTick(MapProxy map) {
		map.later(() -> state = Math.random() < 0.5);
	}

}
