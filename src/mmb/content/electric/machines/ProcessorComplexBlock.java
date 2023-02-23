/**
 * 
 */
package mmb.content.electric.machines;

import mmb.NN;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.content.electric.helper.ComplexProcessHelper;
import mmb.engine.craft.rgroups.ComplexRecipeGroup;
import mmb.engine.craft.rgroups.ComplexRecipeGroup.ComplexRecipe;

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
		helper = new ComplexProcessHelper<@NN ComplexRecipe>(group, in, out0, 1000, elec, type.volt, null);
	}
	
	//Contents
	@NN public final ComplexRecipeGroup group;
	@Override
	public ComplexRecipeGroup recipes() {
		return group;
	}
	@NN private final ComplexProcessHelper<@NN ComplexRecipe> helper;
	@Override
	public ComplexProcessHelper<@NN ComplexRecipe> helper() {
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
