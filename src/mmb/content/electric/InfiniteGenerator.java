/**
 * 
 */
package mmb.content.electric;

import javax.annotation.Nonnull;

import mmb.engine.block.BlockEntityDataless;
import mmb.engine.block.BlockEntityType;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class InfiniteGenerator extends BlockEntityDataless {
	@Override
	public void onTick(MapProxy map) {
		shoveElectricity(Side.U);
		shoveElectricity(Side.D);
		shoveElectricity(Side.L);
		shoveElectricity(Side.R);
	}
	public void shoveElectricity(Side s) {
		Electricity elec = getAtSide(s).getElectricalConnection(s.negate());
		if(elec == null) return;
		elec.insert(Double.POSITIVE_INFINITY, voltage);
	}

	@Override
	public Electricity getElectricalConnection(Side s) {
		return Electricity.NONE;
	}

	@Override
	public BlockType type() {
		return type;
	}

	/**
	 * The voltage of this generator
	 */
	@Nonnull public final VoltageTier voltage;
	private final BlockEntityType type;
	public InfiniteGenerator(VoltageTier voltage, BlockEntityType type) {
		this.voltage = voltage;
		this.type = type;
	}
}
