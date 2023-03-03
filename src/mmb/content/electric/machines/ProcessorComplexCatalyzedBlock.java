/**
 * 
 */
package mmb.content.electric.machines;

import mmb.NN;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.content.electric.helper.ComplexProcessHelper;
import mmb.content.electric.recipes.ComplexCatRecipeGroup;
import mmb.content.electric.recipes.ComplexCatRecipeGroup.ComplexCatalyzedRecipe;
import mmb.engine.inv.storage.SingleItemInventory;

/**
 * Runs a complex, catalyzed recipe. The catalyst selects the recipe from the pool of valid recipes with the same items.
 * @author oskar
 */
public class ProcessorComplexCatalyzedBlock extends ProcessorAbstractBlock{
	//Constructor
	public ProcessorComplexCatalyzedBlock(ElectroMachineType type, ComplexCatRecipeGroup group) {
		super(type);
		this.recipes = group;
		helper = new ComplexProcessHelper<@NN ComplexCatalyzedRecipe>(group, in, out0, 1000, elec, type.volt, catalyst);
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
	
	@NN private final ComplexProcessHelper<@NN ComplexCatalyzedRecipe> helper;
	@Override
	public ComplexProcessHelper<@NN ComplexCatalyzedRecipe> helper() {
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
