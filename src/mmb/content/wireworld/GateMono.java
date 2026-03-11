/**
 * 
 */
package mmb.content.wireworld;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.PropertyExtension;
import mmb.annotations.Nil;
import mmb.engine.block.BlockEntity;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
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
	public static class MonoGateType extends BlockType{
		public final MonoBool gate;
		public final RotatedImageGroup rig;
		public MonoGateType(String id, String texture, MonoBool gate, PropertyExtension... props) {
			super(id, props);
			this.rig = RotatedImageGroup.create(texture);
			setTexture(this.rig.U);
			setBlockFactory(json -> GateMono.load(this, json));
			this.gate = gate;
		}
	}
	@Override
	public boolean provideSignal(Side s) {
		return (s == getRotation().U()) && result;
	}
	public static GateMono load(MonoGateType monoGateType, @Nil JsonNode json) {
		GateMono result = new GateMono(monoGateType);
		result.loadRotation(json);
		return result;
	}
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner().getAtSide(getRotation().D(), posX(), posY()).provideSignal(getRotation().U());
		result = type.gate.run(a);
	}
	@Override
	public MonoGateType itemType() {
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
