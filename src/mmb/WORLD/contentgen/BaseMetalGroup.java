/**
 * 
 */
package mmb.WORLD.contentgen;

import java.awt.Color;

import javax.annotation.Nonnull;

import mmb.GRAPHICS.texgen.TexGen;
import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.item.Items;
import mmb.WORLD.recipes.CraftingGroups;

/**
 * @author oskar
 *
 */
public class BaseMetalGroup extends MetalGroup{

	@Nonnull public final Block ore;
	@Nonnull public final BlockEntityType crop;
	
	/**
	 * @param c display color
	 * @param id material ID
	 * @param volt minimum voltage tier for recipes
	 * @param baseEnergy base cost of smelting in joules
	 * @param orePerSmelt amount of material obtained by smelting an ore
	 * @param isGem is given material a gem?
	 */
	public BaseMetalGroup(Color c, String id, VoltageTier volt, double baseEnergy, int orePerSmelt, boolean isGem) {
		super(c, id, volt, baseEnergy, isGem);
		ore = new Block()
		.texture(TexGen.genOre(c))
		.title(materialConcatenateShort("mattype-ore"))
		.finish("ore."+id);
		crop = ContentsBlocks.crop(1500, ore, materialConcatenateShort("mattype-crop"), TexGen.genCrop(c), "crop."+id);
		
		//Recipes
		CraftingGroups.smelting.add(ore, base, orePerSmelt, volt, baseEnergy);
		CraftingGroups.crusher.add(ore, dust, 2*orePerSmelt, volt, baseEnergy/2);
		
		//Tags
		Items.tagItems("material-"+id, ore, crop);
		Items.tagItem("shape-ore", ore);
		Items.tagItem("shape-crop", crop);
	}
}