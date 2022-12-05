/**
 * 
 */
package mmbgame.wireworld;

import java.awt.Point;

import mmbeng.block.BlockEntry;
import mmbeng.block.BlockType;
import mmbeng.rotate.RotatedImageGroup;
import mmbeng.texture.Textures;
import mmbeng.worlds.MapProxy;
import mmbgame.ContentsBlocks;

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
