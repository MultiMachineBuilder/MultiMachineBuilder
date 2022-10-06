/**
 * 
 */
package mmb.WORLD.blocks.actuators;

import java.awt.Point;

import javax.annotation.Nullable;

import mmb.BEANS.BlockActivateListener;
import mmb.DATA.contents.Textures;
import mmb.MENU.world.window.WorldWindow;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.rotate.ChirotatedImageGroup;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;

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
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		flip();
	}

	@Override
	public BlockEntry blockCopy() {
		ActuatorRotations result = new ActuatorRotations();
		result.setChirotation(getChirotation());
		return result;
	}

}
