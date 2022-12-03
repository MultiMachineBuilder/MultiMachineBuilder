/**
 * 
 */
package mmb.world.electromachine;

import javax.annotation.Nonnull;

import mmb.beans.Electric;
import mmb.world.block.BlockEntry;
import mmb.world.blocks.SkeletalBlockEntityRotary;
import mmb.world.contentgen.ElectricMachineGroup.ElectroMachineType;
import mmb.world.electric.Electricity;
import mmb.world.rotate.RotatedImageGroup;

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
