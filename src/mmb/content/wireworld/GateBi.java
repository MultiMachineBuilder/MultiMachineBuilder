/**
 * 
 */
package mmb.content.wireworld;

import mmb.engine.block.BlockEntity;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntityType;
import mmb.engine.block.BlockEntry;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;

/**
 * This block reads two signals, from both rear corners and combines them in a specific fashion, defined by this block's type,
 * and outputs the result of computation forward
 * @author oskar
 */
public final class GateBi extends BlockEntityRotary{
	protected boolean result;
	public final BiGateType type;
	
	public GateBi(BiGateType type) {
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
			factory(() -> new GateBi(this));
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
		BlockEntity result = new GateBi(type);
		result.setRotation(getRotation());
		return result;
	}
	@Override
	public RotatedImageGroup getImage() {
		return type.rig;
	}
}
