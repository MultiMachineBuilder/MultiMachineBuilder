/**
 * 
 */
package mmb.content.electric;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.engine.block.BlockEntityData;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;

/**
 * A power cable, used to transfer electricity
 * @author oskar
 */
public class BlockConduit extends BlockEntityData {
	//Constructors
	/**
	 * @param type block type
	 * @param cap capacity in coulombs per tick
	 * @param volt maximum voltage
	 */
	public BlockConduit(BlockType type, double cap, VoltageTier volt) {
		tf = new TransferHelper(this, cap, volt, 1);
		this.type = type;
		this.volt = volt;
		u = tf.proxy(Side.D);
		d = tf.proxy(Side.U);
		l = tf.proxy(Side.R);
		r = tf.proxy(Side.L);
	}

	//Electric properties
	@NN private Electricity u;
	@NN private Electricity d;
	@NN private Electricity l;
	@NN private Electricity r;
	@NN private final BlockType type;
	@NN private TransferHelper tf;
	/** Maximum voltage of this conduit */
	@NN public final VoltageTier volt;
	
	//Conduit methods
	/** @return maximum power in coulombs per tick */
	public double condCapacity() {
		return tf.power;
	}
	/** @return the transfer helper */
	public TransferHelper getTransfer() {
		return tf;
	}

	//Block methods
	@Override
	public void onTick(MapProxy map) {
		Electricity.equatePPs(this, map, tf, 1, 0);
	}
	@Override
	public BlockType type() {
		return type;
	}
	@Override
	public Electricity getElectricalConnection(Side s) {
		switch(s) {
		case U:
			return u;
		case D:
			return d;
		case L:
			return l;
		case R:
			return r;
		default:
			return null;
		}
	}
	@Override
	public BlockEntry blockCopy() {
		BlockConduit copy = new BlockConduit(type, tf.power, volt);
		copy.tf.set(tf);
		return copy;
	}
	
	//Serialization
	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		JsonNode nodePPres = data.get("ppres");
		if(nodePPres != null) tf.setPressure(nodePPres.asDouble());
	}
	@Override
	protected void save0(ObjectNode node) {
		node.put("ppres", tf.pressure());
	}
}
