/**
 * 
 */
package mmb.content;

import java.awt.image.BufferedImage;

import mmb.PropertyExtension;
import mmb.annotations.NN;
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
import mmb.engine.block.BlockType;
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
	@NN public static final Block plank = new Block("mmb.planks",
		PropertyExtension.setTextureAsset("plank.png"),
		PropertyExtension.translateTitle("#planks")
	);
	
	/** Hosts ores */
	@NN public static final Block stone = new Block("mmb.stone",
		PropertyExtension.setTextureAsset("stone.png"),
		PropertyExtension.translateTitle("#stone")
	);
	/** A basic ingredient of crops */
	@NN public static final Block leaves = new Block("mmb.leaves",
		PropertyExtension.setTextureAsset("block/leaves.png"),
		PropertyExtension.translateTitle("#leaves")
	);
	/** Decorative, unused */
	@NN public static final Block asmtb = new Block("mmb.craft",
		PropertyExtension.setTextureAsset("crafting.png"),
		PropertyExtension.translateTitle("#asmtb")
	);
	/** The main wood source */
	@NN public static final Block logs = new Block("mmb.tree",
		PropertyExtension.setTextureAsset("log.png"),
		PropertyExtension.translateTitle("#logs")
	);
	/** A very fine dust block, smeltable to glass */
	@NN public static final Block sand = new Block("mmb.sand",
		PropertyExtension.setTextureAsset("block/sand.png"),
		PropertyExtension.translateTitle("#sand")
	);
	/** A coarse grain block, grindable to sand */
	@NN public static final Block gravel = new Block("mmb.gravel",
		PropertyExtension.setTextureAsset("block/gravel.png"),
		PropertyExtension.translateTitle("#gravel")
	);
	/** Currently unused */
	@NN public static final Block clay = new Block("mmb.clay",
		PropertyExtension.setTextureAsset("block/clay.png"),
		PropertyExtension.translateTitle("#clay")
	);
	
	//WireWorld wires
	/**
	 * A WireWorld cell.
	 * This block turns into head if it is provided with 1 or 2 signals in 8 block neighborhood.
	 */
	@NN public static final BlockType ww_wire = new BlockType("wireworld.wire",
		PropertyExtension.setTextureAsset("logic/wire.png"),
		PropertyExtension.translateTitle("#ww-c"),
		PropertyExtension.setBlockFactory(json -> new WWWire())
	);
	/** This block emits a signal, and it turns into a tail */
	@NN public static final BlockType ww_head = new BlockType("wireworld.head",
		PropertyExtension.setTextureAsset("logic/head.png"),
		PropertyExtension.translateTitle("#ww-h"),
		PropertyExtension.setBlockFactory(json -> new WWHead()),
		PropertyExtension.setMiningDrops(ww_wire)
	);
	/** This block is left by a head and it turns into wire. This block does not emit any signal. */
	@NN public static final BlockType ww_tail = new BlockType("wireworld.tail",
		PropertyExtension.setTextureAsset("logic/tail.png"),
		PropertyExtension.translateTitle("#ww-t"),
		PropertyExtension.setBlockFactory(json -> new WWTail()),
		PropertyExtension.setMiningDrops(ww_wire)
	);
	
	//Emitters
	/** Generates a single random signal for every neighbor */
	@NN public static final BlockType URANDOM = new BlockType("wireworld.urandom",
		PropertyExtension.setTextureAsset("logic/urandom.png"),
		PropertyExtension.translateTitle("#ww-ur"),
		PropertyExtension.setBlockFactory(json -> new EmitUniformRandom()),
		PropertyExtension.setMiningDrops(ww_wire)
	);
	/** Always provides 'true' signal */
 	@NN public static final Block TRUE = new EmitTrue("wireworld.true",
		PropertyExtension.setTextureAsset("logic/true.png"),
		PropertyExtension.translateTitle("#ww-true")
	);
	/** Generates a separate random signal for every neighbor */
	@NN public static final Block RANDOM = new EmitRandom("wireworld.random",
		PropertyExtension.setTextureAsset("logic/random.png"),
		PropertyExtension.translateTitle("#ww-rnd")
	);
	/** Generates a signal when the world is loaded */
	@NN public static final BlockType ONLOAD = new BlockType("wireworld.onload",
		PropertyExtension.setTextureAsset("logic/loadsensor.png"),
		PropertyExtension.translateTitle("#ww-load"),
		PropertyExtension.setBlockFactory(json -> new WorldLoadDetector())
	);
	
	//WireWorld output devices
	/** This block logs specified message if it is activated by a gate, wire or other signal source */
	@NN public static final BlockType ww_chatter = new BlockType("wireworld.chatter",
		PropertyExtension.setTextureAsset("printer.png"),
		PropertyExtension.translateTitle("#ww-chat"),
		PropertyExtension.setBlockFactory(json -> TextChatter.load(json)),
		PropertyExtension.translateDescription("A block which prints out it text, when activated by a signal")
	);
	/** This block displays the logic inputs to itself*/
	@NN public static final BlockType LAMP = new BlockType("wireworld.lamp",
		PropertyExtension.setTextureAsset("logic/off lamp.png"),
		PropertyExtension.translateTitle("#ww-lamp"),
		PropertyExtension.setBlockFactory(json -> new Lamp())
	);
	
	//Logic gates
	/** This block receives signals from DR and DR corners and outputs a signal if both inputs are active */
	@NN public static final BlockType AND = new BiGateType("logic/AND.png", (a, b) -> a && b)
		.title("#ww-A")
		.finish("wireworld.and");
	/** This block receives signals from DR and DR corners and outputs a signal if any input is active */
	@NN public static final BlockType OR = new BiGateType("logic/OR.png", (a, b) -> a || b)
		.title("#ww-O")
		.finish("wireworld.or");
	/** This block receives signals from DR and DR corners and outputs a signal if only one input are active */
	@NN public static final BlockType XOR = new BiGateType("logic/XOR.png", (a, b) -> a ^ b)
		.title("#ww-X")
		.finish("wireworld.xor");
	/** This block emits a pulse when pressed by a player or Block Clicking Claw */
	@NN public static final BlockType BUTTON = new BlockType("wireworld.button",
		PropertyExtension.setTextureAsset("logic/button.png"),
		PropertyExtension.translateTitle("#ww-btn"),
		PropertyExtension.setBlockFactory(json -> new BlockButton())
	);
	/** This block toggles state when clicked by a player or Block Clicking Claw */
	@NN public static final BlockType TOGGLE = new StateGateType("logic/toggle on.png","logic/toggle off.png","logic/toggle inert.png",(a,s) -> {
		boolean s2 = side ^ a;
		return (byte) (s2?3:0);
	})
		.title("#ww-tgl")
		.finish("wireworld.toggle");
	/** Recreates the provided signal*/
	@NN public static final BlockType YES = new MonoGateType("logic/YES.png", a -> a)
			.title("#ww-Y")
			.finish("wireworld.yes");
	/** Negates provided signal */
	@NN public static final BlockType NOT = new MonoGateType("logic/NOT.png", a -> !a)
			.title("#ww-N")
			.finish("wireworld.not");
	/**
	 * Sends a random signal if powered
	 * Else does not send signal
	 */
	@NN public static final BlockType RANDOMCTRL= new MonoGateType("logic/randomctrl.png", a -> a && Math.random() < 0.5)
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
	@NN public static final BlockType PLACER = new BlockType()
			.title("#ww-placer")
			.factory(ActuatorPlaceBlock::new)
			.texture("machine/placer.png")
			.finish("machines.placer");
	/** Block Clicking Claw, auto-enables blocks */
	@NN public static final BlockType CLICKER = new BlockType()
			.title("#ww-click")
			.factory(ActuatorClick::new)
			.texture("machine/claw.png")
			.finish("machines.clicker");
	/** Block Rotator, rotates blocks */
	@NN public static final BlockType ROTATOR = new BlockType()
			.title("#ww-rot")
			.factory(ActuatorRotations::new)
			.texture("machine/CW.png")
			.finish("machines.rotator");

	//Power generators
	/** Basic ULV generator */
	@NN public static final BlockType COALGEN1 = coalgen(1, VoltageTier.V1, BlockGeneratorSolid.img, 1);
	/** Basic VLV genertor */
	@NN public static final BlockType COALGEN2 = coalgen(1, VoltageTier.V2, BlockGeneratorSolid.img1, 2);
	/** Basic LV gnerator */
	@NN public static final BlockType COALGEN3 = coalgen(1, VoltageTier.V3, BlockGeneratorSolid.img2, 3);
	/** Advanced VLV generator*/
	@NN public static final BlockType TURBOGEN1 = coalgen(2, VoltageTier.V2, BlockGeneratorSolid.turboimg, 1);
	/** Advanced LV generator */
	@NN public static final BlockType TURBOGEN2 = coalgen(2, VoltageTier.V3, BlockGeneratorSolid.turboimg1, 2);
	/** Basic MV generator */
	@NN public static final BlockType TURBOGEN3 = coalgen(2, VoltageTier.V4, BlockGeneratorSolid.turboimg2, 3);
	
	//Electrical testing equipment
	/** A series of 9 infinite power generators. Intended for machine testing.*/
	@NN public static final ElectricMachineGroup infinigens =
			new ElectricMachineGroup(Textures.get("machine/power/infinity.png"), InfiniteGenerator::new, "infinigen");
	/** Measures power of machines */
	@NN public static final BlockType PMETER = new BlockType()
			.title("#elec-meter")
			.factory(PowerMeter::new)
			.texture("machine/power/pmeter.png")
			.finish("elec.meter");
	/** Consumes specified amount of electricity. Intended for generator testing */
	@NN public static final BlockType LOAD = new BlockType()
			.title("#elec-load")
			.factory(PowerLoad::new)
			.texture("machine/power/load.png")
			.finish("elec.load");
	
	//Chests
	/** Stores 6 m³ of items*/
	@NN public static final BlockType CHEST = chest(6, "machine/chest1.png", "chest.beginning", 1);
	/** Stores 16 m³ of items*/
	@NN public static final BlockType CHEST1 = chest(16, "machine/chest2.png", "chest.simple", 2);
	/** Stores 32 m³ of items*/
	@NN public static final BlockType CHEST2 = chest(32, "machine/chest3.png", "chest.intermediate", 3);
	/** Stores 64 m³ of items*/
	@NN public static final BlockType CHEST3 = chest(64, "machine/chest4.png", "chest.advanced", 4);
	/** Stores 128 m³ of items*/
	@NN public static final BlockType CHEST4 = chest(128, "machine/chest5.png", "chest.extreme", 5);
	/** Stores 256 m³ of items*/
	@NN public static final BlockType CHEST5 = chest(256, "machine/chest6.png", "chest.ultimate", 6);
	/** Stores 1024 m³ of items*/
	@NN public static final BlockType CHEST6 = chest(1024, "machine/chest7.png", "chest.penultimate", 7);
	/** Stores 4096 m³ of items*/
	@NN public static final BlockType CHEST7 = chest(4096, "machine/chest8.png", "chest.penultimate2", 8);
	/** Stores 16384 m³ of items*/
	@NN public static final BlockType CHEST8 = chest(16384, "machine/chest9.png", "chest.penultimate3", 9);
	/** A chest that auto-inserts items */
	@NN public static final BlockType HOPPER = new BlockType()
			.title("#chest-hopper")
			.factory(() -> new Hopper((byte) 1))
			.texture("machine/hopper.png")
			.finish("chest.hopper1");
	/** A chest thatt auto-extracts items */
	@NN public static final BlockType HOPPER_suck = new BlockType()
			.title("#chest-sucker")
			.factory(() -> new Hopper((byte) 2))
			.texture("machine/sucker.png")
			.finish("chest.hopper2");
	/**  A chest that both auto-inserts and auto-extracts items */
	@NN public static final BlockType HOPPER_both = new BlockType()
			.title("#chest-both")
			.factory(() -> new Hopper((byte) 3))
			.texture("machine/transferrer.png")
			.finish("chest.hopper3");
	/** Item extractor */
	@NN public static final BlockType IMOVER = new BlockType()
			.title("#ipipe-extractor")
			.factory(ItemTransporter::new)
			.texture(ItemTransporter.TEXTURE)
			.finish("itemsystem.mover");
	/** Places incoming items */
	@NN public static final BlockType PLACEITEMS = new BlockType()
			.title("#placeitems")
			.factory(PlaceIncomingItems::new)
			.texture("machine/block place interface.png")
			.finish("industry.placeitems");
	/** Wide area block collector */
	@NN public static final BlockType COLLECTOR = new BlockType()
			.title("#collector")
			.factory(BlockCollector::new)
			.texture("machine/vacuum.png")
			.finish("industry.collector");
	@NN public static final Block TRASH = new TrashCan("imachine.trash",
		PropertyExtension.translateTitle("#trashcan"),
		PropertyExtension.setTextureAsset("machine/trash.png")
	);
	@NN public static final BlockType BOMMAKER = new BlockType("imachine.bom",
		PropertyExtension.setTextureAsset("machine/BOMer.png"),
		PropertyExtension.setBlockFactory(json -> BOMFactory.load(json)),
		PropertyExtension.translateTitle("#bommaker")
	);
	
	//Item pipes
	/** Represets a straight pipe */
	@NN public static final BlockType ipipe_STRAIGHT;
	/** Represents an elbow pipe */
	@NN public static final BlockType ipipe_ELBOW;
	/** An Intersecting Pipe Extractor has a perpendicular straight pipe */
	@NN public static final BlockType ipipe_IPE;
	/** An item pipe which connects from the left*/
	@NN public static final BlockType ipipe_TOLEFT;
	/** An item pipe which connects from the right*/
	@NN public static final BlockType ipipe_TORIGHT;
	/** A pair of intersecting item pipes*/
	@NN public static final BlockType ipipe_CROSS;
	/** A pair of non-intersection bend pipes*/
	@NN public static final BlockType ipipe_DUALTURN;
	/** A pair of non-intersection bend pipes*/
	@NN public static final BlockType ipipe_FILTER;
	//Init all item pipes
	static {
		ChirotatedImageGroup textureStraight = ChirotatedImageGroup.create("machine/pipe straight.png");
		ipipe_STRAIGHT = new BlockType()
		.texture("machine/pipe straight.png")
		.describe("A pipe which is straight.")
		.title("#ipipe-straight")
		.factory(() -> new Pipe(Side.R, Side.L, ContentsBlocks.ipipe_STRAIGHT, textureStraight))
		.finish("pipe.I");
		
		ChirotatedImageGroup textureElbow = ChirotatedImageGroup.create("machine/pipe elbow.png");
		ipipe_ELBOW = new BlockType()
		.texture("machine/pipe elbow.png")
		.describe("A pipe which is bent.")
		.title("#ipipe-turn")
		.factory(() -> new Pipe(Side.R, Side.D, ContentsBlocks.ipipe_ELBOW, textureElbow))
		.finish("pipe.L");
		
		ChirotatedImageGroup textureIPE = ChirotatedImageGroup.create("machine/imover intersected.png");
		ipipe_IPE = new BlockType()
		.texture("machine/imover intersected.png")
		.describe("A hybrid of Item Mover and straight pipe")
		.title("#ipipe-ipe")
		.factory(() -> new IntersectingPipeExtractor(Side.R, Side.L, ContentsBlocks.ipipe_IPE, textureIPE))
		.finish("pipe.IPE");
		
		ChirotatedImageGroup textureToLeft = ChirotatedImageGroup.create("machine/pipe merge left.png");
		ipipe_TOLEFT = new BlockType()
		.texture("machine/pipe merge left.png")
		.describe("A pipe which directs its side input to the left.")
		.title("#ipipe-left")
		.factory(() -> new PipeBinder(ContentsBlocks.ipipe_TOLEFT, Side.L, textureToLeft))
		.finish("pipe.toleft");
		
		ChirotatedImageGroup textureToRight = ChirotatedImageGroup.create("machine/pipe merge right.png");
		ipipe_TORIGHT = new BlockType()
		.texture("machine/pipe merge right.png")
		.describe("A pipe which directs its side input to the right.")
		.title("#ipipe-right")
		.factory(() -> new PipeBinder(ContentsBlocks.ipipe_TORIGHT, Side.R, textureToRight))
		.finish("pipe.toright");
		
		ChirotatedImageGroup textureCross = ChirotatedImageGroup.create("machine/pipe bridged.png");
		ipipe_CROSS = new BlockType()
		.texture("machine/pipe bridged.png")
		.describe("A pair of disconnected perpendicular pipes.")
		.title("#ipipe-cross")
		.factory(() -> new DualPipe(Side.D, Side.R, ContentsBlocks.ipipe_CROSS, textureCross))
		.finish("pipe.X");
		
		ChirotatedImageGroup textureDualTurn = ChirotatedImageGroup.create("machine/pipe biturn.png");
		ipipe_DUALTURN = new BlockType()
		.texture("machine/pipe biturn.png")
		.describe("A pair of curved pipes.")
		.title("#ipipe-dual")
		.factory(() -> new DualPipe(Side.R, Side.D, ContentsBlocks.ipipe_DUALTURN, textureDualTurn))
		.finish("pipe.D");
		
		ChirotatedImageGroup textureFilter = ChirotatedImageGroup.create("machine/filter pipe.png");
		ipipe_FILTER = new BlockType()
		.texture("machine/filter pipe.png")
		.describe("A filtering pipe")
		.title("#ipipe-filter")
		.factory(() -> new PipeFilter(ContentsBlocks.ipipe_FILTER, textureFilter))
		.finish("pipe.F");
	}
	
	//Liquids
	/** The most basic fluid, used to make beer */
	@NN public static final Block water = new Block("liquid.water",
		PropertyExtension.translateTitle("#water"),
		PropertyExtension.setTextureAsset("liquid/water.png")
	);
	/** Currently unused */
	@NN public static final Block lava = new Block("liquid.lava",
		PropertyExtension.translateTitle("#lava"),
		PropertyExtension.setTextureAsset("liquid/lava.png")
	);
	/** A gas created by boiling water, currently unused */
	@NN public static final Block steam = new Block("liquid.steam",
		PropertyExtension.translateTitle("#steam"),
		PropertyExtension.setTextureAsset("liquid/steam.png")
	);
	
	//Non-electic processing machines
	/** A basic, non-electric furnace capable of ULV recipes */
	@NN public static final BlockType FURNACE = new BlockType()
			.title("#furnace")
			.factory(Furnace::new)
			.texture(Furnace.TEXTURE_INERT)
			.finish("industry.furnace");
	@NN public static final BlockType AUTOCRAFTER1 = createAutoCrafter(1, 64);
	@NN public static final BlockType AUTOCRAFTER2 = createAutoCrafter(2, 16);
	@NN public static final BlockType AUTOCRAFTER3 = createAutoCrafter(3, 4);
	@NN public static final BlockType AUTOCRAFTER4 = createAutoCrafter(4, 1);
	/** The Timed Ingredient Packet Dispatcher */
	@NN public static final BlockType TIPD = new BlockType()
			.title("#TIPD")
			.factory(mmb.content.imachine.tipd.TIPD::new)
			.texture("machine/rationer.png")
			.finish("industry.TIPD");
	
	@NN private static BlockType createAutoCrafter(int index, int delay) {
		BlockType result = new BlockType();
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
	
	/** Initializes blocks */
	public static void init() {
		//initialization method
	}
	
	@NN private static BlockType coalgen(int mul, VoltageTier volt, BufferedImage texture, int n) {
		BlockType type = new BlockType();
		return type
				.title((mul==2?GlobalSettings.$res("machine-turbogen"):GlobalSettings.$res("machine-coalgen"))+" "+n)
				.factory(() -> new BlockGeneratorSolid(mul, volt, type))
				.texture(texture)
				.finish((mul==2?"elec.turbogen":"elec.coalgen")+n);
	}
	@NN private static BlockType chest(double capacity, String texture, String id, int n) {
		final String descr1 = GlobalSettings.$res("descr-chest1");
		final String descr2 = GlobalSettings.$res("descr-chest2");
		BlockType type = new BlockType(id);
		Items.tagItem("chest", type);
		BufferedImage image = Textures.get(texture);
		type.setTitle(GlobalSettings.$res("chest")+" #"+n);
		type.setDescription(descr1+" "+capacity+" "+descr2);
		type.setBlockFactory(json -> Chest.load());
		return type
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
	}
}
