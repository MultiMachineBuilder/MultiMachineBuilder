/**
 * 
 */
package mmb.world.blocks.actuators;

import java.awt.Point;

import mmb.texture.Textures;
import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.rotate.RotatedImageGroup;
import mmb.world.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class ActuatorClick extends AbstractActuatorBase {
	private static final RotatedImageGroup texture = RotatedImageGroup.create(Textures.get("machine/claw.png"));
	
	@Override
	public BlockType type() {
		return ContentsBlocks.CLICKER;
	}

	@Override
	public RotatedImageGroup getImage() {
		return texture;
	}

	@Override
	protected void run(Point p, BlockEntry ent, MapProxy proxy) {
		proxy.getMap().click(p.x, p.y);
	}

	@Override
	public BlockEntry blockCopy() {
		ActuatorClick result = new ActuatorClick();
		result.setChirotation(getChirotation());
		return result;
	}

}
