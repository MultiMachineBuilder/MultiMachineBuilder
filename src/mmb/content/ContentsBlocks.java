/**
 * 
 */
package mmb.content;

import java.awt.image.BufferedImage;

import mmb.DeprecatedExtra;
import mmb.NN;
import mmb.content.craft.ManCrafter;
import mmb.content.electric.ElectricMachineGroup;
import mmb.content.electric.InfiniteGenerator;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.machines.BlockBattery;
import mmb.content.electric.machines.BlockDigger;
import mmb.content.electric.machines.BlockGeneratorSolid;
import mmb.content.electric.machines.BlockPowerReceiver;
import mmb.content.electric.machines.BlockPowerTower;
import mmb.content.electric.machines.PowerLoad;
import mmb.content.electric.machines.PowerMeter;
import mmb.content.electric.machines.ProcessorComplexBlock;
import mmb.content.electric.machines.ProcessorComplexCatalyzedBlock;
import mmb.content.electric.machines.ProcessorSimpleBlock;
import mmb.content.electric.machines.ProcessorSimpleCatalyzedBlock;
import mmb.content.electric.recipes.ComplexCatRecipeGroup;
import mmb.content.electric.recipes.ComplexRecipeGroup;
import mmb.content.electric.recipes.SimpleRecipeGroup;
import mmb.content.imachine.PlaceIncomingItems;
import mmb.content.imachine.TrashCan;
import mmb.content.imachine.bom.BOMFactory;
import mmb.content.imachine.chest.Chest;
import mmb.content.imachine.chest.Hopper;
import mmb.content.imachine.craft.Crafter;
import mmb.content.imachine.extractor.BlockCollector;
import mmb.content.imachine.pipe.DualPipe;
import mmb.content.imachine.pipe.IntersectingPipeExtractor;
import mmb.content.imachine.pipe.ItemTransporter;
import mmb.content.imachine.pipe.Pipe;
import mmb.content.imachine.pipe.PipeBinder;
import mmb.content.imachine.pipe.PipeFilter;
import mmb.content.machinemics.line.Furnace;
import mmb.content.media.Speaker;
import mmb.content.old.AutoCrafter;
import mmb.content.old.CycleAssembler;
import mmb.content.old.FurnacePlus;
import mmb.content.old.Nuker;
import mmb.content.ppipe.JoiningPlayerPipe;
import mmb.content.ppipe.PlayerPipe;
import mmb.content.ppipe.PlayerPipeEntry;
import mmb.content.ppipe.TwinPlayerPipe;
import mmb.content.wireworld.BlockButton;
import mmb.content.wireworld.EmitTrue;
import mmb.content.wireworld.EmitUniformRandom;
import mmb.content.wireworld.Lamp;
import mmb.content.wireworld.OffToggle;
import mmb.content.wireworld.OnToggle;
import mmb.content.wireworld.EmitRandom;
import mmb.content.wireworld.WWHead;
import mmb.content.wireworld.WWTail;
import mmb.content.wireworld.WWWire;
import mmb.content.wireworld.WorldLoadDetector;
import mmb.content.wireworld.GateState.StateGateType;
import mmb.content.wireworld.GateBi.BiGateType;
import mmb.content.wireworld.GateMono.MonoGateType;
import mmb.content.wireworld.actuator.ActuatorClick;
import mmb.content.wireworld.actuator.ActuatorPlaceBlock;
import mmb.content.wireworld.actuator.ActuatorRotations;
import mmb.content.wireworld.text.TextChatter;
import mmb.engine.block.Block;
import mmb.engine.block.BlockEntityType;
import mmb.engine.block.Blocks;
import mmb.engine.item.Items;
import mmb.engine.rotate.ChirotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.texture.Textures;

/**
 * A comprehensive (but not complete) collection of blocks in the MultiMachineBuilder.
 * 
 * Some entries are arrays, {@link ElectricMachineGroup}s, and others.
 * @author oskar
 */
public class ContentsBlocks {
	private ContentsBlocks() {}
	
	//Simple blocks
	/** A block of wood planks */
	@NN public static final Block plank = new Block()
			.texture("plank.png")
			.title("#planks")
			.finish("mmb.planks");
	/** Hosts ores */
	@NN public static final Block stone = new Block()
			.texture("stone.png")
			.title("#stone")
			.finish("mmb.stone");
	/** A basic ingredient of crops */
	@NN public static final Block leaves = new Block()
			.texture("block/leaves.png")
			.title("#leaves")
			.finish("mmb.leaves");
	/** Decorative, unused */
	@NN public static final Block asmtb = new Block()
			.texture("crafting.png")
			.title("#asmtb")
			.finish("mmb.craft");
	/** The main wood source */
	@NN public static final Block logs = new Block()
			.texture("log.png")
			.title("#logs")
			.finish("mmb.tree");
	/** A very fine dust block, smeltable to glass */
	@NN public static final Block sand = new Block()
			.texture("block/sand.png")
			.title("#sand")
			.finish("mmb.sand");
	/** A coarse grain block, grindable to sand */
	@NN public static final Block gravel = new Block()
			.texture("block/gravel.png")
			.title("#gravel")
			.finish("mmb.gravel");
	/** Currently unused */
	@NN public static final Block clay = new Block()
	.texture("block/clay.png")
	.title("#clay")
	.finish("mmb.clay");
	
