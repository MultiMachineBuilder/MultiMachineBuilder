/**
 * 
 */
package mmb.WORLD.blocks.actuators;

import java.awt.Point;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 *
 */
public class ActuatorClick extends AbstractActuatorBase {
	public ActuatorClick(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

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
		owner.click(p.x, p.y);
	}

}
