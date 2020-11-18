/**
 * 
 */
package mmb.WORLD.tileworld.block;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonObject;

import mmb.DATA.contents.texture.Textures;
import mmb.DATA.file.FileGetter;
import mmb.WORLD.tileworld.BlockDrawer;
import mmb.WORLD.tileworld.DrawerImage;
import mmb.WORLD.tileworld.DrawerPlainColor;
import mmb.WORLD.tileworld.world.BlockProxy;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Blocks {
	private static final Debugger debug = new Debugger("BLOCKS");
	public static BiMap<String, Block> blocks = HashBiMap.create();
	
	public static final Block 
					air, grass,
					ww_head, ww_tail, ww_wire,
					ww_chatter;
	private static boolean isGeneratingFurther = false;
	/**
	 * @return the isGeneratingFurther
	 */
	public static boolean isGeneratingFurther() {
		return isGeneratingFurther;
	}

	static {
		//Toolkit.getDefaultToolkit().beep();
		debug.printl("Creating blocks");
		air = new Block();
		air.texture = new DrawerPlainColor(Color.CYAN);
		air.leaveBehind = air;
		
		BlockDrawer tmp;
		try {
			BufferedImage i = Textures.get("grass.png");
			tmp = new DrawerImage(i);
			if(i == null) debug.printl("got null texture");
		} catch (Exception e) {
			tmp = new DrawerPlainColor(Color.GREEN);
			debug.pstm(e, "Failed to load grass texture, switching to plain color");
		}
		
		grass = new Block();
		grass.texture = tmp;
		grass.leaveBehind = air;
		isGeneratingFurther = true;
		
		ww_wire = new Block();
		ww_wire.texture = new DrawerPlainColor(Color.ORANGE);
		ww_wire.update = (e) -> {handlerWire(e);};
		
		ww_head = new Block();
		ww_head.texture = new DrawerPlainColor(Color.BLUE);
		ww_head.update = (e) -> {handlerHead(e);};
		ww_head.leaveBehind = ww_wire;
		
		ww_tail = new Block();
		ww_tail.texture = new DrawerPlainColor(Color.WHITE);
		ww_tail.update = (e) -> {handlerTail(e);};
		ww_tail.leaveBehind = ww_wire;
		
		//NEW FEATURES Chatter
		BlockEntityLoader logBEL = new BlockEntityLoader();
		logBEL.blockEntityGen = () -> {
			return new BlockDataText();
		};
		logBEL.loader = (jo) -> {
			return new BlockDataText(jo.get("value").getAsString());
		};
		ww_chatter = new Block();
		ww_chatter.texture = new DrawerImage(Textures.get("printer.png"));
		ww_chatter.bel = logBEL;
		ww_chatter.update = (e -> handlerChat(e));
		
		//Register
		grass.register("mmb.grass");
		air.register("mmb.air");
		ww_tail.register("wireworld.tail");
		ww_head.register("wireworld.head");
		ww_wire.register("wireworld.wire");
		ww_chatter.register("wireworld.chatter");
		
		
		debug.printl(blocks.toString());
	}
	
	private static void handlerWire(BlockUpdateEvent bue) {
		BlockProxy p = bue.world;
		Block[] n = p.getNeighbors8(bue.block);
		int x = 0;
		for(int i = 0; i < n.length; i++) {
			if(n[i] == ww_head) x++;
		}
		//debug.printl(x + " neighbours");
		if(x == 1 || x == 2) p.set(ww_head, bue.block);
	}
	/**
	 * @param e
	 * @return
	 */
	private static void handlerChat(BlockUpdateEvent e) {
		Block[] n = e.world.getNeighbors4(e.block);
		for(int i = 0; i < n.length; i++) {
			if(n[i] == Blocks.ww_head) {
				BlockDataText bdt = (BlockDataText) e.getBED();
				debug.printl(bdt.data);
				return;
			}
		}
	}
	private static void handlerHead(BlockUpdateEvent bue) {
		bue.world.set(ww_tail, bue.block);
	}
	private static void handlerTail(BlockUpdateEvent bue) {
		bue.world.set(ww_wire, bue.block);
	}
	public static Block[] getBlocks() {
		return blocks.values().toArray(new Block[blocks.size()]);
	}
	
	public static class BlockDataText implements BlockEntityData {
		public String data;
		public BlockDataText() {
			data = "";
		}
		public BlockDataText(String txt) {
			data = txt;
		}
		@Override
		public JsonObject save() {
			JsonObject jo = new JsonObject();
			jo.addProperty("value", data);
			return jo;
		}

		@Override
		public String name() {
			return "BlockEntityDataString";
		}

		@Override
		public BlockDataText clone() {
			return new BlockDataText(data);
		}
		
	}

}
