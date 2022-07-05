/**
 * 
 */
package mmb.WORLD.crafting;

import static mmb.WORLD.blocks.ContentsBlocks.*;
import static mmb.WORLD.contentgen.Materials.*;
import static mmb.WORLD.items.ContentsItems.*;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.Bag;
import org.joml.Vector3d;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.blocks.machine.manual.Crafting;
import mmb.WORLD.chance.ListChance;
import mmb.WORLD.chance.RandomChance;
import mmb.WORLD.chance.RandomOrElseChance;
import mmb.WORLD.contentgen.Materials;
import mmb.WORLD.crafting.recipes.AgroRecipeGroup;
import mmb.WORLD.crafting.recipes.ComplexCatalyzedProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.ComplexProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.CraftingRecipeGroup;
import mmb.WORLD.crafting.recipes.ElectroLuckySimpleProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.ElectroSimpleProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.StackedProcessingRecipeGroup;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.electromachine.Transformer.TransformerData;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.item.Item;
import mmb.WORLD.items.ItemEntry;
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
	 */
	public static void transact(Bag<@Nonnull ItemEntry> in, RecipeOutput out, Inventory from, Inventory to) {
		//Count
		for(ItemEntry ent: in.uniqueSet()) {
			int amt = in.getCount(ent);
			int act = from.get(ent).amount();
			if(act < amt) {
				//debug.printl("Not enough "+ent+", got "+act+", expected "+amt);
				return;
			} //Not enough items in the source
		}
		//Calculate capacity
		Vector3d calcvec = new Vector3d();
		out.outVolume();
		double remainCapacity = to.capacity() - to.volume();
		if(calcvec.x > remainCapacity) {
			//debug.printl("Required "+calcvec.x+" capacity, got "+remainCapacity);
			return;
		} //Not enough space in the output
		//Withdraw from input
		for(ItemEntry ent: in.uniqueSet()) {
			int amt = in.getCount(ent);
			from.extract(ent, amt);
		}
		out.produceResults(to.createWriter());
	}
	public static void transact(ItemEntry in, RecipeOutput out, Inventory from, Inventory to) {
		transact(in, 1, out, from, to);
	}
	public static void transact(ItemStack in, RecipeOutput out, Inventory from, Inventory to) {
		transact(in.item, in.amount, out, from, to);
	}
	public static void transact(ItemEntry entry, int inAmount, RecipeOutput out, Inventory from, Inventory to) {
		Objects.requireNonNull(from, "from is null");
		if(from.get(entry).amount() < inAmount) return;
		//Calculate capacity
		double volume = out.outVolume();
		double remainCapacity = to.capacity() - to.volume();
		if(volume > remainCapacity) {
			debug.printl("Required "+volume+" capacity, got "+remainCapacity);
			return;
		} //Not enough space in the output
		//Withdraw from input
		from.extract(entry, inAmount);
		out.produceResults(to.createWriter());
	}
	
	//Recipes
	/** The list of all furnace fuels */
	@Nonnull public static final Object2DoubleMap<ItemEntry> furnaceFuels = new Object2DoubleOpenHashMap<>();
	/** The list of all nuclear reactor fuels */
	@Nonnull public static final Object2DoubleMap<ItemEntry> nukeFuels = new Object2DoubleOpenHashMap<>();
	@Nonnull public static final ElectroSimpleProcessingRecipeGroup smelting = new ElectroSimpleProcessingRecipeGroup("electrofurnace");
	@Nonnull public static final ElectroSimpleProcessingRecipeGroup clusterMill = new ElectroSimpleProcessingRecipeGroup("clustermill");
	@Nonnull public static final ElectroSimpleProcessingRecipeGroup crusher = new ElectroSimpleProcessingRecipeGroup("crusher");
	@Nonnull public static final ElectroSimpleProcessingRecipeGroup wiremill = new ElectroSimpleProcessingRecipeGroup("wiremill");
	@Nonnull public static final ElectroSimpleProcessingRecipeGroup splitter = new ElectroSimpleProcessingRecipeGroup("spllitter");
	@Nonnull public static final StackedProcessingRecipeGroup combiner = new StackedProcessingRecipeGroup("splicer");
	@Nonnull public static final ComplexProcessingRecipeGroup alloyer = new ComplexProcessingRecipeGroup("alloyer", 2);
	@Nonnull public static final ComplexCatalyzedProcessingRecipeGroup assembler = new ComplexCatalyzedProcessingRecipeGroup("assembler", 2);
	@Nonnull public static final ComplexProcessingRecipeGroup brewery = new ComplexProcessingRecipeGroup("brewery", 2);
	@Nonnull public static final CraftingRecipeGroup crafting = new CraftingRecipeGroup("crafter");
	@Nonnull public static final ElectroLuckySimpleProcessingRecipeGroup quarry = new ElectroLuckySimpleProcessingRecipeGroup("quarry");
	@Nonnull public static final AgroRecipeGroup agro = new AgroRecipeGroup("agrorecipes");
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
		crafting.addRecipeGrid(new ItemEntry[]{plank, plank, logs, logs}, 2, 2, PICKBUILDER);
		
		crafting.addRecipe(new FixedGrid<>(3,
		plank, logs,  plank,
		null,  plank, null,
		null,  plank, null), pickHeadWood, 1);
		crafting.addRecipe(new FixedGrid<>(3,
		Materials.rudimentary.nugget, Materials.rudimentary.base,   Materials.rudimentary.nugget,
		null,                         Materials.rudimentary.nugget, null,
		null,                         Materials.rudimentary.nugget, null), pickHeadRudimentary, 1);
		
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
		crafting.addRecipeGrid(new ItemEntry[]{ContentsBlocks.ipipe_ELBOW, Materials.iron.nugget, ContentsBlocks.ipipe_STRAIGHT}, 3, 1, ContentsBlocks.ipipe_TOLEFT, 1);
		crafting.addRecipeGrid(new ItemEntry[]{ContentsBlocks.ipipe_STRAIGHT, Materials.iron.nugget, ContentsBlocks.ipipe_ELBOW}, 3, 1, ContentsBlocks.ipipe_TORIGHT, 1);
		
		//Actuator blocks
		//No recipe for Creative Block Placer
		crafting.addRecipeGrid(new ItemEntry[]{rod1, bearing1, frame1}, 1, 3, ROTATOR, 1); //Block Rotator
		
		//WireWorld blocks
		crafting.addRecipeGrid(new ItemEntry[]{Materials.silicon.base, Materials.copper.base}, 1, 2, ww_wire, 24); //WireWorld cell
		
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
		
		ItemEntry steel = Materials.steel.base;
		ItemEntry nuggetSteel = Materials.steel.nugget;
		
		//WireWorld signallers
		crafting.addRecipeGrid(new ItemEntry[]{
		Materials.silicon.base , ww_wire, Materials.silicon.base,
		ww_wire, ww_wire, ww_wire,
		Materials.silicon.base, ww_wire, Materials.silicon.base
		}, 3, 3, TRUE, 4); //Always true
		crafting.addRecipeGrid(new ItemEntry[]{
		null,  null,  steel,
		null,  steel, null,
		steel, null,  null
		}, 3, 3, RANDOM, 4); //Random
		crafting.addRecipeGrid(new ItemEntry[]{
		Materials.silicon.base, Materials.silicon.base, Materials.silicon.base,
		Materials.silicon.base, ww_wire, Materials.silicon.base,
		Materials.silicon.base, Materials.silicon.base, Materials.silicon.base
		}, 3, 3, URANDOM, 4); //Uniform random
		
		//Machine parts
		crafting.addRecipeGrid(new ItemEntry[]{
		steel, Materials.iron.base, steel,
		Materials.iron.base,  null, Materials.iron.base,
		steel, Materials.iron.base, steel
		}, 3, 3, frame1);
		crafting.addRecipeGrid(new ItemEntry[]{
		null,  null,  steel,
		null,  steel, null,
		steel, null,  null
		}, 3, 3, rod1);
		crafting.addRecipeGrid(new ItemEntry[]{
		nuggetSteel,  nuggetSteel,  nuggetSteel,
		nuggetSteel,  null,         nuggetSteel,
		nuggetSteel,  nuggetSteel,  nuggetSteel,
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
		
		//Electronic parts
		assembler.add(new SimpleItemList(coal.smalldust.stack(1), paper.stack(1),     copper.wire.stack(1)), resistor,   null, 8, VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(alu.foil.stack(2),       paper.stack(2),     copper.wire.stack(1)), capacitor,  null, 8, VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(iron.frag.stack(1),                          copper.wire.stack(4)), inductor,   null, 8, VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(silicon.frag.stack(1),                       copper.wire.stack(2)), diode,      null, 8, VoltageTier.V1,  20000);
		assembler.add(new SimpleItemList(silicon.frag.stack(1),                       silver.wire.stack(1)), transistor, null, 8, VoltageTier.V1,  40000);
		assembler.add(new SimpleItemList(silicon.frag.stack(1),                         gold.wire.stack(1)), IC,         null, 8, VoltageTier.V1,  80000);
		assembler.add(new SimpleItemList(paper.stack(1),                         rudimentary.foil.stack(2)), substrate0, null, 8, VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(paper.stack(1),                              copper.foil.stack(2)), substrate1, null,    VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(
			substrate0.stack(3),
			resistor.stack(2),
			capacitor.stack(2),
			inductor.stack(2),
			rudimentary.foil.stack(1)
			), circuit0, null, VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(
			substrate1.stack(4),
			resistor.stack(8),
			capacitor.stack(8),
			inductor.stack(8),
			diode.stack(4),
			transistor.stack(2),
			IC.stack(1),
			circuit0.stack(4),
			copper.foil.stack(8),
			tin.wire.stack(3)
			), circuit1, null, VoltageTier.V1, 100000);
		assembler.add(new SimpleItemList(
			resistor.stack(16),
			nickel.wire.stack(8)
			), resistors, null, VoltageTier.V1, 100000);
		assembler.add(new SimpleItemList(
			steel.stack(2),
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
		
		_craftrsULV(); //Electric machines - ULV
		_craftrsVLV();
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
	/**
	 * 
	 */
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
		splitter,         rudimentary.panel,   splicer,
		rudimentary.wire, coal.nugget,         rudimentary.wire,
		splicer,          rudimentary.panel,   splitter,
		}, 3, 3, bassembly.get(0)); //Machine Assembler ULV
		
		crafting.addRecipeGrid(new ItemEntry[]{
		iron.panel, glassp,     iron.panel,
		iron.panel, iron.panel, iron.panel,
		coal.base,  seeds,      wireRudimentary.medium,
		}, 3, 3, bbrewery.get(0)); //Brewery ULV
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
		
		//Quarry ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor2,    steel.gear, motor2,
		steel.wire, stone, steel.wire,
		motor2,    steel.gear, motor2,
		}, 3, 3, bquarry.get(1)); 
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
		alloyer.add(new SimpleItemList(redstone.base.stack(2), rudimentary.base.stack(2)), resrc1, 2, VoltageTier.V2, 180000);
		//simple stone regeneration recipe
		alloyer.add(new SimpleItemList(stone, rudimentary.nugget), stone, 8, VoltageTier.V1, 80000);
		
		//sifting seeds sometimes gives yeast
		quarry.add(seeds, RecipeOutput.NONE, VoltageTier.V1, 40000, new RandomOrElseChance(0.05, yeast, seeds));
	}
}
