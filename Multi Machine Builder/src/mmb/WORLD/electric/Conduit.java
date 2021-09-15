/**
 * 
 */
package mmb.WORLD.electric;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.block.BlockEntityData;

/**
 * @author oskar
 *
 */
public class Conduit extends BlockEntityData {
	private Electricity u, d, l, r;
	@Nonnull private final BlockType type;
	private TransferHelper tf;
	public Conduit(BlockType type, double cap) {
		tf = new TransferHelper(this, 50, cap/50);
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
	public double condCapacity() {
		return tf.maxPower;
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
		if(data == null) return;
		tf.amt = data.get("charge").asDouble();
	}

	@Override
	protected void save0(ObjectNode node) {
		node.put("charge", tf.amt);
	}

	@Override
	public Conduit clone() {
		Conduit copy = (Conduit) super.clone();
		copy.tf = new TransferHelper(copy, 50, tf.maxPower);
		copy.u = copy.tf.proxy(Side.D);
		copy.d = copy.tf.proxy(Side.U);
		copy.l = copy.tf.proxy(Side.R);
		copy.r = copy.tf.proxy(Side.L);
		return copy;
	}

}
