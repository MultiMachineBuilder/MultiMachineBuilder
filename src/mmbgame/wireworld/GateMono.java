/**
 * 
 */
package mmbgame.wireworld;

import mmbeng.block.BlockEntity;
import mmbeng.block.BlockEntityType;
import mmbeng.block.BlockEntry;
import mmbeng.block.BlockEntityRotary;
import mmbeng.rotate.RotatedImageGroup;
import mmbeng.rotate.Side;
import mmbeng.worlds.MapProxy;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public class GateMono extends BlockEntityRotary{
	protected boolean result;
	public final MonoGateType type;
	
	public static interface MonoBool{
		/**
		 * @param a the input
		 * @return the result
		 */
		public boolean run(boolean a);
	}
	
	/**
	 * @author oskar
	 * The block type for block entity
	 */
	public static class MonoGateType extends BlockEntityType{
		public final MonoBool gate;
		public final RotatedImageGroup rig;
		public MonoGateType(String texture, MonoBool gate) {
			this.rig = RotatedImageGroup.create(texture);
			texture(texture);
			factory(() -> new GateMono(this));
			this.gate = gate;
		}
	}
	@Override
	public boolean provideSignal(Side s) {
		return (s == getRotation().U()) && result;
	}
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner().getAtSide(getRotation().D(), posX(), posY()).provideSignal(getRotation().U());
		result = type.gate.run(a);
	}
	@Override
	public MonoGateType type() {
		return type;
	}
	public GateMono(MonoGateType type) {
		super();
		this.type = type;
	}
	@Override
	public BlockEntry blockCopy() {
		BlockEntity result = new GateMono(type);
		result.setRotation(getRotation());
		return result;
	}
	@Override
	public RotatedImageGroup getImage() {
		return type.rig;
	}
}
