/**
 * 
 */
package mmb.content.modular.chest;

import java.awt.Color;
import java.awt.image.BufferedImage;

import mmb.NN;
import mmb.Nil;
import mmb.content.ContentsBlocks;
import mmb.content.ContentsItems;
import mmb.content.CraftingGroups;
import mmb.content.ditems.ItemBOM;
import mmb.content.electric.VoltageTier;
import mmb.content.modular.part.Part;
import mmb.content.modular.part.PartEntity;
import mmb.content.modular.part.PartEntityType;
import mmb.content.modular.universal.MoverModule;
import mmb.content.modular.universal.Plug;
import mmb.content.modular.universal.MoverModule.MoverPair;
import mmb.content.rawmats.BaseMetalGroup;
import mmb.content.rawmats.MaterialType;
import mmb.content.rawmats.Materials;
import mmb.content.rawmats.MetalGroup;
import mmb.engine.block.BlockEntityType;
import mmb.engine.craft.Craftings;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.craft.SimpleItemList;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventories;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.io.InventoryReader.ExtractionLevel;
import mmb.engine.item.Item;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.Items;
import mmb.engine.java2d.TexGen;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.texture.Textures;

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
	@NN private static final String s_import = GlobalSettings.$res("modchest-import");
	@NN private static final String s_export = GlobalSettings.$res("modchest-export");
	
	//The modular chest itself
	/** The modular chest body */
	@NN public static final BlockEntityType chest = new BlockEntityType()
		.texture("modules/chest_body.png")
		.title("#modchest-chest")
		.factory(ModularChest::new)
		.finish("modchest.chest");
	
	//Modules
	/** Plug, blocks all traffic */
	@NN public static final Part plug = new Plug()
		.title("#modchest-plug")
		.texture("modules/plug.png")
		.volumed(0.002)
		.finish("modchest.plug");
	
	//Chest cores
	//Chest cores - simple
	@NN private static final ChestProductionParams paramsSimple
	= new ChestProductionParams("modchest-coresimple", "modules/chest_std.png", SimpleChestCore::new, "modchest.coreSimple");
	@NN public static final PartEntityType coreSimple1 = chestCore(paramsSimple, Color.RED,       6, 1);
	@NN public static final PartEntityType coreSimple2 = chestCore(paramsSimple, Color.YELLOW,   16, 2);
	@NN public static final PartEntityType coreSimple3 = chestCore(paramsSimple, Color.GREEN,    32, 3);
	@NN public static final PartEntityType coreSimple4 = chestCore(paramsSimple, Color.CYAN,     64, 4);
	@NN public static final PartEntityType coreSimple5 = chestCore(paramsSimple, Color.BLUE,    128, 5);
	@NN public static final PartEntityType coreSimple6 = chestCore(paramsSimple, Color.MAGENTA, 256, 6);
	@NN public static final PartEntityType coreSimple7 = chestCore(paramsSimple, Color.WHITE,  1024, 7);
	@NN public static final PartEntityType coreSimple8 = chestCore(paramsSimple, Color.BLACK,  4096, 8);
	@NN public static final PartEntityType coreSimple9 = chestCore(paramsSimple, Color.GRAY,  16384, 9);
	//Chest cores - drawers
	@NN private static final ChestProductionParams paramsDrawer
	= new ChestProductionParams("modchest-coredrawer", "modules/chest_drawer.png", DrawerChestCore::new, "modchest.coreDrawer");
	@NN public static final PartEntityType coreDrawer1 = chestCore(paramsDrawer, Color.RED,      12, 1);
	@NN public static final PartEntityType coreDrawer2 = chestCore(paramsDrawer, Color.YELLOW,   32, 2);
	@NN public static final PartEntityType coreDrawer3 = chestCore(paramsDrawer, Color.GREEN,    64, 3);
	@NN public static final PartEntityType coreDrawer4 = chestCore(paramsDrawer, Color.CYAN,    128, 4);
	@NN public static final PartEntityType coreDrawer5 = chestCore(paramsDrawer, Color.BLUE,    256, 5);
	@NN public static final PartEntityType coreDrawer6 = chestCore(paramsDrawer, Color.MAGENTA, 512, 6);
	@NN public static final PartEntityType coreDrawer7 = chestCore(paramsDrawer, Color.WHITE,  2048, 7);
	@NN public static final PartEntityType coreDrawer8 = chestCore(paramsDrawer, Color.BLACK,  8192, 8);
	@NN public static final PartEntityType coreDrawer9 = chestCore(paramsDrawer, Color.GRAY,  32768, 9);
	//Chest cores - set
	@NN private static final ChestProductionParams paramsSet
	= new ChestProductionParams("modchest-coreset", "modules/chest_unique.png", ChestCoreSet::new, "modchest.coreSet");
	@NN public static final PartEntityType coreSet1 = chestCore(paramsSet, Color.RED,      12, 1);
	@NN public static final PartEntityType coreSet2 = chestCore(paramsSet, Color.YELLOW,   32, 2);
	@NN public static final PartEntityType coreSet3 = chestCore(paramsSet, Color.GREEN,    64, 3);
	@NN public static final PartEntityType coreSet4 = chestCore(paramsSet, Color.CYAN,    128, 4);
	@NN public static final PartEntityType coreSet5 = chestCore(paramsSet, Color.BLUE,    256, 5);
	@NN public static final PartEntityType coreSet6 = chestCore(paramsSet, Color.MAGENTA, 512, 6);
	@NN public static final PartEntityType coreSet7 = chestCore(paramsSet, Color.WHITE,  2048, 7);
	@NN public static final PartEntityType coreSet8 = chestCore(paramsSet, Color.BLACK,  8192, 8);
	@NN public static final PartEntityType coreSet9 = chestCore(paramsSet, Color.GRAY,  32768, 9);
	//Chest core - single item
	@NN private static final ChestProductionParams paramsSingle
	= new ChestProductionParams("modchest-coresingle", "modules/chest_single.png", ChestCoreSingle::new, "modchest.coreSingle");
	@NN public static final PartEntityType coreSingle = chestCore(paramsSingle, Color.RED, 6, 1);
		
	//Chest modules
	@NN private static final BufferedImage moverImg = Textures.get("modules/mover.png");
	/** Single-item mover pair */
	@NN public static final MoverPair moverPairSingle = MoverModule.create(ModularChests::moverImplSingle, moverImg, "#modchest-single", "single");
	static void moverImplSingle(InventoryReader reader, InventoryWriter writer, @Nil ItemEntry settings, int stacking, double maxVolume) {
		if(settings != null && reader.level() == ExtractionLevel.RANDOM) {
			Inventories.transferStackVolumeLimited(reader, writer, settings, stacking, maxVolume); //Extract filtered
		}else {
			Inventories.transferFirst(reader, writer); //Extract sequentially
		}
	}
	
	@NN private static final BufferedImage moverMultiImg = TexGen.colormap(Color.RED, Color.BLUE, moverImg, null);
	/** Multi-item mover pair */
	@NN public static final MoverPair moverPairMulti = MoverModule.create(ModularChests::moverImplMulti, moverMultiImg, "#modchest-multi", "multi");
	static void moverImplMulti(InventoryReader reader, InventoryWriter writer, @Nil ItemEntry settings, int stacking, double maxVolume) {
		if(settings instanceof ItemBOM && reader.level() == ExtractionLevel.RANDOM) {
			ItemBOM bom = (ItemBOM) settings;
			RecipeOutput items = bom.contents();
			Inventories.transferMultiVolumeLimited(reader, writer, items, stacking, maxVolume);
		}
	}
	
	@NN private static final BufferedImage moverBulkImg = TexGen.colormap(Color.RED, Color.CYAN, moverImg, null);
	/** Bulk item mover pair */
	@NN public static final MoverPair moverPairBulk = MoverModule.create(ModularChests::moverImplBulk, moverBulkImg, "#modchest-bulk", "bulk");
	static void moverImplBulk(InventoryReader reader, InventoryWriter writer, @Nil ItemEntry settings, int stacking, double maxVolume) {
		if(settings instanceof ItemBOM && reader.level() == ExtractionLevel.RANDOM) {
			ItemBOM bom = (ItemBOM) settings;
			RecipeOutput items = bom.contents();
			Inventories.transferBulkVolumeLimited(reader, writer, items, stacking, maxVolume);
		}
	}
	
	//Helper classes
	private static class ChestProductionParams{
		@NN public final String prefix;
		@NN public final BufferedImage img;
		@NN public final ChestCoreCtor ctor;
		@NN public final String id;
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
	@NN private static PartEntityType chestCore(ChestProductionParams params, Color c, double capacity, int n) {
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
		
		//Recipes for cores
		Item[] chests = {
				ContentsBlocks.CHEST,  ContentsBlocks.CHEST1, ContentsBlocks.CHEST2,
				ContentsBlocks.CHEST3, ContentsBlocks.CHEST4, ContentsBlocks.CHEST5,
				ContentsBlocks.CHEST6, ContentsBlocks.CHEST7, ContentsBlocks.CHEST8};
		CraftingGroups.assembler.add(new SimpleItemList(
			ContentsBlocks.CHEST,
			Materials.silicon.nugget
		), coreSingle, plug, VoltageTier.V1, 8000);
		for(int i = 0; i < 9; i++) {
			int volt = (i < 2)?0:(i-2);
			int energint = (8000)<<(i*2);
			CraftingGroups.assembler.add(new SimpleItemList(
				chests[i],
				Materials.rudimentary.nugget
			), coresSimple[i], plug, VoltageTier.VOLTS.get(volt), energint);
			CraftingGroups.assembler.add(new SimpleItemList(
				chests[i],
				Materials.iron.nugget
			), coresDrawer[i], plug, VoltageTier.VOLTS.get(volt), energint);
			CraftingGroups.assembler.add(new SimpleItemList(
				chests[i],
				Materials.copper.nugget
			), coresSet[i], plug, VoltageTier.VOLTS.get(volt), energint);
		}
	
		//Recipes for modules
		CraftingGroups.crafting.addRecipeGrid(new ItemEntry[]{
		ContentsItems.paper, ContentsItems.paper, ContentsItems.paper,
		}, 3, 1, plug);
		moverRecipes(moverPairSingle, Materials.rudimentary);
		moverRecipes(moverPairMulti, Materials.rudimentium);
		moverRecipes(moverPairBulk, Materials.silver);
		
		//Recipe for the body
		CraftingGroups.crafting.addRecipeGrid(new ItemEntry[]{
		null, plug,                 null,
		plug, Materials.iron.frame, plug,
		null, plug,                 null
		}, 3, 3, chest);
	}
	
	private static void moverRecipes(MoverPair movers, MetalGroup frag) {
		CraftingGroups.crafting.addRecipeGrid(new ItemEntry[]{
			Materials.iron.frag, Materials.iron.frag,  Materials.iron.frag,
			null,                frag.frag,            null
		}, 3, 2, movers.importer);
		CraftingGroups.crafting.addRecipeGrid(new ItemEntry[]{
			null,                frag.frag,            null,
			Materials.iron.frag, Materials.iron.frag,  Materials.iron.frag
		}, 3, 2, movers.exporter);
		CraftingGroups.assembler.add(new SimpleItemList(
			frag.frag.stack(2),
			Materials.iron.frag.stack(3)
		), movers.importer.stack(4), plug, frag.volt, (frag.baseCost/2)+2000);
		CraftingGroups.crafting.addRecipeGrid(movers.importer, 1, 1, movers.exporter);
		CraftingGroups.crafting.addRecipeGrid(movers.exporter, 1, 1, movers.importer);
	}
}
