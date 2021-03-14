/**
 * 
 */
package mmb.WORLD.blocks;

import java.awt.Graphics;
import java.awt.Point;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.Rotable;
import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.Rotation;
import mmb.WORLD.block.SkeletalBlockEntityData;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public abstract class AbstractActuatorBase extends SkeletalBlockEntityData implements Rotable{
	public abstract RotatedImageGroup getImage();
	@Override
	public void render(int x, int y, Graphics g) {
		getImage().get(side).draw(x, y, g);
	}
	private Rotation side = Rotation.N;
	protected boolean result;
	/**
	 * @param x
	 * @param y
	 * @param owner2
	 */
	protected AbstractActuatorBase(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}
	protected abstract void run(Point p, BlockEntry ent);
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner.getAtSide(side.D(), x, y).provideSignal(side.U());
		Point pt = side.U().offset(x, y);
		if(a) run(pt, owner.get(pt.x, pt.y));
	}
	@SuppressWarnings({"null", "unused"})
	@Override
	public void load(JsonNode data) {
		side = Rotation.valueOf(data.get("side").asText());
		if(side == null) side = Rotation.N;
	}
	@Override
	public void setRotation(Rotation rotation) {
		side = rotation;
	}
	@Override
	public Rotation getRotation() {
		return side;
	}
	@Override
	protected void save0(ObjectNode node) {
		node.put("side", side.toString());
	}
}
