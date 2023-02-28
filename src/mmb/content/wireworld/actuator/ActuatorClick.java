/**
 * 
 */
package mmb.content.wireworld.actuator;

import java.awt.Point;

import mmb.content.ContentsBlocks;
import mmb.content.wireworld.AbstractActuatorBase;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.texture.Textures;
import mmb.engine.worlds.MapProxy;

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
