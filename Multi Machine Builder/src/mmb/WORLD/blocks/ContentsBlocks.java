/**
 * 
 */
package mmb.WORLD.blocks;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.block.BlockFactory;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.actuators.ActuatorClick;
import mmb.WORLD.blocks.actuators.ActuatorPlaceBlock;
import mmb.WORLD.blocks.actuators.ActuatorRotations;
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
import mmb.WORLD.blocks.machine.Chest;
import mmb.WORLD.blocks.machine.Crafting;
import mmb.WORLD.blocks.machine.CycleAssembler;
import mmb.WORLD.blocks.machine.Furnace;
import mmb.WORLD.blocks.machine.FurnacePlus;
import mmb.WORLD.blocks.machine.ItemTransporter;
import mmb.WORLD.blocks.machine.Nuker;
import mmb.WORLD.electric.Conduit;
import mmb.WORLD.electric.ElecRenderer;
import mmb.WORLD.electric.InfiniteGenerator;
import mmb.WORLD.electric.PowerLoad;
import mmb.WORLD.electric.PowerMeter;
import mmb.WORLD.electric.TestElectricity;
import mmb.WORLD.texture.BlockDrawer;
import mmb.debug.Debugger;

/**
 * @author oskar
 * This class contains blocks in the new world system
 */
public class ContentsBlocks {
	private static final Debugger debug = new Debugger("BLOCKS");

	@SuppressWarnings("null")
	@Nonnull public static final BlockEntityType ww_wire = new BlockEntityType()
		.texture(Color.ORANGE)
		.title("WireWorld conductor")
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
	@Nonnull public static final Block crafting = new Block()
			.texture("crafting.png")
			.title("Assembly Table")
			.finish("mmb.craft");
	@Nonnull public static final Block logs = new Block()
			.texture("log.png")
			.title("Log")
			.finish("mmb.tree");
	@Nonnull public static final Block testelec = new TestElectricity()
			.texture("machine/autoassembly 1.png")
			.title("Electric Testing Tool")
			.finish("elec.test");
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
	/** Generates a random signal for every neighbour */
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
	public static final Block ON = new OnToggle()
			.texture("logic/on.png")
			.title("Active Switch")
			.finish("wireworld.on");
	/** Disabled switch */
	public static final Block OFF = new OffToggle()
			.texture("logic/off.png")
			.title("Inactive Switch")
			.finish("wireworld.off");
	public static final BlockEntityType URANDOM = new BlockEntityType()
			.title("Uniform Random")
			.factory(UniformRandom::new)
			.texture("logic/urandom.png")
			.finish("wireworld.urandom");
	public static final BlockEntityType LAMP = new BlockEntityType()
			.title("Lamp")
			.factory(Lamp::new)
			.texture("logic/off lamp.png")
			.finish("wireworld.lamp");
	public static final BlockEntityType PLACER = new BlockEntityType()
			.title("Block Placer")
			.factory(ActuatorPlaceBlock::new)
			.texture("machine/placer.png")
			.finish("machines.placer");
	public static final BlockEntityType CLICKER = new BlockEntityType()
			.title("Block Clicking Claw")
			.factory(ActuatorClick::new)
			.texture("machine/claw.png")
			.finish("machines.clicker");
	public static final BlockEntityType ROTATOR = new BlockEntityType()
			.title("Block Rotator")
			.factory(ActuatorRotations::new)
			.texture("machine/CW.png")
			.finish("machines.rotator");
	/**
	 * A chest stores items of mutiple types
	 */
	public static final BlockEntityType CHEST = new BlockEntityType()
			.title("Chest")
			.factory(Chest::new)
			.texture("machine/chest1.png")
			.finish("chest.beginning");
	public static final BlockEntityType IMOVER = new BlockEntityType()
			.title("Item Mover")
			.factory(ItemTransporter::new)
			.texture(ItemTransporter.TEXTURE)
			.finish("itemsystem.mover");
	public static final BlockEntityType FURNACE = new BlockEntityType()
			.title("Furnace")
			.factory(Furnace::new)
			.texture(Furnace.TEXTURE_INERT)
			.finish("industry.furnace");
	
	static {
		//REQUIRES SPECIAL INIT - SELF-REFERNECE
		debug.printl("Creating blocks");
		air = new Block();
		air.drawer = BlockDrawer.ofColor(Color.CYAN);
		air.leaveBehind = air;
		air.title = "Air";
		
		BlockDrawer tmp;
		BufferedImage i = Textures.nget("grass.png");
		if(i != null) tmp = BlockDrawer.ofImage(i);
		else tmp = BlockDrawer.ofColor(Color.GREEN);
		
		grass = new Block();
		grass.drawer = tmp;
		grass.leaveBehind = air;
		grass.title = "Grass";
		grass.setDescription("A default block in the world");
		//NO LONGER REQUIRES SPECIAL INIT
		
		//Register
		grass.register("mmb.grass");
		air.register("mmb.air");
		
		Crafting.setupRefsTable();
	}
	@Nonnull private static BlockEntityType conduit(String title, double pwr, BlockDrawer texture, String id) {
		BlockEntityType b = new BlockEntityType();
		/*return b.title("Medium Power Cable")
		.factory(condFact(() -> b, 1_000_000))
		.texture(ElecRenderer.render)
		.finish("elec.mediumwire");*/
		return b.title(title)
				.factory(condFact(b, pwr))
				.texture(texture)
				.finish(id);
	}
	@Nonnull private static BlockFactory condFact(BlockType type, double cap) {
		return (x, y, m) -> new Conduit(x, y, m, type, cap);
	}

}
