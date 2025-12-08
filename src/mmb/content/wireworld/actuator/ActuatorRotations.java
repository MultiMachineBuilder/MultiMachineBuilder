/**
 * 
 */
package mmb.content.wireworld.actuator;

import java.awt.Point;

import mmb.annotations.Nil;
import mmb.beans.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.content.wireworld.AbstractChiralActuatorBase;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.ChirotatedImageGroup;
import mmb.engine.texture.Textures;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * @author oskar
 *
 */
public class ActuatorRotations extends AbstractChiralActuatorBase implements BlockActivateListener {
	//Block methods
	@Override
	public BlockType type() {
		return ContentsBlocks.ROTATOR;
	}
	@Override
	public BlockEntry blockCopy() {
		ActuatorRotations result = new ActuatorRotations();
		result.setChirotation(getChirotation());
		return result;
	}
	private static final ChirotatedImageGroup TEXTURE = ChirotatedImageGroup.create(Textures.get("machine/CW.png"));
	@Override
	public ChirotatedImageGroup getImage() {
		return TEXTURE;
	}
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		flip();
	}
	
	//Actuator methods
	@Override
	protected void run(Point p, BlockEntry ent, MapProxy proxy) {
		ent.wrenchRight(getChirality());
	}

	

	

}
