/**
 * 
 */
package mmb.content;

import static mmb.content.ContentsBlocks.*;
import static mmb.content.ContentsItems.*;
import static mmb.content.CraftingGroups.*;
import static mmb.content.rawmats.Materials.*;

import mmb.content.agro.Agro;
import mmb.content.craft.ManCrafter;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.machines.BlockTransformer.TransformerData;
import mmb.content.electronics.Electronics;
import mmb.content.rawmats.Materials;
import mmb.engine.chance.ListChance;
import mmb.engine.chance.RandomChance;
import mmb.engine.chance.RandomOrElseChance;
import mmb.engine.item.Item;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemRaw;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.recipe.SimpleItemList;
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
		
		//Crafting tables
		crafting.addRecipeGrid(plank, 2, 2, ManCrafter.types.get(0), 1); //4*wood plank → crafting table 1
		crafting.addRecipeGrid(new ItemEntry[]{
		ManCrafter.types.get(0), steel.frame,        ManCrafter.types.get(0),
		steel.frame,           robot.items.get(1), steel.frame,
		ManCrafter.types.get(0), steel.frame,        ManCrafter.types.get(0)
		}, 3, 3, ManCrafter.types.get(1));
		
		//Crafting aids
		crafting.addRecipeGrid(new ItemEntry[]{
		paper, stone
		}, 2, 1, ItemRaw.make(stencil));
		crafting.addRecipeGrid(new ItemEntry[]{
		stone, paper
		}, 2, 1, ItemRaw.make(BOM));
		crafting.addRecipeGrid(new ItemEntry[]{
		paper, ItemRaw.make(BOM)
		}, 2, 1, ItemRaw.make(pingredients));
		
		smelting.add(rudimentary.base, wireRudimentary.medium, VoltageTier.V1, 80_000);
		smelting.add(logs, Materials.coal.base, VoltageTier.V1, 50_000);
		
		//Rubber production
		smelting.add(rrubber, Materials.rubber.base, VoltageTier.V1, 20_000);
		extruder.add(logs, rrubber, rudimentary.frame, 16, VoltageTier.V1, 20_000);
		
		//Rocks
		clusterMill.add(plank, paper, 16, VoltageTier.V1, 1000);
		smelting.add(sand, glass, VoltageTier.V1, 70_000);
		clusterMill.add(glass, glassp, VoltageTier.V1, 70_000);
		crusher.add(stone, gravel, VoltageTier.V1, 15_000);
		crusher.add(gravel, sand, VoltageTier.V1, 15_000);
		
		//Furnace
		crafting.addRecipeGrid(new ItemEntry[]{
		stone, stone, stone,
		stone, null,  stone,
		stone, stone, stone,
		}, 3, 3, FURNACE);
		
		_chest();
		_tools();
		_imachines();
		_wireworld();
		_mechparts();
		_sinter();
		_upgrade();	
		
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.base, rudimentary.base, rudimentary.base,
		wireRudimentary.medium, coal.base, wireRudimentary.medium,
		rudimentary.base, rudimentary.base, rudimentary.base
		}, 3, 3, LOAD);
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.base, rudimentary.base, rudimentary.base,
		wireRudimentary.medium, iron.base, wireRudimentary.medium,
		rudimentary.base, rudimentary.base, rudimentary.base
		}, 3, 3, PMETER);
		
		
		_craftrsULV(); //Electric machines - ULV
		_craftrsVLV();
		_craftrsLV();
		_craftrsMV();
		_craftrsHV();
		quarry();
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
		Electronics.circuit0,           null,                  rudimentary.rod,
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
			Electronics.circuit0,
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
			Electronics.circuit1,
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
			Electronics.circuit2,
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
			Electronics.circuit3,
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
			Electronics.circuit4,
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
			Electronics.circuit5,
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
			Electronics.circuit6,
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
	private static void _imachines() {
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
		crafting.addRecipeGrid(new ItemEntry[]{
		ipipe_STRAIGHT,
		ipipe_STRAIGHT,
		iron.gear,
		}, 1, 3, IMOVER, 3);
		crafting.addRecipeGrid(new ItemEntry[]{
		ipipe_STRAIGHT,
		IMOVER,
		iron.gear,
		}, 1, 3, ipipe_IPE, 1);
		
		//Item machines
		crafting.addRecipeGrid(new ItemEntry[]{
		iron.base, IMOVER, iron.base,
		iron.base, CHEST, iron.base,
		iron.base, ipipe_STRAIGHT, iron.base
		}, 3, 3, COLLECTOR);
		crafting.addRecipeGrid(new ItemEntry[]{
		iron.base, CLICKER, iron.base,
		iron.base, CHEST, iron.base,
		iron.base, ipipe_STRAIGHT, iron.base
		}, 3, 3, PLACEITEMS);
	}
	private static void _wireworld() {
		//Actuator blocks
		//Block Rotator
		crafting.addRecipeGrid(new ItemEntry[]{rod1, bearing1, frame1}, 1, 3, ROTATOR, 1);
		
		//WireWorld cell
		crafting.addRecipeGrid(new ItemEntry[]{Materials.silicon.base, Materials.copper.base}, 1, 2, ww_wire, 24);
		
		//WireWorld gates - 2 inputs
		crafting.addRecipeGrid(new ItemEntry[] {
		null, YES, null,
		YES, ww_wire, YES}, 3, 2, OR, 4); //OR
		crafting.addRecipeGrid(new ItemEntry[] {
		null, NOT, null,
		YES, ww_wire, YES}, 3, 2, XOR, 4); //XOR
		crafting.addRecipeGrid(new ItemEntry[] {
		null, NOT, null,
		NOT, ww_wire, NOT}, 3, 1, AND, 4); //AND
		
		//WireWorld gates - 1 input
		crafting.addRecipeGrid(ww_wire, 1, 2, YES); //YES
		crafting.addRecipeGrid(new ItemEntry[]{ww_wire, YES}, 1, 2, NOT); //NOT
		crafting.addRecipeGrid(new ItemEntry[]{AND, RANDOM}, 1, 2, RANDOMCTRL); //Random if
		
		//WireWorld signallers
		crafting.addRecipeGrid(new ItemEntry[]{
		Materials.silicon.base , ww_wire, Materials.silicon.base,
		ww_wire, ww_wire, ww_wire,
		Materials.silicon.base, ww_wire, Materials.silicon.base
		}, 3, 3, TRUE, 4); //Always true
		crafting.addRecipeGrid(new ItemEntry[]{
		Materials.silicon.base, Materials.silicon.base, Materials.silicon.base,
		Materials.silicon.base, ww_wire, Materials.silicon.base,
		Materials.silicon.base, Materials.silicon.base, Materials.silicon.base
		}, 3, 3, RANDOM, 4); //Random
		crafting.addRecipeGrid(new ItemEntry[]{
		Materials.silicon.base, Materials.silicon.base, Materials.silicon.base,
		Materials.silicon.base, RANDOM, Materials.silicon.base,
		Materials.silicon.base, Materials.silicon.base, Materials.silicon.base
		}, 3, 3, URANDOM, 4); //Uniform random
		crafting.addRecipeGrid(new ItemEntry[]{
		URANDOM, TRUE, URANDOM,
		TRUE, ww_wire, TRUE,
		URANDOM, TRUE, URANDOM,
		}, 3, 3, ONLOAD, 4); //World Load Detecttor
		
		//Speaker
		crafting.addRecipeGrid(new ItemEntry[]{
			Electronics.inductor_, Materials.iron.frag, paper
		}, 3, 1, SPEAKER);
	}
	private static void _tools() {
		crafting.addRecipe(PICKBUILDER, new SimpleItemList(
			plank.stack(2),
			logs.stack(2)
		)); //pickaxe builder is deprecated - ingredient recovery recipe
		crafting.addRecipe(bucket, stone.stack(5)); //item bucket is deprecated - ingredient recovery recipe
		
		
		crafting.addRecipe(new FixedGrid<>(3,
				plank,	logs,	plank,
				null,	plank,	null,
				null,	plank,	null),
				ItemRaw.make(pickWood), 1);
		
		crafting.addRecipe(new FixedGrid<>(3,
				stone,	stone,	stone,
				null,	stone,	null,
				null,	stone,	null),
				ItemRaw.make(pickStone), 1);
		
		crafting.addRecipe(new FixedGrid<>(3,
				Materials.rudimentary.nugget,	Materials.rudimentary.base,	Materials.rudimentary.nugget,
				null,				Materials.rudimentary.nugget,	null,
				null,				Materials.rudimentary.nugget,	null),
				ItemRaw.make(pickRudimentary), 1);
		
		crafting.addRecipe(new FixedGrid<>(3,
				Materials.iron.nugget,		Materials.iron.base,		Materials.iron.nugget,
				null,				Materials.iron.nugget,		null,
				null,				Materials.iron.nugget,		null), 
				ItemRaw.make(pickIron));
		
		crafting.addRecipe(new FixedGrid<>(3,
				Materials.steel.nugget,		Materials.steel.base,		Materials.steel.nugget,
				null,				Materials.steel.nugget,		null,
				null,				Materials.steel.nugget,		null), 
				ItemRaw.make(pickSteel));
		
		crafting.addRecipe(new FixedGrid<>(3,
				Materials.stainless.nugget,	Materials.stainless.base,	Materials.stainless.nugget,
				null,				Materials.stainless.nugget,	null,
				null,				Materials.stainless.nugget,	null), 
				ItemRaw.make(pickStainless));
		crafting.addRecipeGrid(new ItemEntry[]{
				null,	stone,	null,
				stone,	null,	stone,
				null,	stone,	null
				}, 3, 3, aim); //Aimer
		crafting.addRecipeGrid(new ItemEntry[]{
				null,	stone,	stone,
				stone,	null,	null,
				stone,	null,	null
				}, 3, 3, configExtractors); //Configure dropped item extractors
		
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
		//Mover chests
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.frame, CHEST1, stone,
		}, 3, 1, HOPPER);
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.frame, CHEST1, gravel,
		}, 3, 1, HOPPER_suck);
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.frame, CHEST1, sand,
		}, 3, 1, HOPPER_both);
		//Trash can
		crafting.addRecipeGrid(new ItemEntry[]{
			rudimentary.base, null, rudimentary.base,
			rudimentary.base, null, rudimentary.base,
			rudimentary.base, null, rudimentary.base,
		}, 3, 3, TRASH);
		//BOM Workbench
		crafting.addRecipeGrid(new ItemEntry[]{
			rudimentary.base, iron.base, rudimentary.base,
			rudimentary.base, ItemRaw.make(BOM), rudimentary.base,
			rudimentary.base, iron.base, rudimentary.base,
		}, 3, 3, BOMMAKER);
		//Autocrafters
		crafting.addRecipeGrid(new ItemEntry[]{
			rudimentary.base, rudimentary.base, rudimentary.base,
			rudimentary.base, ManCrafter.types.get(0), rudimentary.base,
			rudimentary.base, rudimentary.base, rudimentary.base,
		}, 3, 3, AUTOCRAFTER1);
		crafting.addRecipeGrid(new ItemEntry[]{
			steel.base, iron.base, steel.base,
			iron.base, ManCrafter.types.get(0), iron.base,
			steel.base, iron.base, steel.base,
		}, 3, 3, AUTOCRAFTER2);
		crafting.addRecipeGrid(new ItemEntry[]{
			titanium.base, stainless.base, titanium.base,
			stainless.base, ManCrafter.types.get(0), stainless.base,
			titanium.base, stainless.base, titanium.base,
		}, 3, 3, AUTOCRAFTER3);
		/*crafting.addRecipeGrid(new ItemEntry[]{
			rudimentary.base, rudimentary.base, rudimentary.base,
			rudimentary.base, ManCrafter.types.get(0), rudimentary.base,
			rudimentary.base, rudimentary.base, rudimentary.base,
		}, 3, 3, AUTOCRAFTER4);*/
		crafting.addRecipeGrid(new ItemEntry[]{
			rudimentary.base, IMOVER, rudimentary.base,
			CHEST, ManCrafter.types.get(0), IMOVER,
			iron.base, ItemRaw.make(BOM), iron.base
		}, 3, 3, TIPD);
	}
	private static void _craftrsULV() {
		//Coal Generator ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		wireRudimentary.medium, wireRudimentary.medium, wireRudimentary.medium,
		wireRudimentary.medium, FURNACE,                wireRudimentary.medium,
		wireRudimentary.medium, wireRudimentary.medium, wireRudimentary.medium,
		}, 3, 3, COALGEN1);
		
		//Electric furnace ULV
		ItemEntry efurnace0 = efurnace.blocks.get(0);
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
		}, 3, 3, bcrusher.blocks.get(0)); 
		
		//Alloyer ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		stone,     wireRudimentary.medium, stone,
		efurnace0, FURNACE,                efurnace0,
		stone,     wireRudimentary.medium, stone,
		}, 3, 3, balloyer.blocks.get(0)); 
		
		//Cluster Mill ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(0),                  wireRudimentary.medium, motor.items.get(0),
		wireRudimentary.medium,  rudimentary.gear,       wireRudimentary.medium,
		motor.items.get(0),                  wireRudimentary.medium, motor.items.get(0),
		}, 3, 3, bcmill.blocks.get(0)); 
		
		//Wiremill ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(0),    motor.items.get(0),                 motor.items.get(0),
		rudimentary.gear, rudimentary.panel,     rudimentary.gear,
		motor.items.get(0),    wireRudimentary.medium, motor.items.get(0),
		}, 3, 3, bwiremill.blocks.get(0)); 
		
		Item splitter = bsplitter.blocks.get(0);
		Item splicer = bsplicer.blocks.get(0);
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
		}, 3, 3, bquarry.blocks.get(0)); 
		
		crafting.addRecipeGrid(new ItemEntry[]{
		splitter,         robot.items.get(0), splicer,
		rudimentary.wire, Electronics.circuit0,           rudimentary.wire,
		splicer,          rudimentary.panel,  splitter,
		}, 3, 3, bassembly.blocks.get(0)); //Machine Assembler ULV
		
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.panel, glassp,     rudimentary.panel,
		rudimentary.panel, rudimentary.panel, rudimentary.panel,
		coal.base,  Agro.seeds,      wireRudimentary.medium,
		}, 3, 3, bbrewery.blocks.get(0)); //Brewery ULV
		
		crafting.addRecipeGrid(new ItemEntry[]{
		null,       motor.items.get(0),     rudimentary.base,
		rudimentary.panel, rudimentary.panel, rudimentary.panel,
		rudimentary.panel, rudimentary.panel, rudimentary.panel,
		}, 3, 3, bdig.blocks.get(0));//Digger ULV
		
		//Battery ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.base, glass, rudimentary.base,
		lead.panel,       paper, lead.panel,
		lead.panel,       paper, lead.panel,
		}, 3, 3, bbattery.blocks.get(0));
		
		//Extruder ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(0),            rudimentary.gear,  rudimentary.panel,
		rudimentary.panel, rudimentary.panel, rudimentary.panel,
		rudimentary.panel, rudimentary.panel, rudimentary.panel,
		}, 3, 3, bextruder.blocks.get(0));
		
		//Power Receiver ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.wire, rudimentary.wire,  rudimentary.wire,
		rudimentary.wire, rudimentary.base, rudimentary.wire,
		rudimentary.wire, rudimentary.wire, rudimentary.wire,
		}, 3, 3, prec.blocks.get(0));
		
		//Power Tower ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		wireRudimentary.medium, wireRudimentary.medium,  wireRudimentary.medium,
		wireRudimentary.medium, prec.blocks.get(0),             wireRudimentary.medium,
		wireRudimentary.medium, wireRudimentary.medium,  wireRudimentary.medium,
		}, 3, 3, ptower.blocks.get(0));
		
		//Inscriber ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(0),            rudimentary.gear,  rudimentary.panel,
		rudimentary.panel, null,              null,
		motor.items.get(0),            rudimentary.gear,  rudimentary.panel,
		}, 3, 3, binscriber.blocks.get(0));
	}
	private static void _craftrsVLV() {
		//Coal Generator VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		iron.cluster, Electronics.circuit1, iron.cluster,
		FURNACE,      FURNACE,  FURNACE,
		iron.cluster, Electronics.circuit1, iron.cluster,
		}, 3, 3, COALGEN2);
		
		//Turbo Generator VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		Electronics.circuit1,    steel.panel, FURNACE,
		steel.panel, steel.frame, steel.panel,
		FURNACE,     steel.panel, Electronics.circuit1,
		}, 3, 3, TURBOGEN1);
		
		//Transformer VLV/ULV
		crafting.addRecipeGrid(new ItemEntry[]{
		null,              iron.base, wireRudimentary.medium,
		wireCopper.medium, iron.base, wireRudimentary.medium,
		null,              iron.base, wireRudimentary.medium,
		}, 3, 3, TransformerData.VLV.type); 
		
		//Electric Furnace VLV
		ItemEntry efurnace1 = efurnace.blocks.get(1);
		crafting.addRecipeGrid(new ItemEntry[]{
		nickel.base, Electronics.resistors_, iron.base,
		nickel.wire, FURNACE,   nickel.wire,
		iron.base,   Electronics.resistors_, nickel.base,
		}, 3, 3, efurnace1);
		
		//Alloyer VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		nickel.base, Electronics.circuit1,  iron.base,
		efurnace1,   efurnace1, efurnace1,
		iron.base,   Electronics.resistors_, nickel.base,
		}, 3, 3, balloyer.blocks.get(1)); 
		
		//Crusher VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		copper.base, Electronics.circuit1, iron.base,
		stone,       wireCopper.medium,    stone,
		iron.base,   motor.items.get(1),   copper.base,
		}, 3, 3, bcrusher.blocks.get(1)); 
		
		//Cluster Mill VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(1),    Electronics.circuit1,   motor.items.get(1),
		iron.gear, iron.panel, iron.gear,
		motor.items.get(1),    Electronics.circuit1,   motor.items.get(1),
		}, 3, 3, bcmill.blocks.get(1));
		
		//WireMill VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(1), Electronics.circuit1, iron.ring,
		iron.gear,          iron.panel,           iron.gear,
		iron.ring,          Electronics.circuit1, motor.items.get(1),
		}, 3, 3, bwiremill.blocks.get(1));
		
		//Quarry VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(1),    iron.gear, motor.items.get(1),
		iron.wire,             stone,     iron.wire,
		motor.items.get(1),    iron.gear, motor.items.get(1),
		}, 3, 3, bquarry.blocks.get(1)); 
		
		//Machine Assembler VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		bsplitter.blocks.get(1), robot.items.get(1),     bcmill.blocks.get(1),
		iron.ring,        Electronics.circuit1,               iron.ring,
		bsplicer.blocks.get(1),  wireRudimentium.medium, balloyer.blocks.get(1),
		}, 3, 3, bassembly.blocks.get(1)); 
		
		//Digger VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		null,       motor.items.get(1), iron.base,
		iron.panel, Electronics.circuit1,           iron.panel,
		iron.panel, iron.panel,         iron.panel,
		}, 3, 3, bdig.blocks.get(1));
		
		//Splitter VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(1), iron.gear, motor.items.get(1),
		iron.wire,          iron.wire, iron.wire,
		motor.items.get(1), Electronics.circuit1,  motor.items.get(1),
		}, 3, 3, bsplitter.blocks.get(1)); 
		
		//Splicer VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(1), iron.gear,   motor.items.get(1),
		iron.wire,          iron.nugget, iron.wire,
		motor.items.get(1), Electronics.circuit1,    motor.items.get(1),
		}, 3, 3, bsplicer.blocks.get(1)); 
		
		//Battery VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentium.base, glass, rudimentium.base,
		nickel.panel,     paper, nickel.panel,
		nickel.panel,     paper, nickel.panel,
		}, 3, 3, bbattery.blocks.get(1));
		
		//Extruder VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(1), iron.gear,  iron.panel,
		iron.panel,         iron.panel, iron.panel,
		iron.panel,         Electronics.circuit1,   iron.panel,
		}, 3, 3, bextruder.blocks.get(1));
		
		//Power Receiver VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		copper.wire, copper.wire, copper.wire,
		copper.wire, iron.base,   copper.wire,
		copper.wire, copper.wire, copper.wire,
		}, 3, 3, prec.blocks.get(1));
		
		//Power Tower VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		wireCopper.medium, wireCopper.medium,  wireCopper.medium,
		wireCopper.medium, prec.blocks.get(1), wireCopper.medium,
		wireCopper.medium, wireCopper.medium,  wireCopper.medium,
		}, 3, 3, ptower.blocks.get(1));
		
		//Inscriber VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(1), iron.gear,  iron.panel,
		iron.panel,         null,       null,
		motor.items.get(1), iron.gear,  iron.panel,
		}, 3, 3, binscriber.blocks.get(1));
		
		//Brewery VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		iron.panel,  glassp,     iron.panel,
		iron.panel,  iron.panel, iron.panel,
		nickel.base, Agro.seeds,      wireCopper.medium,
		}, 3, 3, bbrewery.blocks.get(1));
	}
	private static void _craftrsLV() {
		//Turbo Generator LV
		crafting.addRecipeGrid(new ItemEntry[]{
		TURBOGEN1,       stainless.panel, TURBOGEN1,
		stainless.panel, stainless.frame, stainless.panel,
		Electronics.circuit2,        stainless.panel, Electronics.circuit2,
		}, 3, 3, TURBOGEN2);
		
		//Coal Generator LV
		crafting.addRecipeGrid(new ItemEntry[]{
		steel.cluster, Electronics.circuit2, steel.cluster,
		motor.items.get(1),      FURNACE,  motor.items.get(1),
		steel.cluster, Electronics.circuit2, steel.cluster,
		}, 3, 3, COALGEN3);
		
		//Transformer LV/VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		null,              steel.base, wireCopper.medium,
		wireSilver.medium, steel.base, wireCopper.medium,
		null,              steel.base, wireCopper.medium,
		}, 3, 3, TransformerData.LV.type); 
		
		ItemEntry efurnace1 = efurnace.blocks.get(2);
		crafting.addRecipeGrid(new ItemEntry[]{
		nichrome.base, Electronics.resistors_,  steel.base,
		nickel.wire,   steel.frame, nickel.wire,
		steel.base,    Electronics.resistors_,   nichrome.base,
		}, 3, 3, efurnace1); //Electric Furnace LV
		
		//Battery LV
		crafting.addRecipeGrid(new ItemEntry[]{
		silver.base,  glass,   silver.base,
		nickel.frame, PE.foil, lead.panel,
		lead.panel,   PE.foil, nickel.frame,
		}, 3, 3, bbattery.blocks.get(2));
		
		//Alloyer LV
		crafting.addRecipeGrid(new ItemEntry[]{
		nichrome.base,  Electronics.circuit2, steel.base,
		efurnace1, efurnace1, efurnace1,
		steel.base, steel.frame, nichrome.base,
		}, 3, 3, balloyer.blocks.get(2));
		
		//Crusher LV
		crafting.addRecipeGrid(new ItemEntry[]{
		steel.base,  Electronics.circuit2,          steel.base,
		cobalt.gear, wireSilver.medium, cobalt.gear,
		steel.base,  Electronics.resistors_,         steel.base,
		}, 3, 3, bcrusher.blocks.get(2)); 
		
		//Cluster Mill LV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(2),  Electronics.circuit2,    motor.items.get(2),
		steel.gear,          steel.panel, steel.gear,
		motor.items.get(2),  Electronics.circuit2,    motor.items.get(2),
		}, 3, 3, bcmill.blocks.get(2));
		
		//WireMill VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(2), Electronics.circuit2,    steel.ring,
		steel.gear,         steel.panel, steel.gear,
		steel.ring,         Electronics.circuit1,    motor.items.get(2),
		}, 3, 3, bwiremill.blocks.get(2));
		
		//Quarry VLV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(2), steel.gear, motor.items.get(2),
		steel.wire,         Electronics.circuit2,   steel.wire,
		motor.items.get(2), steel.gear, motor.items.get(2),
		}, 3, 3, bquarry.blocks.get(2));
		
		//Splitter LV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(2), steel.gear, motor.items.get(2),
		steel.wire,         steel.wire, steel.wire,
		motor.items.get(2), Electronics.circuit2,   motor.items.get(2),
		}, 3, 3, bsplitter.blocks.get(2));
		
		//Splicer LV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(2), steel.gear,   motor.items.get(2),
		steel.wire,         steel.nugget, steel.wire,
		motor.items.get(2), Electronics.circuit2,     motor.items.get(2),
		}, 3, 3, bsplicer.blocks.get(2));
		
		//Machine Assembler LV
		crafting.addRecipeGrid(new ItemEntry[]{
		bsplitter.blocks.get(2),   bsplicer.blocks.get(2), bcmill.blocks.get(2),     bcrusher.blocks.get(2),
		robot.items.get(2), nichrome.wire,   nichrome.wire,     robot.items.get(2),
		steel.frame,        steel.panel,     steel.panel,       steel.frame,
		bsplicer.blocks.get(2),    balloyer.blocks.get(2), binscriber.blocks.get(2), bquarry.blocks.get(2)
		}, 4, 4, bassembly.blocks.get(2));
		
		//Digger LV
		crafting.addRecipeGrid(new ItemEntry[]{
		null,        motor.items.get(2), steel.base,
		steel.panel, Electronics.circuit2,           steel.panel,
		steel.panel, steel.panel,        steel.panel,
		}, 3, 3, bdig.blocks.get(2));
		
		//Power Receiver LV
		crafting.addRecipeGrid(new ItemEntry[]{
		silver.wire, silver.wire, silver.wire,
		silver.wire, steel.base,  silver.wire,
		silver.wire, silver.wire, silver.wire,
		}, 3, 3, prec.blocks.get(2));
		
		//Power Tower LV
		crafting.addRecipeGrid(new ItemEntry[]{
		wireSilver.medium, wireSilver.medium, wireSilver.medium,
		wireSilver.medium, prec.blocks.get(2),       wireSilver.medium,
		wireSilver.medium, wireSilver.medium, wireSilver.medium,
		}, 3, 3, ptower.blocks.get(2));
		
		//Inscriber LV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(2),  steel.gear,  steel.panel,
		steel.panel,         null,        null,
		motor.items.get(2),  steel.gear,  steel.panel,
		}, 3, 3, binscriber.blocks.get(2));
		
		//Brewery LV
		crafting.addRecipeGrid(new ItemEntry[]{
		steel.panel,   glassp,      steel.panel,
		steel.panel,   steel.panel, steel.panel,
		nichrome.base, Agro.seeds,       wireSilver.medium,
		}, 3, 3, bbrewery.blocks.get(2));
		
		//Extruder LV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(2), steel.gear,  steel.panel,
		steel.panel,        steel.panel, steel.panel,
		steel.panel,        Electronics.circuit2,    steel.panel,
		}, 3, 3, bextruder.blocks.get(2));
		
		//Sinterer LV
		crafting.addRecipeGrid(new ItemEntry[]{
		motor.items.get(2), steel.panel, steel.panel,
		silver.ring,        coal.wire,   steel.panel,
		motor.items.get(2), Electronics.circuit2,    steel.panel,
		}, 3, 3, bsinterer.blocks.get(2));
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
			new RandomChance(0.2, redstone.ore)
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
		
		//Tier 6: +draconic shards, crystalline shards
		quarry.add(resrc5, resrc4, 2, VoltageTier.V6, 20480000, new ListChance(
			new RandomChance(0.1, sdraconium),
			new RandomChance(0.1, scrystal)
		));
		
		//Tier 7: +awakened draconic shards, stellar shards
		quarry.add(resrc6, resrc5, 2, VoltageTier.V7, 81920000, new ListChance(
			new RandomChance(0.1, sadraconium),
			new RandomChance(0.1, sstellar)
		));
		
		//Tier 8: +chaotic shards, unobtainium shards
		quarry.add(resrc7, resrc6, 2, VoltageTier.V8, 327680000, new ListChance(
			new RandomChance(0.1, schaotium),
			new RandomChance(0.1, sunobtainium)
		));
		
		//Basic Resource Bed
		alloyer.add(new SimpleItemList(redstone.base.stack(2), rudimentary.base.stack(2)), resrc1, 2, VoltageTier.V2, 180000);
		//Enhanced Resource Bed
		alloyer.add(new SimpleItemList(glowstone.base.stack(2), silver.base.stack(2)), resrc2, 4, VoltageTier.V3, 720000);
		//Refined Resource Bed
		alloyer.add(new SimpleItemList(ender.base.stack(2), gold.base.stack(2)), resrc3, 4, VoltageTier.V3, 2880000);
		//Advanced Resource Bed
		alloyer.add(new SimpleItemList(diamond.base.stack(2), platinum.base.stack(2)), resrc4, 4, VoltageTier.V4, 11520000);
		//Extreme Resource Bed
		alloyer.add(new SimpleItemList(diamide.base.stack(2), iridium.base.stack(2)), resrc5, 4, VoltageTier.V5, 46080000);
		//Insane Resource Bed
		alloyer.add(new SimpleItemList(unamide.base.stack(2), draconium.base.stack(2)), resrc6, 4, VoltageTier.V6, 184320000);
		//Ludicrous Resource Bed
		alloyer.add(new SimpleItemList(omnamide.base.stack(2), adraconium.base.stack(2)), resrc7, 4, VoltageTier.V7, 737280000);
		
		//Quarry conversions
		alloyer.add(new SimpleItemList(sdraconium, iridium.base, neodymium.base, enderium.base), draconium.base, VoltageTier.V6, 80_000_000);
		alloyer.add(new SimpleItemList(sadraconium, crystal.base, draconium.base), adraconium.base, VoltageTier.V7, 320_000_000);
		alloyer.add(new SimpleItemList(schaotium, stellar.base, adraconium.base), chaotium.base, VoltageTier.V8, 1280_000_000);
		alloyer.add(new SimpleItemList(scrystal, iridium.base, enderium.base), crystal.base, VoltageTier.V5, 30_000_000);
		alloyer.add(new SimpleItemList(sstellar, draconium.base), stellar.base, VoltageTier.V6, 120_000_000);
		alloyer.add(new SimpleItemList(sunobtainium, adraconium.base), unobtainium.base, VoltageTier.V7, 480_000_000);
		
		//simple stone regeneration recipe
		alloyer.add(new SimpleItemList(stone, rudimentary.nugget), stone, 32, VoltageTier.V1, 80000);
		
		//sifting seeds sometimes gives yeast
		quarry.add(Agro.seeds, RecipeOutput.NONE, VoltageTier.V1, 40000, new RandomOrElseChance(0.05, Agro.yeast, Agro.seeds));
	}
	private static void _upgrade() {
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.base, coal.base,
		coal.base, rudimentary.base
		}, 2, 2, speed1);
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.base, rudimentary.base, rudimentary.base,
		rudimentary.base,        coal.base, rudimentary.base,
		rudimentary.base, rudimentary.base, rudimentary.base,
		}, 3, 3, speed2);
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentary.base, rudimentary.base,  rudimentary.base,
		alu.base,         rudimentary.panel, alu.base,
		alu.base,         alu.base,          alu.base,
		}, 3, 3, speed3);
		crafting.addRecipeGrid(new ItemEntry[]{
		alu.sheet, alu.sheet, alu.sheet,
		iron.base, Electronics.circuit0,  iron.base,
		iron.base, iron.base, iron.base
		}, 3, 3, speed4);
		crafting.addRecipeGrid(new ItemEntry[]{
		rudimentium.base, copper.base, rudimentium.base,
		rudimentium.base, Electronics.circuit0, rudimentium.base,
		rudimentium.base, rudimentium.base, rudimentium.base
		}, 3, 3, speed5);
		crafting.addRecipeGrid(new ItemEntry[]{
		silver.base,      copper.base,      silver.base,
		rudimentium.base, Electronics.circuit0,         rudimentium.base,
		rudimentium.base, rudimentium.base, rudimentium.base
		}, 3, 3, speed6);
		crafting.addRecipeGrid(new ItemEntry[]{
		silver.base, silver.base, silver.base,
		steel.base,  Electronics.circuit0,    steel.base,
		steel.base,  steel.base,  steel.base
		}, 3, 3, speed7);
		crafting.addRecipeGrid(new ItemEntry[]{
		silver.base, silver.base, silver.base,
		steel.base,  Electronics.circuit1,    steel.base,
		steel.base,  steel.base,  steel.base
		}, 3, 3, speed8);
		crafting.addRecipeGrid(new ItemEntry[]{
		silver.base, gold.base, silver.base,
		steel.base,  Electronics.circuit1,   steel.base,
		steel.base,  steel.base, steel.base
		}, 3, 3, speed9);
		crafting.addRecipeGrid(new ItemEntry[]{
		gold.base,  silver.base, gold.base,
		steel.base, Electronics.circuit1,    steel.base,
		steel.base, steel.base,  steel.base
		}, 3, 3, speed10);
		crafting.addRecipeGrid(new ItemEntry[]{
		gold.base,      gold.base,      gold.base,
		steel.base,     Electronics.circuit1,       steel.base,
		stainless.base, stainless.base, stainless.base
		}, 3, 3, speed11);
		crafting.addRecipeGrid(new ItemEntry[]{
		gold.base,      gold.base,      gold.base,
		steel.base,     Electronics.circuit2,       steel.base,
		stainless.base, stainless.base, stainless.base
		}, 3, 3, speed12);
	}

}