	//WireWorld wires
	/**
	 * A WireWorld cell.
	 * This block turns into head if it is provided with 1 or 2 signals in 8 block neighborhood.
	 */
	@NN public static final BlockEntityType ww_wire = new BlockEntityType()
		.texture("logic/wire.png")
		.title("#ww-c")
		.factory(WWWire::new)
		.finish("wireworld.wire");
	/** This block emits a signal, and it turns into a tail */
	@NN public static final BlockEntityType ww_head = new BlockEntityType()
		.texture("logic/head.png")
		.title("#ww-h")
		.factory(WWHead::new)
		.leaveBehind(ww_wire)
		.finish("wireworld.head");
	/** This block is left by a head and it turns into wire. This block does not emit any signal. */
	@NN public static final BlockEntityType ww_tail = new BlockEntityType()
		.texture("logic/tail.png")
		.title("#ww-t")
		.factory(WWTail::new)
		.leaveBehind(ww_wire)
		.finish("wireworld.tail");
	
	//Emitters
	/** Generates a single random signal for every neighbor */
	@NN public static final BlockEntityType URANDOM = new BlockEntityType()
			.title("#ww-ur")
			.factory(EmitUniformRandom::new)
			.texture("logic/urandom.png")
			.finish("wireworld.urandom");
	/** Always provides 'true' signal */
 	@NN public static final Block TRUE = new EmitTrue()
			.title("#ww-true")
			.texture("logic/true.png")
			.finish("wireworld.true");
	/** Generates a separate random signal for every neighbor */
	@NN public static final Block RANDOM = new EmitRandom()
			.texture("logic/random.png")
			.title("#ww-rnd")
			.finish("wireworld.random");
	/** Generates a signal when the world is loaded */
	@NN public static final BlockEntityType ONLOAD = new BlockEntityType()
			.factory(WorldLoadDetector::new)
			.texture("logic/loadsensor.png")
			.title("#ww-load")
			.finish("wireworld.onload");
	
	//WireWorld output devices
	/** This block logs specified message if it is activated by a gate, wire or other signal source */
	@NN public static final BlockEntityType ww_chatter = new BlockEntityType()
		.texture("printer.png")
		.title("#ww-chat")
		.factory(TextChatter::new)
		.describe("A block which prints out its text, when activated by a signal")
		.finish("wireworld.chatter");
	/** This block displays the logic inputs to itself*/
	@NN public static final BlockEntityType LAMP = new BlockEntityType()
			.title("#ww-lamp")
			.factory(Lamp::new)
			.texture("logic/off lamp.png")
			.finish("wireworld.lamp");
	
	//Logic gates
	/** This block receives signals from DR and DR corners and outputs a signal if both inputs are active */
	@NN public static final BlockEntityType AND = new BiGateType("logic/AND.png", (a, b) -> a && b)
		.title("#ww-A")
		.finish("wireworld.and");
	/** This block receives signals from DR and DR corners and outputs a signal if any input is active */
	@NN public static final BlockEntityType OR = new BiGateType("logic/OR.png", (a, b) -> a || b)
		.title("#ww-O")
		.finish("wireworld.or");
	/** This block receives signals from DR and DR corners and outputs a signal if only one input are active */
	@NN public static final BlockEntityType XOR = new BiGateType("logic/XOR.png", (a, b) -> a ^ b)
		.title("#ww-X")
		.finish("wireworld.xor");
	/** This block emits a pulse when pressed by a player or Block Clicking Claw */
	@NN public static final BlockEntityType BUTTON = new BlockEntityType()
			.title("#ww-btn")
			.factory(BlockButton::new)
			.texture("logic/button.png")
			.finish("wireworld.button");
	/** This block toggles state when clicked by a player or Block Clicking Claw */
	@NN public static final BlockEntityType TOGGLE = new StateGateType("logic/toggle on.png","logic/toggle off.png","logic/toggle inert.png",(a,s) -> {
		boolean s2 = s ^ a;
		return (byte) (s2?3:0);
	})
		.title("#ww-tgl")
		.finish("wireworld.toggle");
	/** Recreates the provided signal*/
	@NN public static final BlockEntityType YES = new MonoGateType("logic/YES.png", a -> a)
			.title("#ww-Y")
			.finish("wireworld.yes");
	/** Negates provided signal */
	@NN public static final BlockEntityType NOT = new MonoGateType("logic/NOT.png", a -> !a)
			.title("#ww-N")
			.finish("wireworld.not");
	/**
	 * Sends a random signal if powered
	 * Else does not send signal
	 */
	@NN public static final BlockEntityType RANDOMCTRL= new MonoGateType("logic/randomctrl.png", a -> a && Math.random() < 0.5)
			.title("#ww-rctr")
			.finish("wireworld.randomctrl");
	
