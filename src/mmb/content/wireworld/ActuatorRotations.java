/**
 * 
 */
package mmb.content.wireworld;

import java.awt.Point;

import mmb.Nil;
import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.ChirotatedImageGroup;
import mmb.engine.texture.Textures;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmbbase.cgui.BlockActivateListener;
import mmbbase.menu.world.window.WorldWindow;

/**
 * @author oskar
 *
 */
public class ActuatorRotations extends AbstractChiralActuatorBase implements BlockActivateListener {

	private static final ChirotatedImageGroup TEXTURE = ChirotatedImageGroup.create(Textures.get("machine/CW.png"));

	@Override
	public BlockType type() {
		return ContentsBlocks.ROTATOR;
	}

	@Override
	public ChirotatedImageGroup getImage() {
		return TEXTURE;
	}

	@Override
	protected void run(Point p, BlockEntry ent, MapProxy proxy) {
		ent.wrenchRight(getChirality());
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		flip();
	}

	@Override
	public BlockEntry blockCopy() {
		ActuatorRotations result = new ActuatorRotations();
		result.setChirotation(getChirotation());
		return result;
	}

}
