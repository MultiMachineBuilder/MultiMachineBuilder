/**
 * 
 */
package mmb.WORLD.blocks.actuators;

import java.awt.Point;

import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.AbstractActuatorBase;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public class ActuatorClick extends AbstractActuatorBase {
	public ActuatorClick(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public BlockType type() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RotatedImageGroup getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void run(Point p, BlockEntry ent) {
		owner.click(p.x, p.y);
	}

}
