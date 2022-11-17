/**
 * 
 */
package mmb.world.modulars.chest;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import javax.annotation.Nonnull;

import mmb.GlobalSettings;
import mmb.data.contents.Textures;
import mmb.graphics.texgen.TexGen;
import mmb.world.block.BlockEntityType;
import mmb.world.item.Item;
import mmb.world.item.ItemEntity;
import mmb.world.item.ItemEntityType;
import mmb.world.item.Items;
import mmb.world.item.RotableItem;
import mmb.world.modulars.universal.Plug;
import mmb.world.part.Part;
import mmb.world.part.PartEntity;
import mmb.world.part.PartEntityType;

/**
 * Items and blocks for modular chests
 * @author oskar
 */
public class ModularChests {
	private ModularChests() {}
	/** Initializes the modular chests*/
	public static void init() {
		//empty
	}
	
	//Translation keys
	@Nonnull private static final String s_import = GlobalSettings.$res("modchest-import");
	@Nonnull private static final String s_export = GlobalSettings.$res("modchest-export");
	
	//The modular chest itself
	/** The modular chest body */
	@Nonnull public static final BlockEntityType chest = new BlockEntityType()
		.texture("modules/chest_body.png")
		.title("#modchest-chest")
		.factory(ModularChest::new)
		.finish("modchest.chest");
	
	//Modules
	@Nonnull public static final Part plug = new Plug()
		.title("#modchest-plug")
		.texture("modules/plug.png")
		.volumed(0.002)
		.finish("modchest.plug");
	
	//Chest cores
	//Chest cores - simple
	@Nonnull private static final ChestProductionParams paramsSimple
	= new ChestProductionParams("modchest-coresimple", "modules/chest_std.png", SimpleChestCore::new, "modchest.coreSimple");
	@Nonnull public static final PartEntityType coreSimple1 = chestCore(paramsSimple, Color.RED,       6, 1);
	@Nonnull public static final PartEntityType coreSimple2 = chestCore(paramsSimple, Color.YELLOW,   16, 2);
	@Nonnull public static final PartEntityType coreSimple3 = chestCore(paramsSimple, Color.GREEN,    32, 3);
	@Nonnull public static final PartEntityType coreSimple4 = chestCore(paramsSimple, Color.CYAN,     64, 4);
	@Nonnull public static final PartEntityType coreSimple5 = chestCore(paramsSimple, Color.BLUE,    128, 5);
	@Nonnull public static final PartEntityType coreSimple6 = chestCore(paramsSimple, Color.MAGENTA, 256, 6);
	@Nonnull public static final PartEntityType coreSimple7 = chestCore(paramsSimple, Color.WHITE,  1024, 7);
	@Nonnull public static final PartEntityType coreSimple8 = chestCore(paramsSimple, Color.BLACK,  4096, 8);
	@Nonnull public static final PartEntityType coreSimple9 = chestCore(paramsSimple, Color.GRAY,  16384, 9);
	//Chest cores - drawers
	@Nonnull private static final ChestProductionParams paramsDrawer
	= new ChestProductionParams("modchest-coredrawer", "modules/chest_drawer.png", DrawerChestCore::new, "modchest.coreDrawer");
	@Nonnull public static final PartEntityType coreDrawer1 = chestCore(paramsDrawer, Color.RED,      12, 1);
	@Nonnull public static final PartEntityType coreDrawer2 = chestCore(paramsDrawer, Color.YELLOW,   32, 2);
	@Nonnull public static final PartEntityType coreDrawer3 = chestCore(paramsDrawer, Color.GREEN,    64, 3);
	@Nonnull public static final PartEntityType coreDrawer4 = chestCore(paramsDrawer, Color.CYAN,    128, 4);
	@Nonnull public static final PartEntityType coreDrawer5 = chestCore(paramsDrawer, Color.BLUE,    256, 5);
	@Nonnull public static final PartEntityType coreDrawer6 = chestCore(paramsDrawer, Color.MAGENTA, 512, 6);
	@Nonnull public static final PartEntityType coreDrawer7 = chestCore(paramsDrawer, Color.WHITE,  2048, 7);
	@Nonnull public static final PartEntityType coreDrawer8 = chestCore(paramsDrawer, Color.BLACK,  8192, 8);
	@Nonnull public static final PartEntityType coreDrawer9 = chestCore(paramsDrawer, Color.GRAY,  32768, 9);
	//Chest cores - set
	@Nonnull private static final ChestProductionParams paramsSet
	= new ChestProductionParams("modchest-coreset", "modules/chest_unique.png", ChestCoreSet::new, "modchest.coreSet");
	@Nonnull public static final PartEntityType coreSet1 = chestCore(paramsSet, Color.RED,      12, 1);
	@Nonnull public static final PartEntityType coreSet2 = chestCore(paramsSet, Color.YELLOW,   32, 2);
	@Nonnull public static final PartEntityType coreSet3 = chestCore(paramsSet, Color.GREEN,    64, 3);
	@Nonnull public static final PartEntityType coreSet4 = chestCore(paramsSet, Color.CYAN,    128, 4);
	@Nonnull public static final PartEntityType coreSet5 = chestCore(paramsSet, Color.BLUE,    256, 5);
	@Nonnull public static final PartEntityType coreSet6 = chestCore(paramsSet, Color.MAGENTA, 512, 6);
	@Nonnull public static final PartEntityType coreSet7 = chestCore(paramsSet, Color.WHITE,  2048, 7);
	@Nonnull public static final PartEntityType coreSet8 = chestCore(paramsSet, Color.BLACK,  8192, 8);
	@Nonnull public static final PartEntityType coreSet9 = chestCore(paramsSet, Color.GRAY,  32768, 9);
	//Chest core - single item
	@Nonnull private static final ChestProductionParams paramsSingle
	= new ChestProductionParams("modchest-coresingle", "modules/chest_single.png", ChestCoreSingle::new, "modchest.coreSingle");
	@Nonnull public static final PartEntityType coreSingle = chestCore(paramsSingle, Color.RED, 6, 1);
	
