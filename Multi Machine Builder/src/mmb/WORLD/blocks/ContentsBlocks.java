/**
 * 
 */
package mmb.WORLD.blocks;

import java.awt.Color;
import javax.annotation.Nonnull;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.block.Drop;
import mmb.WORLD.blocks.actuators.ActuatorClick;
import mmb.WORLD.blocks.actuators.ActuatorPlaceBlock;
import mmb.WORLD.blocks.actuators.ActuatorRotations;
import mmb.WORLD.blocks.agro.Crop;
import mmb.WORLD.blocks.chest.Chest;
import mmb.WORLD.blocks.gates.ANDGate;
import mmb.WORLD.blocks.gates.AlwaysTrue;
import mmb.WORLD.blocks.gates.FlipGate;
import mmb.WORLD.blocks.gates.NOTGate;
import mmb.WORLD.blocks.gates.ORGate;
import mmb.WORLD.blocks.gates.RandomGate;
import mmb.WORLD.blocks.gates.Randomizer;
import mmb.WORLD.blocks.gates.UniformRandom;
import mmb.WORLD.blocks.gates.XORGate;
import mmb.WORLD.blocks.gates.YESGate;
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
import mmb.WORLD.blocks.ppipe.PlayerPipe;
import mmb.WORLD.blocks.ppipe.PlayerPipeEntry;
import mmb.WORLD.electric.Conduit;
import mmb.WORLD.electric.ElecRenderer;
import mmb.WORLD.electric.InfiniteGenerator;
import mmb.WORLD.electric.PowerLoad;
import mmb.WORLD.electric.PowerMeter;
import mmb.WORLD.rotate.ChirotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.texture.BlockDrawer;
import mmb.debug.Debugger;

/**
 * @author oskar
 * This class contains blocks in the new world system
 */
public class ContentsBlocks {
	private static final Debugger debug = new Debugger("BLOCKS");

	//WireWorld wires
	/** A WireWorld cell */
	@SuppressWarnings("null")
	@Nonnull public static final BlockEntityType ww_wire = new BlockEntityType()
		.texture(Color.ORANGE)
		.title("WireWorld cell")
		.factory(WWWire::new)
		.finish("wireworld.wire");
		@SuppressWarnings("null")
	@Nonnull public static final BlockEntityType ww_head = new BlockEntityType()
		.texture(Color.WHITE)
		.title("WireWorld head")
		.factory(WWHead::new)
		.leaveBehind(ww_wire)
		.finish("wireworld.head");
	@SuppressWarnings("null")
	@Nonnull public static final BlockEntityType ww_tail = new BlockEntityType()
		.texture(Color.BLUE)
		.title("WireWorld head")
		.factory(WWTail::new)
		.leaveBehind(ww_wire)
		.finish("wireworld.tail");
	
	//WireWorld output devices
	@Nonnull public static final BlockEntityType ww_chatter = new BlockEntityType()
		.texture("printer.png")
		.title("Chatbox")
		.factory(WWChatter::new)
		.describe("A block which prints out its text, when activated by a signal")
		.finish("wireworld.chatter");
	
	@Nonnull public static final Block air, grass; //REQUIRES SPECIAL INIT
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
	@Nonnull public static final Block iron_ore = new Block()
			.texture("block/iron ore.png")
			.title("Iron ore")
			.finish("mmb.iore");
	@Nonnull public static final Block copper_ore = new Block()
			.texture("block/copper ore.png")
			.title("Copper ore")
			.finish("mmb.cpore");
	@Nonnull public static final Block silicon_ore = new Block()
			.texture("block/silicon ore.png")
			.title("Silicon ore")
			.finish("mmb.silicon_ore");
	@Nonnull public static final Block gold_ore = new Block()
			.texture("block/gold ore.png")
			.title("Golden ore")
			.finish("mmb.goldore");
	@Nonnull public static final Block silver_ore = new Block()
			.texture("block/silver ore.png")
			.title("Silver ore")
			.finish("mmb.silverore");
	@Nonnull public static final Block uranium_ore = new Block()
			.texture("block/uranium ore.png")
			.title("Uranium ore")
			.finish("mmb.uraniumore");
	@Nonnull public static final Block coal_ore = new Block()
			.texture("block/coal ore.png")
			.title("Coal ore")
			.finish("ore.coal");
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
	@Nonnull public static final BlockEntityType AND = new BlockEntityType()
			.title("AND")
			.factory(ANDGate::new)
			.texture("logic/AND.png")
			.finish("wireworld.and");
	@Nonnull public static final BlockEntityType OR = new BlockEntityType()
			.title("OR")
			.factory(ORGate::new)
			.texture("logic/OR.png")
			.finish("wireworld.or");
	@Nonnull public static final BlockEntityType XOR = new BlockEntityType()
			.title("XOR")
			.factory(XORGate::new)
			.texture("logic/XOR.png")
			.finish("wireworld.xor");
	@Nonnull public static final BlockEntityType BUTTON = new BlockEntityType()
			.title("Button")
			.factory(BlockButton::new)
			.texture("logic/button.png")
			.finish("wireworld.button");
	@Nonnull public static final BlockEntityType TOGGLE = new BlockEntityType()
			.title("Toggle Latch")
			.factory(FlipGate::new)
			.texture("logic/toggle inert.png")
			.finish("wireworld.toggle");
	@Nonnull public static final BlockEntityType CONDUIT =
			conduit("Medium Power Cable", 1_000_000, ElecRenderer.render, "elec.mediumwire");
	@Nonnull public static final BlockEntityType CONDUITA =
			conduit("Large Power Cable", 3_000_000, ElecRenderer.renderthick, "elec.largewire");
	@Nonnull public static final BlockEntityType INFINIGEN = new BlockEntityType()
			.title("Infinite Generator (Creative only)")
			.factory(InfiniteGenerator::new)
			.texture("machine/power/infinity.png")
			.finish("elec.infinite");
	@Nonnull public static final BlockEntityType NUKEGEN = new BlockEntityType()
			.title("Nuclear reactor")
			.factory(Nuker::new)
			.texture("machine/power/nuke reactor.png")
			.finish("nuke.generator");
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
	@Nonnull public static final BlockEntityType EFURNACE = new BlockEntityType()
			.title("Electrical furnace")
			.factory(FurnacePlus::new)
			.texture("machine/esmelter.png")
			.finish("elec.furnace");
	@Nonnull public static final BlockEntityType CYCLEASSEMBLY = new BlockEntityType()
			.title("Cyclic Assembler")
			.factory(CycleAssembler::new)
			.texture("machine/cyclic assembler.png")
			.finish("industry.cycle0");
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
	
