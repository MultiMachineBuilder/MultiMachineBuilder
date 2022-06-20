/**
 * 
 */
package mmb.WORLD.blocks;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;

import mmb.GlobalSettings;
import mmb.DATA.contents.Textures;
import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.blocks.actuators.ActuatorClick;
import mmb.WORLD.blocks.actuators.ActuatorPlaceBlock;
import mmb.WORLD.blocks.actuators.ActuatorRotations;
import mmb.WORLD.blocks.agro.Crop;
import mmb.WORLD.blocks.chest.Chest;
import mmb.WORLD.blocks.chest.Hopper;
import mmb.WORLD.blocks.gates.MonoGate.MonoGateType;
import mmb.WORLD.blocks.gates.AlwaysTrue;
import mmb.WORLD.blocks.gates.BiGate.BiGateType;
import mmb.WORLD.blocks.gates.Randomizer;
import mmb.WORLD.blocks.gates.UniformRandom;
import mmb.WORLD.blocks.gates.AbstractStateGate.StateGateType;
import mmb.WORLD.blocks.ipipe.DualPipe;
import mmb.WORLD.blocks.ipipe.IntersectingPipeExtractor;
import mmb.WORLD.blocks.ipipe.ItemTransporter;
import mmb.WORLD.blocks.ipipe.Pipe;
import mmb.WORLD.blocks.ipipe.PipeBinder;
import mmb.WORLD.blocks.machine.Collector;
import mmb.WORLD.blocks.machine.CycleAssembler;
import mmb.WORLD.blocks.machine.FurnacePlus;
import mmb.WORLD.blocks.machine.Nuker;
import mmb.WORLD.blocks.machine.PlaceIncomingItems;
import mmb.WORLD.blocks.machine.line.AutoCrafter;
import mmb.WORLD.blocks.machine.line.Furnace;
import mmb.WORLD.blocks.machine.manual.Crafting;
import mmb.WORLD.blocks.machine.manual.PickaxeWorkbench;
import mmb.WORLD.blocks.media.Speaker;
import mmb.WORLD.blocks.ppipe.JoiningPlayerPipe;
import mmb.WORLD.blocks.ppipe.PlayerPipe;
import mmb.WORLD.blocks.ppipe.PlayerPipeEntry;
import mmb.WORLD.blocks.ppipe.TwinPlayerPipe;
import mmb.WORLD.blocks.wireworld.BlockButton;
import mmb.WORLD.blocks.wireworld.Lamp;
import mmb.WORLD.blocks.wireworld.OffToggle;
import mmb.WORLD.blocks.wireworld.OnToggle;
import mmb.WORLD.blocks.wireworld.WWChatter;
import mmb.WORLD.blocks.wireworld.WWHead;
import mmb.WORLD.blocks.wireworld.WWTail;
import mmb.WORLD.blocks.wireworld.WWWire;
import mmb.WORLD.chance.Chance;
import mmb.WORLD.contentgen.ElectricMachineGroup;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.crafting.recipes.ComplexCatalyzedProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.ComplexProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.ElectroSimpleProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.StackedProcessingRecipeGroup;
import mmb.WORLD.electric.InfiniteGenerator;
import mmb.WORLD.electric.PowerLoad;
import mmb.WORLD.electric.PowerMeter;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.electromachine.AlloySmelter;
import mmb.WORLD.electromachine.CoalGen;
import mmb.WORLD.electromachine.ElectroFurnace;
import mmb.WORLD.electromachine.ElectroQuarry;
import mmb.WORLD.electromachine.MachineAssembler;
import mmb.WORLD.electromachine.Splicer;
import mmb.WORLD.item.Items;
import mmb.WORLD.rotate.ChirotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.debug.Debugger;

/**
 * @author oskar
 * This class contains blocks in the new world system
 */
public class ContentsBlocks {
	private ContentsBlocks() {}
	private static final Debugger debug = new Debugger("BLOCKS");
	
	//Primitive blocks
	@Nonnull public static final Block air = createAir();
	@Nonnull private static Block createAir() {
		debug.printl("Creating blocks");
		Block result = new Block();
		result.texture(Color.CYAN)
		.leaveBehind(result)
		.title("#air")
		.finish("mmb.air");
		result.setSurface(true);
		return result;
	}
	@Nonnull public static final Block grass = createGrass(); //REQUIRES SPECIAL INIT
	@Nonnull private static Block createGrass() {
		Block grass = new Block();
		grass.texture("grass.png")
		.leaveBehind(air)
		.title("#grass")
		.describe("A default block in the world")
		.finish("mmb.grass");
		grass.setSurface(true);
		return grass;
	}
	
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
	@Nonnull public static final Block crafting = new Block()
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
	/** A block, which upon loading crashes the game*/
	@Nonnull public static final Block COL = new COLBlock()
			.texture("block/gravel.png")
			.title("#CoL")
			.finish("REMOVE THIS");
	//WireWorld wires
	/**
	 * A WireWorld cell.
	 * This block turns into head if it is provided with 1 or 2 signals in 8 block neighborhood.
	 */
	@SuppressWarnings("null")
	@Nonnull public static final BlockEntityType ww_wire = new BlockEntityType()
		.texture(Color.ORANGE)
		.title("#ww-c")
		.factory(WWWire::new)
		.finish("wireworld.wire");
	/** This block emits a signal, and it turns into a tail */
	@SuppressWarnings("null")
	@Nonnull public static final BlockEntityType ww_head = new BlockEntityType()
		.texture(Color.WHITE)
		.title("#ww-h")
		.factory(WWHead::new)
		.leaveBehind(ww_wire)
		.finish("wireworld.head");
	/** This block is left by a head and it turns into wire. This block does not emit any signal. */
	@SuppressWarnings("null")
	@Nonnull public static final BlockEntityType ww_tail = new BlockEntityType()
		.texture(Color.BLUE)
		.title("#ww-t")
		.factory(WWTail::new)
		.leaveBehind(ww_wire)
		.finish("wireworld.tail");
	
