/**
 * 
 */
package mmb.WORLD;

import static mmb.WORLD.blocks.ContentsBlocks.*;
import static mmb.WORLD.contentgen.Materials.*;
import static mmb.WORLD.items.ContentsItems.*;
import static mmb.WORLD.recipes.CraftingGroups.*;

import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.blocks.machine.manual.Crafting;
import mmb.WORLD.chance.ListChance;
import mmb.WORLD.chance.RandomChance;
import mmb.WORLD.chance.RandomOrElseChance;
import mmb.WORLD.contentgen.Materials;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.crafting.SimpleItemList;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.electromachine.BlockTransformer.TransformerData;
import mmb.WORLD.item.Item;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.items.electronics.Electronics;
import monniasza.collects.grid.FixedGrid;

/**
 * @author oskar
 *
 */
public class ContentsRecipes {
	private ContentsRecipes() {}
	
	//Recipe creation
		private static boolean inited = false;
		/** Creates all crafting recipes */
		public static void createRecipes() {
			if(inited) return;
			inited = true;
			//Crafting ingredients
			crafting.addRecipe(logs, plank, 16); // wood → planks
			splitter.add(logs, plank, 16, VoltageTier.V1, 1000);
			crafting.addRecipeGrid(plank, 2, 2, Crafting.types.get(0), 1); //4*wood plank → crafting table 1
			
			smelting.add(rudimentary.base, wireRudimentary.medium, VoltageTier.V1, 80_000);
			smelting.add(logs, Materials.coal.base, VoltageTier.V1, 50_000);
			smelting.add(rrubber, Materials.rubber.base, VoltageTier.V1, 20_000);
			
			//Rocks
			clusterMill.add(plank, paper, 16, VoltageTier.V1, 1000);
			smelting.add(sand, glass, VoltageTier.V1, 70_000);
			clusterMill.add(glass, glassp, VoltageTier.V1, 70_000);
			crusher.add(stone, gravel, VoltageTier.V1, 15_000);
			crusher.add(gravel, sand, VoltageTier.V1, 15_000);
			extruder.add(logs, rrubber, rudimentary.frame, 1, VoltageTier.V1, 20_000);
			
			_chest();
			_tools();
			_pipes();
			_wireworld();
			_mechparts();
			_sinter();
			
			//Furnace
			crafting.addRecipeGrid(new ItemEntry[]{
			stone, stone, stone,
			stone, null,  stone,
			stone, stone, stone,
			}, 3, 3, FURNACE);
				
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

		private static void _sinter() {
			//Tungsten Carbide
			sinterer.add(new SimpleItemList(tungsten.nugget, coal.nugget),
			tungstenC.nugget, 2, VoltageTier.V5, 640000);
			//Silicon Carbide
			sinterer.add(new SimpleItemList(silicon.nugget, coal.nugget),
			silicarbide.nugget, 2, VoltageTier.V3, 320000);
		}
		private static void _mechparts() {
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
			Materials.steel.nugget,  null,                    Materials.steel.nugget,
			Materials.steel.nugget,  Materials.steel.nugget,  Materials.steel.nugget,
			}, 3, 3, bearing1);
			
