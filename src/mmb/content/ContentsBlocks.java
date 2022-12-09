/**
 * 
 */
package mmbgame;

import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;

import mmbeng.GlobalSettings;
import mmbeng.block.Block;
import mmbeng.block.BlockEntityType;
import mmbeng.block.Blocks;
import mmbeng.craft.RecipeOutput;
import mmbeng.craft.rgroups.ComplexCatalyzedRecipeGroup;
import mmbeng.craft.rgroups.ComplexRecipeGroup;
import mmbeng.craft.singles.SimpleRecipeGroup;
import mmbeng.item.Items;
import mmbeng.rotate.ChirotatedImageGroup;
import mmbeng.rotate.Side;
import mmbeng.texture.Textures;
import mmbgame.agro.Crop;
import mmbgame.electric.ElectricMachineGroup;
import mmbgame.electric.InfiniteGenerator;
import mmbgame.electric.VoltageTier;
import mmbgame.electric.machines.BlockBattery;
import mmbgame.electric.machines.BlockDigger;
import mmbgame.electric.machines.BlockGeneratorSolid;
import mmbgame.electric.machines.BlockPowerReceiver;
import mmbgame.electric.machines.BlockPowerTower;
import mmbgame.electric.machines.ProcessorComplexBlock;
import mmbgame.electric.machines.ProcessorComplexCatalyzedBlock;
import mmbgame.electric.machines.ProcessorSimpleBlock;
import mmbgame.electric.machines.ProcessorSimpleCatalyzedBlock;
import mmbgame.electric.machines.FurnacePlus;
import mmbgame.electric.machines.Nuker;
import mmbgame.electric.machines.PowerLoad;
import mmbgame.electric.machines.PowerMeter;
import mmbgame.imachine.BlockCollector;
import mmbgame.imachine.DualPipe;
import mmbgame.imachine.IntersectingPipeExtractor;
import mmbgame.imachine.ItemTransporter;
import mmbgame.imachine.Pipe;
import mmbgame.imachine.PipeBinder;
import mmbgame.imachine.PipeFilter;
import mmbgame.imachine.PlaceIncomingItems;
import mmbgame.imachine.chest.Chest;
import mmbgame.imachine.chest.Hopper;
import mmbgame.machinemics.CycleAssembler;
import mmbgame.machinemics.line.AutoCrafter;
import mmbgame.machinemics.line.Furnace;
import mmbgame.machinemics.manual.Crafting;
import mmbgame.media.Speaker;
import mmbgame.ppipe.JoiningPlayerPipe;
import mmbgame.ppipe.PlayerPipe;
import mmbgame.ppipe.PlayerPipeEntry;
import mmbgame.ppipe.TwinPlayerPipe;
import mmbgame.wireworld.ActuatorClick;
import mmbgame.wireworld.ActuatorPlaceBlock;
import mmbgame.wireworld.ActuatorRotations;
import mmbgame.wireworld.EmitTrue;
import mmbgame.wireworld.BlockButton;
import mmbgame.wireworld.Lamp;
import mmbgame.wireworld.OffToggle;
import mmbgame.wireworld.OnToggle;
import mmbgame.wireworld.Randomizer;
import mmbgame.wireworld.EmitUniformRandom;
import mmbgame.wireworld.WWChatter;
import mmbgame.wireworld.WWHead;
import mmbgame.wireworld.WWTail;
import mmbgame.wireworld.WWWire;
import mmbgame.wireworld.AbstractStateGate.StateGateType;
import mmbgame.wireworld.GateBi.BiGateType;
import mmbgame.wireworld.GateMono.MonoGateType;

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
	@Nonnull public static final Block plank = new Block()
			.texture("plank.png")
			.title("#planks")
			.finish("mmb.planks");
	@Nonnull public static final Block stone = new Block()
			.texture("stone.png")
			.title("#stone")
			.finish("mmb.stone");
	@Nonnull public static final Block leaves = new Block()
			.texture("block/leaves.png")
			.title("#leaves")
			.finish("mmb.leaves");
	@Nonnull public static final Block asmtb = new Block()
			.texture("crafting.png")
			.title("#asmtb")
			.finish("mmb.craft");
	@Nonnull public static final Block logs = new Block()
			.texture("log.png")
			.title("#logs")
			.finish("mmb.tree");
	
	@Nonnull public static final Block sand = new Block()
			.texture("block/sand.png")
			.title("#sand")
			.finish("mmb.sand");
	@Nonnull public static final Block gravel = new Block()
			.texture("block/gravel.png")
			.title("#gravel")
			.finish("mmb.gravel");

	//WireWorld wires
	/**
	 * A WireWorld cell.
	 * This block turns into head if it is provided with 1 or 2 signals in 8 block neighborhood.
	 */
	@Nonnull public static final BlockEntityType ww_wire = new BlockEntityType()
		.texture("logic/wire.png")
		.title("#ww-c")
		.factory(WWWire::new)
		.finish("wireworld.wire");
	/** This block emits a signal, and it turns into a tail */
	@Nonnull public static final BlockEntityType ww_head = new BlockEntityType()
		.texture("logic/head.png")
		.title("#ww-h")
		.factory(WWHead::new)
		.leaveBehind(ww_wire)
		.finish("wireworld.head");
	/** This block is left by a head and it turns into wire. This block does not emit any signal. */
	@Nonnull public static final BlockEntityType ww_tail = new BlockEntityType()
		.texture("logic/tail.png")
		.title("#ww-t")
		.factory(WWTail::new)
		.leaveBehind(ww_wire)
		.finish("wireworld.tail");
	
	//Emitters
	/** Generates a single random signal for every neighbor */
	@Nonnull public static final BlockEntityType URANDOM = new BlockEntityType()
			.title("#ww-ur")
			.factory(EmitUniformRandom::new)
			.texture("logic/urandom.png")
			.finish("wireworld.urandom");
	/** Always provides 'true' signal */
 	@Nonnull public static final Block TRUE = new EmitTrue()
			.title("#ww-true")
			.texture("logic/true.png")
			.finish("wireworld.true");
	/** Generates a separate random signal for every neighbor */
	@Nonnull public static final Block RANDOM = new Randomizer()
			.texture("logic/random.png")
			.title("#ww-rnd")
			.finish("wireworld.random");
	/** Generates a signal when the world is loaded */
	@Nonnull public static final Block ONLOAD = new Randomizer()
			.texture("logic/loadsensor.png")
			.title("#ww-load")
			.finish("wireworld.onload");
	
	//WireWorld output devices
	/** This block logs specified message if it is activated by a gate, wire or other signal source */
	@Nonnull public static final BlockEntityType ww_chatter = new BlockEntityType()
		.texture("printer.png")
		.title("#ww-chat")
		.factory(WWChatter::new)
		.describe("A block which prints out its text, when activated by a signal")
		.finish("wireworld.chatter");
	/** This block displays the logic inputs to itself*/
	@Nonnull public static final BlockEntityType LAMP = new BlockEntityType()
			.title("#ww-lamp")
			.factory(Lamp::new)
			.texture("logic/off lamp.png")
			.finish("wireworld.lamp");
	
	//Logic gates
	/** This block receives signals from DR and DR corners and outputs a signal if both inputs are active */
	@Nonnull public static final BlockEntityType AND = new BiGateType("logic/AND.png", (a, b) -> a && b)
		.title("#ww-A")
		.finish("wireworld.and");
	/** This block receives signals from DR and DR corners and outputs a signal if any input is active */
	@Nonnull public static final BlockEntityType OR = new BiGateType("logic/OR.png", (a, b) -> a || b)
		.title("#ww-O")
		.finish("wireworld.or");
	/** This block receives signals from DR and DR corners and outputs a signal if only one input are active */
	@Nonnull public static final BlockEntityType XOR = new BiGateType("logic/XOR.png", (a, b) -> a ^ b)
		.title("#ww-X")
		.finish("wireworld.xor");
	/** This block emits a pulse when pressed by a player or Block Clicking Claw */
	@Nonnull public static final BlockEntityType BUTTON = new BlockEntityType()
			.title("#ww-btn")
			.factory(BlockButton::new)
			.texture("logic/button.png")
			.finish("wireworld.button");
	/** This block toggles state when clicked by a player or Block Clicking Claw */
	@Nonnull public static final BlockEntityType TOGGLE = new StateGateType("logic/toggle on.png","logic/toggle off.png","logic/toggle inert.png",(a,s) -> {
		boolean s2 = s ^ a;
		return (byte) (s2?3:0);
	})
		.title("#ww-tgl")
		.finish("wireworld.toggle");
	/** Recreates the provided signal*/
	@Nonnull public static final BlockEntityType YES = new MonoGateType("logic/YES.png", a -> a)
			.title("#ww-Y")
			.finish("wireworld.yes");
	/** Negates provided signal */
	@Nonnull public static final BlockEntityType NOT = new MonoGateType("logic/NOT.png", a -> !a)
			.title("#ww-N")
			.finish("wireworld.not");
	/**
	 * Sends a random signal if powered
	 * Else does not send signal
	 */
	@Nonnull public static final BlockEntityType RANDOMCTRL= new MonoGateType("logic/randomctrl.png", a -> a && Math.random() < 0.5)
			.title("#ww-rctr")
			.finish("wireworld.randomctrl");
	
	//Switches
	/** Enabled switch */
	@Nonnull public static final Block ON = new OnToggle()
			.texture("logic/on.png")
			.title("#ww-on")
			.finish("wireworld.on");
	/** Disabled switch */
	@Nonnull public static final Block OFF = new OffToggle()
			.texture("logic/off.png")
			.title("#ww-off")
			.finish("wireworld.off");
	
	//WireWorld machines
	/** Creative block placement, unobtainable in survival */
	@Nonnull public static final BlockEntityType PLACER = new BlockEntityType()
			.title("#ww-placer")
			.factory(ActuatorPlaceBlock::new)
			.texture("machine/placer.png")
			.finish("machines.placer");
	/** Block Clicking Claw, auto-enables blocks */
	@Nonnull public static final BlockEntityType CLICKER = new BlockEntityType()
			.title("#ww-click")
			.factory(ActuatorClick::new)
			.texture("machine/claw.png")
			.finish("machines.clicker");
	/** Block Rotator, rotates blocks */
	@Nonnull public static final BlockEntityType ROTATOR = new BlockEntityType()
			.title("#ww-rot")
			.factory(ActuatorRotations::new)
			.texture("machine/CW.png")
			.finish("machines.rotator");

	//Power generators
	@Nonnull public static final BlockEntityType COALGEN1 = coalgen(1, VoltageTier.V1, BlockGeneratorSolid.img, 1);
	@Nonnull public static final BlockEntityType COALGEN2 = coalgen(1, VoltageTier.V2, BlockGeneratorSolid.img1, 2);
	@Nonnull public static final BlockEntityType COALGEN3 = coalgen(1, VoltageTier.V3, BlockGeneratorSolid.img2, 3);
	@Nonnull public static final BlockEntityType TURBOGEN1 = coalgen(2, VoltageTier.V2, BlockGeneratorSolid.turboimg, 1);
	@Nonnull public static final BlockEntityType TURBOGEN2 = coalgen(2, VoltageTier.V3, BlockGeneratorSolid.turboimg1, 2);
	@Nonnull public static final BlockEntityType TURBOGEN3 = coalgen(2, VoltageTier.V4, BlockGeneratorSolid.turboimg2, 3);
	
	//Electrical testing equipment
	/** A series of 9 infinite power generators. Intended for machine testing.*/
	@Nonnull public static final ElectricMachineGroup infinigens =
			new ElectricMachineGroup(Textures.get("machine/power/infinity.png"), type -> new InfiniteGenerator(type.volt, type), "infinigen");
	/** Measures power of machines */
	@Nonnull public static final BlockEntityType PMETER = new BlockEntityType()
			.title("#elec-meter")
			.factory(PowerMeter::new)
			.texture("machine/power/pmeter.png")
			.finish("elec.meter");
	/** Consumes specified amount of electricity. Intended for generator testing */
	@Nonnull public static final BlockEntityType LOAD = new BlockEntityType()
			.title("#elec-load")
			.factory(PowerLoad::new)
			.texture("machine/power/load.png")
			.finish("elec.load");
	
	//DEPRECATED old modular machines
	/** @deprecated An old block for a furnace. Use {@link #efurnace}{@code .}{@link #ElectricMachineGroup.getBlock(int) get}{@code (1)} instead */
	@Deprecated(since="0.5") @Nonnull public static final BlockEntityType EFURNACE = new BlockEntityType() //NOSONAR
			.title("#depr-furnace")
			.factory(FurnacePlus::new)
			.texture("machine/esmelter.png")
			.finish("elec.furnace");
	@Nonnull public static final BlockEntityType NUKEGEN = new BlockEntityType()
			.title("#depr-nuker")
			.factory(Nuker::new)
			.texture("machine/power/nuke reactor.png")
			.finish("nuke.generator");
	
	//Chests
	/** Stores 6 m³ of items*/
	@Nonnull public static final BlockEntityType CHEST = chest(6, "machine/chest1.png", "chest.beginning", 1);
	/** Stores 16 m³ of items*/
	@Nonnull public static final BlockEntityType CHEST1 = chest(16, "machine/chest2.png", "chest.simple", 2);
	/** Stores 32 m³ of items*/
	@Nonnull public static final BlockEntityType CHEST2 = chest(32, "machine/chest3.png", "chest.intermediate", 3);
	/** Stores 64 m³ of items*/
	@Nonnull public static final BlockEntityType CHEST3 = chest(64, "machine/chest4.png", "chest.advanced", 4);
	/** Stores 128 m³ of items*/
	@Nonnull public static final BlockEntityType CHEST4 = chest(128, "machine/chest5.png", "chest.extreme", 5);
	/** Stores 256 m³ of items*/
	@Nonnull public static final BlockEntityType CHEST5 = chest(256, "machine/chest6.png", "chest.ultimate", 6);
	/** Stores 1024 m³ of items*/
	@Nonnull public static final BlockEntityType CHEST6 = chest(1024, "machine/chest7.png", "chest.penultimate", 7);
	/** Stores 4096 m³ of items*/
	@Nonnull public static final BlockEntityType CHEST7 = chest(4096, "machine/chest8.png", "chest.penultimate2", 8);
	/** Stores 16384 m³ of items*/
	@Nonnull public static final BlockEntityType CHEST8 = chest(16384, "machine/chest9.png", "chest.penultimate3", 9);
	/** A chest that auto-inserts items */
	@Nonnull public static final BlockEntityType HOPPER = new BlockEntityType()
			.title("#chest-hopper")
			.factory(() -> new Hopper((byte) 1))
			.texture("machine/hopper.png")
			.finish("chest.hopper1");
	/** A chest thatt auto-extracts items */
	@Nonnull public static final BlockEntityType HOPPER_suck = new BlockEntityType()
			.title("#chest-sucker")
			.factory(() -> new Hopper((byte) 2))
			.texture("machine/sucker.png")
			.finish("chest.hopper2");
	/**  A chest that both auto-inserts and auto-extracts items */
	@Nonnull public static final BlockEntityType HOPPER_both = new BlockEntityType()
			.title("#chest-both")
			.factory(() -> new Hopper((byte) 3))
			.texture("machine/transferrer.png")
			.finish("chest.hopper3");
	
	@Nonnull public static final BlockEntityType IMOVER = new BlockEntityType()
			.title("#ipipe-extractor")
			.factory(ItemTransporter::new)
			.texture(ItemTransporter.TEXTURE)
			.finish("itemsystem.mover");
	
	@Nonnull public static final BlockEntityType PLACEITEMS = new BlockEntityType()
			.title("#placeitems")
			.factory(PlaceIncomingItems::new)
			.texture("machine/block place interface.png")
			.finish("industry.placeitems");
	@Nonnull public static final BlockEntityType COLLECTOR = new BlockEntityType()
			.title("#collector")
			.factory(BlockCollector::new)
			.texture("machine/vacuum.png")
			.finish("industry.collector");
	
	//Item pipes
	/** Represets a straight pipe */
	@Nonnull public static final BlockEntityType ipipe_STRAIGHT;
	/** Represents an elbow pipe */
	@Nonnull public static final BlockEntityType ipipe_ELBOW;
	/** An Intersecting Pipe Extractor has a perpendicular straight pipe */
	@Nonnull public static final BlockEntityType ipipe_IPE;
	/** An item pipe which connects from the left*/
	@Nonnull public static final BlockEntityType ipipe_TOLEFT;
	/** An item pipe which connects from the right*/
	@Nonnull public static final BlockEntityType ipipe_TORIGHT;
	/** A pair of intersecting item pipes*/
	@Nonnull public static final BlockEntityType ipipe_CROSS;
	/** A pair of non-intersection bend pipes*/
	@Nonnull public static final BlockEntityType ipipe_DUALTURN;
	/** A pair of non-intersection bend pipes*/
	@Nonnull public static final BlockEntityType ipipe_FILTER;
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
	@Nonnull public static final Block water = new Block()
	.texture("liquid/water.png")
	.title("#water")
	.finish("liquid.water");
	@Nonnull public static final Block lava = new Block()
	.texture("liquid/lava.png")
	.title("#lava")
	.finish("liquid.lava");
	@Nonnull public static final Block steam = new Block()
	.texture("liquid/steam.png")
	.title("#steam")
	.finish("liquid.steam");
	@Nonnull public static final Block alcohol = new Block()
	.texture("liquid/alcohol.png")
	.title("#alcohol")
	.finish("liquid.alcohol");
	@Nonnull public static final Block clay = new Block()
	.texture("block/clay.png")
	.title("#clay")
	.finish("mmb.clay");
	
	//Crops
	@Nonnull public static final BlockEntityType AGRO_TREE =
			crop(1500, logs, "#machine-tree", Textures.get("block/tree.png"), "crop.tree");
	@Nonnull public static final BlockEntityType AGRO_WATER =
			crop(1000, water, "#machine-water", Textures.get("machine/water well.png"), "crop.water");
	@Nonnull public static final BlockEntityType AGRO_SEEDS =
			crop(1000, ContentsItems.seeds, "#machine-seeds", Textures.get("block/cropfield.png"), "crop.seeds");
	@Nonnull public static final BlockEntityType AGRO_HOPS =
			crop(1000, ContentsItems.hops, "#machine-hops", Textures.get("machine/hops.png"), "crop.hops");
	
	//Non-electic processing machines
	@Nonnull public static final BlockEntityType FURNACE = new BlockEntityType()
			.title("#furnace")
			.factory(Furnace::new)
			.texture(Furnace.TEXTURE_INERT)
			.finish("industry.furnace");
	@Nonnull public static final BlockEntityType CYCLEASSEMBLY = new BlockEntityType()
			.title("#cycleassembly")
			.factory(CycleAssembler::new)
			.texture("machine/cyclic assembler.png")
			.finish("industry.cycle0");
	@Nonnull public static final Block PICKBUILDER = new Block()
			.texture("machine/pickaxe workbench.png")
			.title("#pickbuilder")
			.finish("machines.pickbuilder");
	@Nonnull public static final BlockEntityType AUTOCRAFTER = new BlockEntityType()
			.title("#autocraft1")
			.factory(AutoCrafter::new)
			.texture("machine/AutoCrafter 1.png")
			.finish("industry.autocraft1");
	
	//Electrical processing machines
	/** Electric furnace */
	@Nonnull public static final ElectricMachineGroup efurnace = machinesSimple("machine/electrosmelter.png", CraftingGroups.smelting, "electrofurnace");
	@Nonnull public static final ElectricMachineGroup bcrusher = machinesSimple("machine/pulverizer.png", CraftingGroups.crusher, "crusher");
	@Nonnull public static final ElectricMachineGroup bcmill = machinesSimple("machine/cluster mill.png", CraftingGroups.clusterMill, "clustermill");
	@Nonnull public static final ElectricMachineGroup bwiremill = machinesSimple("machine/wiremill.png", CraftingGroups.wiremill, "wiremill");
	@Nonnull public static final ElectricMachineGroup balloyer = machinesComplex("machine/alloyer.png", CraftingGroups.alloyer, "alloyer");
	@Nonnull public static final ElectricMachineGroup bassembly = machinesComplexCat("machine/machinemaker.png", CraftingGroups.assembler, "assembler");
	@Nonnull public static final ElectricMachineGroup bsplitter = machinesSimple("machine/splitter.png", CraftingGroups.splitter, "spllitter", 0.1);
	@Nonnull public static final ElectricMachineGroup bsplicer = machinesSimple("machine/splicer.png", CraftingGroups.combiner, "splicer", 0.1);
	@Nonnull public static final ElectricMachineGroup bbrewery = machinesComplex("machine/brewery.png", CraftingGroups.brewery, "brewery");
	@Nonnull public static final ElectricMachineGroup bextruder = machinesSimpleCat("machine/extruder.png", CraftingGroups.extruder, "extruder");
	@Nonnull public static final ElectricMachineGroup binscriber = machinesSimpleCat("machine/inscriber.png", CraftingGroups.inscriber, "inscriber");
	@Nonnull public static final ElectricMachineGroup bsinterer = machinesComplex("machine/sinterer.png", CraftingGroups.sinterer, "sinterer");
	@Nonnull public static final ElectricMachineGroup bdig = createDigger();
	@Nonnull public static final ElectricMachineGroup ptower = createTower();
	@Nonnull public static final ElectricMachineGroup prec = createReceiver();
	@Nonnull public static final ElectricMachineGroup bquarry = machinesSimple("machine/quarry.png", CraftingGroups.quarry, "quarry");
	@Nonnull public static final ElectricMachineGroup bbattery = createBattery();
	
	//Player pipes
	/** Straight player pipe */
	@Nonnull public static final BlockEntityType PPIPE_lin = ppipe(1, Side.U, Side.D, "machine/ppipe straight.png", "#ppipe-s", "playerpipe.straight");
	@Nonnull public static final BlockEntityType PPIPE_bend = ppipe(0.8, Side.R, Side.D, "machine/ppipe turn.png", "#ppipe-b", "playerpipe.bend");
	@Nonnull public static final BlockEntityType PPIPE_lin2 = ppipe2(1, Side.U, Side.D, Side.L, Side.R, "machine/ppipe cross.png", "#ppipe-x", "playerpipe.straight2");
	@Nonnull public static final BlockEntityType PPIPE_bend2 = ppipe2(0.8, Side.R, Side.D, Side.L, Side.U, "machine/ppipe biturn.png", "#ppipe-z", "playerpipe.bend2");
	@Nonnull public static final BlockEntityType PPIPE_cap = new BlockEntityType()
			.title("#ppipe-t")
			.factory(PlayerPipeEntry::new)
			.texture("machine/pipe exit.png")
			.finish("playerpipe.end");
	@Nonnull public static final BlockEntityType PPIPE_join = ppipea(1, Side.U, "machine/ppipe adjoin.png","#ppipe-l" ,"playerpipe.adj");
	@Nonnull public static final BlockEntityType PPIPE_join2 = ppipea(0.8, Side.L, "machine/ppipe adjoin2.png","#ppipe-y" ,"playerpipe.adj2");
	
	/** Initializes blocks */
	public static void init() {
		//initialization method
	}

	//Multimedia devices
	/** A block which plays a specific sound */
	@Nonnull public static final BlockEntityType SPEAKER = new BlockEntityType()
			.title("#multi-speaker")
			.factory(Speaker::new)
			.texture("machine/speaker 2.png")
			.finish("multi.speaker");
	
	//Reusable block methods
	@Nonnull
	public static BlockEntityType crop(int duration, RecipeOutput cropDrop, String title, BufferedImage texture, String id) {
		BlockEntityType result = new BlockEntityType().title(title).texture(texture).finish(id);
		CraftingGroups.agro.add(result, cropDrop, duration);
		return result.factory(() -> new Crop(result, duration, cropDrop));
	}
	@Nonnull private static BlockEntityType ppipe(double length, Side a, Side b, String texture, String title, String id) {
		BlockEntityType type = new BlockEntityType();
		ChirotatedImageGroup tex = ChirotatedImageGroup.create(texture);
		return type
		.title(title)
		.factory(() -> new PlayerPipe(type, tex, a, b, length))
		.texture(texture)
		.finish(id);
	}
	@Nonnull private static BlockEntityType ppipe2(double length, Side a1, Side b1, Side a2, Side b2, String texture, String title, String id) {
		BlockEntityType type = new BlockEntityType();
		ChirotatedImageGroup tex = ChirotatedImageGroup.create(texture);
		return type
		.title(title)
		.factory(() -> new TwinPlayerPipe(type, tex, a1, b1, a2, b2, length))
		.texture(texture)
		.finish(id);
	}
	@Nonnull private static BlockEntityType ppipea(double length, Side main, String texture, String title, String id) {
		BlockEntityType type = new BlockEntityType();
		ChirotatedImageGroup tex = ChirotatedImageGroup.create(texture);
		return type
		.title(title)
		.factory(() -> new JoiningPlayerPipe(length, main, tex, type))
		.texture(texture)
		.finish(id);
	}
	@Nonnull private static BlockEntityType coalgen(int mul, VoltageTier volt, BufferedImage texture, int n) {
		BlockEntityType type = new BlockEntityType();
		return type
				.title((mul==2?GlobalSettings.$res("machine-turbogen"):GlobalSettings.$res("machine-coalgen"))+" "+n)
				.factory(() -> new BlockGeneratorSolid(mul, volt, type))
				.texture(texture)
				.finish((mul==2?"elec.turbogen":"elec.coalgen")+n);
	}
	@Nonnull private static BlockEntityType chest(double capacity, String texture, String id, int n) {
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
	@Nonnull private static ElectricMachineGroup machinesSimple(String texture, SimpleRecipeGroup<?> group, String id) {
		return machinesSimple(texture, group, id, 1);
	}
	@Nonnull private static ElectricMachineGroup machinesSimpleCat(String texture, SimpleRecipeGroup<?> group, String id) {
		return machinesSimpleCat(texture, group, id, 1);
	}
	@Nonnull private static ElectricMachineGroup machinesSimpleCat(String texture, SimpleRecipeGroup<?> group, String id, double d) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new ProcessorSimpleCatalyzedBlock(type, group), id, d);
	}
	@Nonnull private static ElectricMachineGroup machinesComplex(String texture, ComplexRecipeGroup group, String id) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new ProcessorComplexBlock(type, group), id);
	}
	@Nonnull private static ElectricMachineGroup machinesComplexCat(String texture, ComplexCatalyzedRecipeGroup group, String id) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new ProcessorComplexCatalyzedBlock(type, group), id);
	}
	@Nonnull private static ElectricMachineGroup machinesSimple(String texture, SimpleRecipeGroup<?> group, String id, double d) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new ProcessorSimpleBlock(type, group), id, d);
	}
	@Nonnull private static ElectricMachineGroup createDigger() {
		return new ElectricMachineGroup(Textures.get("machine/digger.png"), BlockDigger::new, "digger");
	}
	@Nonnull private static ElectricMachineGroup createBattery() {
		return new ElectricMachineGroup(Textures.get("machine/battery.png"), BlockBattery::new, "battery");
	}
	@Nonnull private static ElectricMachineGroup createTower() {
		return new ElectricMachineGroup(Textures.get("machine/ptower.png"), BlockPowerTower::new, "ptower");
	}
	@Nonnull private static ElectricMachineGroup createReceiver() {
		return new ElectricMachineGroup(Textures.get("machine/preceiver.png"), BlockPowerReceiver::new, "preceiver");
	}
	//Item tags
	static {
		Items.tagItems("wireworld", ww_wire, ww_head, ww_tail, ww_chatter,
				AND, OR, XOR, BUTTON, TOGGLE, YES, NOT, RANDOMCTRL, TRUE, RANDOM, ON, OFF, URANDOM, LAMP, SPEAKER, CLICKER, PLACER, ROTATOR);
		Items.tagItems("player-pipe", PPIPE_lin, PPIPE_bend, PPIPE_lin2, PPIPE_bend2, PPIPE_join, PPIPE_join2, PPIPE_cap);
		Items.tagItem("workbench", PICKBUILDER);
		Items.tagItems("workbench", Crafting.types);
		Items.tagItems("fluid", water, lava, steam);
		Items.tagItems("special", Blocks.air, Blocks.grass);
		Items.tagItems("basic", Blocks.air, Blocks.grass, plank, stone, leaves, logs, sand, gravel, clay, water);
		Items.tagItems("chest", HOPPER, HOPPER_suck, HOPPER_both);
		Items.tagItems("shape-crop", AGRO_TREE, AGRO_WATER, AGRO_SEEDS, AGRO_HOPS);
		Items.tagItems("pipe",ipipe_STRAIGHT, ipipe_ELBOW, ipipe_IPE, ipipe_TOLEFT, ipipe_TORIGHT, ipipe_CROSS, ipipe_DUALTURN, ipipe_FILTER, IMOVER);
		Items.tagItem("voltage-ULV", COALGEN1);
		Items.tagItems("voltage-VLV", COALGEN2, TURBOGEN1);
		Items.tagItems("voltage-LV", COALGEN3, TURBOGEN2);
		Items.tagItem("voltage-MV", TURBOGEN3);
		Items.tagItems("machine-coalgen", COALGEN1, COALGEN2, COALGEN3);
		Items.tagItems("machine-turbogen", TURBOGEN1, TURBOGEN2, TURBOGEN3);
		Items.tagItems("imachine", PLACEITEMS, COLLECTOR, IMOVER);
	}
}
