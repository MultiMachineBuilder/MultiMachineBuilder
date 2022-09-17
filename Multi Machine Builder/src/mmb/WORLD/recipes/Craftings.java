/**
 * 
 */
package mmb.WORLD.recipes;

import static mmb.WORLD.blocks.ContentsBlocks.*;
import static mmb.WORLD.contentgen.Materials.*;
import static mmb.WORLD.items.ContentsItems.*;

import java.util.Objects;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.blocks.machine.manual.Crafting;
import mmb.WORLD.chance.ListChance;
import mmb.WORLD.chance.RandomChance;
import mmb.WORLD.chance.RandomOrElseChance;
import mmb.WORLD.contentgen.Materials;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.crafting.SimpleItemList;
import mmb.WORLD.crafting.SingleItem;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.electromachine.BlockTransformer.TransformerData;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.item.Item;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.items.electronics.Electronics;
import mmb.debug.Debugger;
import monniasza.collects.grid.FixedGrid;

/**
 * @author oskar
 * Contains crafting utilities and recipe groups
 */
public class Craftings {
	private static final Debugger debug = new Debugger("CRAFTING PROCESSOR");
	/**
	 * @param in ingredients
	 * @param out recipe output
	 * @param from source inventory
	 * @param to target inventory
	 * @return is crafting successfull?
	 */
	public static boolean transact(RecipeOutput in, RecipeOutput out, Inventory from, Inventory to) {
		//Count
		for(ItemEntry ent: in.items()) {
			int amt = in.get(ent);
			int act = from.get(ent).amount();
			if(act < amt) {
				return false;
			} //Not enough items in the source
		}
		//BULK INSERTION
		int insertions = to.bulkInsert(out, 1);
		if(insertions == 0) return false;
		//Withdraw from input
		for(ItemEntry ent: in.items()) {
			int amt = in.get(ent);
			from.extract(ent, amt);
		}
		return true;
	}

	public static boolean transact(SingleItem in, RecipeOutput out, Inventory from, Inventory to) {
		return transact(in.item(), in.amount(), out, from, to);
	}
	public static boolean transact(ItemEntry entry, int inAmount, RecipeOutput out, Inventory from, Inventory to) {
		Objects.requireNonNull(from, "from is null");
		if(from.get(entry).amount() < inAmount) return false;
		//Calculate capacity
		double volume = out.outVolume();
		double remainCapacity = to.capacity() - to.volume();
		if(volume > remainCapacity) {
			debug.printl("Required "+volume+" capacity, got "+remainCapacity);
			return false;
		} //Not enough space in the output
		//Withdraw from input
		from.extract(entry, inAmount);
		out.produceResults(to.createWriter());
		return true;
	}
	
