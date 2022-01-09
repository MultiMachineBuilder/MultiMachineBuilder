/**
 * 
 */
package mmb.WORLD.contentgen;

import java.awt.Color;

import javax.annotation.Nonnull;

import mmb.WORLD.block.Blocks;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.crafting.SimpleItemList;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.inventory.ItemStack;
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
	
	//Tier 1 materials
	@Nonnull public static final BaseMetalGroup rudimentary = new BaseMetalGroup(colorRudimentary, "rudimentary", "Rudimentary", VoltageTier.V1, 50_000, 3);
	@Nonnull public static final GemGroup coal = new GemGroup(colorCoal, "coal", "Coal", VoltageTier.V1, 50_000);
	
	//Tier 2 materials
	@Nonnull public static final BaseMetalGroup copper =    new BaseMetalGroup(colorCopper, "copper", "Copper", VoltageTier.V1, 100_000, 1);
	@Nonnull public static final BaseMetalGroup iron =      new BaseMetalGroup(colorIron, "iron", "Iron", VoltageTier.V1, 120_000, 1);
	@Nonnull public static final BaseMetalGroup silicon =   new BaseMetalGroup(colorSilicon, "silicon", "Silicon", VoltageTier.V1, 115_000, 1);
	@Nonnull public static final BaseMetalGroup tin =    new BaseMetalGroup(colorTin, "tin", "Tin", VoltageTier.V1, 20_000, 1);
	@Nonnull public static final BaseMetalGroup zinc =    new BaseMetalGroup(colorZinc, "zinc", "Zinc", VoltageTier.V1, 30_000, 1);
	@Nonnull public static final BaseMetalGroup alu =    new BaseMetalGroup(colorAlu, "alu", "Aluminum", VoltageTier.V1, 40_000, 1);
	@Nonnull public static final BaseMetalGroup lead =    new BaseMetalGroup(colorLead, "lead", "Lead", VoltageTier.V1, 25_000, 1);
	
	//Tier 3 materials
	@Nonnull public static final BaseMetalGroup silver =    new BaseMetalGroup(colorSilver, "silver", "Silver", VoltageTier.V1, 100_000, 1);
	@Nonnull public static final BaseMetalGroup uranium =  new BaseMetalGroup(colorUranium, "uranium", "Uranium", VoltageTier.V1, 200_000, 1);
	@Nonnull public static final MetalGroup steel =     new MetalGroup(colorSteel, "steel", "Steel", VoltageTier.V2, 150_000);
	@Nonnull public static final MetalGroup bronze =     new MetalGroup(colorBronze, "bronze", "Bronze", VoltageTier.V1, 100_000);
	@Nonnull public static final MetalGroup brass =     new MetalGroup(colorBrass, "brass", "Brass", VoltageTier.V1, 110_000);
	@Nonnull public static final GemGroup quartz = new GemGroup(colorQuartz, "quartz", "Quartz", VoltageTier.V2, 150_000);
	
	//Tier 4 materials
	@Nonnull public static final BaseMetalGroup cobalt =    new BaseMetalGroup(colorCobalt, "cobalt", "Cobalt", VoltageTier.V2, 200_000, 1);
	@Nonnull public static final BaseMetalGroup gold =     new BaseMetalGroup(colorGold, "gold", "Gold", VoltageTier.V1, 120_000, 1);
	@Nonnull public static final MetalGroup stainless = new MetalGroup(colorStainless, "stainless", "Stainless steel", VoltageTier.V2, 180_000);
	@Nonnull public static final GemGroup diamond = new GemGroup(colorDiamond, "diamond", "Diamond", VoltageTier.V2, 250_000);
	
	//Tier 5 materials
	@Nonnull public static final MetalGroup HSS =     new MetalGroup(colorHSS, "HSS", "High Speed Steel", VoltageTier.V3, 250_000);
	@Nonnull public static final BaseMetalGroup platinum =    new BaseMetalGroup(colorPlatinum, "platinum", "Platinum", VoltageTier.V2, 170_000, 1);
	
	//Tier 6 materials
	@Nonnull public static final BaseMetalGroup tungsten = new BaseMetalGroup(colorTungsten, "tungsten", "Tungsten", VoltageTier.V4, 500_000, 1);
	@Nonnull public static final BaseMetalGroup iridium =    new BaseMetalGroup(colorIridium, "iridium", "Iridium", VoltageTier.V4, 220_000, 1);
	
	//Tier 7 materials
	@Nonnull public static final MetalGroup tungstenC =     new MetalGroup(colorTungstenC, "tungstenC", "Tungsten Carbide", VoltageTier.V5, 1000_000);
	@Nonnull public static final MetalGroup crystal =     new MetalGroup(colorCrystal, "crystal", "Crystalline", VoltageTier.V5, 4000_000);
	
	//Tier 8 materials
	@Nonnull public static final MetalGroup stellar =     new MetalGroup(colorStellar, "stellar", "Stellarine", VoltageTier.V6, 8000_000);
	@Nonnull public static final MetalGroup duranium =     new MetalGroup(colorDuranium, "duranium", "Duranium", VoltageTier.V6, 16_000_000);
	
	//Tier 9 materials
	@Nonnull public static final MetalGroup unobtainium =     new MetalGroup(colorUnobtainium, "unobtainium", "Unobtainium", VoltageTier.V7, 64_000_000);
	
	@Nonnull public static final WireGroup wireRudimentary = new WireGroup(colorRudimentary,    50_000, rudimentary, "rudimentary", "Rudimentary");
	@Nonnull public static final WireGroup wireCopper      = new WireGroup(colorCopper,        100_000, copper,      "copper", "Copper");
	@Nonnull public static final WireGroup wireSilver      = new WireGroup(colorSilver,        200_000, silver,      "silver", "Silver");
	@Nonnull public static final WireGroup wireGold        = new WireGroup(colorGold,          500_000, gold,        "gold", "Gold");
	@Nonnull public static final WireGroup wirePlatinum    = new WireGroup(colorPlatinum,     1000_000, platinum,    "platinum", "Platinum");
	@Nonnull public static final WireGroup wireIridium     = new WireGroup(colorIridium,      2000_000, iridium,     "iridium", "Iridium");
	@Nonnull public static final WireGroup wireCrystal     = new WireGroup(colorCrystal,      5000_000, crystal,     "crystal", "Crystalline");
	@Nonnull public static final WireGroup wireStellar     = new WireGroup(colorStellar,     10000_000, stellar,     "stellar", "Stellarine");
	@Nonnull public static final WireGroup wireUnobtainium = new WireGroup(colorUnobtainium, 20000_000, unobtainium, "unobtainium", "Unobtainium");
	
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
		Blocks.deprecate("elec.infinite", ContentsBlocks.INFINIGEN);
		
		//Alloying recipes
		alloying(copper, 4, zinc, 1, brass, 5, VoltageTier.V2, 80_000);
		alloying(copper, 3, tin, 1, bronze, 4, VoltageTier.V2, 75_000);
		
		//Furnace fuels
		Craftings.furnaceFuels.put(Materials.coal.base, 10_000_000);
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
		//Small recipes NOT WORKING
		ItemStack nugA = new ItemStack(partA.nugget, ratioA);
		ItemStack nugB = new ItemStack(partB.nugget, ratioB);
		ItemStack mdustA = new ItemStack(partA.minidust, ratioA);
		ItemStack mdustB = new ItemStack(partB.minidust, ratioB);
		double energys = energy/16;
		RecipeOutput recs1 = new SimpleItemList(nugA, nugB);
		Craftings.alloyer.add(recs1, out.nugget, ratioO, volt, energys);
		RecipeOutput recs2 = new SimpleItemList(nugA, mdustB);
		Craftings.alloyer.add(recs2, out.nugget, ratioO, volt, energys);
		RecipeOutput recs3 = new SimpleItemList(mdustA, nugB);
		Craftings.alloyer.add(recs3, out.nugget, ratioO, volt, energys);
		RecipeOutput recs4 = new SimpleItemList(mdustA, mdustB);
		Craftings.alloyer.add(recs4, out.nugget, ratioO, volt, energys);
		
		//Medium recipes
		ItemStack ingA = new ItemStack(partA.base, ratioA);
		ItemStack ingB = new ItemStack(partB.base, ratioB);
		ItemStack dustA = new ItemStack(partA.dust, ratioA);
		ItemStack dustB = new ItemStack(partB.dust, ratioB);
		RecipeOutput recm1 = new SimpleItemList(ingA, ingB);
		Craftings.alloyer.add(recm1, out.base, ratioO, volt, energy);
		RecipeOutput recm2 = new SimpleItemList(ingA, dustB);
		Craftings.alloyer.add(recm2, out.base, ratioO, volt, energy);
		RecipeOutput recm3 = new SimpleItemList(dustA, ingB);
		Craftings.alloyer.add(recm3, out.base, ratioO, volt, energy);
		RecipeOutput recm4 = new SimpleItemList(dustA, dustB);
		Craftings.alloyer.add(recm4, out.base, ratioO, volt, energy);
		
		//Large recipes
		ItemStack blokA = new ItemStack(partA.block, ratioA);
		ItemStack blokB = new ItemStack(partB.block, ratioB);
		double energyl = energy*16;
		RecipeOutput recl = new SimpleItemList(blokA, blokB);
		Craftings.alloyer.add(recl, out.block, ratioO, volt, energyl);
	}
}
