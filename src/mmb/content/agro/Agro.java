/**
 * 
 */
package mmb.content.agro;

import static mmb.content.ContentsBlocks.ipipe_ELBOW;
import static mmb.content.ContentsBlocks.ipipe_STRAIGHT;
import static mmb.content.ContentsBlocks.leaves;
import static mmb.content.ContentsBlocks.logs;
import static mmb.content.ContentsBlocks.plank;
import static mmb.content.ContentsItems.paper;
import static mmb.content.CraftingGroups.crafting;
import static mmb.content.rawmats.Materials.iron;

import java.awt.image.BufferedImage;
import java.util.ResourceBundle;

import mmb.NN;
import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntityType;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.item.Item;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.Items;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.texture.Textures;

/**
 *
 * @author oskar
 *
 */
public class Agro {
	
	private Agro() {}
	static {
		GlobalSettings.injectResources(ResourceBundle.getBundle("mmb.content.agro.bundle"));
	}
	
	//Recipes
	/** Crop outputs */
	@NN public static final AgroRecipeGroup crops = new AgroRecipeGroup("agrorecipes");
	
	//Crop putputs
	/** Yeast, used to make beer */
	@NN public static final Item yeast = new Item()
		.title("#yeast")
		.texture("item/yeast.png")
		.volumed(0.001)
		.finish("item.yeast");
	/** Hops, used to make beer */
	@NN public static final Item hops = new Item()
		.title("#hops")
		.texture("item/hops.png")
		.volumed(0.001)
		.finish("item.hops");
	/** Seeds, used to make beer */
	@NN public static final Item seeds = new Item()
		.title("#seeds")
		.texture("item/seeds.png")
		.volumed(0.002)
		.finish("item.seeds");
	
	//Crops
	/** Tree, produces wood logs */
	@NN public static final BlockEntityType AGRO_TREE =
		Agro.crop(1500, ContentsBlocks.logs, "#machine-tree", Textures.get("block/tree.png"), "crop.tree");
	/** Water well, produces water */
	@NN public static final BlockEntityType AGRO_WATER =
		Agro.crop(1000, ContentsBlocks.water, "#machine-water", Textures.get("machine/water well.png"), "crop.water");
	/** Crop field, produces seeds */
	@NN public static final BlockEntityType AGRO_SEEDS =
		Agro.crop(1000, seeds, "#machine-seeds", Textures.get("block/cropfield.png"), "crop.seeds");
	/** Hop field, produces hops */
	@NN public static final BlockEntityType AGRO_HOPS =
		Agro.crop(1000, hops, "#machine-hops", Textures.get("machine/hops.png"), "crop.hops");
	
	
	
	
	
	private static boolean inited = false;
	/** Initializes items */
	public static void init() {
		if(inited) return;
		inited = true;
		
		//Recipes
		crafting.addRecipeGrid(new ItemEntry[]{
		leaves, leaves, leaves,
		plank,  plank,  plank,
		logs,   logs,   logs
		}, 3, 3, AGRO_TREE); //Tree
		crafting.addRecipeGrid(new ItemEntry[]{
		leaves, leaves, leaves,
		paper,  paper,  paper,
		logs,   logs,   logs
		}, 3, 3, AGRO_SEEDS); //Crop field
		crafting.addRecipeGrid(new ItemEntry[]{
		leaves, logs, leaves,
		paper,  paper,  paper,
		logs,   paper,   logs
		}, 3, 3, AGRO_HOPS); //Hops
		crafting.addRecipeGrid(new ItemEntry[]{
		iron.base, null,
		ipipe_ELBOW, ipipe_ELBOW,
		ipipe_STRAIGHT, null
		}, 2, 3, AGRO_WATER); //Water well
		
		Items.tagItems("shape-crop", AGRO_TREE, AGRO_WATER, AGRO_SEEDS, AGRO_HOPS);
	}
	//Reusable block methods
	@NN
	public static BlockEntityType crop(int duration, RecipeOutput cropDrop, String title, BufferedImage texture, String id) {
		BlockEntityType result = new BlockEntityType().title(title).texture(texture).finish(id);
		crops.add(result, cropDrop, duration);
		return result.factory(() -> new Crop(result, duration, cropDrop));
	}
}