			crafting.addRecipeGrid(new ItemEntry[]{
			wireRudimentary.tiny,  rudimentary.base,  wireRudimentary.tiny,
			wireRudimentary.tiny,  rudimentary.base,  wireRudimentary.tiny,
			wireRudimentary.tiny,  rudimentary.base,  wireRudimentary.tiny,
			}, 3, 3, motor.items.get(0));
			crafting.addRecipeGrid(new ItemEntry[]{
			rudimentary.base,  rudimentary.panel,  rudimentary.base,
			rudimentary.panel, motor.items.get(0), rudimentary.panel,
			rudimentary.base,  rudimentary.panel,  rudimentary.base,
			}, 3, 3, pump.items.get(0));
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(0), rudimentary.rod,       motor.items.get(0),
			circuit0,           null,                  rudimentary.rod,
			rudimentary.panel,  conveyor.items.get(0), motor.items.get(0),
			}, 3, 3, robot.items.get(0));
			crafting.addRecipeGrid(new ItemEntry[]{
			rubber.sheet,     rubber.sheet,       rubber.sheet,
			rudimentary.ring, motor.items.get(0), rudimentary.ring,
			rubber.sheet,     rubber.sheet,       rubber.sheet,
			}, 3, 3, conveyor.items.get(0));
			
			//ULV items
			assembler.add(new SimpleItemList(
				rudimentary.base,
				rudimentary.wire.stack(16)
			), motor.items.get(0), null, VoltageTier.V1, 50000);
			assembler.add(new SimpleItemList(
				rudimentary.base,
				circuit0,
				motor.items.get(0)
			), robot.items.get(0).stack(3), null, VoltageTier.V1, 50000);
			assembler.add(new SimpleItemList(
				rubber.panel.stack(2),
				motor.items.get(0),
				rudimentary.ring.stack(3)
			), conveyor.items.get(0).stack(3), null, VoltageTier.V1, 50000);
			assembler.add(new SimpleItemList(
				rudimentary.sheet.stack(2),
				motor.items.get(0),
				rubber.ring.stack(1)
			), pump.items.get(0).stack(2), null, VoltageTier.V1, 50000);
			
			//VLV items
			assembler.add(new SimpleItemList(
				iron.base,
				copper.wire.stack(16)
			), motor.items.get(1), null, VoltageTier.V1, 100000);
			assembler.add(new SimpleItemList(
				iron.base,
				circuit1,
				motor.items.get(1)
			), robot.items.get(1).stack(3), null, VoltageTier.V1, 100000);
			assembler.add(new SimpleItemList(
				rubber.panel.stack(2),
				motor.items.get(1),
				iron.ring.stack(3)
			), conveyor.items.get(1).stack(3), null, VoltageTier.V1, 100000);
			assembler.add(new SimpleItemList(
				iron.sheet.stack(2),
				motor.items.get(1),
				rubber.ring.stack(1)
			), pump.items.get(1).stack(2), null, VoltageTier.V1, 100000);
			
			//LV items
			assembler.add(new SimpleItemList(
				electrosteel.base,
				silver.wire.stack(16)
			), motor.items.get(2), null, VoltageTier.V2, 400000);
			assembler.add(new SimpleItemList(
				steel.base,
				circuit2,
				motor.items.get(2)
			), robot.items.get(2).stack(3), null, VoltageTier.V2, 400000);
			assembler.add(new SimpleItemList(
				rubber.panel.stack(2),
				motor.items.get(2),
				steel.ring.stack(3)
			), conveyor.items.get(2).stack(3), null, VoltageTier.V2,400000);
			assembler.add(new SimpleItemList(
				steel.sheet.stack(2),
				motor.items.get(2),
				rubber.ring.stack(1)
			), pump.items.get(2).stack(2), null, VoltageTier.V2, 400000);
			
			//MV items
			assembler.add(new SimpleItemList(
				alnico.base,
				gold.wire.stack(16)
			), motor.items.get(3), null, VoltageTier.V3, 400000);
			assembler.add(new SimpleItemList(
				stainless.base,
				circuit3,
				motor.items.get(3)
			), robot.items.get(3).stack(3), null, VoltageTier.V3, 400000);
			assembler.add(new SimpleItemList(
				rubber.panel.stack(2),
				motor.items.get(3),
				stainless.ring.stack(3)
			), conveyor.items.get(3).stack(3), null, VoltageTier.V3,400000);
			assembler.add(new SimpleItemList(
				stainless.sheet.stack(2),
				motor.items.get(3),
				rubber.ring.stack(1)
			), pump.items.get(3).stack(2), null, VoltageTier.V3, 400000);
			
			//HV items
			assembler.add(new SimpleItemList(
				neodymium.base,
				platinum.wire.stack(16)
			), motor.items.get(4), null, VoltageTier.V4, 1600000);
			assembler.add(new SimpleItemList(
				titanium.base,
				circuit4,
				motor.items.get(4)
			), robot.items.get(4).stack(3), null, VoltageTier.V4, 1600000);
			assembler.add(new SimpleItemList(
				rubber.panel.stack(2),
				motor.items.get(4),
				titanium.ring.stack(3)
			), conveyor.items.get(4).stack(3), null, VoltageTier.V4,1600000);
			assembler.add(new SimpleItemList(
				titanium.sheet.stack(2),
				motor.items.get(4),
				rubber.ring.stack(1)
			), pump.items.get(4).stack(2), null, VoltageTier.V4, 1600000);
			
			//EV items
			assembler.add(new SimpleItemList(
				neosteel.base,
				iridium.wire.stack(16)
			), motor.items.get(5), null, VoltageTier.V5, 6400000);
			assembler.add(new SimpleItemList(
				signalum.base,
				circuit5,
				motor.items.get(5)
			), robot.items.get(5).stack(3), null, VoltageTier.V5, 6400000);
			assembler.add(new SimpleItemList(
				rubber.panel.stack(2),
				motor.items.get(5),
				signalum.ring.stack(3)
			), conveyor.items.get(5).stack(3), null, VoltageTier.V5,6400000);
			assembler.add(new SimpleItemList(
				signalum.sheet.stack(2),
				motor.items.get(5),
				rubber.ring.stack(1)
			), pump.items.get(5).stack(2), null, VoltageTier.V5, 6400000);
			
			//IV items
			assembler.add(new SimpleItemList(
				neosteel.base,
				enderium.wire.stack(16)
			), motor.items.get(6), null, VoltageTier.V6, 25600000);
			assembler.add(new SimpleItemList(
				enderium.base,
				circuit6,
				motor.items.get(6)
			), robot.items.get(6).stack(3), null, VoltageTier.V6, 25600000);
			assembler.add(new SimpleItemList(
				rubber.panel.stack(2),
				motor.items.get(6),
				enderium.ring.stack(3)
			), conveyor.items.get(6).stack(3), null, VoltageTier.V6,25600000);
			assembler.add(new SimpleItemList(
				enderium.sheet.stack(2),
				motor.items.get(6),
				rubber.ring.stack(1)
			), pump.items.get(6).stack(2), null, VoltageTier.V6, 25600000);
		}
		private static void _pipes() {
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
			assembler.add(new SimpleItemList(
				gold.wire,
				gold.nugget,
				PE.sheet.stack(4)),
			Electronics.die1, null, 24, VoltageTier.V3, 160000);
			
			//Extreme
			assembler.add(new SimpleItemList(
				gold.wire,
				nichrome.wire),
			Electronics.resistor2, null, 24, VoltageTier.V3,  80000);
			assembler.add(new SimpleItemList(
				gold.wire,PE.foil),
			Electronics.capacitor2, null, 24, VoltageTier.V3, 160000);
			assembler.add(new SimpleItemList(
				gold.wire.stack(2),
				electrosteel.nugget),
			Electronics.inductor2, null, 24, VoltageTier.V3, 320000);
			assembler.add(new SimpleItemList(
				gold.wire,
				copper.nugget,
				silicon.nugget.stack(2)),
			Electronics.diode2, null, 24, VoltageTier.V3, 640000);
			assembler.add(new SimpleItemList(
				gold.wire,
				silver.nugget,
				silicon.nugget.stack(2)),
			Electronics.transistor2, null, 16, VoltageTier.V3, 1280000);
			assembler.add(new SimpleItemList(
				platinum.wire.stack(4),
				platinum.nugget.stack(4),
				silicopper.nugget.stack(16),
				silicon.sheet.stack(2)),
			Electronics.ic2, null, 48, VoltageTier.V3, 2560000);
			assembler.add(new SimpleItemList(
				platinum.wire.stack(2),
				platinum.nugget.stack(2),
				PVC.sheet.stack(4)),
			Electronics.die2, null, 24, VoltageTier.V4, 640000);
			inscriber.add(silicon.panel, Electronics.wafer2, Electronics.ic1, 1, VoltageTier.V4, 640000);
			assembler.add(new SimpleItemList(
				gold.wire,
				quartz.nugget.stack(2),
				platinum.wire),
			Electronics.ceritor2, null, 24, VoltageTier.V4, 320000);
			assembler.add(new SimpleItemList(
				platinum.wire,
				Electronics.ic2.stack(6),
				copper.nugget,
				silicopper.nugget.stack(4)),
			Electronics.ram2, null, 16, VoltageTier.V4, 2560000);
			assembler.add(new SimpleItemList(
				platinum.wire,
				Electronics.ic2.stack(6),
				gold.nugget,
				silicopper.nugget.stack(4)),
			Electronics.gpu2, null, 2, VoltageTier.V4, 2560000);
			assembler.add(new SimpleItemList(
				platinum.wire,
				Electronics.ic2.stack(6),
				silver.nugget,
				silicopper.nugget.stack(4)),
			Electronics.cpu2, null, 24, VoltageTier.V4, 2560000);
			
			
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
				stone,            rudimentary.base, stone,
				rudimentary.base, null,             rudimentary.base,
				stone,            rudimentary.base, stone
			}, 3, 3, CHEST1);
			crafting.addRecipeGrid(new ItemEntry[]{
				iron.base,         rudimentary.base, iron.base,
				rudimentary.base,  null,             rudimentary.base,
				iron.base,         rudimentary.base, iron.base
			}, 3, 3, CHEST2);
			crafting.addRecipeGrid(new ItemEntry[]{
				iron.base,  steel.base, iron.base,
				steel.base, null,       steel.base,
				iron.base,  steel.base, iron.base
			}, 3, 3, CHEST3);
			crafting.addRecipeGrid(new ItemEntry[]{
				stainless.base,  steel.base, stainless.base,
				steel.base,      null,       steel.base,
				stainless.base,  steel.base, stainless.base
			}, 3, 3, CHEST4);
			crafting.addRecipeGrid(new ItemEntry[]{
				stainless.base,  HSS.base, stainless.base,
				HSS.base,        null,     HSS.base,
				stainless.base,  HSS.base, stainless.base
			}, 3, 3, CHEST5);
			crafting.addRecipeGrid(new ItemEntry[]{
				signalum.base,  HSS.base, signalum.base,
				HSS.base,       null,     HSS.base,
				signalum.base,  HSS.base, signalum.base
			}, 3, 3, CHEST6);
			crafting.addRecipeGrid(new ItemEntry[]{
				signalum.base,  enderium.base, signalum.base,
				enderium.base,  null,          enderium.base,
				signalum.base,  enderium.base, signalum.base
			}, 3, 3, CHEST7);
			crafting.addRecipeGrid(new ItemEntry[]{
				draconium.base, enderium.base, draconium.base,
				enderium.base,  null,          enderium.base,
				draconium.base, enderium.base, draconium.base
			}, 3, 3, CHEST8);
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
			motor.items.get(0),                  wireRudimentary.medium, motor.items.get(0),
			wireRudimentary.medium,  rudimentary.gear,       wireRudimentary.medium,
			motor.items.get(0),                  wireRudimentary.medium, motor.items.get(0),
			}, 3, 3, bcmill.get(0)); 
			
			//Wiremill ULV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(0),    motor.items.get(0),                 motor.items.get(0),
			rudimentary.gear, rudimentary.panel,     rudimentary.gear,
			motor.items.get(0),    wireRudimentary.medium, motor.items.get(0),
			}, 3, 3, bwiremill.get(0)); 
			
			Item splitter = bsplitter.get(0);
			Item splicer = bsplicer.get(0);
			//Material Splitter ULV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(0),    rudimentary.gear,       motor.items.get(0),
			coal.wire, coal.wire,              coal.wire,
			motor.items.get(0),    wireRudimentary.medium, motor.items.get(0),
			}, 3, 3, splitter); 
			
			//Material Combiner ULV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(0),    rudimentary.gear,       motor.items.get(0),
			coal.wire, coal.nugget,              coal.wire,
			motor.items.get(0),    wireRudimentary.medium, motor.items.get(0),
			}, 3, 3, splicer); 
			
			//Quarry ULV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(0),    iron.gear, motor.items.get(0),
			iron.wire, stone, iron.wire,
			motor.items.get(0),    iron.gear, motor.items.get(0),
			}, 3, 3, bquarry.get(0)); 
			
			crafting.addRecipeGrid(new ItemEntry[]{
			splitter,         robot.items.get(0), splicer,
			rudimentary.wire, circuit0,           rudimentary.wire,
			splicer,          rudimentary.panel,  splitter,
			}, 3, 3, bassembly.get(0)); //Machine Assembler ULV
			
			crafting.addRecipeGrid(new ItemEntry[]{
			iron.panel, glassp,     iron.panel,
			iron.panel, iron.panel, iron.panel,
			coal.base,  seeds,      wireRudimentary.medium,
			}, 3, 3, bbrewery.get(0)); //Brewery ULV
			
			crafting.addRecipeGrid(new ItemEntry[]{
			null,       motor.items.get(0),     iron.base,
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
			motor.items.get(0),            rudimentary.gear,  rudimentary.panel,
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
			
			//Inscriber ULV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(0),            rudimentary.gear,  rudimentary.panel,
			rudimentary.panel, null,              null,
			motor.items.get(0),            rudimentary.gear,  rudimentary.panel,
			}, 3, 3, binscriber.get(0));
		}
		private static void _craftrsVLV() {
			//Coal Generator VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			iron.cluster, circuit1, iron.cluster,
			FURNACE,      FURNACE,  FURNACE,
			iron.cluster, circuit1, iron.cluster,
			}, 3, 3, COALGEN2);
			
			//Turbo Generator VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			circuit1,    steel.panel, FURNACE,
			steel.panel, steel.frame, steel.panel,
			FURNACE,     steel.panel, circuit1,
			}, 3, 3, TURBOGEN1);
			
			//Transformer VLV/ULV
			crafting.addRecipeGrid(new ItemEntry[]{
			null,              iron.base, wireRudimentary.medium,
			wireCopper.medium, iron.base, wireRudimentary.medium,
			null,              iron.base, wireRudimentary.medium,
			}, 3, 3, TransformerData.VLV.type); 
			
			//Electric Furnace VLV
			ItemEntry efurnace1 = efurnace.get(1);
			crafting.addRecipeGrid(new ItemEntry[]{
			nickel.base, resistors, iron.base,
			nickel.wire, FURNACE,   nickel.wire,
			iron.base,   resistors, nickel.base,
			}, 3, 3, efurnace1);
			
			//Alloyer VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			nickel.base, circuit1,  iron.base,
			efurnace1,   efurnace1, efurnace1,
			iron.base,   resistors, nickel.base,
			}, 3, 3, balloyer.get(1)); 
			
			//Crusher VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			copper.base, circuit1,  iron.base,
			stone,   wireCopper.medium, stone,
			iron.base,   resistors, copper.base,
			}, 3, 3, bcrusher.get(1)); 
			
			//Cluster Mill VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(1),    circuit1,   motor.items.get(1),
			iron.gear, iron.panel, iron.gear,
			motor.items.get(1),    circuit1,   motor.items.get(1),
			}, 3, 3, bcmill.get(1));
			
			//WireMill VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(1),      circuit1,   bearing1,
			iron.gear,   iron.panel, iron.gear,
			bearing1, circuit1,   motor.items.get(1),
			}, 3, 3, bwiremill.get(1));
			
			//Quarry VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(1),    steel.gear, motor.items.get(1),
			steel.wire, stone, steel.wire,
			motor.items.get(1),    steel.gear, motor.items.get(1),
			}, 3, 3, bquarry.get(1)); 
			
			//Machine Assembler VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			bsplitter.get(1), robot.items.get(1),     bcmill.get(1),
			steel.ring,       circuit1,               steel.ring,
			bsplicer.get(1),  wireRudimentium.medium, balloyer.get(1),
			}, 3, 3, bassembly.get(1)); 
			
			//Digger VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			null,        motor.items.get(1),     steel.base,
			steel.panel, circuit1,    steel.panel,
			steel.panel, steel.panel, steel.panel,
			}, 3, 3, bdig.get(1));
			
			//Splitter VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(1),     steel.gear, motor.items.get(1),
			steel.wire, steel.wire, steel.wire,
			motor.items.get(1),     circuit1,   motor.items.get(1),
			}, 3, 3, bsplitter.get(1)); 
			
			//Splicer VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(1),    steel.gear,    motor.items.get(1),
			steel.wire, steel.nugget, steel.wire,
			motor.items.get(1),    circuit1,      motor.items.get(1),
			}, 3, 3, bsplicer.get(1)); 
			
			//Battery VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			rudimentium.base, glass, rudimentium.base,
			nickel.panel,     paper, nickel.panel,
			nickel.panel,     paper, nickel.panel,
			}, 3, 3, bbattery.get(1));
			
			//Extruder VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(1),      steel.gear,  steel.panel,
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
			
			//Inscriber VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(1), steel.gear,  steel.panel,
			steel.panel,        null,              null,
			motor.items.get(1), steel.gear,  steel.panel,
			}, 3, 3, binscriber.get(1));
			
			//Brewery VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			steel.panel, glassp,      steel.panel,
			steel.panel, steel.panel, steel.panel,
			nickel.base, seeds,       wireCopper.medium,
			}, 3, 3, bbrewery.get(1));
		}
		private static void _craftrsLV() {
			//Turbo Generator LV
			crafting.addRecipeGrid(new ItemEntry[]{
			TURBOGEN1,       stainless.panel, TURBOGEN1,
			stainless.panel, stainless.frame, stainless.panel,
			circuit2,        stainless.panel, circuit2,
			}, 3, 3, TURBOGEN2);
			
			//Coal Generator LV
			crafting.addRecipeGrid(new ItemEntry[]{
			steel.cluster, circuit2, steel.cluster,
			motor.items.get(1),        FURNACE,  motor.items.get(1),
			steel.cluster, circuit2, steel.cluster,
			}, 3, 3, COALGEN3);
			
			//Transformer LV/VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			null,              steel.base, wireCopper.medium,
			wireSilver.medium, steel.base, wireCopper.medium,
			null,              steel.base, wireCopper.medium,
			}, 3, 3, TransformerData.LV.type); 
			
			ItemEntry efurnace1 = efurnace.get(2);
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
			}, 3, 3, bbattery.get(2));
			
			//Alloyer LV
			crafting.addRecipeGrid(new ItemEntry[]{
			nichrome.base,  circuit2, steel.base,
			efurnace1, efurnace1, efurnace1,
			steel.base, steel.frame, nichrome.base,
			}, 3, 3, balloyer.get(2));
			
			//Crusher LV
			crafting.addRecipeGrid(new ItemEntry[]{
			steel.base,  circuit2,  steel.base,
			cobalt.gear, wireSilver.medium, cobalt.gear,
			steel.base,  resistors, steel.base,
			}, 3, 3, bcrusher.get(2)); 
			
			//Cluster Mill LV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(2),  circuit2,    motor.items.get(2),
			steel.gear,          steel.panel, steel.gear,
			motor.items.get(2),  circuit2,    motor.items.get(2),
			}, 3, 3, bcmill.get(2));
			
			//WireMill VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(2),      circuit2,   steel.ring,
			steel.gear,   steel.panel, steel.gear,
			steel.ring, circuit1,   motor.items.get(2),
			}, 3, 3, bwiremill.get(2));
			
			//Quarry VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(2),    steel.gear, motor.items.get(2),
			stainless.wire,        circuit2,   stainless.wire,
			motor.items.get(2),    steel.gear, motor.items.get(2),
			}, 3, 3, bquarry.get(2));
			
			//Splitter LV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(2),    stainless.gear,    motor.items.get(2),
			stainless.wire, stainless.wire, stainless.wire,
			motor.items.get(2),    circuit2,      motor.items.get(2),
			}, 3, 3, bsplitter.get(2));
			
			//Splicer LV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(2),    stainless.gear,    motor.items.get(2),
			stainless.wire, stainless.nugget, stainless.wire,
			motor.items.get(2),    circuit2,      motor.items.get(2),
			}, 3, 3, bsplicer.get(2));
			
			//Machine Assembler LV
			crafting.addRecipeGrid(new ItemEntry[]{
			bsplitter.get(2), bsplicer.get(2), bcmill.get(2), bcrusher.get(2),
			robot.items.get(2), nichrome.wire, nichrome.wire, robot.items.get(2),
			stainless.frame, stainless.panel, stainless.panel, stainless.frame,
			bsplicer.get(2),  balloyer.get(2), binscriber.get(2), bquarry.get(2)
			}, 4, 4, bassembly.get(2));
			
			//Digger LV
			crafting.addRecipeGrid(new ItemEntry[]{
			null,            motor.items.get(2), stainless.base,
			stainless.panel, circuit2,           stainless.panel,
			stainless.panel, stainless.panel,    stainless.panel,
			}, 3, 3, bdig.get(2));
			
			//Power Receiver VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			silver.wire, silver.wire, silver.wire,
			silver.wire, steel.base,  silver.wire,
			silver.wire, silver.wire, silver.wire,
			}, 3, 3, prec.get(2));
			
			//Power Tower VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			wireSilver.medium, wireSilver.medium, wireSilver.medium,
			wireSilver.medium, prec.get(2),       wireSilver.medium,
			wireSilver.medium, wireSilver.medium, wireSilver.medium,
			}, 3, 3, ptower.get(2));
			
			//Inscriber VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(2),  stainless.gear,  stainless.panel,
			stainless.panel,     null,            null,
			motor.items.get(2),  stainless.gear,  stainless.panel,
			}, 3, 3, binscriber.get(2));
			
			//Brewery VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			stainless.panel, glassp,          stainless.panel,
			stainless.panel, stainless.panel, stainless.panel,
			nichrome.base,   seeds,           wireSilver.medium,
			}, 3, 3, bbrewery.get(2));
			
			//Extruder VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(2), stainless.gear,  stainless.panel,
			stainless.panel,    stainless.panel, stainless.panel,
			stainless.panel,    circuit2,        stainless.panel,
			}, 3, 3, bextruder.get(2));
			
			//Sinterer VLV
			crafting.addRecipeGrid(new ItemEntry[]{
			motor.items.get(2), stainless.panel, stainless.panel,
			silver.ring,        coal.wire,       stainless.panel,
			motor.items.get(2), circuit2,        stainless.panel,
			}, 3, 3, bsinterer.get(2));
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
			
			//Tier 4: +platinum, neodymium, titanium
			quarry.add(resrc3, resrc2, 2, VoltageTier.V4, 1280000, new ListChance(
				new RandomChance(0.1, platinum.ore),
				new RandomChance(0.2, titanium.ore),
				new RandomChance(0.2, neodymium.ore)
			));
			
			//Tier 5: +tungsten, iridium
			quarry.add(resrc4, resrc3, 2, VoltageTier.V5, 5120000, new ListChance(
				new RandomChance(0.1, tungsten.ore),
				new RandomChance(0.1, iridium.ore)
			));
			
			//TODO Tier 6: +draconic shards, crystalline shards
			quarry.add(resrc5, resrc4, 2, VoltageTier.V6, 20480000, new ListChance(
				new RandomChance(0.1, sdraconium)
			));
			
			//TODO Tier 7: +awakened draconic shards, stellar shards
			quarry.add(resrc6, resrc5, 2, VoltageTier.V7, 81920000, new ListChance(
				new RandomChance(0.1, sadraconium)
			));
			
			//TODO Tier 8: +chaotic shards, unobtainium shards
			quarry.add(resrc7, resrc6, 2, VoltageTier.V8, 327680000, new ListChance(
				new RandomChance(0.1, schaotium)
			));
			
			//TODO Tier 9: +ultimate shards, singularities
			
			alloyer.add(new SimpleItemList(redstone.base.stack(2), rudimentary.base.stack(2)), resrc1, 2, VoltageTier.V2, 180000);
			//simple stone regeneration recipe
			alloyer.add(new SimpleItemList(stone, rudimentary.nugget), stone, 8, VoltageTier.V1, 80000);
			
			//sifting seeds sometimes gives yeast
			quarry.add(seeds, RecipeOutput.NONE, VoltageTier.V1, 40000, new RandomOrElseChance(0.05, yeast, seeds));
		}

}