	/** Recreates the provided signal*/
	@Nonnull public static final BlockEntityType YES = new BlockEntityType()
			.title("YES")
			.factory(YESGate::new)
			.texture("logic/YES.png")
			.finish("wireworld.yes");
	/** Negates provided signal */
	@Nonnull public static final BlockEntityType NOT = new BlockEntityType()
			.title("NOT")
			.factory(NOTGate::new)
			.texture("logic/NOT.png")
			.finish("wireworld.not");
	/**
	 * Sends a random signal if powered
	 * Else does not send signal
	 */
	@Nonnull public static final BlockEntityType RANDOMCTRL= new BlockEntityType()
			.title("Random if")
			.factory(RandomGate::new)
			.texture("logic/randomctrl.png")
			.finish("wireworld.randomctrl");
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
	@Nonnull public static final BlockEntityType FURNACE = new BlockEntityType()
			.title("Furnace")
			.factory(Furnace::new)
			.texture(Furnace.TEXTURE_INERT)
			.finish("industry.furnace");
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
	@Nonnull public static final BlockEntityType AGRO_COPPPER =
			crop(1500, copper_ore, "Copper crop", BlockDrawer.ofImage(Textures.get("block/copper crop.png")), "crop.copper");
	@Nonnull public static final BlockEntityType AGRO_IRON =
			crop(1500, iron_ore, "Iron crop", BlockDrawer.ofImage(Textures.get("block/iron crop.png")), "crop.iron");
	@Nonnull public static final BlockEntityType AGRO_SILICON =
			crop(1500, silicon_ore, "Silicon crop", BlockDrawer.ofImage(Textures.get("block/silicon crop.png")), "crop.silicon");
	@Nonnull public static final BlockEntityType AGRO_GOLD =
			crop(1500, gold_ore, "Gold crop", BlockDrawer.ofImage(Textures.get("block/gold crop.png")), "crop.gold");
	@Nonnull public static final BlockEntityType AGRO_COAL =
			crop(1500, coal_ore, "Coal crop", BlockDrawer.ofImage(Textures.get("block/coal crop.png")), "crop.coal");
	@Nonnull public static final BlockEntityType AGRO_TREE =
			crop(1500, logs, "Tree", BlockDrawer.ofImage(Textures.get("block/tree.png")), "crop.tree");
	@Nonnull public static final Block PICKBUILDER = new PickaxeWorkbench()
			.texture("machine/pickaxe workbench.png")
			.title("Pickaxe workbench")
			.finish("machines.pickbuilder");
	@Nonnull public static final BlockEntityType AUTOCRAFTER = new BlockEntityType()
			.title("AutoCrafter 3X3")
			.factory(AutoCrafter::new)
			.texture("machine/AutoCrafter 1.png")
			.finish("industry.autocraft1");
	
	@Nonnull public static final BlockEntityType PPIPE_lin = ppipe(1, Side.U, Side.D, "machine/ppipe straight.png", "Player Pipe - straight", "playerpipe.straight");
	@Nonnull public static final BlockEntityType PPIPE_bend = ppipe(0.8, Side.R, Side.D, "machine/ppipe turn.png", "Player Pipe - turn", "playerpipe.bend");
	@Nonnull public static final BlockEntityType PPIPE_cap = new BlockEntityType()
			.title("Player Pipe - end")
			.factory(PlayerPipeEntry::new)
			.texture("machine/pipe exit.png")
			.finish("playerpipe.end");
	static {
		//REQUIRES SPECIAL INIT - SELF-REFERNECE
		debug.printl("Creating blocks");
		air = new Block();
		@SuppressWarnings("null")
		@Nonnull Color c = Color.CYAN;
		air.texture(c)
		.leaveBehind(air)
		.title("Air")
		.finish("mmb.air");
		air.setSurface(true);
		
		grass = new Block();
		grass.texture("grass.png")
		.leaveBehind(air)
		.title("Grass")
		.describe("A default block in the world")
		.finish("mmb.grass");
		grass.setSurface(true);
		//NO LONGER REQUIRES SPECIAL INIT		
		Crafting.init();
	}
	@Nonnull private static BlockEntityType conduit(String title, double pwr, BlockDrawer texture, String id) {
		BlockEntityType b = new BlockEntityType();
		return b.title(title)
				.factory(() -> new Conduit(b, pwr))
				.texture(texture)
				.finish(id);
	}
	/** Initializes blocks */
	public static void init() {
		//initialization method
	}

	@Nonnull private static BlockEntityType crop(int duration, Drop cropDrop, String title, BlockDrawer texture, String id) {
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
}
