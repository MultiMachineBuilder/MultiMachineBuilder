/**
 * 
 */
package mmb.WORLD.blocks.pipe;

import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.machine.ItemTransporter;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 *
 */
public class IntersectingPipeExtractor extends Pipe {
	@SuppressWarnings("javadoc")
	public IntersectingPipeExtractor(int x, int y, BlockMap owner2, Side sideA, Side sideB, BlockType type,
			RotatedImageGroup rig) {
		super(x, y, owner2, sideA, sideB, type, rig);
	}

	@Override
	public void onTick(MapProxy map) {
		super.onTick(map);
		ItemTransporter.moveItems(x, y, map, side);
	}
	

}