	//Emitters
	/** Generates a single random signal for every neighbor */
	@Nonnull public static final BlockEntityType URANDOM = new BlockEntityType()
			.title("#ww-ur")
			.factory(UniformRandom::new)
			.texture("logic/urandom.png")
			.finish("wireworld.urandom");
	/** Always provides 'true' signal */
 	@Nonnull public static final Block TRUE = new AlwaysTrue()
			.title("#ww-true")
			.texture("logic/true.png")
			.finish("wireworld.true");
	/** Generates a separate random signal for every neighbor */
	@Nonnull public static final Block RANDOM = new Randomizer()
			.texture("logic/random.png")
			.title("#ww-rnd")
			.finish("wireworld.random");
	
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
	@Nonnull public static final BlockEntityType PLACER = new BlockEntityType()
			.title("#ww-placer")
			.factory(ActuatorPlaceBlock::new)
			.texture("machine/placer.png")
			.finish("machines.placer");
	@Nonnull public static final BlockEntityType CLICKER = new BlockEntityType()
			.title("#ww-click")
			.factory(ActuatorClick::new)
			.texture("machine/claw.png")
			.finish("machines.clicker");
	@Nonnull public static final BlockEntityType ROTATOR = new BlockEntityType()
			.title("#ww-rot")
			.factory(ActuatorRotations::new)
			.texture("machine/CW.png")
			.finish("machines.rotator");

	//Power generators
	@Nonnull public static final BlockEntityType COALGEN1 = coalgen(VoltageTier.V1, CoalGen.img, 1);
	@Nonnull public static final BlockEntityType COALGEN2 = coalgen(VoltageTier.V2, CoalGen.img1, 2);
	@Nonnull public static final BlockEntityType COALGEN3 = coalgen(VoltageTier.V3, CoalGen.img2, 3);
	/** A series of 9 infinite power generators. They are used for testing.*/
	@Nonnull public static final ElectricMachineGroup infinigens =
			new ElectricMachineGroup(Textures.get("machine/power/infinity.png"), type -> new InfiniteGenerator(type.volt, type), "infinigen");
	
	//Electrical equipment
	@Nonnull public static final BlockEntityType PMETER = new BlockEntityType()
			.title("#elec-meter")
			.factory(PowerMeter::new)
			.texture("machine/power/pmeter.png")
			.finish("elec.meter");
	@Nonnull public static final BlockEntityType LOAD = new BlockEntityType()
			.title("#elec-load")
			.factory(PowerLoad::new)
			.texture("machine/power/load.png")
			.finish("elec.load");
	
