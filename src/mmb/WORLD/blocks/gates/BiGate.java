/**
 * 
 */
package mmb.WORLD.blocks.gates;

import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public final class BiGate extends SkeletalBlockEntityRotary{
	protected boolean result;
	public final BiGateType type;
	
	public BiGate(BiGateType type) {
		this.type = type;
	}
	
	public static interface BiBool{
		/**
		 * @param a the left input
		 * @param b the right input
		 * @return the result
		 */
		public boolean run(boolean a, boolean b);
	}
	
	/**
	 * @author oskar
	 * The block type for block entity
	 */
	public static class BiGateType extends BlockEntityType{
		public final BiBool gate;
		public final RotatedImageGroup rig;
		public BiGateType(String texture, BiBool gate) {
			this.rig = RotatedImageGroup.create(texture);
			texture(texture);
			factory(() -> new BiGate(this));
			this.gate = gate;
		}
	}
	
	@Override
	public boolean provideSignal(Side s) {
		return (s == getRotation().U()) && result;
	}
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner().getAtSide(getRotation().DL(), posX(), posY()).provideSignal(getRotation().DL());
		boolean b = owner().getAtSide(getRotation().DR(), posX(), posY()).provideSignal(getRotation().DR());
		result = type.gate.run(a, b);
	}
	@Override
	public BiGateType type() {
		return type;
	}
	@Override
	public BlockEntry blockCopy() {
		BlockEntity result = new BiGate(type);
		result.setRotation(getRotation());
		return result;
	}
	@Override
	public RotatedImageGroup getImage() {
		return type.rig;
	}
}
