/**
 * 
 */
package mmb.content.rawmats;

import java.awt.Color;

import mmb.NN;
import mmb.content.CraftingGroups;
import mmb.content.agro.Agro;
import mmb.content.electric.VoltageTier;
import mmb.engine.block.Block;
import mmb.engine.block.BlockEntityType;
import mmb.engine.item.Items;
import mmb.engine.java2d.TexGen;

/**
 * An extension to a material group with an ore and a crop
 * @author oskar
 */
public class BaseMetalGroup extends MetalGroup{

	@NN public final Block ore;
	@NN public final BlockEntityType crop;
	
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
		crop = Agro.crop(1500, ore, materialConcatenateShort("mattype-crop"), TexGen.genCrop(c), "crop."+id);
		
		//Recipes
		CraftingGroups.smelting.add(ore, base, orePerSmelt, volt, baseEnergy);
		CraftingGroups.crusher.add(ore, dust, 2*orePerSmelt, volt, baseEnergy/2);
		
		//Tags
		Items.tagItems("material-"+id, ore, crop);
		Items.tagItem("shape-ore", ore);
		Items.tagItem("shape-crop", crop);
	}
}