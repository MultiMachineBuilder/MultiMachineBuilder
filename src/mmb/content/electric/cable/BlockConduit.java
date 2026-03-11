/**
 * 
 */
package mmb.content.electric.cable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.annotations.Nil;
import mmb.content.electric.Electricity;
import mmb.content.electric.TransferHelper;
import mmb.content.electric.VoltageTier;
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
	 * @param volt maximum voltage
	 */
	public BlockConduit(ConduitType type) {
		var volt = type.getVoltRating();
		tf = new TransferHelper(this, type.getPowerRating() / volt.volts, volt, 1);
		this.type = type;
		u = tf.proxy(Side.D);
		d = tf.proxy(Side.U);
		l = tf.proxy(Side.R);
		r = tf.proxy(Side.L);
	}

	//Electric properties
	private Electricity u;
	private Electricity d;
	private Electricity l;
	private Electricity r;
	private final ConduitType type;
	private TransferHelper tf;
	
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
	public ConduitType itemType() {
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
		BlockConduit copy = new BlockConduit(type);
		copy.tf.set(tf);
		return copy;
	}
	
	//Serialization
	@Override
	protected void save0(ObjectNode node) {
		node.put("ppres", tf.pressure());
	}
	public static BlockConduit load(ConduitType type, @Nil JsonNode data) {
		BlockConduit conduit = new BlockConduit(type);
		if(data == null) return conduit;
		JsonNode nodePPres = data.get("ppres");
		if(nodePPres != null) conduit.tf.setPressure(nodePPres.asDouble());
		return conduit;
	}
}
