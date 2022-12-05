/**
 * 
 */
package mmbgame.wireworld;

import mmbeng.block.BlockType;
import mmbeng.worlds.MapProxy;
import mmbgame.ContentsBlocks;

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
