/**
 * 
 */
package mmb.WORLD.electric;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;
import mmb.debug.Debugger;
import mmb.WORLD.block.BlockEntityData;

/**
 * @author oskar
 *
 */
public class Conduit extends BlockEntityData {
	private Electricity u, d, l, r;
	@Nonnull private final BlockType type;
	private TransferHelper tf;
	private static final Debugger debug = new Debugger("CONDUIT");
	/**
	 * @param type block type
	 * @param cap capacity in coulombs per tick
	 */
	public Conduit(BlockType type, double cap) {
		tf = new TransferHelper(this, cap);
		tf.pressureWt = cap*100;
		this.type = type;
		u = tf.proxy(Side.D);
		d = tf.proxy(Side.U);
		l = tf.proxy(Side.R);
		r = tf.proxy(Side.L);
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
	public void load(@Nullable JsonNode data) {
		
	}

	@Override
	protected void save0(ObjectNode node) {

	}
	
	/**
	 * @return maximum power in coulombs per tick
	 */
	public double condCapacity() {
		return tf.power;
	}

	@Override
	public Conduit clone() {
		Conduit copy = (Conduit) super.clone();
		copy.tf = new TransferHelper(copy, tf.power);
		copy.u = copy.tf.proxy(Side.D);
		copy.d = copy.tf.proxy(Side.U);
		copy.l = copy.tf.proxy(Side.R);
		copy.r = copy.tf.proxy(Side.L);
		return copy;
	}
	
	public TransferHelper getTransfer() {
		return tf;
	}

	@Override
	public void onTick(MapProxy map) {
		Electricity.equatePPs(this, map, tf, 0.99);
		//debug.printl("Power pressure: "+tf.pressure+" at ["+posX()+","+posY()+"]");
	}
}
