/**
 * 
 */
package mmb.WORLD.electric;

import mmb.WORLD.block.BlockType;

import javax.annotation.Nonnull;

import mmb.WORLD.block.BlockEntityDataless;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;

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
