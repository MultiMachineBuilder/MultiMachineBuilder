/**
 * 
 */
package mmb.content.wireworld;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.engine.block.BlockEntity;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntityType;
import mmb.engine.block.BlockEntry;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;

/**
 * This block reads one signal and its state, from the rear and processes it in a specific fashion, defined by this block's type,
 * and outputs the result of computation forward and saves it for later
 * @author oskar
 *
 */
public class GateState extends BlockEntityRotary{
	
	public final StateGateType type;

	public GateState(StateGateType type) {
		this.type = type;
	}

	@Override
	public RotatedImageGroup getImage() {
		return state ? type.rigY : type.rigN;
	}

	protected boolean state;
	
	public static interface StatefulBool{
		/**
		 * @param a the input
		 * @param s current state
		 * @return the result, where byte 0 is result and byte 1 is new state
		 */
		public byte run(boolean a, boolean s);
	}
	
	/**
	 * @author oskar
	 * The block type for block entity
	 */
	public static class StateGateType extends BlockEntityType{
		public final StatefulBool gate;
		public final RotatedImageGroup rigY;
		public final RotatedImageGroup rigN;
		public StateGateType(String textureOn, String textureOff, String textureStanding, StatefulBool gate) {
			rigY = RotatedImageGroup.create(textureOn);
			rigN = RotatedImageGroup.create(textureOff);
			texture(textureStanding);
			factory(() -> new GateState(this));
			this.gate = gate;
		}
	}
	
	@Override
	protected final void save1(ObjectNode node) {
		node.put("state", state);
	}

	@Override
	protected final void load1(ObjectNode node) {
		state = node.get("state").asBoolean();
	}
	
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner().getAtSide(getRotation().D(), posX(), posY()).provideSignal(getRotation().U());
		byte res = type.gate.run(a, state);
		result = (res & 1) != 0;
		state = (res & 1) != 0;
	}
	
	@Override
	public StateGateType type() {
		return type;
	}
	@Override
	public BlockEntry blockCopy() {
		BlockEntity result = new GateState(type);
		result.setRotation(getRotation());
		return result;
	}
	
	protected boolean result;
	@Override
	public boolean provideSignal(Side s) {
		return (s == getRotation().U()) && result;
	}
}
