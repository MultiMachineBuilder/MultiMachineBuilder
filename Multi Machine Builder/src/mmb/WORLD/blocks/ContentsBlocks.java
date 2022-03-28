/**
 * 
 */
package mmb.WORLD.blocks;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;

import mmb.DATA.contents.texture.Textures;
import mmb.GRAPHICS.texgen.TexGen;
import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.block.Drop;
import mmb.WORLD.blocks.actuators.ActuatorClick;
import mmb.WORLD.blocks.actuators.ActuatorPlaceBlock;
import mmb.WORLD.blocks.actuators.ActuatorRotations;
import mmb.WORLD.blocks.agro.Crop;
import mmb.WORLD.blocks.chest.Chest;
import mmb.WORLD.blocks.gates.MonoGate.MonoGateType;
import mmb.WORLD.blocks.gates.AlwaysTrue;
import mmb.WORLD.blocks.gates.BiGate.BiGateType;
import mmb.WORLD.blocks.gates.Randomizer;
import mmb.WORLD.blocks.gates.UniformRandom;
import mmb.WORLD.blocks.gates.AbstractStateGate.StateGateType;
import mmb.WORLD.blocks.ipipe.ItemTransporter;
import mmb.WORLD.blocks.machine.Collector;
import mmb.WORLD.blocks.machine.CycleAssembler;
import mmb.WORLD.blocks.machine.FurnacePlus;
import mmb.WORLD.blocks.machine.Nuker;
import mmb.WORLD.blocks.machine.PlaceIncomingItems;
import mmb.WORLD.blocks.machine.line.AutoCrafter;
import mmb.WORLD.blocks.machine.line.Furnace;
import mmb.WORLD.blocks.machine.manual.Crafting;
import mmb.WORLD.blocks.machine.manual.PickaxeWorkbench;
import mmb.WORLD.blocks.ppipe.JoiningPlayerPipe;
import mmb.WORLD.blocks.ppipe.PlayerPipe;
import mmb.WORLD.blocks.ppipe.PlayerPipeEntry;
import mmb.WORLD.blocks.ppipe.TwinPlayerPipe;
import mmb.WORLD.contentgen.ElectricMachineGroup;
import mmb.WORLD.contentgen.Materials;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.crafting.recipes.ComplexCatalyzedProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.ComplexProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.SimpleProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.StackedProcessingRecipeGroup;
import mmb.WORLD.electric.InfiniteGenerator;
import mmb.WORLD.electric.PowerLoad;
import mmb.WORLD.electric.PowerMeter;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.electromachine.AlloySmelter;
import mmb.WORLD.electromachine.CoalGen;
import mmb.WORLD.electromachine.ElectroFurnace;
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
	private static final Debugger debug = new Debugger("BLOCKS");
	
	//Simple blocks
	@Nonnull public static final Block air = createAir();
	@Nonnull private static Block createAir() {
		debug.printl("Creating blocks");
		Block result = new Block();
		result.texture(Color.CYAN)
		.leaveBehind(result)
		.title("Air")
		.finish("mmb.air");
		result.setSurface(true);
		return result;
	}
	@Nonnull public static final Block grass = createGrass(); //REQUIRES SPECIAL INIT
	@Nonnull private static Block createGrass() {
		Block grass = new Block();
		grass.texture("grass.png")
		.leaveBehind(air)
		.title("Grass")
		.describe("A default block in the world")
		.finish("mmb.grass");
		grass.setSurface(true);
		return grass;
	}
	
	//WireWorld wires
	/**
	 * A WireWorld cell.
	 * This block turns into head if it is provided with 1 or 2 signals in 8 block neighborhood.
	 */
	@SuppressWarnings("null")
	@Nonnull public static final BlockEntityType ww_wire = new BlockEntityType()
		.texture(Color.ORANGE)
		.title("WireWorld cell")
		.factory(WWWire::new)
		.finish("wireworld.wire");
	/** This block emits a signal, and it turns into a tail */
	@SuppressWarnings("null")
	@Nonnull public static final BlockEntityType ww_head = new BlockEntityType()
		.texture(Color.WHITE)
		.title("WireWorld head")
		.factory(WWHead::new)
		.leaveBehind(ww_wire)
		.finish("wireworld.head");
	/** This block is left by a head and it turns into wire. This block does not emit any signal. */
	@SuppressWarnings("null")
	@Nonnull public static final BlockEntityType ww_tail = new BlockEntityType()
		.texture(Color.BLUE)
		.title("WireWorld head")
		.factory(WWTail::new)
		.leaveBehind(ww_wire)
		.finish("wireworld.tail");
	
	//WireWorld output devices
	/** This block logs specified message if it is activated by a gate, wire or other signal source */
	@Nonnull public static final BlockEntityType ww_chatter = new BlockEntityType()
		.texture("printer.png")
		.title("Chatbox")
		.factory(WWChatter::new)
		.describe("A block which prints out its text, when activated by a signal")
		.finish("wireworld.chatter");
	
	//Simple blocks
	/** A block of wood planks */
	@Nonnull public static final Block plank = new Block()
			.texture("plank.png")
			.title("Wooden planks")
			.finish("mmb.planks");
	@Nonnull public static final Block stone = new Block()
			.texture("stone.png")
			.title("Stone")
			.finish("mmb.stone");
	@Nonnull public static final Block leaves = new Block()
			.texture("block/leaves.png")
			.title("Leaves")
			.finish("mmb.leaves");

	@Nonnull public static final Block coal_ore = new Block()
			.texture(TexGen.genOre(Color.BLACK))
			.title("Coal ore")
			.finish("ore.coal");
	@Nonnull public static final Block diamond_ore = new Block()
			.texture(TexGen.genOre(Color.CYAN))
			.title("Diamond ore")
			.finish("ore.diamond");
	
	@Nonnull public static final Block crafting = new Block()
			.texture("crafting.png")
			.title("Assembly Table")
			.finish("mmb.craft");
	@Nonnull public static final Block logs = new Block()
			.texture("log.png")
			.title("Log")
			.finish("mmb.tree");
	@Nonnull public static final Block sand = new Block()
			.texture("block/sand.png")
			.title("Sand")
			.finish("mmb.sand");
	@Nonnull public static final Block gravel = new Block()
			.texture("block/gravel.png")
			.title("Gravel")
			.finish("mmb.gravel");
	/** A block, which upon loading crashes the game*/
	@Nonnull public static final Block COL = new COLBlock()
			.texture("block/gravel.png")
			.title("Crash on load")
			.finish("REMOVE THIS");
	
	//Logic gates
	/** This block receives signals from DR and DR corners and outputs a signal if both inputs are active */
	@Nonnull public static final BlockEntityType AND = new BiGateType("logic/AND.png", (a, b) -> a && b)
		.title("AND")
		.finish("wireworld.and");
	/** This block receives signals from DR and DR corners and outputs a signal if any input is active */
	@Nonnull public static final BlockEntityType OR = new BiGateType("logic/OR.png", (a, b) -> a || b)
		.title("OR")
		.finish("wireworld.or");
	/** This block receives signals from DR and DR corners and outputs a signal if only one input are active */
	@Nonnull public static final BlockEntityType XOR = new BiGateType("logic/XOR.png", (a, b) -> a ^ b)
		.title("XOR")
		.finish("wireworld.xor");
	/** This block emits a pulse when pressed by a player or Block Clicking Claw */
	@Nonnull public static final BlockEntityType BUTTON = new BlockEntityType()
			.title("Button")
			.factory(BlockButton::new)
			.texture("logic/button.png")
			.finish("wireworld.button");
	/** This block toggles state when clicked by a player or Block Clicking Claw */
	@Nonnull public static final BlockEntityType TOGGLE = new StateGateType("logic/toggle on.png","logic/toggle off.png","logic/toggle inert.png",(a,s) -> {
		boolean s2 = s ^ a;
		return (byte) (s2?3:0);
	}).title("Toggle Latch")
			.title("Toggle Latch")
			.finish("wireworld.toggle");
	/** Recreates the provided signal*/
	@Nonnull public static final BlockEntityType YES = new MonoGateType("logic/YES.png", a -> a)
			.title("YES")
			.finish("wireworld.yes");
	/** Negates provided signal */
	@Nonnull public static final BlockEntityType NOT = new MonoGateType("logic/NOT.png", a -> !a)
			.title("NOT")
			.finish("wireworld.not");
	/**
	 * Sends a random signal if powered
	 * Else does not send signal
	 */
	@Nonnull public static final BlockEntityType RANDOMCTRL= new MonoGateType("logic/randomctrl.png", a -> a && Math.random() < 0.5)
			.title("Random if")
			.finish("wireworld.randomctrl");
	
	//Power generators
	@Nonnull public static final BlockEntityType COALGEN1 = coalgen(VoltageTier.V1, CoalGen.img, "Furnace Generator I", "elec.coalgen1");
	@Nonnull public static final BlockEntityType COALGEN2 = coalgen(VoltageTier.V2, CoalGen.img1, "Furnace Generator II", "elec.coalgen2");
	@Nonnull public static final BlockEntityType COALGEN3 = coalgen(VoltageTier.V3, CoalGen.img2, "Furnace Generator III", "elec.coalgen3");
	/** A series of 9 infinite power generators. They are used for testing.*/
	@Nonnull public static final ElectricMachineGroup infinigens =
			new ElectricMachineGroup(Textures.get("machine/power/infinity.png"), type -> new InfiniteGenerator(type.volt, type), "Infinite generator", "infinigen");
	
	//Electrical equipment
	@Nonnull public static final BlockEntityType PMETER = new BlockEntityType()
			.title("Power meter")
			.factory(PowerMeter::new)
			.texture("machine/power/pmeter.png")
			.finish("elec.meter");
	@Nonnull public static final BlockEntityType LOAD = new BlockEntityType()
			.title("Electrical load")
			.factory(PowerLoad::new)
			.texture("machine/power/load.png")
			.finish("elec.load");
	
	//Modular machines
	@Nonnull public static final BlockEntityType EFURNACE = new BlockEntityType()
			.title("Electrical furnace")
			.factory(FurnacePlus::new)
			.texture("machine/esmelter.png")
			.finish("elec.furnace");
	@Nonnull public static final BlockEntityType NUKEGEN = new BlockEntityType()
			.title("Nuclear reactor")
			.factory(Nuker::new)
			.texture("machine/power/nuke reactor.png")
			.finish("nuke.generator");
	
	/** Always provides 'true' signal */
 	@Nonnull public static final Block TRUE = new AlwaysTrue()
			.title("Always True")
			.texture("logic/true.png")
			.finish("wireworld.true");
	/** Generates a random signal for every neighbor */
	@Nonnull public static final Block RANDOM = new Randomizer()
			.texture("logic/random.png")
			.title("Random")
			.finish("wireworld.random");
	
	
	/** Enabled switch */
	@Nonnull public static final Block ON = new OnToggle()
			.texture("logic/on.png")
			.title("Active Switch")
			.finish("wireworld.on");
	/** Disabled switch */
	@Nonnull public static final Block OFF = new OffToggle()
			.texture("logic/off.png")
			.title("Inactive Switch")
			.finish("wireworld.off");
	@Nonnull public static final BlockEntityType URANDOM = new BlockEntityType()
			.title("Uniform Random")
			.factory(UniformRandom::new)
			.texture("logic/urandom.png")
			.finish("wireworld.urandom");
	@Nonnull public static final BlockEntityType LAMP = new BlockEntityType()
			.title("Lamp")
			.factory(Lamp::new)
			.texture("logic/off lamp.png")
			.finish("wireworld.lamp");
	@Nonnull public static final BlockEntityType PLACER = new BlockEntityType()
			.title("Creative Block Placer")
			.factory(ActuatorPlaceBlock::new)
			.texture("machine/placer.png")
			.finish("machines.placer");
	@Nonnull public static final BlockEntityType CLICKER = new BlockEntityType()
			.title("Block Clicking Claw")
			.factory(ActuatorClick::new)
			.texture("machine/claw.png")
			.finish("machines.clicker");
	@Nonnull public static final BlockEntityType ROTATOR = new BlockEntityType()
			.title("Block Rotator")
			.factory(ActuatorRotations::new)
			.texture("machine/CW.png")
			.finish("machines.rotator");
	/**
	 * A chest stores items of mutiple types
	 */
	@Nonnull public static final BlockEntityType CHEST = new BlockEntityType()
			.title("Chest")
			.factory(Chest::new)
			.texture("machine/chest1.png")
			.finish("chest.beginning");
	@Nonnull public static final BlockEntityType IMOVER = new BlockEntityType()
			.title("Item Mover")
			.factory(ItemTransporter::new)
			.texture(ItemTransporter.TEXTURE)
			.finish("itemsystem.mover");
	
	@Nonnull public static final BlockEntityType PLACEITEMS = new BlockEntityType()
			.title("Item & Block Placing Machine")
			.factory(PlaceIncomingItems::new)
			.texture("machine/block place interface.png")
			.finish("industry.placeitems");
	@Nonnull public static final BlockEntityType COLLECTOR = new BlockEntityType()
			.title("Dropped item collector")
			.factory(Collector::new)
			.texture("machine/vacuum.png")
			.finish("industry.collector");
	
	//Crops
	@Nonnull public static final BlockEntityType AGRO_COAL =
			crop(1500, coal_ore, "Coal crop", TexGen.genCrop(Materials.colorCoal), "crop.coal");
	@Nonnull public static final BlockEntityType AGRO_TREE =
			crop(1500, logs, "Tree", Textures.get("block/tree.png"), "crop.tree");
	
	//Automatic processing machines
	@Nonnull public static final BlockEntityType FURNACE = new BlockEntityType()
			.title("Furnace")
			.factory(Furnace::new)
			.texture(Furnace.TEXTURE_INERT)
			.finish("industry.furnace");
	@Nonnull public static final BlockEntityType CYCLEASSEMBLY = new BlockEntityType()
			.title("Cyclic Assembler")
			.factory(CycleAssembler::new)
			.texture("machine/cyclic assembler.png")
			.finish("industry.cycle0");
	@Nonnull public static final Block PICKBUILDER = new PickaxeWorkbench()
			.texture("machine/pickaxe workbench.png")
			.title("Pickaxe workbench")
			.finish("machines.pickbuilder");
	@Nonnull public static final BlockEntityType AUTOCRAFTER = new BlockEntityType()
			.title("AutoCrafter 3X3")
			.factory(AutoCrafter::new)
			.texture("machine/AutoCrafter 1.png")
			.finish("industry.autocraft1");
	
	//Electrical processing machines
	@Nonnull public static final ElectricMachineGroup efurnace = machinesSimple("machine/electrosmelter.png", Craftings.smelting, "Electric furnace", "electrofurnace");
	@Nonnull public static final ElectricMachineGroup bcrusher = machinesSimple("machine/pulverizer.png", Craftings.crusher, "Crusher", "crusher");
	@Nonnull public static final ElectricMachineGroup bcmill = machinesSimple("machine/cluster mill.png", Craftings.clusterMill, "Cluster mill", "clustermill");
	@Nonnull public static final ElectricMachineGroup bwiremill = machinesSimple("machine/wiremill.png", Craftings.wiremill, "Wiremill", "wiremill");
	@Nonnull public static final ElectricMachineGroup balloyer = machinesComplex("machine/alloyer.png", Craftings.alloyer, "Alloy smelter", "alloyer");
	@Nonnull public static final ElectricMachineGroup bassembly = machinesAssembly("machine/machinemaker.png", Craftings.assembler, "Machine Assembler", "assembler");
	@Nonnull public static final ElectricMachineGroup bsplitter = machinesSimple("machine/splitter.png", Craftings.splitter, "Material Splitter", "spllitter", 0.1);
	@Nonnull public static final ElectricMachineGroup bsplicer = machinesStacked("machine/splicer.png", Craftings.combiner, "Material Combiner", "splicer", 0.1);
	
	//Player pipes
	@Nonnull public static final BlockEntityType PPIPE_lin = ppipe(1, Side.U, Side.D, "machine/ppipe straight.png", "Player Pipe - straight", "playerpipe.straight");
	@Nonnull public static final BlockEntityType PPIPE_bend = ppipe(0.8, Side.R, Side.D, "machine/ppipe turn.png", "Player Pipe - turn", "playerpipe.bend");
	@Nonnull public static final BlockEntityType PPIPE_lin2 = ppipe2(1, Side.U, Side.D, Side.L, Side.R, "machine/ppipe cross.png", "Player Pipe - crossover", "playerpipe.straight2");
	@Nonnull public static final BlockEntityType PPIPE_bend2 = ppipe2(0.8, Side.R, Side.D, Side.L, Side.U, "machine/ppipe biturn.png", "Player Pipe - two bends", "playerpipe.bend2");
	@Nonnull public static final BlockEntityType PPIPE_cap = new BlockEntityType()
			.title("Player Pipe - end")
			.factory(PlayerPipeEntry::new)
			.texture("machine/pipe exit.png")
			.finish("playerpipe.end");
	@Nonnull public static final BlockEntityType PPIPE_join = ppipea(1, Side.U, "machine/ppipe adjoin.png","Player Pipe - adjoin" ,"playerpipe.adj");
	@Nonnull public static final BlockEntityType PPIPE_join2 = ppipea(0.8, Side.L, "machine/ppipe adjoin2.png","Player Pipe - wye" ,"playerpipe.adj2");
	
	//Liquids
	@Nonnull public static final Block water = new Block()
	.texture("liquid/water.png")
	.title("Water")
	.finish("liquid.water");
	@Nonnull public static final Block lava = new Block()
	.texture("liquid/lava.png")
	.title("Lava")
	.finish("liquid.lava");
	@Nonnull public static final Block steam = new Block()
	.texture("liquid/steam.png")
	.title("Steam")
	.finish("liquid.steam");
	@Nonnull public static final Block clay = new Block()
	.texture("block/clay.png")
	.title("Clay")
	.finish("mmb.clay");
	/** Initializes blocks */
	public static void init() {
		//initialization method
	}

	//Reusable block methods
	@Nonnull
	public static BlockEntityType crop(int duration, Drop cropDrop, String title, BufferedImage texture, String id) {
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
	@Nonnull private static BlockEntityType coalgen(VoltageTier volt, BufferedImage texture,String title, String id) {
		BlockEntityType type = new BlockEntityType();
		return type
				.title(title)
				.factory(() -> new CoalGen(volt, type))
				.texture(texture)
				.finish(id);
	}
	@Nonnull private static ElectricMachineGroup machinesSimple(String texture, SimpleProcessingRecipeGroup group, String title, String id) {
		return machinesSimple(texture, group, title, id, 1);
	}
	@Nonnull private static ElectricMachineGroup machinesComplex(String texture, ComplexProcessingRecipeGroup group, String title, String id) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new AlloySmelter(type, group), title, id);
	}
	@Nonnull private static ElectricMachineGroup machinesAssembly(String texture, ComplexCatalyzedProcessingRecipeGroup group, String title, String id) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new MachineAssembler(type, group), title, id);
	}
	@Nonnull private static ElectricMachineGroup machinesSimple(String texture, SimpleProcessingRecipeGroup group, String title, String id, double d) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new ElectroFurnace(type, group), title, id, d);
	}
	@Nonnull private static ElectricMachineGroup machinesStacked(String texture, StackedProcessingRecipeGroup group, String title, String id) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new Splicer(type, group), title, id);
	}
	@Nonnull private static ElectricMachineGroup machinesStacked(String texture, StackedProcessingRecipeGroup group, String title, String id, double power) {
		return new ElectricMachineGroup(Textures.get(texture), type -> new Splicer(type, group), title, id, power);
	}
	static {
		Items.tagItems("wireworld", ww_wire, ww_head, ww_tail, ww_chatter,
				AND, OR, XOR, BUTTON, TOGGLE, YES, NOT, RANDOMCTRL, TRUE, RANDOM, ON, OFF, URANDOM, LAMP);
		Items.tagItems("player-pipe", PPIPE_lin, PPIPE_bend, PPIPE_lin2, PPIPE_bend2, PPIPE_join, PPIPE_join2, PPIPE_cap);
		Items.tagItem("workbench", PICKBUILDER);
		Items.tagItems("workbench", Crafting.types);
		Items.tagItems("fluid", water, lava, steam);
		Items.tagItems("special", COL, air, grass);
		Items.tagItems("basic", air, grass, plank, stone, leaves, logs, sand, gravel, clay, water);
	}
}