	//Switches
	/** Enabled switch */
	@NN public static final Block ON = new OnToggle()
			.texture("logic/on.png")
			.title("#ww-on")
			.finish("wireworld.on");
	/** Disabled switch */
	@NN public static final Block OFF = new OffToggle()
			.texture("logic/off.png")
			.title("#ww-off")
			.finish("wireworld.off");
	
	//WireWorld machines
	/** Creative block placement, unobtainable in survival */
	@NN public static final BlockEntityType PLACER = new BlockEntityType()
			.title("#ww-placer")
			.factory(ActuatorPlaceBlock::new)
			.texture("machine/placer.png")
			.finish("machines.placer");
	/** Block Clicking Claw, auto-enables blocks */
	@NN public static final BlockEntityType CLICKER = new BlockEntityType()
			.title("#ww-click")
			.factory(ActuatorClick::new)
			.texture("machine/claw.png")
			.finish("machines.clicker");
	/** Block Rotator, rotates blocks */
	@NN public static final BlockEntityType ROTATOR = new BlockEntityType()
			.title("#ww-rot")
			.factory(ActuatorRotations::new)
			.texture("machine/CW.png")
			.finish("machines.rotator");

	//Power generators
	/** Basic ULV generator */
	@NN public static final BlockEntityType COALGEN1 = coalgen(1, VoltageTier.V1, BlockGeneratorSolid.img, 1);
	/** Basic VLV genertor */
	@NN public static final BlockEntityType COALGEN2 = coalgen(1, VoltageTier.V2, BlockGeneratorSolid.img1, 2);
	/** Basic LV gnerator */
	@NN public static final BlockEntityType COALGEN3 = coalgen(1, VoltageTier.V3, BlockGeneratorSolid.img2, 3);
	/** Advanced VLV generator*/
	@NN public static final BlockEntityType TURBOGEN1 = coalgen(2, VoltageTier.V2, BlockGeneratorSolid.turboimg, 1);
	/** Advanced LV generator */
	@NN public static final BlockEntityType TURBOGEN2 = coalgen(2, VoltageTier.V3, BlockGeneratorSolid.turboimg1, 2);
	/** Basic MV generator */
	@NN public static final BlockEntityType TURBOGEN3 = coalgen(2, VoltageTier.V4, BlockGeneratorSolid.turboimg2, 3);
	
	//Electrical testing equipment
	/** A series of 9 infinite power generators. Intended for machine testing.*/
	@NN public static final ElectricMachineGroup infinigens =
			new ElectricMachineGroup(Textures.get("machine/power/infinity.png"), InfiniteGenerator::new, "infinigen");
	/** Measures power of machines */
	@NN public static final BlockEntityType PMETER = new BlockEntityType()
			.title("#elec-meter")
			.factory(PowerMeter::new)
			.texture("machine/power/pmeter.png")
			.finish("elec.meter");
	/** Consumes specified amount of electricity. Intended for generator testing */
	@NN public static final BlockEntityType LOAD = new BlockEntityType()
			.title("#elec-load")
			.factory(PowerLoad::new)
			.texture("machine/power/load.png")
			.finish("elec.load");
	