	//Modular machines
	@Nonnull public static final BlockEntityType EFURNACE = new BlockEntityType()
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
	/**
	 * A chest stores items of mutiple types
	 */
	@Nonnull public static final BlockEntityType CHEST = new BlockEntityType()
			.title("#chest")
			.factory(Chest::new)
			.texture("machine/chest1.png")
			.finish("chest.beginning");
	/**
	 * A chest that auto-inserts items
	 */
	@Nonnull public static final BlockEntityType HOPPER = new BlockEntityType()
			.title("#chest-hopper")
			.factory(() -> new Hopper((byte) 1))
			.texture("machine/hopper.png")
			.finish("chest.hopper1");
	@Nonnull public static final BlockEntityType HOPPER_suck = new BlockEntityType()
			.title("#chest-sucker")
			.factory(() -> new Hopper((byte) 2))
			.texture("machine/sucker.png")
			.finish("chest.hopper2");
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
			.factory(Collector::new)
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
	}
	
	//Crops
	@Nonnull public static final BlockEntityType AGRO_TREE =
			crop(1500, logs, "#machine-tree", Textures.get("block/tree.png"), "crop.tree");
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
	@Nonnull public static final Block PICKBUILDER = new PickaxeWorkbench()
			.texture("machine/pickaxe workbench.png")
			.title("#pickbuilder")
			.finish("machines.pickbuilder");
	@Nonnull public static final BlockEntityType AUTOCRAFTER = new BlockEntityType()
			.title("#autocraft1")
			.factory(AutoCrafter::new)
			.texture("machine/AutoCrafter 1.png")
			.finish("industry.autocraft1");
	
	//Electrical processing machines
	@Nonnull public static final ElectricMachineGroup efurnace = machinesSimple("machine/electrosmelter.png", Craftings.smelting, "electrofurnace");
	@Nonnull public static final ElectricMachineGroup bcrusher = machinesSimple("machine/pulverizer.png", Craftings.crusher, "crusher");
	@Nonnull public static final ElectricMachineGroup bcmill = machinesSimple("machine/cluster mill.png", Craftings.clusterMill, "clustermill");
	@Nonnull public static final ElectricMachineGroup bwiremill = machinesSimple("machine/wiremill.png", Craftings.wiremill, "wiremill");
	@Nonnull public static final ElectricMachineGroup balloyer = machinesComplex("machine/alloyer.png", Craftings.alloyer, "alloyer");
	@Nonnull public static final ElectricMachineGroup bassembly = machinesAssembly("machine/machinemaker.png", Craftings.assembler, "assembler");
	@Nonnull public static final ElectricMachineGroup bsplitter = machinesSimple("machine/splitter.png", Craftings.splitter, "spllitter", 0.1);
	@Nonnull public static final ElectricMachineGroup bsplicer = machinesStacked("machine/splicer.png", Craftings.combiner, "splicer", 0.1);
	@Nonnull public static final ElectricMachineGroup bquarry = createQuarry();
	
	//Player pipes
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
	/** Initializes blocks */
	public static void init() {
		//initialization method
	}

	//Multimedia devices
	@Nonnull public static final BlockEntityType SPEAKER = new BlockEntityType()
			.title("#multi-speaker")
			.factory(Speaker::new)
			.texture("machine/speaker 2.png")
			.finish("multi.speaker");
	
	//Reusable block methods
	@Nonnull
	public static BlockEntityType crop(int duration, Chance cropDrop, String title, BufferedImage texture, String id) {
		BlockEntityType result = new BlockEntityType();
		return result.title(title).factory(() -> new Crop(result, duration, cropDrop)).texture(texture).finish(id);
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
	@Nonnull private static BlockEntityType coalgen(VoltageTier volt, BufferedImage texture, int n) {
		BlockEntityType type = new BlockEntityType();
		return type
				.title(GlobalSettings.$res("machine-coalgen")+" "+n)
				.factory(() -> new CoalGen(volt, type))
				.texture(texture)
				.finish("elec.coalgen"+n);
	}
	@Nonnull private static ElectricMachineGroup machinesSimple(String texture, ElectroSimpleProcessingRecipeGroup group, String id) {
		return machinesSimple(texture, group, id, 1);
	}
	@Nonnull private static ElectricMachineGroup machinesComplex(String texture, ComplexProcessingRecipeGroup group, String id) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new AlloySmelter(type, group), id);
	}
	@Nonnull private static ElectricMachineGroup machinesAssembly(String texture, ComplexCatalyzedProcessingRecipeGroup group, String id) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new MachineAssembler(type, group), id);
	}
	@Nonnull private static ElectricMachineGroup machinesSimple(String texture, ElectroSimpleProcessingRecipeGroup group, String id, double d) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new ElectroFurnace(type, group), id, d);
	}
	@Nonnull private static ElectricMachineGroup machinesStacked(String texture, StackedProcessingRecipeGroup group, String id) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new Splicer(type, group), id);
	}
	@Nonnull private static ElectricMachineGroup machinesStacked(String texture, StackedProcessingRecipeGroup group, String id, double power) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new Splicer(type, group), id, power);
	}
	private static ElectricMachineGroup createQuarry() {
		return new ElectricMachineGroup(Textures.get("machine/quarry.png"), type -> new ElectroQuarry(type, Craftings.quarry), "quarry");
	}
	
	//Item tags
	static {
		Items.tagItems("wireworld", ww_wire, ww_head, ww_tail, ww_chatter,
				AND, OR, XOR, BUTTON, TOGGLE, YES, NOT, RANDOMCTRL, TRUE, RANDOM, ON, OFF, URANDOM, LAMP);
		Items.tagItems("player-pipe", PPIPE_lin, PPIPE_bend, PPIPE_lin2, PPIPE_bend2, PPIPE_join, PPIPE_join2, PPIPE_cap);
		Items.tagItem("workbench", PICKBUILDER);
		Items.tagItems("workbench", Crafting.types);
		Items.tagItems("fluid", water, lava, steam);
		Items.tagItems("special", COL, air, grass);
		Items.tagItems("basic", air, grass, plank, stone, leaves, logs, sand, gravel, clay, water);
		Items.tagItems("chest", CHEST, HOPPER, HOPPER_suck, HOPPER_both);
		Items.tagItems("shape-crop", AGRO_TREE);
		Items.tagItems("pipe",ipipe_STRAIGHT, ipipe_ELBOW, ipipe_IPE, ipipe_TOLEFT, ipipe_TORIGHT, ipipe_CROSS, ipipe_DUALTURN);
	}
}
