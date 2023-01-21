/**
 * 
 */
package mmb.content.electric.machines;

import mmb.NN;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.content.electric.helper.Helper;
import mmb.content.electric.helper.SimpleProcessHelper;
import mmb.engine.craft.singles.SimpleRecipeGroup;
import mmb.engine.inv.storage.SingleItemInventory;

/**
 * @author oskar
 * A machine capable of obtaining resources out of nothing
 */
public class ProcessorSimpleCatalyzedBlock extends ProcessorAbstractBlock{
	
	//Constructor
	public ProcessorSimpleCatalyzedBlock(ElectroMachineType type, SimpleRecipeGroup<?> group) {
		super(type);
		this.recipes = group;
		helper = new SimpleProcessHelper(group, in, out0, 1000, elec, catalyst, type.volt);
	}
	
	//Contents
	@NN public final SingleItemInventory catalyst = new SingleItemInventory();
	@Override
	public @NN SingleItemInventory catalyst() {
		return catalyst;
	}
	
	/** The recipe group used for this machine */
	public final SimpleRecipeGroup<?> recipes;
	@Override
	public SimpleRecipeGroup<?> recipes() {
		return recipes;
	}
	
	@NN private final SimpleProcessHelper helper;
	@Override
	public Helper<?> helper() {
		return helper;
	}
	
	//Block methods
	@Override
	protected ProcessorAbstractBlock copy0() {
		ProcessorSimpleCatalyzedBlock copy = new ProcessorSimpleCatalyzedBlock(type, recipes);
		copy.helper.set(helper);
		copy.catalyst.setContents(catalyst.getContents());
		return copy;
	}

	
}
