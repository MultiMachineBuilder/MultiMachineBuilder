package mmb.engine.recipe2.agro;

import mmb.content.electric.VoltageTier;
import mmb.engine.block.BlockEntityType;
import mmb.engine.recipe2.Recipe;
import mmb.engine.recipe2.RecipeOutput;
import mmb.engine.recipe2.processing.ProcessingRecipeData;

/**
 * Defines an agricultural recipe. The crop is not consumed.
 * The speed is the speed that crop grows naturally at.
 * In ULV machines it grows 4 times faster, thus the provided time is correct for natural growth and 25 amps ULV growing machine.
 * @param crop the crop the recipe applies to
 * @param drops items produced by the crop
 * @param time item every each drop
 */
public record AgroRecipe(BlockEntityType crop, RecipeOutput drops, double time) implements Recipe<BlockEntityType> {

	@Override
	public ProcessingRecipeData getProcessData() {
		return new ProcessingRecipeData(crop, crop, "", 0, VoltageTier.V1, drops, 2500 * time, 0);
	}

	@Override
	public BlockEntityType getConfiguration() {
		return crop;
	}
}
