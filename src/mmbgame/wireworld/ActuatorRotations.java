/**
 * 
 */
package mmbgame.wireworld;

import java.awt.Point;

import javax.annotation.Nullable;

import mmb.cgui.BlockActivateListener;
import mmb.menu.world.window.WorldWindow;
import mmbeng.block.BlockEntry;
import mmbeng.block.BlockType;
import mmbeng.rotate.ChirotatedImageGroup;
import mmbeng.texture.Textures;
import mmbeng.worlds.MapProxy;
import mmbeng.worlds.world.World;
import mmbgame.ContentsBlocks;

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
