/**
 * 
 */
package mmb.WORLD.tileworld.tools;

import java.awt.Color;
import java.io.IOException;

import mmb.DATA.contents.texture.Textures;
import mmb.DATA.file.FileGetter;
import mmb.WORLD.tileworld.BlockDrawer;
import mmb.WORLD.tileworld.DrawerImage;
import mmb.WORLD.tileworld.DrawerPlainColor;
import mmb.WORLD.tileworld.block.Block;
import mmb.WORLD.tileworld.block.Blocks;
import mmb.WORLD.tileworld.tool.BlockTool;
import mmb.WORLD.tileworld.tool.ToolEvent;
import mmb.WORLD.tileworld.tool.ToolProxy;
import mmb.WORLD.tileworld.tool.Tools;
import mmb.debug.Debugger;

/**
 * @author oskar
 * This is a tool used to mine blocks
 */
public class ToolMine implements BlockTool {
	private static final BlockDrawer drawer;
	private static final Debugger debug = new Debugger("TOOL-MINE");
	private ToolProxy proxy;
	static {
		drawer = new DrawerImage(Textures.get("mine.png"));
	}
	@Override
	public void update(ToolEvent e) {
		//set tool proxy
		proxy.setStatus("Click left do break the block");
		proxy.selectionA = e.blockPosition;
		proxy.selectionB = e.blockPosition;
		debug.printl("block: "+e.blockPosition.toString());
	}

	@Override
	public void mousePress(ToolEvent e, int button) {
		
		if(e.creative) {
			e.proxy.remove(e.blockPosition);
		}else{
			Block b = e.proxy.getBlock(e.blockPosition);
			if(b == Blocks.grass) return;
			e.proxy.remove(e.blockPosition);
			e.inventory.insert(b);
		}
	}

	@Override
	public void setProxy(ToolProxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public BlockDrawer texture() {
		return drawer;
	}

}
