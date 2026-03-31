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
import static mmb.materials.Materials.iron;

import java.awt.image.BufferedImage;
import java.util.ResourceBundle;

import mmb.PropertyExtension;
import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockType;
import mmb.engine.blockdrawer.TextureDrawer;
import mmb.engine.item.Item;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.Items;
import mmb.engine.recipe.ItemList;
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
	public static final AgroRecipeGroup crops = new AgroRecipeGroup("agrorecipes");
	
	//Crop outputs
	/** Yeast, used to make beer */
	public static final Item yeast = new Item("item.yeast",
		PropertyExtension.translateTitle("#yeast"),
		PropertyExtension.setTextureAsset("item/yeast.png")
	);
	/** Hops, used to make beer */
	public static final Item hops = new Item("item.hops",
		PropertyExtension.translateTitle("#hops"),
		PropertyExtension.setTextureAsset("item/hops.png")
	);
	/** Seeds, used to make beer */
	public static final Item seeds = new Item("item.seeds",
		PropertyExtension.translateTitle("#seeds"),
		PropertyExtension.setTextureAsset("item/seeds.png")
	);
	
	//Crops
	/** Tree, produces wood logs */
	public static final BlockType AGRO_TREE =
		Agro.crop(1500, ContentsBlocks.logs, "#machine-tree", Textures.get("block/tree.png"), "crop.tree");
	/** Water well, produces water */
	public static final BlockType AGRO_WATER =
		Agro.crop(1000, ContentsBlocks.water, "#machine-water", Textures.get("machine/water well.png"), "crop.water");
	/** Crop field, produces seeds */
	public static final BlockType AGRO_SEEDS =
		Agro.crop(1000, seeds, "#machine-seeds", Textures.get("block/cropfield.png"), "crop.seeds");
	/** Hop field, produces hops */
	public static final BlockType AGRO_HOPS =
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
	/**
	 * Creates a crop
	 * @param duration time in ticks between drops
	 * @param cropDrop item(s) to produce
	 * @param title block title
	 * @param texture block texture
	 * @param id block ID
	 * @return the crop block entity type
	 */
	public static CropType crop(int duration, ItemList cropDrop, String title, BufferedImage texture, String id) {
		var result = new CropType(id);
		result.drops = cropDrop.toRecipeOutput();
		result.time = duration;
		result.translateTitle(title);
		result.setTexture(new TextureDrawer(texture));
		crops.add(result, cropDrop, duration);
		result.setBlockFactory((json) -> Crop.load(result, duration, cropDrop, json));
		return result;
	}
}
