/**
 * 
 */
package mmb.WORLD.blocks.actuators;

import java.awt.Point;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.BEANS.Rotable;
import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 *
 */
public class ActuatorRotations extends AbstractActuatorBase implements BlockActivateListener {

	private static final RotatedImageGroup CW = RotatedImageGroup.create(Textures.get("machine/CW.png"));
	private static final RotatedImageGroup CCW = RotatedImageGroup.create(Textures.get("machine/CCW.png"));
	
	//False - CW
	//True - CCW
	private boolean direction;

	public ActuatorRotations(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.ROTATOR;
	}

	@Override
	protected void save1(ObjectNode node) {
		node.put("dir", direction);
	}

	@Override
	protected void load1(ObjectNode node) {
		direction = node.get("dir").asBoolean();
	}

	@Override
	public RotatedImageGroup getImage() {
		return direction ? CCW : CW;
	}

	@Override
	protected void run(Point p, BlockEntry ent, MapProxy proxy) {
		if(!(ent instanceof Rotable)) return;
		Rotable rot = (Rotable) ent;
		if(direction) rot.ccw();
		else rot.cw();
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window) {
		direction = !direction;
	}

}
