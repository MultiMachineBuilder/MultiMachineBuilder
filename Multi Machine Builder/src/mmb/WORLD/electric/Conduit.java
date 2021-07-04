/**
 * 
 */
package mmb.WORLD.electric;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityData;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 *
 */
public class Conduit extends SkeletalBlockEntityData {
	private final Electricity u, d, l, r;
	@Nonnull private final BlockType type;
	private final TransferHelper tf;
	public Conduit(int x, int y, @Nonnull BlockMap map, @Nonnull BlockType type, double cap) {
		super(x, y, map);
		tf = new TransferHelper(map, x, y, 50, cap/50);
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
	public void load(@Nonnull JsonNode data) {
		tf.amt = data.get("charge").asDouble();
	}

	@Override
	protected void save0(ObjectNode node) {
		node.put("charge", tf.amt);
	}

}
