/**
 * 
 */
package mmb.WORLD.tileworld.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.time.temporal.ChronoUnit;

import mmb.CountDown;
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
	
	//Survival mode timer
	private CountDown cd = new CountDown();
	private boolean pressed = false;
	public double reqTime = 2;
	private int oldX = 0, oldY = 0;
	@Override
	public void mouseRelease(ToolEvent e, int button) {
		pressed = false;
	}

	@Override
	public void reset() {
		pressed = false;
		
	}

	static {
		drawer = new DrawerImage(Textures.get("mine.png"));
	}
	@Override
	public void mouseDrag(ToolEvent e) {
		if(e.blockPosition.x != oldX)
		if(e.blockPosition.y != oldY) {
			cd.resetNanos((long) (reqTime * 1000000000));
			oldX = e.blockPosition.x;
			oldY = e.blockPosition.y;
		}
	}

	@Override
	public void mouseMoved(ToolEvent e) {
		mouseDrag(e);
	}

	@Override
	public void update(ToolEvent e) {
		//set tool proxy
		proxy.setStatus("Click left do break the block");
		proxy.selectionA = e.blockPosition;
		proxy.selectionB = e.blockPosition;
		//debug.printl("block: "+e.blockPosition.toString());
		if(pressed) {
			Point p = e.gui.renderPos(e.blockPosition);
			int x = p.x + 16;
			int y = p.y + 16;
			Graphics g = e.g;
			double progress = 1-cd.remaining(ChronoUnit.NANOS) / (1000000000*reqTime);
			debug.printl("progress: "+progress);
			int s = (int) (progress * 16);
			g.setColor(Color.BLACK);
			g.drawLine(x, y, x-s, y-s);
			g.drawLine(x, y, x-s, y+s);
			g.drawLine(x, y, x+s, y-s);
			g.drawLine(x, y, x+s, y+s);
			boolean eligible = checkEligibility(e);
			if(progress > 1) {
				cd.resetNanos((long) (reqTime * 1000000000));
				if(eligible) {
					Block b = e.proxy.getBlock(e.blockPosition);
					if(e.inventory.insert(b)) e.proxy.remove(e.blockPosition);
				}
			}
			if(!eligible) {
				cd.resetNanos((long) (reqTime * 1000000000));
			}
		}		
	}
	

	@Override
	public void mousePress(ToolEvent e, int button) {
		if(e.creative) {
			e.proxy.remove(e.blockPosition);
		}else if(checkEligibility(e)){
			oldX = e.blockPosition.x;
			oldY = e.blockPosition.y;
			cd.resetNanos((long) (reqTime * 1000000000));
			pressed = true;
		}
	}
	
	private static boolean checkEligibility(ToolEvent e) {
		Block b = e.proxy.getBlock(e.blockPosition);
		if(b == Blocks.grass) return false;
		if(b == Blocks.air) return false;
		return true;
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
