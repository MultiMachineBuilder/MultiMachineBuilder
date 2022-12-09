/**
 * 
 */
package mmbgame.electric.machines;

import javax.annotation.Nonnull;

import mmbeng.block.BlockEntry;
import mmbeng.block.BlockEntityRotary;
import mmbeng.rotate.RotatedImageGroup;
import mmbgame.electric.Electric;
import mmbgame.electric.Electricity;
import mmbgame.electric.ElectricMachineGroup.ElectroMachineType;

/**
 * This block receiver power from Power Towers
 * @author oskar
 */
public class BlockPowerReceiver extends BlockEntityRotary implements Electric {
	public static final RotatedImageGroup rig = RotatedImageGroup.create("machine/preceiver.png");
	@Nonnull private final ElectroMachineType type;

	@Override
	public ElectroMachineType type() {
		return type;
	}

	@Override
	public BlockEntry blockCopy() {
		return new BlockPowerReceiver(type);
	}
	/**
	 * Creates a power receiver
	 * @param type
	 */
	public BlockPowerReceiver(ElectroMachineType type) {
		this.type = type;
	}

	@Override
	public Electricity getElectricity() {
		Electricity elec = getAtSide(getRotation().U()).getElectricalConnection(getRotation().D());
		return Electricity.circuitBreaker(Electricity.optional(elec), type.volt, this);
	}

	@Override
	public RotatedImageGroup getImage() {
		return rig;
	}

}
