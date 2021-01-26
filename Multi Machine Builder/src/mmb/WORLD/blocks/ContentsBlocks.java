/**
 * 
 */
package mmb.WORLD.blocks;

import java.awt.Color;
import java.awt.image.BufferedImage;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.properties.BlockProperty;
import mmb.WORLD.block.properties.BlockPropertyInfo;
import mmb.debug.Debugger;

/**
 * @author oskar
 * This class contains blocks in the new world system
 */
public class ContentsBlocks {
	private static final Debugger debug = new Debugger("BLOCKS");

	public static final BlockType
	air, grass,
	ww_head, ww_tail, ww_wire,
	ww_chatter, plank, stone, leaves,
	iron_ore, copper_ore, silicon_ore, crafting, logs;
	
	static {	
		//Toolkit.getDefaultToolkit().beep();
		debug.printl("Creating blocks");
		air = new BlockType();
		air.drawer = BlockDrawer.ofColor(Color.CYAN);
		air.leaveBehind = air;
		air.title = "Air";
		
		BlockDrawer tmp;
		try {
			BufferedImage i = Textures.get("grass.png");
			tmp = BlockDrawer.ofImage(i);
			if(i == null) debug.printl("got null texture");
		} catch (Exception e) {
			tmp = BlockDrawer.ofColor(Color.GREEN);
			debug.pstm(e, "Failed to load grass texture, switching to plain color");
		}
		
		grass = new BlockType();
		grass.drawer = tmp;
		grass.leaveBehind = air;
		grass.title = "Grass";
		
		ww_wire = new BlockType();
		ww_wire.drawer = BlockDrawer.ofColor(Color.ORANGE);
		ww_wire.title = "WireWorld conductor";
		
		ww_head = new BlockType();
		ww_head.drawer = BlockDrawer.ofColor(Color.BLUE);
		ww_head.leaveBehind = ww_wire;
		ww_head.title = "WireWorld head";
		
		ww_tail = new BlockType();
		ww_tail.drawer = BlockDrawer.ofColor(Color.WHITE);
		ww_tail.leaveBehind = ww_wire;
		ww_tail.title = "WireWorld tail";
		
		ww_wire.onUpdate = (e, p) -> {
			BlockEntry[] blocks = e.getNeighbors8();
			int count = 0;
			for(int i = 0; i < blocks.length; i++) {
				if(blocks[i].typeof(ContentsBlocks.ww_head)) count++;
			}
			
			if(count == 1 || count == 2) p.place(ww_head, e.posX(), e.posY());
		};
		ww_head.onUpdate = (e, p) -> {
			p.place(ww_tail, e.posX(), e.posY());
		};
		ww_tail.onUpdate = (e, p) -> {
			p.place(ww_wire, e.posX(), e.posY());
		};
		
		
		
		ww_chatter = new BlockType();
		ww_chatter.drawer = BlockDrawer.ofImage(Textures.get("printer.png"));
		ww_chatter.onUpdate = (e, p) -> {
			BlockEntry[] blocks = e.getNeighbors4();
			for(int i = 0; i < blocks.length; i++) {
				if(blocks[i].typeof(ContentsBlocks.ww_head)) {
					BlockProperty prop = e.getProperty("value");
					if(!(prop instanceof StringValue)) return;
					debug.printl(((StringValue)prop).value);
					return;
				}
			}
		};
		ww_chatter.title = "Chatbox";
		ww_chatter.properties = new BlockPropertyInfo[] {StringValue.bpi_SV};
		
		stone = new BlockType();
		stone.drawer = BlockDrawer.ofImage(Textures.get("stone.png"));
		stone.title = "Stone";
		
		plank = new BlockType();
		plank.drawer = BlockDrawer.ofImage(Textures.get("plank.png"));
		plank.title = "Wooden planks";
		
		leaves = new BlockType();
		leaves.drawer = BlockDrawer.ofImage(Textures.get("leaves.png"));
		leaves.title = "Leaves";
		
		iron_ore = new BlockType();
		iron_ore.drawer = BlockDrawer.ofImage(Textures.get("iron_ore.png"));
		iron_ore.title = "Iron ore";
		
		copper_ore = new BlockType();
		copper_ore.drawer = BlockDrawer.ofImage(Textures.get("copper_ore.png"));
		copper_ore.title = "Copper ore";
		
		silicon_ore = new BlockType();
		silicon_ore.drawer = BlockDrawer.ofImage(Textures.get("silicon_ore.png"));
		silicon_ore.title = "Silicon ore";
		
		crafting = new BlockType();
		crafting.drawer = BlockDrawer.ofImage(Textures.get("crafting.png"));
		crafting.title = "Assembly Table";
		
		logs = new BlockType();
		logs.drawer = BlockDrawer.ofImage(Textures.get("log.png"));
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
