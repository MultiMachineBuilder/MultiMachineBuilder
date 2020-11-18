/**
 * 
 */
package mmb.WORLD.tileworld.REWORK;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import org.joml.Vector2d;

import mmb.MENU.toolkit.ComponentEventHandler;
import mmb.MENU.toolkit.events.SharedEventHandler;
import mmb.MENU.toolkit.events.UIMouseEvent;
import mmb.WORLD.tileworld.block.Block;
import mmb.WORLD.tileworld.block.BlockUpdateEvent;
import mmb.WORLD.tileworld.block.Blocks;
import mmb.WORLD.tileworld.block.MapEntry;
import mmb.WORLD.tileworld.map.World;
import mmb.WORLD.tileworld.tool.BlockTool;
import mmb.WORLD.tileworld.tool.ToolEvent;
import mmb.WORLD.tileworld.tool.ToolProxy;
import mmb.WORLD.tileworld.tool.Tools;
import mmb.WORLD.tileworld.world.BlockProxy;
import mmb.WORLD.tileworld.world.WorldDataProvider;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class CommonTileAgent implements WorldDataProvider, SharedEventHandler, ComponentEventHandler {
	public Vector2d offset;
	public World map;
	private final Debugger debug = new Debugger("TILES");
	public Point mousePos = new Point(0 ,0);
	public BlockProxy proxy = new BlockProxy(this);
	public Point selectionA, selectionB;
	private boolean active = false;
	public int lastMouseButton = 0;
	
	//Positioning
	/**
	 * @return the effective size of the world canvas
	 */
	public Dimension getEffectiveSize() {
		return effectiveSize;
	}
	/**
	 * @return the effectiveMousePos
	 */
	public Point getEffectiveMousePos() {
		return effectiveMousePos;
	}

	private Dimension effectiveSize;
	private Point effectiveMousePos;
	private ToolProxy tproxy = new ToolProxy();
	
	//Block getters/setters
	private int blockIndex = 0;
	private int scroll = 0;
	private Block currBlock = null;
	private Block[] blockCache = new Block[0];
	/**
	 * Set the current block using the index
	 */
	public void setBlock(int index) {
		blockIndex = index;
		currBlock = blockCache[index];
		debug.printl("Block:"+currBlock.getID());
	}
	/**
	 * Get the current block
	 */
	public Block getBlock() {
		return currBlock;
	}
	/**
	 * Get current block index
	 */
	public int getBlockIndex() {
		return blockIndex;
	}
	
	
	//Tool getters/setters
	private int toolIndex = 0;
	private BlockTool currTool = null;
	private BlockTool[] toolCache = new BlockTool[0];
	/**
	 * Set the current tool using the index
	 */
	public void setTool(int index) {
		debug.printl("Tool #"+index);
		toolIndex = index;
		currTool = toolCache[index];
		currTool.setProxy(tproxy);
	}
	/**
	 * Get the current tool
	 */
	public BlockTool getTool() {
		return currTool;
	}
	/**
	 * Get current tool index
	 */
	public int getToolIndex() {
		return toolIndex;
	}
	
	/**
	 * Create the panel.
	 */
	public CommonTileAgent() {
		tproxy = new ToolProxy();
		toolCache = Tools.getTools();
		blockCache = Blocks.getBlocks();
	}
	
	public ToolEvent createToolEvent() {
		ToolEvent e = new ToolEvent();
		e.screenPosition = effectiveMousePos;
		e.proxy = proxy;
		e.blockPosition = blockByPoint(effectiveMousePos);
		e.worldPosition = worldPosByPoint(effectiveMousePos);
		e.tproxy = tproxy;
		e.selectedBlock = currBlock;
		return e;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	/*
	 * leftmost 64 pixels:
	 * left half: blocks
	 * right half: tools
	 */
	public void render(Graphics g) {
		if(!active) return;
		
		
		//calculate screen dimensions
		int h = g.getClipBounds().height;
		int w = g.getClipBounds().width;
		int effW = w - 64;
		effectiveSize = new Dimension(w-64, h);
		
		Graphics blockBar = g.create(0, 0, 32, h);
		Graphics toolBar = g.create(32, 0, 32, h);
		Graphics canvas = g.create(64, 0, w-64, h);
		
		blockBar.setColor(Color.DARK_GRAY);
		toolBar.setColor(Color.LIGHT_GRAY);
		blockBar.clearRect(0, 0, 32, h);
		toolBar.clearRect(0, 0, 32, h);
		
		double endX = (effW / 32) + offset.x;
		double endY = (h / 32) + offset.y;
		int ex1 = (int) Math.floor(offset.x);
		int ey1 = (int) Math.floor(offset.y);
		int ex2 = (int) Math.ceil(endX);
		int ey2 = (int) Math.ceil(endY);
		//debug.printl(ex1+","+ey1);
		
		//draw tools
		toolCache = Tools.getTools();
		for(int i = 0; i < toolCache.length; i++) {
			toolCache[i].texture().draw(0, i*32, toolBar);
		}
		
		//draw blocks
		int startY = scroll * -32;
		for(int i = 0; i < blockCache.length; i++) {
			blockCache[i].texture.draw(0, (i*32)+startY, blockBar);
		}
		
		//update
		int x1 = map.blocks.startX;
		int y1 = map.blocks.startY;
		int x2 = x1 + map.blocks.sizeX;
		int y2 = y1 + map.blocks.sizeY;
		for(int x = x1; x < x2; x++) {
			for(int y = y1; y < y2; y++) {
				Block b = map.get(x, y).getBlock();
				if(b != null && b.update != null) {
					BlockUpdateEvent bue = new BlockUpdateEvent();
					bue.block = new Point(x, y);
					bue.world = proxy;
					b.update.accept(bue);
				}
			}
		}
		
		//draw
		for(int x = ex1; x <=ex2; x++) {
			for(int y = ey1; y <=ey2; y++) {
				paintTile(x, y, canvas);
			}
		}
		
		//pointer
		if(effectiveMousePos != null) {
			Point tile = blockByPoint(effectiveMousePos);
			int X = (int)(tile.x-offset.x)*32;
			int Y = (int)(tile.y-offset.y)*32;
			
			canvas.setColor(Color.BLACK);
			canvas.drawRect(X, Y, 32, 32);
			canvas.drawRect(X+2, Y+2, 28, 28);
			canvas.setColor(Color.RED);
			canvas.drawRect(X+1, Y+1, 30, 30);
		}
		//apply
		proxy.apply();
	}
	
	
	
	private void paintTile(int x, int y, Graphics g) {
		Block b = null;
		int X = (int)(x-offset.x)*32;
		int Y = (int)(y-offset.y)*32;
		
		if(map.check(x, y)) {
			b = map.get2(x, y);
		}
		
		if(b == null) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(X, Y, 32, 32);
		}else {
			b.texture.draw(X, Y, g);
		}
	}
	public Vector2d worldPosByPoint(Point p) {
		Vector2d result = new Vector2d(p.x/32, p.y/32);
		result.add(offset);
		return result;
	}
	public Vector2d worldPosByPoint(Vector2d p) {
		Vector2d result = new Vector2d();
		p.div(32, result);
		result.add(offset);
		return result;
	}
	
	public Point blockByPoint(Point p) {
		Vector2d tmp = worldPosByPoint(p);
		return new Point((int)tmp.x, (int)tmp.y);
	}
	public Point blockByPoint(Vector2d p) {
		Vector2d tmp = worldPosByPoint(p);
		return new Point((int)tmp.x, (int)tmp.y);
	}
	
	@Override public MapEntry get(int x, int y) {
		return map.blocks.get(x, y);
	}
	@Override public void set(int x, int y, MapEntry me) {
		map.blocks.set(x, y, me);
	}
	
	
	//EVENTS
	@Override
	public void drag(UIMouseEvent ee) {
		Point p = ee.p;
		if(p.x < 64) return;
		effectiveMousePos = new Point(p.x - 64, p.y);
		ToolEvent e = createToolEvent();
		e.mouse = ee;
		if(currTool != null) currTool.mouseDrag(e);
	}
	@Override
	public void press(UIMouseEvent ee) {
		lastMouseButton = ee.button;
		Point p = ee.p;
		if(p.x < 32) {
			//select block
			int index = (p.y/32) + scroll;
			if(index < 0) return;
			if(index > blockCache.length) return;
			setBlock(index);
			Toolkit.getDefaultToolkit().beep();
		}else if(p.x < 64) {
			//select tool
			int index = p.y/32;
			if(index < toolCache.length) {
				setTool(index);
				Toolkit.getDefaultToolkit().beep();
			}
		}else {
			//select world
			effectiveMousePos = new Point(p.x - 64, p.y);
			ToolEvent e = createToolEvent();
			e.mouse = ee;
			if(currTool != null) currTool.mousePress(e, lastMouseButton);
		}
	}
	@Override
	public void release(UIMouseEvent e) {
		// TODO Auto-generated method stub
		ComponentEventHandler.super.release(e);
	}
	@Override
	public void keyPress(int key) {
		switch(key) {
		case KeyEvent.VK_A:
			offset.x--;
			break;
		case KeyEvent.VK_D:
			offset.x++;
			break;
		case KeyEvent.VK_W:
			offset.y--;
			break;
		case KeyEvent.VK_S:
			offset.y++;
			break;
		}
		//debug.printl(offset.toString());
	}
	@Override
	public void keyRelease(int key) {
		// TODO Auto-generated method stub
		SharedEventHandler.super.keyRelease(key);
	}
	@Override
	public void scroll(int scroll) {
		this.scroll += scroll;
	}
	@Override
	public void mouseMoved(UIMouseEvent e) {
		Point p = e.p;
		effectiveMousePos = new Point(p.x - 64, p.y);
	}
	
	
	//SETUP
	public void load(World w) {
		
	}
	public void start() {
		
	}
	public void stop() {
		
	}
	
	//PROCESSING
	public void update(Graphics g) {
		
	}
}
