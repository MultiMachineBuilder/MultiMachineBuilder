/**
 * 
 */
package mmb.WORLD.blocks;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.blocks.actuators.ActuatorClick;
import mmb.WORLD.blocks.actuators.ActuatorPlaceBlock;
import mmb.WORLD.blocks.actuators.ActuatorRotations;
import mmb.WORLD.blocks.gates.ANDGate;
import mmb.WORLD.blocks.gates.NOTGate;
import mmb.WORLD.blocks.gates.ORGate;
import mmb.WORLD.blocks.gates.RandomGate;
import mmb.WORLD.blocks.gates.UniformRandom;
import mmb.WORLD.blocks.gates.XORGate;
import mmb.WORLD.blocks.gates.YESGate;
import mmb.debug.Debugger;

/**
 * @author oskar
 * This class contains blocks in the new world system
 */
public class ContentsBlocks {
	private static final Debugger debug = new Debugger("BLOCKS");

	@Nonnull public static final BlockEntityType
	ww_head, ww_tail, ww_wire,
	ww_chatter;
	
	@Nonnull public static final Block air, grass, plank, stone, leaves,
	iron_ore, copper_ore, silicon_ore, crafting, logs;
	
	@Nonnull public static final BlockEntityType
	AND, OR, XOR, BUTTON;
	
	/**
	 * Signal generators
	 */
	@Nonnull public static final Block TRUE, RANDOM;
	
	/**
	 * Unary gates
	 */
	@Nonnull public static final BlockEntityType
	YES, NOT, RANDOMCTRL;
	
	public static final Block ON, OFF;
	
	public static final BlockEntityType URANDOM, LAMP;
	
	public static final BlockEntityType PLACER, CLICKER, ROTATOR;
	
