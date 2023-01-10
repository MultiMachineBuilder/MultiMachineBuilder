/**
 * 
 */
package mmb.content.electric;

import mmb.NN;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
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
	//Block methods
	@Override
	public Electricity getElectricalConnection(Side s) {
		return Electricity.NONE;
	}

	@Override
	public ElectroMachineType type() {
		return type;
	}

	@NN private final ElectroMachineType type;
	/** 
	 * Creates an infinite generator
	 * @param type block type
	 */
	public InfiniteGenerator(ElectroMachineType type) {
		this.type = type;
	}
	
	//Tick handler
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
		elec.insert(Double.POSITIVE_INFINITY, type.volt);
	}
}