	//Chests
	/** Stores 6 m³ of items*/
	@NN public static final BlockEntityType CHEST = chest(6, "machine/chest1.png", "chest.beginning", 1);
	/** Stores 16 m³ of items*/
	@NN public static final BlockEntityType CHEST1 = chest(16, "machine/chest2.png", "chest.simple", 2);
	/** Stores 32 m³ of items*/
	@NN public static final BlockEntityType CHEST2 = chest(32, "machine/chest3.png", "chest.intermediate", 3);
	/** Stores 64 m³ of items*/
	@NN public static final BlockEntityType CHEST3 = chest(64, "machine/chest4.png", "chest.advanced", 4);
	/** Stores 128 m³ of items*/
	@NN public static final BlockEntityType CHEST4 = chest(128, "machine/chest5.png", "chest.extreme", 5);
	/** Stores 256 m³ of items*/
	@NN public static final BlockEntityType CHEST5 = chest(256, "machine/chest6.png", "chest.ultimate", 6);
	/** Stores 1024 m³ of items*/
	@NN public static final BlockEntityType CHEST6 = chest(1024, "machine/chest7.png", "chest.penultimate", 7);
	/** Stores 4096 m³ of items*/
	@NN public static final BlockEntityType CHEST7 = chest(4096, "machine/chest8.png", "chest.penultimate2", 8);
	/** Stores 16384 m³ of items*/
	@NN public static final BlockEntityType CHEST8 = chest(16384, "machine/chest9.png", "chest.penultimate3", 9);
	/** A chest that auto-inserts items */
	@NN public static final BlockEntityType HOPPER = new BlockEntityType()
			.title("#chest-hopper")
			.factory(() -> new Hopper((byte) 1))
			.texture("machine/hopper.png")
			.finish("chest.hopper1");
	/** A chest thatt auto-extracts items */
	@NN public static final BlockEntityType HOPPER_suck = new BlockEntityType()
			.title("#chest-sucker")
			.factory(() -> new Hopper((byte) 2))
			.texture("machine/sucker.png")
			.finish("chest.hopper2");
	/**  A chest that both auto-inserts and auto-extracts items */
	@NN public static final BlockEntityType HOPPER_both = new BlockEntityType()
			.title("#chest-both")
			.factory(() -> new Hopper((byte) 3))
			.texture("machine/transferrer.png")
			.finish("chest.hopper3");
	/** Item extractor */
	@NN public static final BlockEntityType IMOVER = new BlockEntityType()
			.title("#ipipe-extractor")
			.factory(ItemTransporter::new)
			.texture(ItemTransporter.TEXTURE)
			.finish("itemsystem.mover");
	/** Places incoming items */
	@NN public static final BlockEntityType PLACEITEMS = new BlockEntityType()
			.title("#placeitems")
			.factory(PlaceIncomingItems::new)
			.texture("machine/block place interface.png")
			.finish("industry.placeitems");
	/** Wide area block collector */
	@NN public static final BlockEntityType COLLECTOR = new BlockEntityType()
			.title("#collector")
			.factory(BlockCollector::new)
			.texture("machine/vacuum.png")
			.finish("industry.collector");
	@NN public static final Block TRASH = new TrashCan()
			.texture("machine/trash.png")
			.title("#trashcan")
			.finish("imachine.trash");
	@NN public static final BlockEntityType BOMMAKER = new BlockEntityType()
			.title("#bommaker")
			.factory(BOMFactory::new)
			.texture("machine/BOMer.png")
			.finish("imachine.bom");
	
