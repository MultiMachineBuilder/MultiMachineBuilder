/**
 * 
 */
package mmb.world.blocks.gates;

import mmb.world.block.BlockEntity;
import mmb.world.block.BlockEntityType;
import mmb.world.block.BlockEntry;
import mmb.world.block.SkeletalBlockEntityRotary;
import mmb.world.rotate.RotatedImageGroup;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public class MonoGate extends SkeletalBlockEntityRotary{
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
			factory(() -> new MonoGate(this));
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
	public MonoGate(MonoGateType type) {
		super();
		this.type = type;
	}
	@Override
	public BlockEntry blockCopy() {
		BlockEntity result = new MonoGate(type);
		result.setRotation(getRotation());
		return result;
	}
	@Override
	public RotatedImageGroup getImage() {
		return type.rig;
	}
}