	static {	
		//Toolkit.getDefaultToolkit().beep();
		debug.printl("Creating blocks");
		air = new Block();
		air.drawer = BlockDrawer.ofColor(Color.CYAN);
		air.leaveBehind = air;
		air.title = "Air";
		
		BlockDrawer tmp;
		try {
			BufferedImage i = Textures.get("grass.png");
			tmp = BlockDrawer.ofImage(i);
		} catch (Exception e) {
			tmp = BlockDrawer.ofColor(Color.GREEN);
			debug.pstm(e, "Failed to load grass texture, switching to plain color");
		}
		
		grass = new Block();
		grass.drawer = tmp;
		grass.leaveBehind = air;
		grass.title = "Grass";
		
		ww_wire = new BlockEntityType();
		ww_wire.drawer = BlockDrawer.ofColor(Color.ORANGE);
		ww_wire.title = "WireWorld conductor";
		ww_wire.setFactory(WWWire::new);
		
		ww_head = new BlockEntityType();
		ww_head.drawer = BlockDrawer.ofColor(Color.BLUE);
		ww_head.leaveBehind = ww_wire;
		ww_head.title = "WireWorld head";
		ww_head.setFactory(WWHead::new);
		
		ww_tail = new BlockEntityType();
		ww_tail.drawer = BlockDrawer.ofColor(Color.WHITE);
		ww_tail.leaveBehind = ww_wire;
		ww_tail.title = "WireWorld tail";
		ww_tail.setFactory(WWTail::new);

		ww_chatter = new BlockEntityType();
		ww_chatter.drawer = BlockDrawer.ofImage(Textures.get("printer.png"));
		ww_chatter.setFactory(WWChatter::new);
		ww_chatter.title = "Chatbox";
		
		stone = new Block();
		stone.drawer = BlockDrawer.ofImage(Textures.get("stone.png"));
		stone.title = "Stone";
		
		plank = new Block();
		plank.drawer = BlockDrawer.ofImage(Textures.get("plank.png"));
		plank.title = "Wooden planks";
		
		leaves = new Block();
		leaves.drawer = BlockDrawer.ofImage(Textures.get("leaves.png"));
		leaves.title = "Leaves";
		
		iron_ore = new Block();
		iron_ore.drawer = BlockDrawer.ofImage(Textures.get("iron_ore.png"));
		iron_ore.title = "Iron ore";
		
		copper_ore = new Block();
		copper_ore.drawer = BlockDrawer.ofImage(Textures.get("copper_ore.png"));
		copper_ore.title = "Copper ore";
		
		silicon_ore = new Block();
		silicon_ore.drawer = BlockDrawer.ofImage(Textures.get("silicon_ore.png"));
		silicon_ore.title = "Silicon ore";
		
		crafting = new Block();
		crafting.drawer = BlockDrawer.ofImage(Textures.get("crafting.png"));
		crafting.title = "Assembly Table";
		
		logs = new Block();
		logs.drawer = BlockDrawer.ofImage(Textures.get("log.png"));
		logs.title = "Log";
		
		AND = new BlockEntityType();
		AND.title = "AND";
		AND.setFactory(ANDGate::new);
		AND.drawer = BlockDrawer.ofImage(Textures.get("logic/AND.png"));
		
		OR = new BlockEntityType();
		OR.title = "OR";
		OR.setFactory(ORGate::new);
		OR.drawer = BlockDrawer.ofImage(Textures.get("logic/OR.png"));
		
		XOR = new BlockEntityType();
		XOR.title = "XOR";
		XOR.setFactory(XORGate::new);
		XOR.drawer = BlockDrawer.ofImage(Textures.get("logic/XOR.png"));
		
		TRUE = new AlwaysTrue();
		TRUE.title = "Always True";
		TRUE.drawer = BlockDrawer.ofImage(Textures.get("logic/true.png"));
		
		RANDOMCTRL = new BlockEntityType();
		RANDOMCTRL.title = "Random if";
		RANDOMCTRL.setFactory(RandomGate::new);
		RANDOMCTRL.drawer = BlockDrawer.ofImage(Textures.get("logic/randomctrl.png"));
		
		RANDOM = new Randomizer();
		RANDOM.drawer = BlockDrawer.ofImage(Textures.get("logic/random.png"));
		RANDOM.title = "Random";
		
		BUTTON = new BlockEntityType();
		BUTTON.title = "Button";
		BUTTON.setFactory(BlockButton::new);
		BUTTON.drawer = BlockDrawer.ofImage(Textures.get("logic/button.png"));
		
		YES = new BlockEntityType();
		YES.title = "YES";
		YES.setFactory(YESGate::new);
		YES.drawer = BlockDrawer.ofImage(Textures.get("logic/YES.png"));
		
		NOT = new BlockEntityType();
		NOT.title = "NOT";
		NOT.setFactory(NOTGate::new);
		NOT.drawer = BlockDrawer.ofImage(Textures.get("logic/NOT.png"));
		
		ON = new OnToggle();
		ON.drawer = BlockDrawer.ofImage(Textures.get("logic/on.png"));
		ON.title = "Active Switch";
		
		OFF = new OffToggle();
		OFF.drawer = BlockDrawer.ofImage(Textures.get("logic/off.png"));
		OFF.title = "Inactive Switch";
		
		URANDOM = new BlockEntityType();
		URANDOM.title = "Uniform Random";
		URANDOM.setFactory(UniformRandom::new);
		URANDOM.drawer = BlockDrawer.ofImage(Textures.get("logic/urandom.png"));
		
		LAMP = new BlockEntityType();
		LAMP.title = "Lamp";
		LAMP.setFactory(Lamp::new);
		LAMP.drawer = BlockDrawer.ofImage(Textures.get("logic/off lamp.png"));
		
		PLACER = new BlockEntityType();
		PLACER.title = "Block Placer";
		PLACER.setFactory(ActuatorPlaceBlock::new);
		PLACER.drawer = BlockDrawer.ofImage(Textures.get("machine/placer.png"));
		
		CLICKER = new BlockEntityType();
		CLICKER.title = "Block Clicking Claw";
		CLICKER.setFactory(ActuatorClick::new);
		CLICKER.drawer = BlockDrawer.ofImage(Textures.get("machine/claw.png"));
		
		ROTATOR = new BlockEntityType();
		ROTATOR.title = "Block Rotator";
		ROTATOR.setFactory(ActuatorRotations::new);
		ROTATOR.drawer = BlockDrawer.ofImage(Textures.get("machine/CW.png"));
		
		//Register
		grass.register("mmb.grass");
		air.register("mmb.air");
		
		ww_tail.register("wireworld.tail");
		ww_head.register("wireworld.head");
		ww_wire.register("wireworld.wire");
		ww_chatter.register("wireworld.chatter");
		
		stone.register("mmb.stone");
		plank.register("mmb.planks");
		leaves.register("mmb.leaves");
		iron_ore.register("mmb.iore");
		copper_ore.register("mmb.cpore");
		silicon_ore.register("mmb.silicon_ore");
		crafting.register("mmb.craft");
		logs.register("mmb.tree");
		
		AND.register("wireworld.and");
		OR.register("wireworld.or");
		XOR.register("wireworld.xor");
		TRUE.register("wireworld.true");
		RANDOMCTRL.register("wireworld.randomctrl");
		RANDOM.register("wireworld.random");
		BUTTON.register("wireworld.button");
		NOT.register("wireworld.not");
		YES.register("wireworld.yes");
		ON.register("wireworld.on");
		OFF.register("wireworld.off");
		URANDOM.register("wireworld.urandom");
		LAMP.register("wireworld.lamp");
		PLACER.register("machines.placer");
		CLICKER.register("machines.clicker");
		ROTATOR.register("machines.rotator");
	}

}