	//Item pipes
	/** Represets a straight pipe */
	@NN public static final BlockEntityType ipipe_STRAIGHT;
	/** Represents an elbow pipe */
	@NN public static final BlockEntityType ipipe_ELBOW;
	/** An Intersecting Pipe Extractor has a perpendicular straight pipe */
	@NN public static final BlockEntityType ipipe_IPE;
	/** An item pipe which connects from the left*/
	@NN public static final BlockEntityType ipipe_TOLEFT;
	/** An item pipe which connects from the right*/
	@NN public static final BlockEntityType ipipe_TORIGHT;
	/** A pair of intersecting item pipes*/
	@NN public static final BlockEntityType ipipe_CROSS;
	/** A pair of non-intersection bend pipes*/
	@NN public static final BlockEntityType ipipe_DUALTURN;
	/** A pair of non-intersection bend pipes*/
	@NN public static final BlockEntityType ipipe_FILTER;
	//Init all item pipes
	static {
		ChirotatedImageGroup textureStraight = ChirotatedImageGroup.create("machine/pipe straight.png");
		ipipe_STRAIGHT = new BlockEntityType()
		.texture("machine/pipe straight.png")
		.describe("A pipe which is straight.")
		.title("#ipipe-straight")
		.factory(() -> new Pipe(Side.R, Side.L, ContentsBlocks.ipipe_STRAIGHT, textureStraight))
		.finish("pipe.I");
		
		ChirotatedImageGroup textureElbow = ChirotatedImageGroup.create("machine/pipe elbow.png");
		ipipe_ELBOW = new BlockEntityType()
		.texture("machine/pipe elbow.png")
		.describe("A pipe which is bent.")
		.title("#ipipe-turn")
		.factory(() -> new Pipe(Side.R, Side.D, ContentsBlocks.ipipe_ELBOW, textureElbow))
		.finish("pipe.L");
		
		ChirotatedImageGroup textureIPE = ChirotatedImageGroup.create("machine/imover intersected.png");
		ipipe_IPE = new BlockEntityType()
		.texture("machine/imover intersected.png")
		.describe("A hybrid of Item Mover and straight pipe")
		.title("#ipipe-ipe")
		.factory(() -> new IntersectingPipeExtractor(Side.R, Side.L, ContentsBlocks.ipipe_IPE, textureIPE))
		.finish("pipe.IPE");
		
		ChirotatedImageGroup textureToLeft = ChirotatedImageGroup.create("machine/pipe merge left.png");
		ipipe_TOLEFT = new BlockEntityType()
		.texture("machine/pipe merge left.png")
		.describe("A pipe which directs its side input to the left.")
		.title("#ipipe-left")
		.factory(() -> new PipeBinder(ContentsBlocks.ipipe_TOLEFT, Side.L, textureToLeft))
		.finish("pipe.toleft");
		
		ChirotatedImageGroup textureToRight = ChirotatedImageGroup.create("machine/pipe merge right.png");
		ipipe_TORIGHT = new BlockEntityType()
		.texture("machine/pipe merge right.png")
		.describe("A pipe which directs its side input to the right.")
		.title("#ipipe-right")
		.factory(() -> new PipeBinder(ContentsBlocks.ipipe_TORIGHT, Side.R, textureToRight))
		.finish("pipe.toright");
		
		ChirotatedImageGroup textureCross = ChirotatedImageGroup.create("machine/pipe bridged.png");
		ipipe_CROSS = new BlockEntityType()
		.texture("machine/pipe bridged.png")
		.describe("A pair of disconnected perpendicular pipes.")
		.title("#ipipe-cross")
		.factory(() -> new DualPipe(Side.D, Side.R, ContentsBlocks.ipipe_CROSS, textureCross))
		.finish("pipe.X");
		
		ChirotatedImageGroup textureDualTurn = ChirotatedImageGroup.create("machine/pipe biturn.png");
		ipipe_DUALTURN = new BlockEntityType()
		.texture("machine/pipe biturn.png")
		.describe("A pair of curved pipes.")
		.title("#ipipe-dual")
		.factory(() -> new DualPipe(Side.R, Side.D, ContentsBlocks.ipipe_DUALTURN, textureDualTurn))
		.finish("pipe.D");
		
		ChirotatedImageGroup textureFilter = ChirotatedImageGroup.create("machine/filter pipe.png");
		ipipe_FILTER = new BlockEntityType()
		.texture("machine/filter pipe.png")
		.describe("A filtering pipe")
		.title("#ipipe-filter")
		.factory(() -> new PipeFilter(ContentsBlocks.ipipe_FILTER, textureFilter))
		.finish("pipe.F");
	}
	
	//Liquids
	/** The most basic fluid, used to make beer */
	@NN public static final Block water = new Block()
	.texture("liquid/water.png")
	.title("#water")
	.finish("liquid.water");
	/** Currently unused */
	@NN public static final Block lava = new Block()
	.texture("liquid/lava.png")
	.title("#lava")
	.finish("liquid.lava");
	/** A gas created by boiling water, currently unused */
	@NN public static final Block steam = new Block()
	.texture("liquid/steam.png")
	.title("#steam")
	.finish("liquid.steam");
	
	//Non-electic processing machines
	/** A basic, non-electric furnace capable of ULV recipes */
	@NN public static final BlockEntityType FURNACE = new BlockEntityType()
			.title("#furnace")
			.factory(Furnace::new)
			.texture(Furnace.TEXTURE_INERT)
			.finish("industry.furnace");
	@NN public static final BlockEntityType AUTOCRAFTER1 = createAutoCrafter(1, 64);
	@NN public static final BlockEntityType AUTOCRAFTER2 = createAutoCrafter(2, 16);
	@NN public static final BlockEntityType AUTOCRAFTER3 = createAutoCrafter(3, 4);
	@NN public static final BlockEntityType AUTOCRAFTER4 = createAutoCrafter(4, 1);
	/** The Timed Ingredient Packet Dispatcher */
	@NN public static final BlockEntityType TIPD = new BlockEntityType()
			.title("#TIPD")
			.factory(mmb.content.imachine.tipd.TIPD::new)
			.texture("machine/rationer.png")
			.finish("industry.TIPD");
	
	private static BlockEntityType createAutoCrafter(int index, int delay) {
		BlockEntityType result = new BlockEntityType();
		result	.title(GlobalSettings.$res("autocrafter")+" "+index)
				.factory(() -> new Crafter(delay, result))
				.texture("machine/autocraft"+index+".png")
				.describe(GlobalSettings.$res("craftevery")+" "+delay+" "+GlobalSettings.$res("ticks"))
				.finish("industry.autocrafter"+index);
		return result;
	}
	
