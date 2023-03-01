/**
 * 
 */
package mmb.content.electric.machines;

import mmb.NN;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.content.electric.helper.Helper;
import mmb.content.electric.helper.SimpleProcessHelper;
import mmb.engine.craft.singles.SimpleRecipe;
import mmb.engine.craft.singles.SimpleRecipeGroup;
import mmb.engine.inv.storage.SingleItemInventory;

/**
 * Runs a stacked or single, catalyzed recipe. The catalyst selects the recipe from the pool of valid recipes with the same item.
 * @author oskar
 * @param <Trecipe> type of recipes
 */
public class ProcessorSimpleCatalyzedBlock<@NN Trecipe extends SimpleRecipe<Trecipe>> extends ProcessorAbstractBlock{
	
	//Constructor
	public ProcessorSimpleCatalyzedBlock(ElectroMachineType type, SimpleRecipeGroup<Trecipe> group) {
		super(type);
		this.recipes = group;
		helper = new SimpleProcessHelper<>(group, in, out0, 1000, elec, type.volt, catalyst);
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
	
	@NN private final SimpleProcessHelper<Trecipe> helper;
	@Override
	public SimpleProcessHelper<Trecipe> helper() {
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
