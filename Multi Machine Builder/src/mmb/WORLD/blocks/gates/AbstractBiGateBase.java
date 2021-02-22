/**
 * 
 */
package mmb.WORLD.blocks.gates;

import java.awt.Graphics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.Rotable;
import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.Side;
import mmb.WORLD.block.Rotation;
import mmb.WORLD.block.SkeletalBlockEntityData;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public abstract class AbstractBiGateBase extends SkeletalBlockEntityRotary implements Rotable{
	protected boolean result;
	/**
	 * @param x
	 * @param y
	 * @param owner2
	 */
	protected AbstractBiGateBase(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
		// TODO Auto-generated constructor stub
	}
	protected abstract boolean run(boolean a, boolean b);
	
	@Override
	public boolean provideSignal(Side s) {
		return (s == side.U()) && result;
	}
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner.getAtSide(side.DL(), x, y).provideSignal(side.DL());
		boolean b = owner.getAtSide(side.DR(), x, y).provideSignal(side.DR());
		result = run(a, b);
	}
}
