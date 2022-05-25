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
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.electric.VoltageTier;

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
	 */
	public BaseMetalGroup(Color c, String id, VoltageTier volt, double baseEnergy, int orePerSmelt) {
		super(c, id, volt, baseEnergy);
		ore = new Block()
		.texture(TexGen.genOre(c))
		.title(t_nominative+" ore")
		.finish("ore."+id);
		crop = ContentsBlocks.crop(1500, ore, t_nominative+" crop", TexGen.genCrop(c), "crop."+id);
		
		//Recipes
		Craftings.smelting.add(ore, base, orePerSmelt, volt, baseEnergy);
		Craftings.crusher.add(ore, dust, 2*orePerSmelt, volt, baseEnergy/2);
	}
	
}