	//Chest modules
	
	//Helper classes
	private static class ChestProductionParams{
		@Nonnull public final String prefix;
		@Nonnull public final BufferedImage img;
		@Nonnull public final ChestCoreCtor ctor;
		@Nonnull public final String id;
		public ChestProductionParams(String prefixKey, String texture, ChestCoreCtor ctor, String idPrefix) {
			this.prefix = GlobalSettings.$res(prefixKey);
			this.img = Textures.get(texture);
			this.ctor = ctor;
			this.id = idPrefix;
		}
	}
	/**
	 * Creates chest cores
	 * @author oskar
	 */
	public interface ChestCoreCtor{
		/**
		 * Constructs a chest core
		 * @param iet item type
		 * @param capacity capacity
		 * @return a new chest core
		 */
		public PartEntity apply(PartEntityType iet, double capacity);
	}
	
	//Helper methods
	@Nonnull private static PartEntityType chestCore(ChestProductionParams params, Color c, double capacity, int n) {
		//Color mapping
		BufferedImage outTexture = TexGen.colormap(Color.RED, c, params.img, null);
		String prefix = params.prefix;
		if(n > 0) prefix += " #"+n;
		
		PartEntityType iet = new PartEntityType();
		return iet.title(prefix)
		.texture(outTexture)
		.factory(() -> params.ctor.apply(iet, capacity))
		.finish(params.id+n);
	}
	
	static {
		PartEntityType[] coresSimple = {coreSimple1, coreSimple2, coreSimple3, coreSimple4, coreSimple5, coreSimple6, coreSimple7, coreSimple8, coreSimple9};
		PartEntityType[] coresDrawer = {coreDrawer1, coreDrawer2, coreDrawer3, coreDrawer4, coreDrawer5, coreDrawer6, coreDrawer7, coreDrawer8, coreDrawer9};
		PartEntityType[] coresSet =    {coreSet1,    coreSet2,    coreSet3,    coreSet4,    coreSet5,    coreSet6,    coreSet7,    coreSet8,    coreSet9};
		String[] chesttags = {"modular", "modchest"};
		Items.tagsItems(chesttags, coresDrawer);
		Items.tagsItems(chesttags, coresSimple);
		Items.tagsItems(chesttags, coresSet);
		Items.tagsItem(coreSingle, chesttags);
		Items.tagItems("modchest-drawer", coresDrawer);
		Items.tagItems("modchest-simple", coresSimple);
		Items.tagItems("modchest-set", coresSet);
		Items.tagItem("modchest-single", coreSingle);
		Items.tagsItems(chesttags, chest, plug);
	}
}
