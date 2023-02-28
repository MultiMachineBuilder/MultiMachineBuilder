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
 * This block reads one signal, from the rear and processes it in a specific fashion, defined by this block's type,
 * and outputs the result of computation forward
 * @author oskar
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
