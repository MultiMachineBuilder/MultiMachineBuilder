/**
 * 
 */
package mmb.WORLD.electromachine;

import javax.annotation.Nonnull;

import mmb.BEANS.Electric;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.contentgen.ElectricMachineGroup.ElectroMachineType;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.rotate.RotatedImageGroup;

/**
 * This block receiver power from Power Towers
 * @author oskar
 */
public class BlockPowerReceiver extends SkeletalBlockEntityRotary implements Electric {
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