	//Electrical processing machines
	/** Smelts ores and dusts into materials */
	@NN public static final ElectricMachineGroup efurnace = machinesSimple("machine/electrosmelter.png", CraftingGroups.smelting, "electrofurnace");
	/** Recovers materials from parts and ores */
	@NN public static final ElectricMachineGroup bcrusher = machinesSimple("machine/pulverizer.png", CraftingGroups.crusher, "crusher");
	/** Mills nuggets, fragments and ingots into foils, sheets and panels respectively */
	@NN public static final ElectricMachineGroup bcmill = machinesSimple("machine/cluster mill.png", CraftingGroups.clusterMill, "clustermill");
	/** Mills nuggets, fragments, ingots and clusters into wires */
	@NN public static final ElectricMachineGroup bwiremill = machinesSimple("machine/wiremill.png", CraftingGroups.wiremill, "wiremill");
	/** Combines two or more metals */
	@NN public static final ElectricMachineGroup balloyer = machinesComplex("machine/alloyer.png", CraftingGroups.alloyer, "alloyer");
	/** Builds advanced machines and components from parts and materials */
	@NN public static final ElectricMachineGroup bassembly = machinesComplexCat("machine/machinemaker.png", CraftingGroups.assembler, "assembler");
	/** Splits coarse materials into fine materials */
	@NN public static final ElectricMachineGroup bsplitter = machinesSimple("machine/splitter.png", CraftingGroups.splitter, "spllitter", 0.1);
	/** Combines fine materials into coarse materials */
	@NN public static final ElectricMachineGroup bsplicer = machinesSimple("machine/splicer.png", CraftingGroups.combiner, "splicer", 0.1);
	/** Produces beer */
	@NN public static final ElectricMachineGroup bbrewery = machinesComplex("machine/brewery.png", CraftingGroups.brewery, "brewery");
	/** Produces rods and rings */
	@NN public static final ElectricMachineGroup bextruder = machinesSimpleCat("machine/extruder.png", CraftingGroups.extruder, "extruder");
	/** Produces IC dies and ICs */
	@NN public static final ElectricMachineGroup binscriber = machinesSimpleCat("machine/inscriber.png", CraftingGroups.inscriber, "inscriber");
	/** Produces sintered carbides */
	@NN public static final ElectricMachineGroup bsinterer = machinesComplex("machine/sinterer.png", CraftingGroups.sinterer, "sinterer");
	/** Excavates the specified area up to a limit */
	@NN public static final ElectricMachineGroup bdig = createDigger();
	/** Transmits power to a specific receiver */
	@NN public static final ElectricMachineGroup ptower = createTower();
	/** Receives power from power towers */
	@NN public static final ElectricMachineGroup prec = createReceiver();
	/** Produces materials from resource beds and stone */
	@NN public static final ElectricMachineGroup bquarry = machinesSimple("machine/quarry.png", CraftingGroups.quarry, "quarry");
	/** Stores electricity */
	@NN public static final ElectricMachineGroup bbattery = createBattery();
	
	//Player pipes
	/** Straight player pipe */
	@NN public static final BlockEntityType PPIPE_lin = ppipe(1, Side.U, Side.D, "machine/ppipe straight.png", "#ppipe-s", "playerpipe.straight");
	/** Changes direction wich player is travelling */
	@NN public static final BlockEntityType PPIPE_bend = ppipe(0.8, Side.R, Side.D, "machine/ppipe turn.png", "#ppipe-b", "playerpipe.bend");
	/** Two independent perpendicular player pipes */
	@NN public static final BlockEntityType PPIPE_lin2 = ppipe2(1, Side.U, Side.D, Side.L, Side.R, "machine/ppipe cross.png", "#ppipe-x", "playerpipe.straight2");
	/** Two bend player pipes */
	@NN public static final BlockEntityType PPIPE_bend2 = ppipe2(0.8, Side.R, Side.D, Side.L, Side.U, "machine/ppipe biturn.png", "#ppipe-z", "playerpipe.bend2");
	/** Inputs players into player pipes */
	@NN public static final BlockEntityType PPIPE_cap = new BlockEntityType()
			.title("#ppipe-t")
			.factory(PlayerPipeEntry::new)
			.texture("machine/pipe exit.png")
			.finish("playerpipe.end");
	/** Joins a player pipe into a mainline */
	@NN public static final BlockEntityType PPIPE_join = ppipea(1, Side.U, "machine/ppipe adjoin.png","#ppipe-l" ,"playerpipe.adj");
	/** Joins two player pipes 180 degrees apart */
	@NN public static final BlockEntityType PPIPE_join2 = ppipea(0.8, Side.L, "machine/ppipe adjoin2.png","#ppipe-y" ,"playerpipe.adj2");
	
