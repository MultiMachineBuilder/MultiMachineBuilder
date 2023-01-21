/**
 * 
 */
package mmb.content.electric.machines;

import mmb.NN;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.content.electric.helper.Helper;
import mmb.content.electric.helper.SimpleProcessHelper;
import mmb.engine.craft.singles.SimpleRecipeGroup;

/**
 * @author oskar
 * A machine capable of obtaining resources out of nothing
 */
public class ProcessorSimpleBlock extends ProcessorAbstractBlock{
	//Constructor
	public ProcessorSimpleBlock(ElectroMachineType type, SimpleRecipeGroup<?> group) {
		super(type);
		this.recipes = group;
		helper = new SimpleProcessHelper(group, in, out0, 1000, elec, null, type.volt);
	}
	
	//Contents
	@NN private final SimpleProcessHelper helper;
	@Override
	public Helper<?> helper() {
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
