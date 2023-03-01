/**
 * 
 */
package mmb.content.rawmats;

import java.awt.Color;
import java.util.function.Function;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.NN;
import mmb.content.ContentsBlocks;
import mmb.content.CraftingGroups;
import mmb.content.electric.VoltageTier;
import mmb.content.rawmats.MetalGroup.MaterialStack;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.craft.SimpleItemList;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.ItemStack;
import mmb.engine.item.Item;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.Items;

/**
 * A collection of base materials used for machine construction in MultiMachineBuilder
 * @author oskar
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
	
	//Fictious materials
	@NN public static final Color colorUnobtainium = new Color(150, 255, 255);
	@NN public static final Color colorCrystal = new Color(100, 255, 255);
	@NN public static final Color colorStellar = new Color(120, 255, 255);
	@NN public static final Color colorDuranium = new Color(20, 200, 255);
	@NN public static final Color colorRudimentary = new Color(255, 0, 0);
	@NN public static final Color colorDraconium = new Color(200, 20, 255);
	@NN public static final Color colorADraconium = new Color(200, 150, 60);
	@NN public static final Color colorChaotium = new Color(0, 30, 30);
	@NN public static final Color colorDiamide = new Color(20, 200, 200);
	@NN public static final Color colorUnamide = new Color(180, 150, 230);
	@NN public static final Color colorOmnamide = new Color(230, 150, 230);
	
	//Fictious alloys
	@NN public static final Color colorRudimentium = new Color(255, 60, 0);
	@NN public static final Color colorSilicopper = new Color(200, 100, 60);

	//Realistic alloys
	@NN public static final Color colorStainless = new Color(156, 199, 199);
	@NN public static final Color colorSteel = new Color(100, 100, 100);
	@NN public static final Color colorHSS = new Color(150, 150, 120);
	@NN public static final Color colorNichrome = new Color(255, 200, 220);
	@NN public static final Color colorBrass = new Color(128, 128, 0);
	@NN public static final Color colorBronze = new Color(128, 60, 0);
	@NN public static final Color colorTungstenC = new Color(30, 0, 60);
	@NN public static final Color colorSilicarbide = new Color(160, 120, 120);
	@NN public static final Color colorAlnico = new Color(160, 20, 120);
	@NN public static final Color colorNeosteel = new Color(80, 120, 100);
	
	//Realistic metals
	@NN public static final Color colorGold = new Color(255, 255, 0);
	@NN public static final Color colorAlu = new Color(128, 140, 150);
	@NN public static final Color colorZinc = new Color(128, 140, 128);
	@NN public static final Color colorTin = new Color(128, 128, 150);
	@NN public static final Color colorCopper = new Color(255, 120, 0);
	@NN public static final Color colorIron = new Color(115, 115, 115);
	@NN public static final Color colorSilicon = new Color(200, 120, 120);
	@NN public static final Color colorUranium = new Color(0, 255, 0);
	@NN public static final Color colorSilver = new Color(150, 150, 150);
	@NN public static final Color colorCobalt = new Color(73, 52, 255);
	@NN public static final Color colorLead = new Color(40, 0, 80);
	@NN public static final Color colorNickel = new Color(128, 128, 255);
	@NN public static final Color colorChrome = new Color(255, 160, 200);
	@NN public static final Color colorTungsten = new Color(60, 60, 60);
	@NN public static final Color colorPlatinum = new Color(200, 200, 216);
	@NN public static final Color colorIridium = new Color(200, 200, 255);
	@NN public static final Color colorTitanium = new Color(170, 170, 150);
	@NN public static final Color colorNeodymium = new Color(0, 120, 100);
	
	//Realistic gems
	@NN public static final Color colorDiamond = new Color(0, 255, 255);
	@NN public static final Color colorQuartz = new Color(180, 200, 255);
	@NN public static final Color colorCoal = new Color(0, 0, 0);	
	
	//Minecraft materials
	@NN public static final Color colorEnder = new Color(20, 120, 120);
	@NN public static final Color colorGlowstone = new Color(180, 180, 10);
	@NN public static final Color colorRedstone = new Color(180, 10, 10);
	
	//Thermal Series alloys
	@NN public static final Color colorLumium = new Color(255, 255, 200);
	@NN public static final Color colorSignalum = new Color(180, 60, 0);
	@NN public static final Color colorEnderium = new Color(20, 80, 80);
	
	//EnderIO alloys
	@NN public static final Color colorEnergetic = new Color(255, 180, 0);
	@NN public static final Color colorVibrant = new Color(180, 255, 0);
	@NN public static final Color colorElectrosteel = new Color(130, 120, 130);
	
	//Plastics
	@NN public static final Color colorRubber = new Color(20, 20, 20);
	@NN public static final Color colorPE = new Color(128, 128, 128);
	@NN public static final Color colorPVC = new Color(150, 150, 128);
	@NN public static final Color colorPTFE = new Color(150, 128, 150);
	
	//Tier 1 materials
	/** The most basic metal in the game */
	@NN public static final BaseMetalGroup rudimentary = new BaseMetalGroup(colorRudimentary, "rudimentary", VoltageTier.V1, 50_000, 3, false);
	/** Important energy resource and resistive material */
	@NN public static final BaseMetalGroup coal = new BaseMetalGroup(colorCoal, "coal", VoltageTier.V1, 50_000, 3, true);
	/** Early game insulation and sealing material */
	@NN public static final MetalGroup rubber =  new MetalGroup(colorRubber, "rubber", VoltageTier.V1, 20_000, false);
	
	//Tier 2 materials
	/** Cheap VLV high power cable alloy */
	@NN public static final MetalGroup rudimentium =  new MetalGroup(colorRudimentium, "rudimentium", VoltageTier.V1, 100_000, false);
	/** The basic conductive, lower power VLV cable material and used for electronic components */
	@NN public static final BaseMetalGroup copper =   new BaseMetalGroup(colorCopper, "copper", VoltageTier.V1, 100_000, 1, false);
	/** The primary VLV construction and transformer material*/
	@NN public static final BaseMetalGroup iron =     new BaseMetalGroup(colorIron, "iron", VoltageTier.V1, 120_000, 1, false);
	/** The basic semiconductor material, used in basic, enhanced, refined and advanced circuits */
	@NN public static final BaseMetalGroup silicon =  new BaseMetalGroup(colorSilicon, "silicon", VoltageTier.V1, 115_000, 1, false);
	/** Solder material, used to solder some electronic components */
	@NN public static final BaseMetalGroup tin =      new BaseMetalGroup(colorTin, "tin", VoltageTier.V1, 20_000, 1, false);
	/** Used only for brass, which is unused */
	@NN public static final BaseMetalGroup zinc =     new BaseMetalGroup(colorZinc, "zinc", VoltageTier.V1, 30_000, 1, false);
	/** A lightweight material, used in some speed upgrades */
	@NN public static final BaseMetalGroup alu =      new BaseMetalGroup(colorAlu, "alu", VoltageTier.V1, 40_000, 1, false);
	/** A heavy material, used in ULV and VLV batteries */
	@NN public static final BaseMetalGroup lead =     new BaseMetalGroup(colorLead, "lead", VoltageTier.V1, 25_000, 1, false);
	/** Enhanced resistive material, used in VLV machines and advanced components*/
	@NN public static final BaseMetalGroup nickel =   new BaseMetalGroup(colorNickel, "nickel", VoltageTier.V1, 120_000, 1, false);
	/** A basic signalling material, used in STN and Energetic Alloy*/
	@NN public static final BaseMetalGroup redstone = new BaseMetalGroup(colorRedstone, "redstone", VoltageTier.V1, 80_000, 15, true);
	
	//Tier 3 materials
	/** A highly conductive material, used in LV cables */
	@NN public static final BaseMetalGroup silver =    new BaseMetalGroup(colorSilver, "silver", VoltageTier.V1, 100_000, 1, false);
	/** A radioactive material, planned to be used in nuclear reactors */
	@NN public static final BaseMetalGroup uranium =   new BaseMetalGroup(colorUranium, "uranium", VoltageTier.V1, 200_000, 1, false);
	/** A strong metal, used in LV machines and advanced inductors */
	@NN public static final MetalGroup steel =         new MetalGroup(colorSteel, "steel", VoltageTier.V2, 150_000, false);
	/** Currently unused */
	@NN public static final MetalGroup bronze =        new MetalGroup(colorBronze, "bronze", VoltageTier.V1, 100_000, false);
	/** Currently unused */
	@NN public static final MetalGroup brass =         new MetalGroup(colorBrass, "brass", VoltageTier.V1, 110_000, false);
	/** A hard material, used in nichrome and stainless */
	@NN public static final BaseMetalGroup chrome =    new BaseMetalGroup(colorChrome, "chrome", VoltageTier.V2, 120_000, 1, false);
	/** An advanced resistive material, used in LV machines and extreme resistors */
	@NN public static final MetalGroup nichrome =      new MetalGroup(colorNichrome, "nichrome", VoltageTier.V2, 150_000, false);
	/** A  higly resonant crystal, used in ceritors */
	@NN public static final MetalGroup quartz =        new MetalGroup(colorQuartz, "quartz", VoltageTier.V2, 150_000, true);
	/** An excellent insulator, used in extreme capacitors and circuit boards */
	@NN public static final MetalGroup PE =            new MetalGroup(colorPE, "PE", VoltageTier.V2, 50_000, false);
	/** A material capable of player teleportation, used in enderium and Vibrant Alloy */
	@NN public static final BaseMetalGroup ender =     new BaseMetalGroup(colorEnder, "ender", VoltageTier.V2, 150_000, 5, true);
	/** A material which produces light, used for Energetic Alloy */
	@NN public static final BaseMetalGroup glowstone = new BaseMetalGroup(colorGlowstone, "glowstone", VoltageTier.V2, 130_000, 5, true);
	/** A high performance semiconductor, used in extreme circuits */
	@NN public static final MetalGroup silicopper =  new MetalGroup(colorSilicopper, "silicopper", VoltageTier.V2, 135_000, false);
	
	//Tier 4 materials
	/** A steel with excellent magnetic properties, used in extreme inductors and MV transformers*/
	@NN public static final MetalGroup electrosteel =  new MetalGroup(colorElectrosteel, "electrosteel", VoltageTier.V2, 100_000, false);
	/** A super hard metal, used in high speed steel and alnico */
	@NN public static final BaseMetalGroup cobalt =  new BaseMetalGroup(colorCobalt, "cobalt", VoltageTier.V2, 200_000, 1, false);
	/** Super expensive, but easy to process material used in MV machines and wires */
	@NN public static final BaseMetalGroup gold =    new BaseMetalGroup(colorGold, "gold", VoltageTier.V1, 120_000, 1, false);
	/** Super durable and long-lasting material used for MV machines and as a half of LV/MV and MV/HV transformers */
	@NN public static final MetalGroup stainless =   new MetalGroup(colorStainless, "stainless", VoltageTier.V2, 180_000, false);
	/** An advanced high-power alloy used for MV wires */
	@NN public static final MetalGroup energetic =   new MetalGroup(colorEnergetic, "energetic", VoltageTier.V2, 280_000, false);
	/** An insanely hard material, used for cutting and as an insulator */
	@NN public static final BaseMetalGroup diamond = new BaseMetalGroup(colorDiamond, "diamond", VoltageTier.V2, 250_000, 1, true);
	/** An advanced insulator, used in insane capacitors and circuit boards */
	@NN public static final MetalGroup PVC =         new MetalGroup(colorPVC, "PVC", VoltageTier.V2, 75_000, false);
	/** A super magnetic material used in HV transformers and extreme inductors*/
	@NN public static final MetalGroup alnico =   new MetalGroup(colorAlnico, "alnico", VoltageTier.V2, 150_000, false);
	
	//Tier 5 materials
	@NN public static final MetalGroup neosteel =      new MetalGroup(colorNeosteel, "neosteel", VoltageTier.V3, 280_000, false);
	@NN public static final BaseMetalGroup neodymium = new BaseMetalGroup(colorNeodymium, "neodymium", VoltageTier.V3, 270_000, 1, false);
	@NN public static final BaseMetalGroup titanium =  new BaseMetalGroup(colorTitanium, "titanium", VoltageTier.V3, 230_000, 1, false);
	@NN public static final MetalGroup HSS =           new MetalGroup(colorHSS, "HSS", VoltageTier.V3, 250_000, false);
	@NN public static final BaseMetalGroup platinum =  new BaseMetalGroup(colorPlatinum, "platinum", VoltageTier.V2, 170_000, 1, false);
	@NN public static final MetalGroup lumium =        new MetalGroup(colorLumium, "lumium", VoltageTier.V3, 400_000, false);
	@NN public static final MetalGroup vibrant =       new MetalGroup(colorVibrant, "vibrant", VoltageTier.V3, 480_000, false);
	@NN public static final MetalGroup PTFE =          new MetalGroup(colorPTFE, "PTFE", VoltageTier.V3, 100_000, false);
	@NN public static final MetalGroup silicarbide =   new MetalGroup(colorSilicarbide, "silicarbide", VoltageTier.V3, 435_000, false);
	@NN public static final MetalGroup diamide =      new MetalGroup(colorDiamide, "diamide", VoltageTier.V3, 280_000, false);
	
	//Tier 6 materials
	@NN public static final BaseMetalGroup tungsten = new BaseMetalGroup(colorTungsten, "tungsten", VoltageTier.V4, 500_000, 1, false);
	@NN public static final MetalGroup signalum = new MetalGroup(colorSignalum, "signalum", VoltageTier.V4, 600_000, false);
	@NN public static final BaseMetalGroup iridium =    new BaseMetalGroup(colorIridium, "iridium", VoltageTier.V4, 220_000, 1, false);
	@NN public static final MetalGroup unamide =      new MetalGroup(colorUnamide, "unamide", VoltageTier.V4, 480_000, false);
	
	//Tier 7 materials
	@NN public static final MetalGroup tungstenC =     new MetalGroup(colorTungstenC, "tungstenC", VoltageTier.V5, 1000_000, false);
	@NN public static final MetalGroup crystal =     new MetalGroup(colorCrystal, "crystal", VoltageTier.V5, 4000_000, false);
	@NN public static final MetalGroup enderium = new MetalGroup(colorEnderium, "enderium", VoltageTier.V5, 5000_000, false);
	@NN public static final MetalGroup omnamide =      new MetalGroup(colorOmnamide, "omnamide", VoltageTier.V5, 2000_000, false);
	
	//Tier 8 materials
	@NN public static final MetalGroup stellar =     new MetalGroup(colorStellar, "stellar", VoltageTier.V6, 8000_000, false);
	@NN public static final MetalGroup duranium =     new MetalGroup(colorDuranium, "duranium", VoltageTier.V6, 16_000_000, false);
	@NN public static final MetalGroup draconium = new MetalGroup(colorDraconium, "draconium", VoltageTier.V6, 20_000_000, false);
	
	//Tier 9 materials
	@NN public static final MetalGroup unobtainium = new MetalGroup(colorUnobtainium, "unobtainium", VoltageTier.V7, 64_000_000, false);
	@NN public static final MetalGroup adraconium = new MetalGroup(colorADraconium, "adraconium", VoltageTier.V7, 80_000_000, false);
	
	//Tier 10 materials
	@NN public static final MetalGroup chaotium = new MetalGroup(colorChaotium, "chaotium", VoltageTier.V8, 400_000_000, false);
	
	//Simple wires
	@NN public static final WireGroup wireRudimentary = new WireGroup(    1_000, rudimentary, VoltageTier.V1);
	@NN public static final WireGroup wireCopper      = new WireGroup(    4_000,      copper, VoltageTier.V2);
	@NN public static final WireGroup wireSilver      = new WireGroup(   16_000,      silver, VoltageTier.V3);
	@NN public static final WireGroup wireGold        = new WireGroup(   64_000,        gold, VoltageTier.V4);
	@NN public static final WireGroup wirePlatinum    = new WireGroup(  256_000,    platinum, VoltageTier.V5);
	@NN public static final WireGroup wireIridium     = new WireGroup( 1024_000,     iridium, VoltageTier.V6);
	@NN public static final WireGroup wireCrystal     = new WireGroup( 4096_000,     crystal, VoltageTier.V7);
	@NN public static final WireGroup wireStellar     = new WireGroup(16384_000,     stellar, VoltageTier.V8);
	@NN public static final WireGroup wireUnobtainium = new WireGroup(65536_000, unobtainium, VoltageTier.V9);
	
	//Alloyed wires
	@NN public static final WireGroup wireRudimentium = new WireGroup(      8_000, rudimentium, VoltageTier.V2);
	@NN public static final WireGroup wireEnergetic   = new WireGroup(    128_000,   energetic, VoltageTier.V4);
	@NN public static final WireGroup wireVibrant     = new WireGroup(    512_000,     vibrant, VoltageTier.V5);
	@NN public static final WireGroup wireSignalum    = new WireGroup(    512_000,    signalum, VoltageTier.V5);
	@NN public static final WireGroup wireEnderium    = new WireGroup(   2048_000,    enderium, VoltageTier.V6);
	@NN public static final WireGroup wireDraconium   = new WireGroup(   8192_000,   draconium, VoltageTier.V7);
	@NN public static final WireGroup wireADraconium  = new WireGroup( 32_768_000,  adraconium, VoltageTier.V8);
	@NN public static final WireGroup wireChaotium    = new WireGroup(131_072_000,    chaotium, VoltageTier.V9);

	static {
		//Deprecation
		Items.deprecate("mmb.iore", Materials.iron.ore);
		Items.deprecate("mmb.cpore", Materials.copper.ore);
		Items.deprecate("mmb.silicon_ore", Materials.silicon.ore);
		Items.deprecate("mmb.silverore", Materials.silver.ore);
		Items.deprecate("mmb.goldore", Materials.gold.ore);
		Items.deprecate("mmb.uraniumore", Materials.uranium.ore);
		Items.deprecate("elec.tinywire", wireRudimentary.tiny);
		Items.deprecate("elec.smallwire", wireRudimentary.small);
		Items.deprecate("elec.mediumwire", wireRudimentary.medium);
		Items.deprecate("elec.largewire", wireRudimentary.large);
		Items.deprecate("elec.infinite", ContentsBlocks.infinigens.blocks.get(0));
		Items.deprecate("elec.infinite1", ContentsBlocks.infinigens.blocks.get(0));
		
		//Alloying recipes
		alloying(copper, 4, zinc, 1, brass, 5, VoltageTier.V2, 80_000);
		alloying(copper, 3, tin, 1, bronze, 4, VoltageTier.V2, 75_000);
		alloying(nickel, 1, chrome, 1, nichrome, 2, VoltageTier.V2, 95_000);
		alloying(iron, 1, coal, 1, steel, 2, VoltageTier.V2, 125_000);
		alloying(silicon, 1, copper, 1, silicopper, 2, VoltageTier.V2, 125_000);
		alloying(signalum, 4, VoltageTier.V4,  800_000,
				copper.stack(3),
				silver.stack(1),
				redstone.stack(1));
		alloying(enderium, 2, VoltageTier.V5, 5000_000,
				tin.stack(3),
				platinum.stack(1),
				ender.stack(1));
		alloying(lumium, 3, VoltageTier.V5,   400_000,
				tin.stack(3),
				silver.stack(1),
				glowstone.stack(1));
		alloying(energetic, 1, VoltageTier.V3, 500_000,
				redstone.stack(1),
				gold.stack(1),
				glowstone.stack(1));
		alloying(alnico, 100, VoltageTier.V3, 30_000_000,
				alu.stack(12),
				nickel.stack(20),
				cobalt.stack(10),
				copper.stack(3),
				iron.stack(65));
		alloying(ender, 1, energetic, 1, vibrant, 1, VoltageTier.V4, 1000_000);
		alloying(copper, 1, rudimentary, 1, rudimentium, 2, VoltageTier.V1, 30_000);
		alloying(steel, 9, neodymium, 1, neosteel, 10, VoltageTier.V4, 1000_000);
		
		//Furnace fuels
		CraftingGroups.furnaceFuels.put(Materials.coal.base, 10_000_000);
		CraftingGroups.furnaceFuels.put(ContentsBlocks.plank, 500_000);
		CraftingGroups.furnaceFuels.put(ContentsBlocks.logs, 8000_000);
	}
	/**
	 * Creates a family of alloying recipes for the given material
	 * @param partA the material A
	 * @param ratioA amount of material A per recipe
	 * 
	 * @param partB the material B
	 * @param ratioB amount of material B per recipe
	 * 
	 * @param out the output material
	 * @param ratioO amount of material 
	 * 
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
		CraftingGroups.alloyer.add(recl, out.block, ratioO, volt, energyl);
	}
	
	private static void alloyingHelper(Item itemA1, Item itemA2, int ratioA, Item itemB1, Item itemB2, int ratioB, Item output, int ratioOut, VoltageTier volt, double energy) {
		ItemStack itemStackA1 = new ItemStack(itemA1, ratioA);
		ItemStack itemStackB1 = new ItemStack(itemB1, ratioB);
		ItemStack itemStackA2 = new ItemStack(itemA2, ratioA);
		ItemStack itemStackB2 = new ItemStack(itemB2, ratioB);
		RecipeOutput recs1 = new SimpleItemList(itemStackA1, itemStackB1);
		CraftingGroups.alloyer.add(recs1, output, ratioOut, volt, energy);
		RecipeOutput recs2 = new SimpleItemList(itemStackA1, itemStackB2);
		CraftingGroups.alloyer.add(recs2, output, ratioOut, volt, energy);
		RecipeOutput recs3 = new SimpleItemList(itemStackA2, itemStackB1);
		CraftingGroups.alloyer.add(recs3, output, ratioOut, volt, energy);
		RecipeOutput recs4 = new SimpleItemList(itemStackA2, itemStackB2);
		CraftingGroups.alloyer.add(recs4, output, ratioOut, volt, energy);
	}

	/**
	 * Alloys 3 or more metal groups
	 * @param out output material group
	 * @param ratioO amount of the output item per craft
	 * @param volt required voltage tier
	 * @param energy energy per craft for ingots
	 * @param mats input material stacks with material and quantity per craft
	 */
	public static void alloying(MetalGroup out, int ratioO, VoltageTier volt, double energy, MaterialStack... mats) {
		malloyingHelper(in -> in.nugget, mats, out, ratioO, volt, energy/16);
		malloyingHelper(in -> in.frag, mats, out, ratioO, volt, energy/4);
		malloyingHelper(in -> in.base, mats, out, ratioO, volt, energy);
		malloyingHelper(in -> in.cluster, mats, out, ratioO, volt, energy*4);
		malloyingHelper(in -> in.block, mats, out, ratioO, volt, energy*16);
	}
	private static void malloyingHelper(Function<@NN MetalGroup, @NN ItemEntry> selector, MaterialStack[] mat, MetalGroup out, int ratioO, VoltageTier volt, double energy) {
		Object2IntMap<ItemEntry> builder = new Object2IntOpenHashMap<>(mat.length);
		for(int i = 0; i < mat.length; i++) {
			builder.put(selector.apply(mat[i].material), mat[i].amount);
		}
		RecipeOutput in = new SimpleItemList(builder);
		CraftingGroups.alloyer.add(in, selector.apply(out), ratioO, volt, energy);
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