	//DEPRECATED blocks
	/** 
	 * @deprecated This machine doesn't even have any purpose.
	 * Use STN instead.
	 * @replacementVer 0.5
	 * @removal 0.6
	 */
	@DeprecatedExtra(replacementVer = "0.5", removal = "0.6")
	@Deprecated(since="0.5", forRemoval=true)
	@NN public static final BlockEntityType CYCLEASSEMBLY = new BlockEntityType()
		.title("#depr-cycleassembly")
		.factory(CycleAssembler::new)
		.texture("machine/cyclic assembler.png")
		.finish("industry.cycle0");
	/**
	 * @deprecated A block used formerly to create pickaxes, now useless.
	 * Use '!!!' button to activate raw items instead
	 * @replacementVer 0.5
	 * @removal 0.7
	 */
	@DeprecatedExtra(replacementVer = "0.5", removal = "0.6")
	@Deprecated(since="0.5", forRemoval=true)
	@NN public static final Block PICKBUILDER = new Block()
		.texture("machine/pickaxe workbench.png")
		.title("#depr-pickbuilder")
		.finish("machines.pickbuilder");
	/**
	 * @deprecated An autocrafter, unobtainable in survival
	 * @replacementVer 0.6
	 * @removal 0.7
	 */
	@DeprecatedExtra(replacementVer = "0.6", removal = "0.7")
	@Deprecated(since="0.5", forRemoval=true)
	@NN public static final BlockEntityType old_AUTOCRAFTER = new BlockEntityType()
		.title("#depr-autocraft1")
		.factory(AutoCrafter::new)
		.texture("machine/AutoCrafter 1.png")
		.finish("industry.autocraft1");
	/**
	 * @deprecated An old block for a furnace.
	 * Use {@link #efurnace}{@code .}{@link ElectricMachineGroup#get(int) get}{@code (1)} instead
	 * @replacementVer 0.5-pre5
	 * @removal 0.6
	 */
	@DeprecatedExtra(replacementVer = "0.5-pre5", removal = "0.6")
	@Deprecated(since="0.5", forRemoval=true)
	@NN public static final BlockEntityType old_EFURNACE = new BlockEntityType()
		.title("#depr-furnace")
		.factory(FurnacePlus::new)
		.texture("machine/esmelter.png")
		.finish("elec.furnace");
	/**
	 * @deprecated An old block for nuclear generator
	 * A replacement is planned for 0.6
	 * @replacementVer 0.6
	 * @removal 0.7
	 */
	@DeprecatedExtra(replacementVer = "0.6", removal = "0.7")
	@Deprecated(since="0.5", forRemoval=true)
	@NN public static final BlockEntityType old_NUKEGEN = new BlockEntityType()
		.title("#depr-nuker")
		.factory(Nuker::new)
		.texture("machine/power/nuke reactor.png")
		.finish("nuke.generator");
	
	/** Initializes blocks */
	public static void init() {
		//initialization method
	}

	//Multimedia devices
	/** A block which plays a specific sound */
	@NN public static final BlockEntityType SPEAKER = new BlockEntityType()
			.title("#multi-speaker")
			.factory(Speaker::new)
			.texture("machine/speaker 2.png")
			.finish("multi.speaker");
	