	//Recipes
	/** The list of all furnace fuels */
	@Nonnull public static final Object2DoubleMap<ItemEntry> furnaceFuels = new Object2DoubleOpenHashMap<>();
	/** The list of all nuclear reactor fuels */
	@Nonnull public static final Object2DoubleMap<ItemEntry> nukeFuels = new Object2DoubleOpenHashMap<>();
	/** Furnace recipes */
	@Nonnull public static final SingleRecipeGroup smelting = new SingleRecipeGroup("electrofurnace");
	/** Cluster mill recipes*/
 	@Nonnull public static final SingleRecipeGroup clusterMill = new SingleRecipeGroup("clustermill");
 	/** Crusher recipes */
	@Nonnull public static final SingleRecipeGroup crusher = new SingleRecipeGroup("crusher");
	/** Wiremill recipes */
	@Nonnull public static final SingleRecipeGroup wiremill = new SingleRecipeGroup("wiremill");
	/** Splitter recipes */
	@Nonnull public static final SingleRecipeGroup splitter = new SingleRecipeGroup("spllitter");
	/** Splicer recipes */
	@Nonnull public static final StackedRecipeGroup combiner = new StackedRecipeGroup("splicer");
	/** Alloy smelter recipes */
	@Nonnull public static final ComplexRecipeGroup alloyer = new ComplexRecipeGroup("alloyer", 2);
	/** Machine assembler recipes */
	@Nonnull public static final ComplexCatalyzedRecipeGroup assembler = new ComplexCatalyzedRecipeGroup("assembler", 2);
	/** Brewery recipes */
	@Nonnull public static final ComplexRecipeGroup brewery = new ComplexRecipeGroup("brewery", 2);
	/** Crafting recipes */
	@Nonnull public static final CraftingRecipeGroup crafting = new CraftingRecipeGroup("crafter");
	/** Quarry recipes */
	@Nonnull public static final SingleRecipeGroup quarry = new SingleRecipeGroup("quarry");
	/** Extruder recipes */
	@Nonnull public static final CatalyzedSingleRecipeGroup extruder = new CatalyzedSingleRecipeGroup("extruder");
	/** Crop outputs */
	@Nonnull public static final AgroRecipeGroup agro = new AgroRecipeGroup("agrorecipes");
	/** Alcoholic beverages */
	@Nonnull public static final AlcoholInfoGroup alcohol = new AlcoholInfoGroup("alcohol");
	//TODO replace with raw items
	/** Pickaxe recipes */
	@Nonnull public static final PickaxeGroup pickaxes = new PickaxeGroup("pickaxe");
	/**
	 * Crafts items according to a recipe
	 * @param input items to be consumed
	 * @param rout items to be crafted
	 * @param tgt target inventory
	 * @param src source inventory
	 * @param amount number of recipes to craft
	 * @return number of recipes crafted
	 */
	public static int transact(RecipeOutput input, RecipeOutput rout, Inventory tgt, Inventory src, int amount) {
		if(!tgt.exists()) return 0;
		if(!tgt.canInsert()) return 0;
		if(!src.exists()) return 0;
		if(!src.canExtract()) return 0;
		double volume = rout.outVolume(); //Per unit
		int fitsInOut = (int) (tgt.iremainVolume()/volume);
		int ingrsInIn = Inventory.howManyTimesThisContainsThat(tgt, input);
		int craftable = Math.min(fitsInOut, ingrsInIn);
		//Remove ingredients from input
		for(Entry<ItemEntry> irecord: input.getContents().object2IntEntrySet()) {
			int amt = irecord.getIntValue()*craftable;
			src.extract(irecord.getKey(), amt);
		}
		//Insert output
		rout.produceResults(tgt.createWriter(), craftable);
		return craftable;
	}
	
	private static boolean inited = false;
	
