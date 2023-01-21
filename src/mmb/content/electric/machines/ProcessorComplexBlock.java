/**
 * 
 */
package mmb.content.electric.machines;

import mmb.NN;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.content.electric.helper.ComplexItemProcessHelper;
import mmb.content.electric.helper.Helper;
import mmb.engine.craft.rgroups.ComplexRecipeGroup;

/**
 * @author oskar
 *
 */
public class ProcessorComplexBlock extends ProcessorAbstractBlock{	
	//Constructor
	/**
	 * Creates an alloy smelter
	 * @param type
	 * @param group
	 */
	public ProcessorComplexBlock(ElectroMachineType type, ComplexRecipeGroup group) {
		super(type);
		this.group = group;
		helper = new ComplexItemProcessHelper(group, in, out0, 1000, elec, type.volt);
	}
	
	//Contents
	@NN public final ComplexRecipeGroup group;
	@Override
	public ComplexRecipeGroup recipes() {
		return group;
	}
	@NN private final ComplexItemProcessHelper helper;
	@Override
	public Helper<?> helper() {
		return helper;
	}

	//Block methods
	@Override
	protected ProcessorAbstractBlock copy0() {
		ProcessorComplexBlock copy = new ProcessorComplexBlock(type, group);
		copy.helper.set(helper);
		return copy;
	}

	
}
