/**
 * 
 */
package mmb.WORLD.blocks;

import java.awt.Color;
import java.awt.image.BufferedImage;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.block.SkeletalBlockEntity;
import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.properties.BlockProperty;
import mmb.debug.Debugger;

/**
 * @author oskar
 * This class contains blocks in the new world system
 */
public class ContentsBlocks {
	private static final Debugger debug = new Debugger("BLOCKS");

	public static final BlockEntityType
	ww_head, ww_tail, ww_wire,
	ww_chatter;
	
	public static final Block air, grass, plank, stone, leaves,
	iron_ore, copper_ore, silicon_ore, crafting, logs;
	
	static {	
		//Toolkit.getDefaultToolkit().beep();
		debug.printl("Creating blocks");
		air = new Block();
		air.texture = BlockDrawer.ofColor(Color.CYAN);
		air.leaveBehind = air;
		air.title = "Air";
		
		BlockDrawer tmp;
		try {
			BufferedImage i = Textures.get("grass.png");
			tmp = BlockDrawer.ofImage(i);
			if(i == null) debug.printl("got null texture");
			tmp = BlockDrawer.ofColor(Color.GREEN);
		} catch (Exception e) {
			tmp = BlockDrawer.ofColor(Color.GREEN);
			debug.pstm(e, "Failed to load grass texture, switching to plain color");
		}
		
		grass = new Block();
		grass.texture = tmp;
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
		stone.texture = BlockDrawer.ofImage(Textures.get("stone.png"));
		stone.title = "Stone";
		
		plank = new Block();
		plank.texture = BlockDrawer.ofImage(Textures.get("plank.png"));
		plank.title = "Wooden planks";
		
		leaves = new Block();
		leaves.texture = BlockDrawer.ofImage(Textures.get("leaves.png"));
		leaves.title = "Leaves";
		
		iron_ore = new Block();
		iron_ore.texture = BlockDrawer.ofImage(Textures.get("iron_ore.png"));
		iron_ore.title = "Iron ore";
		
		copper_ore = new Block();
		copper_ore.texture = BlockDrawer.ofImage(Textures.get("copper_ore.png"));
		copper_ore.title = "Copper ore";
		
		silicon_ore = new Block();
		silicon_ore.texture = BlockDrawer.ofImage(Textures.get("silicon_ore.png"));
		silicon_ore.title = "Silicon ore";
		
		crafting = new Block();
		crafting.texture = BlockDrawer.ofImage(Textures.get("crafting.png"));
		crafting.title = "Assembly Table";
		
		logs = new Block();
		logs.texture = BlockDrawer.ofImage(Textures.get("log.png"));
		logs.title = "Log";
		
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
	}

}