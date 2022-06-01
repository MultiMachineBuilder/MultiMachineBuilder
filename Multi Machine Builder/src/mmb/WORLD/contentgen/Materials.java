/**
 * 
 */
package mmb.WORLD.contentgen;

import java.awt.Color;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.ainslec.picocog.PicoWriter;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.WORLD.block.Blocks;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.contentgen.MetalGroup.MaterialStack;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.crafting.SimpleItemList;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.item.Item;
import mmb.WORLD.items.ItemEntry;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Materials {
	/**
	 * Initializes the material list
	 */
	public static void init() {
		Debugger debug = new Debugger("MATERIALS");
		debug.printl("Color of the coal: "+colorCoal);
	}
	private Materials() {}
	@Nonnull public static final Color colorCopper = new Color(255, 120, 0);
	@Nonnull public static final Color colorIron = new Color(115, 115, 115);
	@Nonnull public static final Color colorSilicon = new Color(200, 120, 120);
	@Nonnull public static final Color colorGold = new Color(255, 255, 0);
	@Nonnull public static final Color colorBrass = new Color(128, 128, 0);
	@Nonnull public static final Color colorUranium = new Color(0, 255, 0);
	@Nonnull public static final Color colorSilver = new Color(150, 150, 150);
	@Nonnull public static final Color colorStainless = new Color(156, 199, 199);
	@Nonnull public static final Color colorSteel = new Color(100, 100, 100);
	@Nonnull public static final Color colorCobalt = new Color(73, 52, 255);
	@Nonnull public static final Color colorBronze = new Color(128, 60, 0);
	@Nonnull public static final Color colorAlu = new Color(128, 140, 150);
	@Nonnull public static final Color colorRudimentary = new Color(255, 0, 0);
	@Nonnull public static final Color colorZinc = new Color(128, 140, 128);
	@Nonnull public static final Color colorTin = new Color(128, 128, 150);
	@Nonnull public static final Color colorCoal = new Color(0, 0, 0);	
	@Nonnull public static final Color colorTungsten = new Color(60, 60, 60);
	@Nonnull public static final Color colorTungstenC = new Color(30, 0, 60);
	@Nonnull public static final Color colorPlatinum = new Color(200, 200, 216);
	@Nonnull public static final Color colorIridium = new Color(200, 200, 255);
	@Nonnull public static final Color colorHSS = new Color(150, 150, 120);
	@Nonnull public static final Color colorUnobtainium = new Color(150, 255, 255);
	@Nonnull public static final Color colorCrystal = new Color(100, 255, 255);
	@Nonnull public static final Color colorStellar = new Color(120, 255, 255);
	@Nonnull public static final Color colorDuranium = new Color(20, 200, 255);
	@Nonnull public static final Color colorLead = new Color(40, 0, 80);
	@Nonnull public static final Color colorDiamond = new Color(0, 255, 255);
	@Nonnull public static final Color colorQuartz = new Color(180, 200, 255);
	@Nonnull public static final Color colorNickel = new Color(128, 128, 255);
	@Nonnull public static final Color colorSignalum = new Color(180, 60, 0);
	@Nonnull public static final Color colorEnderium = new Color(20, 80, 80);
	@Nonnull public static final Color colorChrome = new Color(255, 160, 200);
	@Nonnull public static final Color colorNichrome = new Color(255, 200, 220);
	@Nonnull public static final Color colorEnder = new Color(20, 120, 120);
	@Nonnull public static final Color colorGlowstone = new Color(180, 180, 10);
	@Nonnull public static final Color colorRedstone = new Color(180, 10, 10);
	@Nonnull public static final Color colorLumium = new Color(255, 255, 200);
	
	//Tier 1 materials
	@Nonnull public static final BaseMetalGroup rudimentary = new BaseMetalGroup(colorRudimentary, "rudimentary", VoltageTier.V1, 50_000, 3, false);
	@Nonnull public static final BaseMetalGroup coal = new BaseMetalGroup(colorCoal, "coal", VoltageTier.V1, 50_000, 3, true);
	
	//Tier 2 materials
	@Nonnull public static final BaseMetalGroup copper =   new BaseMetalGroup(colorCopper, "copper", VoltageTier.V1, 100_000, 1, false);
	@Nonnull public static final BaseMetalGroup iron =     new BaseMetalGroup(colorIron, "iron", VoltageTier.V1, 120_000, 1, false);
	@Nonnull public static final BaseMetalGroup silicon =  new BaseMetalGroup(colorSilicon, "silicon", VoltageTier.V1, 115_000, 1, false);
	@Nonnull public static final BaseMetalGroup tin =      new BaseMetalGroup(colorTin, "tin", VoltageTier.V1, 20_000, 1, false);
	@Nonnull public static final BaseMetalGroup zinc =     new BaseMetalGroup(colorZinc, "zinc", VoltageTier.V1, 30_000, 1, false);
	@Nonnull public static final BaseMetalGroup alu =      new BaseMetalGroup(colorAlu, "alu", VoltageTier.V1, 40_000, 1, false);
	@Nonnull public static final BaseMetalGroup lead =     new BaseMetalGroup(colorLead, "lead", VoltageTier.V1, 25_000, 1, false);
	@Nonnull public static final BaseMetalGroup nickel =   new BaseMetalGroup(colorNickel, "nickel", VoltageTier.V1, 120_000, 1, false);
	@Nonnull public static final BaseMetalGroup redstone = new BaseMetalGroup(colorRedstone, "redstone", VoltageTier.V1, 80_000, 15, true);
	
	//Tier 3 materials
	@Nonnull public static final BaseMetalGroup silver =   new BaseMetalGroup(colorSilver, "silver", VoltageTier.V1, 100_000, 1, false);
	@Nonnull public static final BaseMetalGroup uranium =  new BaseMetalGroup(colorUranium, "uranium", VoltageTier.V1, 200_000, 1, false);
	@Nonnull public static final MetalGroup steel =        new MetalGroup(colorSteel, "steel", VoltageTier.V2, 150_000, false);
	@Nonnull public static final MetalGroup bronze =       new MetalGroup(colorBronze, "bronze", VoltageTier.V1, 100_000, false);
	@Nonnull public static final MetalGroup brass =        new MetalGroup(colorBrass, "brass", VoltageTier.V1, 110_000, false);
	@Nonnull public static final BaseMetalGroup chrome =   new BaseMetalGroup(colorChrome, "chrome", VoltageTier.V2, 120_000, 1, false);
	@Nonnull public static final MetalGroup nichrome =     new MetalGroup(colorNichrome, "nichrome", VoltageTier.V2, 150_000, false);
	@Nonnull public static final MetalGroup quartz =       new MetalGroup(colorQuartz, "quartz", VoltageTier.V2, 150_000, true);
	@Nonnull public static final BaseMetalGroup ender =    new BaseMetalGroup(colorEnder, "ender", VoltageTier.V2, 150_000, 5, true);
	@Nonnull public static final BaseMetalGroup glowstone = new BaseMetalGroup(colorGlowstone, "glowstone", VoltageTier.V2, 130_000, 5, true);
	
	//Tier 4 materials
	@Nonnull public static final BaseMetalGroup cobalt =  new BaseMetalGroup(colorCobalt, "cobalt", VoltageTier.V2, 200_000, 1, false);
	@Nonnull public static final BaseMetalGroup gold =    new BaseMetalGroup(colorGold, "gold", VoltageTier.V1, 120_000, 1, false);
	@Nonnull public static final MetalGroup stainless =   new MetalGroup(colorStainless, "stainless", VoltageTier.V2, 180_000, false);
	@Nonnull public static final BaseMetalGroup diamond = new BaseMetalGroup(colorDiamond, "diamond", VoltageTier.V2, 250_000, 1, true);
	
	//Tier 5 materials
	@Nonnull public static final MetalGroup HSS =     new MetalGroup(colorHSS, "HSS", VoltageTier.V3, 250_000, false);
	@Nonnull public static final BaseMetalGroup platinum =    new BaseMetalGroup(colorPlatinum, "platinum", VoltageTier.V2, 170_000, 1, false);
	@Nonnull public static final MetalGroup lumium = new MetalGroup(colorLumium, "lumium", VoltageTier.V3, 400_000, false);
	
	//Tier 6 materials
	@Nonnull public static final BaseMetalGroup tungsten = new BaseMetalGroup(colorTungsten, "tungsten", VoltageTier.V4, 500_000, 1, false);
	@Nonnull public static final MetalGroup signalum = new MetalGroup(colorSignalum, "signalum", VoltageTier.V4, 600_000, false);
	@Nonnull public static final BaseMetalGroup iridium =    new BaseMetalGroup(colorIridium, "iridium", VoltageTier.V4, 220_000, 1, false);
	
	//Tier 7 materials
	@Nonnull public static final MetalGroup tungstenC =     new MetalGroup(colorTungstenC, "tungstenC", VoltageTier.V5, 1000_000, false);
	@Nonnull public static final MetalGroup crystal =     new MetalGroup(colorCrystal, "crystal", VoltageTier.V5, 4000_000, false);
	@Nonnull public static final MetalGroup enderium = new MetalGroup(colorEnderium, "enderium", VoltageTier.V5, 5000_000, false);
	
	//Tier 8 materials
	@Nonnull public static final MetalGroup stellar =     new MetalGroup(colorStellar, "stellar", VoltageTier.V6, 8000_000, false);
	@Nonnull public static final MetalGroup duranium =     new MetalGroup(colorDuranium, "duranium", VoltageTier.V6, 16_000_000, false);
	
	//Tier 9 materials
	@Nonnull public static final MetalGroup unobtainium =     new MetalGroup(colorUnobtainium, "unobtainium", VoltageTier.V7, 64_000_000, false);
	
	@Nonnull public static final WireGroup wireRudimentary = new WireGroup(50_000,    rudimentary, VoltageTier.V1);
	@Nonnull public static final WireGroup wireCopper      = new WireGroup(100_000,        copper, VoltageTier.V2);
	@Nonnull public static final WireGroup wireSilver      = new WireGroup(200_000,        silver, VoltageTier.V3);
	@Nonnull public static final WireGroup wireGold        = new WireGroup(500_000,          gold, VoltageTier.V4);
	@Nonnull public static final WireGroup wirePlatinum    = new WireGroup(1000_000,     platinum, VoltageTier.V5);
	@Nonnull public static final WireGroup wireIridium     = new WireGroup(2000_000,      iridium, VoltageTier.V6);
	@Nonnull public static final WireGroup wireCrystal     = new WireGroup(5000_000,      crystal, VoltageTier.V7);
	@Nonnull public static final WireGroup wireStellar     = new WireGroup(10000_000,     stellar, VoltageTier.V8);
	@Nonnull public static final WireGroup wireUnobtainium = new WireGroup(20000_000, unobtainium, VoltageTier.V9);

	static {
		//Deprecation
		Blocks.deprecate("mmb.iore", Materials.iron.ore);
		Blocks.deprecate("mmb.cpore", Materials.copper.ore);
		Blocks.deprecate("mmb.silicon_ore", Materials.silicon.ore);
		Blocks.deprecate("mmb.silverore", Materials.silver.ore);
		Blocks.deprecate("mmb.goldore", Materials.gold.ore);
		Blocks.deprecate("mmb.uraniumore", Materials.uranium.ore);
		Blocks.deprecate("elec.tinywire", wireRudimentary.tiny);
		Blocks.deprecate("elec.smallwire", wireRudimentary.small);
		Blocks.deprecate("elec.mediumwire", wireRudimentary.medium);
		Blocks.deprecate("elec.largewire", wireRudimentary.large);
		Blocks.deprecate("elec.infinite", ContentsBlocks.infinigens.blocks.get(0));
		Blocks.deprecate("elec.infinite1", ContentsBlocks.infinigens.blocks.get(0));
		
		//Alloying recipes
		alloying(copper, 4, zinc, 1, brass, 5, VoltageTier.V2, 80_000);
		alloying(copper, 3, tin, 1, bronze, 4, VoltageTier.V2, 75_000);
		alloying(nickel, 1, chrome, 1, nichrome, 2, VoltageTier.V2, 95_000);
		alloying(signalum, 4, VoltageTier.V4,  800_000, copper.stack(3), silver.stack(1),   redstone.stack(1));
		alloying(enderium, 2, VoltageTier.V5, 5000_000, tin.stack(3),    platinum.stack(1), ender.stack(1));
		alloying(lumium, 3, VoltageTier.V5,   400_000, tin.stack(3),     silver.stack(1),   glowstone.stack(1));
		
		//Furnace fuels
		Craftings.furnaceFuels.put(Materials.coal.base, 10_000_000);
		Craftings.furnaceFuels.put(ContentsBlocks.plank, 500_000);
	}
	/**
	 * @param partA the material A
	 * @param ratioA amount of material A per recipe
	 * @param partB the material B
	 * @param ratioB amount of material B per recipe
	 * @param out the output material
	 * @param ratioO amount of material 
	 * @param volt the voltage tier of this recipe
	 * @param energy energy required to smelt normal sized recipe (ingots/dusts)
	 */
	public static void alloying(MetalGroup partA, int ratioA, MetalGroup partB, int ratioB, MetalGroup out, int ratioO, VoltageTier volt, double energy) {
		//Tiny recipes
		alloyingHelper(partA.nugget,  partA.tinydust,  ratioA, partB.nugget,  partB.tinydust,  ratioB, out.nugget,  ratioO, volt, energy/16); //nuggets
		alloyingHelper(partA.frag,    partA.smalldust, ratioA, partB.frag,    partB.smalldust, ratioB, out.frag,    ratioO, volt, energy/4);  //fragments
		alloyingHelper(partA.base,    partA.dust,      ratioA, partB.base,    partB.dust,      ratioB, out.base,    ratioO, volt, energy);    //ingots
		alloyingHelper(partA.cluster, partA.megadust,  ratioA, partB.cluster, partB.megadust,  ratioB, out.cluster, ratioO, volt, energy*4);  //clusters
		//alloyingHelper(partA.block,   partA.tinydust,  ratioA, partB.block,   partB.tinydust,  ratioB, out.nugget, ratioO, volt, energy*16); //blocks
		
		//Large recipes
		ItemStack blokA = new ItemStack(partA.block, ratioA);
		ItemStack blokB = new ItemStack(partB.block, ratioB);
		double energyl = energy*16;
		RecipeOutput recl = new SimpleItemList(blokA, blokB);
		Craftings.alloyer.add(recl, out.block, ratioO, volt, energyl);
	}
	
	private static void alloyingHelper(Item itemA1, Item itemA2, int ratioA, Item itemB1, Item itemB2, int ratioB, Item output, int ratioOut, VoltageTier volt, double energy) {
		ItemStack itemStackA1 = new ItemStack(itemA1, ratioA);
		ItemStack itemStackB1 = new ItemStack(itemB1, ratioB);
		ItemStack itemStackA2 = new ItemStack(itemA2, ratioA);
		ItemStack itemStackB2 = new ItemStack(itemB2, ratioB);
		RecipeOutput recs1 = new SimpleItemList(itemStackA1, itemStackB1);
		Craftings.alloyer.add(recs1, output, ratioOut, volt, energy);
		RecipeOutput recs2 = new SimpleItemList(itemStackA1, itemStackB2);
		Craftings.alloyer.add(recs2, output, ratioOut, volt, energy);
		RecipeOutput recs3 = new SimpleItemList(itemStackA2, itemStackB1);
		Craftings.alloyer.add(recs3, output, ratioOut, volt, energy);
		RecipeOutput recs4 = new SimpleItemList(itemStackA2, itemStackB2);
		Craftings.alloyer.add(recs4, output, ratioOut, volt, energy);
	}

	public static void alloying(MetalGroup out, int ratioO, VoltageTier volt, double energy, MaterialStack... mats) {
		malloyingHelper(in -> in.nugget, mats, out, ratioO, volt, energy/16);
		malloyingHelper(in -> in.frag, mats, out, ratioO, volt, energy/4);
		malloyingHelper(in -> in.base, mats, out, ratioO, volt, energy);
		malloyingHelper(in -> in.cluster, mats, out, ratioO, volt, energy*4);
		malloyingHelper(in -> in.block, mats, out, ratioO, volt, energy*16);
	}
	private static void malloyingHelper(Function<@Nonnull MetalGroup, @Nonnull ItemEntry> selector, MaterialStack[] mat, MetalGroup out, int ratioO, VoltageTier volt, double energy) {
		Object2IntMap<ItemEntry> builder = new Object2IntOpenHashMap<>(mat.length);
		for(int i = 0; i < mat.length; i++) {
			builder.put(selector.apply(mat[i].material), mat[i].amount);
		}
		RecipeOutput in = new SimpleItemList(builder);
		Craftings.alloyer.add(in, selector.apply(out), ratioO, volt, energy);
	}
	/*
	 * Minecraft mod-based materials
	 * 
	 * Thermal Expansion:
	 * Signalum
	 * Enderium
	 * 
	 * Ender IO:
	 * 
	 * 
	 * Draconic Evolution:
	 * Draconium
	 * 
	 */
	
	/*
	 * Tier-Construction material-
	 * 1 ULV Rudimentary        Rudimentary Coal
	 * 2 VLV Iron               Copper      Nickel
	 * 3  LV Steel              Silver      Nichrome
	 * 4  MV Stainless          Gold        Tungsten
	 * 5  HV Titanium           Platinum    Tungsten
	 * 6  EV Signalum           
	 * 7  IV Enderium           
	 * 8 LuV Draconium          
	 * 9 ZPM Awakened Draconium 
	 * 10 UV Chaotic Draconium  
	 * 11MAX
	 */
}