	//Everything recipe related
	/** Creates all crafting recipes */
	public static void createRecipes() {
		if(inited) return;
		inited = true;
		//Crafting ingredients
		crafting.addRecipe(logs, plank, 16); // wood → planks
		splitter.add(logs, plank, 16, VoltageTier.V1, 1000);
		crafting.addRecipeGrid(plank, 2, 2, Crafting.types.get(0), 1); //4*wood plank → crafting table 1
		_chest();
		clusterMill.add(plank, paper, 16, VoltageTier.V1, 1000);
		smelting.add(sand, glass, VoltageTier.V1, 70_000);
		clusterMill.add(glass, glassp, VoltageTier.V1, 70_000);
		crusher.add(stone, gravel, VoltageTier.V1, 15_000);
		crusher.add(gravel, sand, VoltageTier.V1, 15_000);
		
		//Pickaxes
		_tools();
		
		//Pipes
		crafting.addRecipe(new FixedGrid<>(3,
		Materials.iron.base,  null, Materials.iron.base,
		Materials.iron.base,  null, Materials.iron.base,
		Materials.iron.base,  null, Materials.iron.base), ContentsBlocks.ipipe_STRAIGHT, 24);
		crafting.addRecipe(new FixedGrid<>(3,
		Materials.iron.base, Materials.iron.base,  Materials.iron.base,
		Materials.iron.base,  null, null,
		Materials.iron.base,  null, Materials.iron.base), ContentsBlocks.ipipe_ELBOW, 24);
		crafting.addRecipe(new FixedGrid<>(3,
		ContentsBlocks.ipipe_ELBOW, null,       null,
		null,  Materials.iron.nugget, null,
		null,  null,       ContentsBlocks.ipipe_ELBOW), ContentsBlocks.ipipe_DUALTURN, 1);
		crafting.addRecipe(new FixedGrid<>(3,
		ContentsBlocks.ipipe_STRAIGHT, null,       null,
		null,  Materials.iron.nugget, null,
		null,  null,       ContentsBlocks.ipipe_STRAIGHT), ContentsBlocks.ipipe_CROSS, 1);
		crafting.addRecipeGrid(new ItemEntry[]{
			ContentsBlocks.ipipe_ELBOW, Materials.iron.nugget, ContentsBlocks.ipipe_STRAIGHT},
		3, 1, ContentsBlocks.ipipe_TOLEFT, 1);
		crafting.addRecipeGrid(new ItemEntry[]{
			ContentsBlocks.ipipe_STRAIGHT, Materials.iron.nugget, ContentsBlocks.ipipe_ELBOW},
		3, 1, ContentsBlocks.ipipe_TORIGHT, 1);
		
		_wireworld();
		
		//Machine parts
		crafting.addRecipeGrid(new ItemEntry[]{
		Materials.steel.base, Materials.iron.base, Materials.steel.base,
		Materials.iron.base,  null, Materials.iron.base,
		Materials.steel.base, Materials.iron.base, Materials.steel.base
		}, 3, 3, frame1);
		crafting.addRecipeGrid(new ItemEntry[]{
		null,  null,  Materials.steel.base,
		null,  Materials.steel.base, null,
		Materials.steel.base, null,  null
		}, 3, 3, rod1);
		crafting.addRecipeGrid(new ItemEntry[]{
		Materials.steel.nugget,  Materials.steel.nugget,  Materials.steel.nugget,
		Materials.steel.nugget,  null,         Materials.steel.nugget,
		Materials.steel.nugget,  Materials.steel.nugget,  Materials.steel.nugget,
		}, 3, 3, bearing1);
		crafting.addRecipeGrid(new ItemEntry[]{
		wireRudimentary.tiny,  rudimentary.base,  wireRudimentary.tiny,
		wireRudimentary.tiny,  rudimentary.base,  wireRudimentary.tiny,
		wireRudimentary.tiny,  rudimentary.base,  wireRudimentary.tiny,
		}, 3, 3, motor1);
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.nugget,      rudimentary.base,  rudimentary.nugget,
		wireRudimentary.medium,  rudimentary.base,  wireRudimentary.medium,
		rudimentary.nugget,      rudimentary.base,  rudimentary.nugget
		}, 3, 3, motor1);
		
		
		assembler.add(new SimpleItemList(
			Materials.steel.base.stack(2),
			copper.wire.stack(32)
			), motor2, null, VoltageTier.V1, 50000);
		assembler.add(new SimpleItemList(
			rudimentary.base.stack(1),
			rudimentary.wire.stack(16)
			), motor1, null, VoltageTier.V1, 100000);
		
		//Furnace
		crafting.addRecipeGrid(new ItemEntry[]{
		stone, stone, stone,
		stone, null,  stone,
		stone, stone, stone,
		}, 3, 3, FURNACE);
		
		smelting.add(rudimentary.base, wireRudimentary.medium, VoltageTier.V1, 80_000);
		smelting.add(ContentsBlocks.logs, Materials.coal.base, 1, VoltageTier.V1, 50_000);
		
		_electronics();
		_craftrsULV(); //Electric machines - ULV
		_craftrsVLV();
		_craftrsLV();
		_craftrsMV();
		_craftrsHV();
		quarry();
		
		//Agriculture
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
		
		//Alcohol
		crafting.addRecipeGrid(new ItemEntry[]{
		glassp, glassp, glassp,
		glassp,  paper, glassp,
		glassp, glassp, glassp
		}, 3, 3, beerEmpty, 16); //Beer bottle
		
		//Brewery recipe for beer
		brewery.add(new SimpleItemList(
		beerEmpty.stack(16),
		seeds.stack(8),
		water.stack(1),
		hops.stack(1),
		yeast.stack(3)
		), new SimpleItemList(beer.stack(16), yeast.stack(4)), VoltageTier.V1, 1000000);
	}

	private static void _wireworld() {
		//Actuator blocks
		//Block Rotator
		crafting.addRecipeGrid(new ItemEntry[]{rod1, bearing1, frame1}, 1, 3, ROTATOR, 1);
		
		//WireWorld cell
		crafting.addRecipeGrid(new ItemEntry[]{Materials.silicon.base, Materials.copper.base}, 1, 2, ww_wire, 24);
		
		//WireWorld gates - 2 inputs
		crafting.addRecipe(new FixedGrid<ItemEntry>(3, 2,
		null, YES, null,
		YES, ww_wire, YES), OR, 4); //OR
		crafting.addRecipe(new FixedGrid<ItemEntry>(3, 2,
		null, NOT, null,
		YES, ww_wire, YES), XOR, 4); //XOR
		crafting.addRecipe(new FixedGrid<ItemEntry>(3, 2,
		null, NOT, null,
		NOT, ww_wire, NOT), AND, 4); //AND
		
		//WireWorld gates - 1 input
		crafting.addRecipeGrid(ww_wire, 1, 2, YES); //YES
		crafting.addRecipeGrid(new ItemEntry[]{ww_wire, YES}, 1, 2, NOT); //NOT
		
		//WireWorld signallers
		crafting.addRecipeGrid(new ItemEntry[]{
		Materials.silicon.base , ww_wire, Materials.silicon.base,
		ww_wire, ww_wire, ww_wire,
		Materials.silicon.base, ww_wire, Materials.silicon.base
		}, 3, 3, TRUE, 4); //Always true
		crafting.addRecipeGrid(new ItemEntry[]{
		null,  null,  Materials.steel.base,
		null,  Materials.steel.base, null,
		Materials.steel.base, null,  null
		}, 3, 3, RANDOM, 4); //Random
		crafting.addRecipeGrid(new ItemEntry[]{
		Materials.silicon.base, Materials.silicon.base, Materials.silicon.base,
		Materials.silicon.base, ww_wire, Materials.silicon.base,
		Materials.silicon.base, Materials.silicon.base, Materials.silicon.base
		}, 3, 3, URANDOM, 4); //Uniform random
		
		//Speaker
		crafting.addRecipeGrid(new ItemEntry[]{
			inductor, Materials.iron.frag, paper
		}, 3, 1, SPEAKER);
	}

	private static void _tools() {
		crafting.addRecipeGrid(new ItemEntry[]{plank, plank, logs, logs}, 2, 2, PICKBUILDER);
		pickaxes.add(pickHeadWood, pickWood);
		pickaxes.add(pickHeadRudimentary, pickRudimentary);
		
		crafting.addRecipe(new FixedGrid<>(3,
		plank, logs,  plank,
		null,  plank, null,
		null,  plank, null), pickHeadWood, 1);
		crafting.addRecipe(new FixedGrid<>(3,
		Materials.rudimentary.nugget, Materials.rudimentary.base,   Materials.rudimentary.nugget,
		null,                         Materials.rudimentary.nugget, null,
		null,                         Materials.rudimentary.nugget, null), pickHeadRudimentary, 1);
		crafting.addRecipeGrid(new ItemEntry[]{
		stone, null, stone,
		stone, null, stone,
		null,  stone, null
		}, 3, 3, bucket); //Item bucket
		crafting.addRecipeGrid(new ItemEntry[]{
		null,  stone, null,
		stone, null, stone,
		null,  stone, null
		}, 3, 3, aim); //Aimer
		
	}
	private static void _electronics() {
		//Electronic parts (basic and enhanced IC)
		assembler.add(new SimpleItemList(
			copper.wire,
			coal.nugget),
			resistor,   null, 8, VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(
			copper.wire,
			paper),
			capacitor,  null, 8, VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(
			copper.wire.stack(2),
			iron.nugget),
			inductor,   null, 8, VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(
			copper.wire,
			rudimentary.nugget,
			silicon.nugget),
			diode,      null, 8, VoltageTier.V1,  20000);
		assembler.add(new SimpleItemList(
			copper.wire, copper.nugget, silicon.nugget),
			transistor, null, 8, VoltageTier.V1,  40000);
		assembler.add(new SimpleItemList(
			copper.wire.stack(4),
			rudimentium.nugget.stack(2),
			silicon.foil,
			silicon.nugget),
			IC, null, 16, VoltageTier.V1,  80000);
		assembler.add(new SimpleItemList(
			resistor.stack(16),
			nickel.wire.stack(8)
		), resistors, null, VoltageTier.V1, 100000);
		
		//Enhanced
		assembler.add(new SimpleItemList(
			silver.wire,
			silver.nugget,
			silicon.sheet,
			silicon.nugget),
		Electronics.ic0, null, 2, VoltageTier.V2,  80000);
		
		//Advanced
		assembler.add(new SimpleItemList(
			silver.wire,
			nickel.wire),
		Electronics.resistor1, null, 16, VoltageTier.V2,  20000);
		assembler.add(new SimpleItemList(
			silver.wire,
			rubber.foil),
		Electronics.capacitor1, null, 16, VoltageTier.V2,  40000);
		assembler.add(new SimpleItemList(
			silver.wire.stack(2),
			steel.nugget),
		Electronics.inductor1, null, 16, VoltageTier.V2,  80000);
		assembler.add(new SimpleItemList(
			silver.wire,
			rudimentary.nugget,
			silicon.nugget),
		Electronics.diode1, null, 16, VoltageTier.V2, 160000);
		assembler.add(new SimpleItemList(
			silver.wire,
			copper.nugget,
			silicon.nugget),
		Electronics.transistor1, null, 16, VoltageTier.V2, 320000);
		assembler.add(new SimpleItemList(
			gold.wire,
			gold.nugget,
			silicon.frag,
			silicon.sheet.stack(2)),
		Electronics.ic1, null, 2, VoltageTier.V3, 640000);
		
		//Substrates
		assembler.add(new SimpleItemList(
				paper.stack(1),                         rudimentary.foil.stack(2)),
				substrate0, null, 8, VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(
				paper.stack(1),                              copper.foil.stack(2)),
				substrate1, null,    VoltageTier.V1,  10000);
		
		
		
		//Primitive Circuit
		crafting.addRecipeGrid(new ItemEntry[]{
			null,                  coal.base,    null,
			wireRudimentary.tiny,  paper,        wireRudimentary.tiny,
			null,                  silicon.frag, null
		}, 3, 3, circuit0);
		assembler.add(new SimpleItemList(
			substrate0.stack(1),
			resistor.stack(2),
			capacitor.stack(2),
			inductor.stack(2)
		), circuit0.stack(8), null, VoltageTier.V1,  10000);
		//Basic Circuit
		assembler.add(new SimpleItemList(
			substrate1.stack(1),
			resistor.stack(4),
			capacitor.stack(4),
			inductor.stack(4),
			diode.stack(2),
			circuit0.stack(2)
		), circuit1, null, VoltageTier.V1, 100000);
		assembler.add(new SimpleItemList(
			substrate1.stack(1),
			IC.stack(1)
		), circuit1.stack(8), null, VoltageTier.V2, 100000);
		//Enhanced Circuit
		assembler.add(new SimpleItemList(
			resistor.stack(16),
			capacitor.stack(16),
			inductor.stack(16),
			diode.stack(8),
			transistor.stack(4),
			substrate2.stack(1),
			circuit1.stack(2)
		), circuit2, null, VoltageTier.V2, 400000);
		assembler.add(new SimpleItemList(
			Electronics.capacitor1.stack(2),
			Electronics.inductor1.stack(2),
			Electronics.resistor1.stack(2),
			Electronics.ic0.stack(1),
			substrate2.stack(1)
		), circuit2.stack(8), null, VoltageTier.V3, 400000);
		//Refined Circuit
		assembler.add(new SimpleItemList(
			Electronics.capacitor1.stack(8),
			Electronics.inductor1.stack(8),
			Electronics.resistor1.stack(8),
			Electronics.diode1.stack(4),
			Electronics.transistor1.stack(2),
			Electronics.ic0.stack(1),
			circuit2.stack(2),
			substrate3.stack(2)
		), circuit3, null, VoltageTier.V3, 1600000);
		assembler.add(new SimpleItemList(
			Electronics.transistor2.stack(16),
			Electronics.diode1.stack(16),
			Electronics.ic1.stack(1),
			substrate3.stack(2)
		), circuit3.stack(8), null, VoltageTier.V4, 1600000);
	}
	private static void _chest() {
		//Chests
		crafting.addRecipeGrid(new ItemEntry[]{
			plank, plank, plank,
			plank, null,  plank,
			plank, plank, plank
		}, 3, 3, CHEST);
		crafting.addRecipeGrid(new ItemEntry[]{
			iron.base,  steel.base, iron.base,
			steel.base, null,       steel.base,
			iron.base,  steel.base, iron.base
		}, 3, 3, CHEST1);
		crafting.addRecipeGrid(new ItemEntry[]{
			stainless.base,  steel.base, stainless.base,
			steel.base,      null,       steel.base,
			stainless.base,  steel.base, stainless.base
		}, 3, 3, CHEST2);
		crafting.addRecipeGrid(new ItemEntry[]{
			stainless.base,  HSS.base, stainless.base,
			HSS.base,        null,     HSS.base,
			stainless.base,  HSS.base, stainless.base
		}, 3, 3, CHEST3);
		crafting.addRecipeGrid(new ItemEntry[]{
			signalum.base,  HSS.base, signalum.base,
			HSS.base,       null,     HSS.base,
			signalum.base,  HSS.base, signalum.base
		}, 3, 3, CHEST4);
		crafting.addRecipeGrid(new ItemEntry[]{
			signalum.base,  enderium.base, signalum.base,
			enderium.base,  null,          enderium.base,
			signalum.base,  enderium.base, signalum.base
		}, 3, 3, CHEST5);
		crafting.addRecipeGrid(new ItemEntry[]{
			draconium.base, enderium.base, draconium.base,
			enderium.base,  null,          enderium.base,
			draconium.base, enderium.base, draconium.base
		}, 3, 3, CHEST6);
	}
	private static void _craftrsULV() {
		//Coal Generator ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		wireRudimentary.medium, wireRudimentary.medium, wireRudimentary.medium,
		wireRudimentary.medium, FURNACE,                wireRudimentary.medium,
		wireRudimentary.medium, wireRudimentary.medium, wireRudimentary.medium,
		}, 3, 3, COALGEN1);
		
		//Electric furnace ULV
		ItemEntry efurnace0 = efurnace.get(0);
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.base,       coal.base, rudimentary.base,
		wireRudimentary.medium, FURNACE,   wireRudimentary.medium,
		rudimentary.base,       coal.base, rudimentary.base,
		}, 3, 3, efurnace0); 
		
		//Crusher ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		stone,                  wireRudimentary.medium, stone,
		wireRudimentary.medium, FURNACE,                wireRudimentary.medium,
		stone,                  wireRudimentary.medium, stone,
		}, 3, 3, bcrusher.get(0)); 
		
		//Alloyer ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		stone,     wireRudimentary.medium, stone,
		efurnace0, FURNACE,                efurnace0,
		stone,     wireRudimentary.medium, stone,
		}, 3, 3, balloyer.get(0)); 
		
		//Cluster Mill ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor1,                  wireRudimentary.medium, motor1,
		wireRudimentary.medium,  rudimentary.gear,       wireRudimentary.medium,
		motor1,                  wireRudimentary.medium, motor1,
		}, 3, 3, bcmill.get(0)); 
		
		//Wiremill ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor1,    motor1,                 motor1,
		rudimentary.gear, rudimentary.panel,     rudimentary.gear,
		motor1,    wireRudimentary.medium, motor1,
		}, 3, 3, bwiremill.get(0)); 
		
		Item splitter = bsplitter.get(0);
		Item splicer = bsplicer.get(0);
		//Material Splitter ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor1,    rudimentary.gear,       motor1,
		coal.wire, coal.wire,              coal.wire,
		motor1,    wireRudimentary.medium, motor1,
		}, 3, 3, splitter); 
		
		//Material Combiner ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor1,    rudimentary.gear,       motor1,
		coal.wire, coal.nugget,              coal.wire,
		motor1,    wireRudimentary.medium, motor1,
		}, 3, 3, splicer); 
		
		//Quarry ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor1,    iron.gear, motor1,
		iron.wire, stone, iron.wire,
		motor1,    iron.gear, motor1,
		}, 3, 3, bquarry.get(0)); 
		
		crafting.addRecipeGrid(new ItemEntry[]{
		splitter,         rudimentary.panel, splicer,
		rudimentary.wire, circuit0,          rudimentary.wire,
		splicer,          rudimentary.panel, splitter,
		}, 3, 3, bassembly.get(0)); //Machine Assembler ULV
		
		crafting.addRecipeGrid(new ItemEntry[]{
		iron.panel, glassp,     iron.panel,
		iron.panel, iron.panel, iron.panel,
		coal.base,  seeds,      wireRudimentary.medium,
		}, 3, 3, bbrewery.get(0)); //Brewery ULV
		
		crafting.addRecipeGrid(new ItemEntry[]{
		null,       motor1,     iron.base,
		iron.panel, iron.panel, iron.panel,
		iron.panel, iron.panel, iron.panel,
		}, 3, 3, bdig.get(0));//Digger ULV
		
		//Battery ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.base, glass, rudimentary.base,
		lead.panel,       paper, lead.panel,
		lead.panel,       paper, lead.panel,
		}, 3, 3, bbattery.get(0));
		
		//Extruder ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor1,            rudimentary.gear,  rudimentary.panel,
		rudimentary.panel, rudimentary.panel, rudimentary.panel,
		rudimentary.panel, rudimentary.panel, rudimentary.panel,
		}, 3, 3, bextruder.get(0));
		
		//Power Receiver ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.wire, rudimentary.wire,  rudimentary.wire,
		rudimentary.wire, rudimentary.base, rudimentary.wire,
		rudimentary.wire, rudimentary.wire, rudimentary.wire,
		}, 3, 3, prec.get(0));
		
		//Power Tower ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		wireRudimentary.medium, wireRudimentary.medium,  wireRudimentary.medium,
		wireRudimentary.medium, prec.get(0),             wireRudimentary.medium,
		wireRudimentary.medium, wireRudimentary.medium,  wireRudimentary.medium,
		}, 3, 3, ptower.get(0));
	}
	private static void _craftrsVLV() {
		//Coal Generator VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		iron.cluster, circuit1, iron.cluster,
		FURNACE,      FURNACE,  FURNACE,
		iron.cluster, circuit1, iron.cluster,
		}, 3, 3, COALGEN2);
		
		//Transformer VLV/ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		null,              iron.base, wireRudimentary.medium,
		wireCopper.medium, iron.base, wireRudimentary.medium,
		null,              iron.base, wireRudimentary.medium,
		}, 3, 3, TransformerData.VLV.type); 
		
		ItemEntry efurnace1 = efurnace.get(1);
		crafting.addRecipeGrid(new ItemEntry[]{
		nickel.base, resistors, iron.base,
		nickel.wire, FURNACE,   nickel.wire,
		iron.base,   resistors, nickel.base,
		}, 3, 3, efurnace1); //Electric Furnace VLV
		
		crafting.addRecipeGrid(new ItemEntry[]{
		nickel.base, circuit1,  iron.base,
		efurnace1,   efurnace1, efurnace1,
		iron.base,   resistors, nickel.base,
		}, 3, 3, balloyer.get(1)); //Alloyer VLV
		
		crafting.addRecipeGrid(new ItemEntry[]{
		copper.base, circuit1,  iron.base,
		stone,   wireCopper.medium, stone,
		iron.base,   resistors, copper.base,
		}, 3, 3, bcrusher.get(1)); //Crusher VLV
		
		crafting.addRecipeGrid(new ItemEntry[]{
		motor2,    circuit1,   motor2,
		iron.gear, iron.panel, iron.gear,
		motor2,    circuit1,   motor2,
		}, 3, 3, bcmill.get(1));//Cluster Mill VLV
		
		crafting.addRecipeGrid(new ItemEntry[]{
		motor2,      circuit1,   bearing1,
		iron.gear,   iron.panel, iron.gear,
		bearing1, circuit1,   motor2,
		}, 3, 3, bwiremill.get(1));//WireMill VLV
		
		crafting.addRecipeGrid(new ItemEntry[]{
		motor2,    steel.gear, motor2,
		steel.wire, stone, steel.wire,
		motor2,    steel.gear, motor2,
		}, 3, 3, bquarry.get(1)); //Quarry VLV
		
		crafting.addRecipeGrid(new ItemEntry[]{
		bsplitter.get(1), circuit1,               bcmill.get(1),
		steel.ring,       circuit1,               steel.ring,
		bsplicer.get(1),  wireRudimentium.medium, balloyer.get(1),
		}, 3, 3, bassembly.get(1)); //Machine Assembler VLV
		
		crafting.addRecipeGrid(new ItemEntry[]{
		null,        motor2,     steel.base,
		steel.panel, circuit1,    steel.panel,
		steel.panel, steel.panel, steel.panel,
		}, 3, 3, bdig.get(1));//Digger VLV
		
		crafting.addRecipeGrid(new ItemEntry[]{
		motor2,     steel.gear, motor2,
		steel.wire, steel.wire, steel.wire,
		motor2,     circuit1,   motor2,
		}, 3, 3, bsplitter.get(1)); //Splitter VLV
		
		//Material Combiner ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor2,    steel.gear,    motor2,
		steel.wire, steel.nugget, steel.wire,
		motor2,    circuit2,      motor2,
		}, 3, 3, bsplicer.get(1)); //Splicer VLV
		
		//Battery VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentium.base, glass, rudimentium.base,
		nickel.panel,     paper, nickel.panel,
		nickel.panel,     paper, nickel.panel,
		}, 3, 3, bbattery.get(1));
		
		//Extruder ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor2,      steel.gear,  steel.panel,
		steel.panel, steel.panel, steel.panel,
		steel.panel, circuit1, steel.panel,
		}, 3, 3, bextruder.get(1));
		
		//Power Receiver VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		copper.wire, copper.wire, copper.wire,
		copper.wire, iron.base,   copper.wire,
		copper.wire, copper.wire, copper.wire,
		}, 3, 3, prec.get(1));
		
		//Power Tower VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		wireCopper.medium, wireCopper.medium, wireCopper.medium,
		wireCopper.medium, prec.get(1),       wireCopper.medium,
		wireCopper.medium, wireCopper.medium, wireCopper.medium,
		}, 3, 3, ptower.get(1));
	}
	private static void _craftrsLV() {
		//Coal Generator LV
		crafting.addRecipeGrid(new ItemEntry[]{
		steel.cluster, circuit2, steel.cluster,
		motor2,        FURNACE,  motor2,
		steel.cluster, circuit2, steel.cluster,
		}, 3, 3, COALGEN3);
		
		//Transformer LV/VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		null,              steel.base, wireCopper.medium,
		wireSilver.medium, steel.base, wireCopper.medium,
		null,              steel.base, wireCopper.medium,
		}, 3, 3, TransformerData.LV.type); 
		
		ItemEntry efurnace1 = efurnace.get(1);
		crafting.addRecipeGrid(new ItemEntry[]{
		nichrome.base, resistors,  steel.base,
		nickel.wire,  steel.frame, nickel.wire,
		steel.base,   resistors,   nichrome.base,
		}, 3, 3, efurnace1); //Electric Furnace LV
		
		//Battery LV
		crafting.addRecipeGrid(new ItemEntry[]{
		silver.base,  glass,   silver.base,
		nickel.frame, PE.foil, lead.panel,
		lead.panel,   PE.foil, nickel.frame,
		}, 3, 3, bbattery.get(0));
	}
	private static void _craftrsMV() {
		//Transformer MV/LV
		crafting.addRecipeGrid(new ItemEntry[]{
		null,            stainless.base, electrosteel.base, wireSilver.medium,
		wireGold.medium, stainless.base, electrosteel.base, wireSilver.medium,
		wireGold.medium, stainless.base, electrosteel.base, wireSilver.medium,
		null,            stainless.base, electrosteel.base, wireSilver.medium,
		}, 4, 4, TransformerData.MV.type);
	}
	private static void _craftrsHV() {
		
	}
	
	private static void quarry() {
		//Tier 1: rudimentary, coal, copper, iron, silicon, nickel, tin, zinc, aluminum, lead, redstone
		quarry.add(stone, RecipeOutput.NONE, VoltageTier.V1, 20000, new ListChance(
			new RandomChance(0.2, rudimentary.ore),
			new RandomChance(0.2, coal.ore),
			new RandomChance(0.04, copper.ore),
			new RandomChance(0.05, iron.ore),
			new RandomChance(0.05, silicon.ore),
			new RandomChance(0.05, nickel.ore),
			new RandomChance(0.05, tin.ore),
			new RandomChance(0.05, zinc.ore),
			new RandomChance(0.05, alu.ore),
			new RandomChance(0.05, lead.ore),
			new RandomChance(0.05, redstone.cluster)
		));
		
		//Tier 2: +silver, uranium, chrome, quartz, ender, glowstone
		quarry.add(resrc1, stone, 2, VoltageTier.V2, 80000, new ListChance(
			new RandomChance(0.1, chrome.ore),
			new RandomChance(0.1, uranium.ore),
			new RandomChance(0.06, silver.ore),
			new RandomChance(0.1, quartz.base),
			new RandomChance(0.08, ender.base),
			new RandomChance(0.1, glowstone.base)
		));
		
		//Tier 3: +cobalt, gold, diamond
		quarry.add(resrc2, resrc1, 2, VoltageTier.V3, 320000, new ListChance(
			new RandomChance(0.12, cobalt.ore),
			new RandomChance(0.1, gold.ore),
			new RandomChance(0.05, diamond.ore)
		));
		
		//Tier 4: +platinum
		quarry.add(resrc3, resrc2, 2, VoltageTier.V4, 1280000, new RandomChance(0.1, platinum.base));
		
		//Tier 5: +tungsten, iridium
		quarry.add(resrc4, resrc3, 2, VoltageTier.V5, 5120000, new ListChance(
			new RandomChance(0.1, tungsten.ore),
			new RandomChance(0.1, iridium.ore)
		));
		
		alloyer.add(new SimpleItemList(redstone.base.stack(2), rudimentary.base.stack(2)), resrc1, 2, VoltageTier.V2, 180000);
		//simple stone regeneration recipe
		alloyer.add(new SimpleItemList(stone, rudimentary.nugget), stone, 8, VoltageTier.V1, 80000);
		
		//sifting seeds sometimes gives yeast
		quarry.add(seeds, RecipeOutput.NONE, VoltageTier.V1, 40000, new RandomOrElseChance(0.05, yeast, seeds));
	}
}