	@NN private static BlockEntityType ppipe(double length, Side a, Side b, String texture, String title, String id) {
		BlockEntityType type = new BlockEntityType();
		ChirotatedImageGroup tex = ChirotatedImageGroup.create(texture);
		return type
		.title(title)
		.factory(() -> new PlayerPipe(type, tex, a, b, length))
		.texture(texture)
		.finish(id);
	}
	@NN private static BlockEntityType ppipe2(double length, Side a1, Side b1, Side a2, Side b2, String texture, String title, String id) {
		BlockEntityType type = new BlockEntityType();
		ChirotatedImageGroup tex = ChirotatedImageGroup.create(texture);
		return type
		.title(title)
		.factory(() -> new TwinPlayerPipe(type, tex, a1, b1, a2, b2, length))
		.texture(texture)
		.finish(id);
	}
	@NN private static BlockEntityType ppipea(double length, Side main, String texture, String title, String id) {
		BlockEntityType type = new BlockEntityType();
		ChirotatedImageGroup tex = ChirotatedImageGroup.create(texture);
		return type
		.title(title)
		.factory(() -> new JoiningPlayerPipe(length, main, tex, type))
		.texture(texture)
		.finish(id);
	}
	@NN private static BlockEntityType coalgen(int mul, VoltageTier volt, BufferedImage texture, int n) {
		BlockEntityType type = new BlockEntityType();
		return type
				.title((mul==2?GlobalSettings.$res("machine-turbogen"):GlobalSettings.$res("machine-coalgen"))+" "+n)
				.factory(() -> new BlockGeneratorSolid(mul, volt, type))
				.texture(texture)
				.finish((mul==2?"elec.turbogen":"elec.coalgen")+n);
	}
	@NN private static BlockEntityType chest(double capacity, String texture, String id, int n) {
		final String descr1 = GlobalSettings.$res("descr-chest1");
		final String descr2 = GlobalSettings.$res("descr-chest2");
		BlockEntityType type = new BlockEntityType().finish(id);
		Items.tagItem("chest", type);
		BufferedImage image = Textures.get(texture);
		return type
				.title(GlobalSettings.$res("chest")+" #"+n)
				.describe(descr1+" "+capacity+" "+descr2)
				.factory(() -> new Chest(capacity, type, image))
				.texture(image);
	}
	@NN private static ElectricMachineGroup machinesSimple(String texture, SimpleRecipeGroup<?> group, String id) {
		return machinesSimple(texture, group, id, 10);
	}
	@NN private static ElectricMachineGroup machinesSimpleCat(String texture, SimpleRecipeGroup<@NN ?> group, String id) {
		return machinesSimpleCat(texture, group, id, 10);
	}
	@NN private static ElectricMachineGroup machinesSimpleCat(String texture, SimpleRecipeGroup<@NN ?> group, String id, double d) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new ProcessorSimpleCatalyzedBlock<>(type, group), id, d);
	}
	@NN private static ElectricMachineGroup machinesComplex(String texture, ComplexRecipeGroup group, String id) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new ProcessorComplexBlock(type, group), id);
	}
	@NN private static ElectricMachineGroup machinesComplexCat(String texture, ComplexCatRecipeGroup group, String id) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new ProcessorComplexCatalyzedBlock(type, group), id);
	}
	@NN private static ElectricMachineGroup machinesSimple(String texture, SimpleRecipeGroup<?> group, String id, double d) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new ProcessorSimpleBlock(type, group), id, d);
	}
	@NN private static ElectricMachineGroup createDigger() {
		return new ElectricMachineGroup(Textures.get("machine/digger.png"), BlockDigger::new, "digger");
	}
	@NN private static ElectricMachineGroup createBattery() {
		return new ElectricMachineGroup(Textures.get("machine/battery.png"), BlockBattery::new, "battery");
	}
	@NN private static ElectricMachineGroup createTower() {
		return new ElectricMachineGroup(Textures.get("machine/ptower.png"), BlockPowerTower::new, "ptower");
	}
	@NN private static ElectricMachineGroup createReceiver() {
		return new ElectricMachineGroup(Textures.get("machine/preceiver.png"), BlockPowerReceiver::new, "preceiver");
	}
	//Item tags
	static {
		Items.tagItems("wireworld", ww_wire, ww_head, ww_tail, ww_chatter,
				AND, OR, XOR, BUTTON, TOGGLE, YES, NOT, RANDOMCTRL, TRUE, RANDOM, ON, OFF, URANDOM, LAMP, SPEAKER, CLICKER, PLACER, ROTATOR);
		Items.tagItems("player-pipe", PPIPE_lin, PPIPE_bend, PPIPE_lin2, PPIPE_bend2, PPIPE_join, PPIPE_join2, PPIPE_cap);
		Items.tagItems("workbench", ManCrafter.types);
		Items.tagItems("fluid", water, lava, steam);
		Items.tagItems("special", Blocks.air, Blocks.grass);
		Items.tagItems("basic", Blocks.air, Blocks.grass, plank, stone, leaves, logs, sand, gravel, clay, water);
		Items.tagItems("chest", HOPPER, HOPPER_suck, HOPPER_both);
		Items.tagItems("pipe",ipipe_STRAIGHT, ipipe_ELBOW, ipipe_IPE, ipipe_TOLEFT, ipipe_TORIGHT, ipipe_CROSS, ipipe_DUALTURN, ipipe_FILTER, IMOVER);
		Items.tagItem("voltage-ULV", COALGEN1);
		Items.tagItems("voltage-VLV", COALGEN2, TURBOGEN1);
		Items.tagItems("voltage-LV", COALGEN3, TURBOGEN2);
		Items.tagItem("voltage-MV", TURBOGEN3);
		Items.tagItems("machine-coalgen", COALGEN1, COALGEN2, COALGEN3);
		Items.tagItems("machine-turbogen", TURBOGEN1, TURBOGEN2, TURBOGEN3);
		Items.tagItems("imachine", PLACEITEMS, COLLECTOR, IMOVER, TRASH, BOMMAKER, AUTOCRAFTER1, AUTOCRAFTER2, AUTOCRAFTER3, AUTOCRAFTER4, TIPD);
		
		//Deprecation
		Items.tagItems("deprecated", CYCLEASSEMBLY, PICKBUILDER, old_AUTOCRAFTER, old_EFURNACE, old_NUKEGEN);
	}
}
