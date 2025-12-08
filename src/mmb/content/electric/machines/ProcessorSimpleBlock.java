/**
 * 
 */
package mmb.content.electric.machines;

import mmb.annotations.NN;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.content.electric.helper.SimpleProcessHelper;
import mmb.content.electric.recipes.SimpleRecipeGroup;

/**
 * Runs a stacked or single, no-catalyst recipe
 * @author oskar
 */
public class ProcessorSimpleBlock extends ProcessorAbstractBlock{
	//Constructor
	public ProcessorSimpleBlock(ElectroMachineType type, SimpleRecipeGroup<?> group) {
		super(type);
		this.recipes = group;
		helper = new SimpleProcessHelper<>(group, in, out0, type.powermul, elec, type.volt, null);
	}
	
	//Contents
	@NN private final SimpleProcessHelper helper;
	@Override
	public SimpleProcessHelper helper() {
		return helper;
	}
	
	/** The recipe group used for this machine */
	public final SimpleRecipeGroup<?> recipes;
	@Override
	public SimpleRecipeGroup<?> recipes() {
		return recipes;
	}
	
	//Block methods
	@Override
	protected ProcessorAbstractBlock copy0() {
		return new ProcessorSimpleBlock(type, recipes);
	}

	
}
