/**
 * 
 */
package mmb.content.electric.machines;

import mmb.NN;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.content.electric.helper.ComplexProcessHelper;
import mmb.content.electric.helper.Helper;
import mmb.engine.craft.rgroups.ComplexCatRecipeGroup;
import mmb.engine.inv.storage.SingleItemInventory;

/**
 * @author oskar
 *
 */
public class ProcessorComplexCatalyzedBlock extends ProcessorAbstractBlock{
	//Constructor
	public ProcessorComplexCatalyzedBlock(ElectroMachineType type, ComplexCatRecipeGroup group) {
		super(type);
		this.recipes = group;
		helper = new ComplexProcessHelper(group, in, out0, 1000, elec, type.volt, catalyst);
	}

	//Contents
	@NN public final ComplexCatRecipeGroup recipes;
	@Override
	public ComplexCatRecipeGroup recipes() {
		return recipes;
	}

	@NN public final SingleItemInventory catalyst = new SingleItemInventory();
	@Override
	public SingleItemInventory catalyst() {
		return catalyst;
	}
	
	@NN private final ComplexProcessHelper helper;
	@Override
	public Helper<?> helper() {
		return helper;
	}
	
	//Block methods
	@Override
	protected ProcessorAbstractBlock copy0() {
		ProcessorComplexCatalyzedBlock copy = new ProcessorComplexCatalyzedBlock(type, recipes);
		copy.catalyst.setContents(catalyst.getContents());
		copy.helper.set(helper);
		return copy;
	}
}
