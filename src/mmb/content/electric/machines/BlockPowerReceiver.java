/**
 * 
 */
package mmb.content.electric.machines;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.content.electric.Electric;
import mmb.content.electric.Electricity;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Side;

/**
 * This block receiver power from Power Towers
 * @author oskar
 */
public class BlockPowerReceiver extends BlockEntityRotary implements Electric {
	/** The texture for a power receiver */
	public static final RotatedImageGroup rig = RotatedImageGroup.create("machine/preceiver.png");
	@NN private final ElectroMachineType type;

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
	public @Nil Electricity getElectricalConnection(Side s) {
		return Electricity.NONE;
	}

	@Override
	public RotatedImageGroup getImage() {
		return type.rig;
	}

	

}
