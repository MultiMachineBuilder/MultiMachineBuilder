/**
 * 
 */
package mmb.WORLD.tileworld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JPanel;

import org.joml.Vector2d;

import mmb.MENU.toolkit.events.UIMouseEvent;
import mmb.WORLD.player.BlockIcon;
import mmb.WORLD.player.DataLayerPlayer;
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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.IntConsumer;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

/**
 * @author oskar
 *
 * ALL BLOCK POSIITON FROM SCREEN POSITION METHODS TAKE 'EFFECTIVE', OR FROM UPPER RIGHT CORNER OF THE TOOLBAR POSITION
 */
@SuppressWarnings("serial")
public class TileGUI extends JPanel implements WorldDataProvider, KeyListener {
	public Vector2d offset;
	public World map;
	private final Debugger debug = new Debugger("TILES");
	public Point mousePos = new Point(0 ,0);
	private final Timer tick;
	public BlockProxy proxy = new BlockProxy(this);
	public Point selectionA, selectionB;
	private boolean active = false;
	public int lastMouseButton = 0;
	public IntConsumer onBlockSelect = (int i) -> {};
	public DataLayerPlayer pdata;
	
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
	
	
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void destroyTimer() {
		tick.cancel();
	}
	
	//Block getters/setters
	private int blockIndex = 0;
	private int scroll = 0;
	private BlockIcon currBlock = null;
	public Block[] blockCache = new Block[0];
	public BlockIcon[] blockList = new BlockIcon[0];
	/**
	 * Set the current block using the index
	 */
	public void setBlock(int index) {
		blockIndex = index;
		currBlock = blockList[index];
		debug.printl("Block:"+currBlock.getName());
	}
	/**
	 * Get the current block
	 */
	public Block getBlock() {
		return currBlock.block;
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
	public TileGUI() {
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				scroll += arg0.getWheelRotation();
			}
		});
		tproxy = new ToolProxy();
		toolCache = Tools.getTools();
		blockCache = Blocks.getBlocks();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				lastMouseButton = arg0.getButton();
				Point p = arg0.getPoint();
				if(p.x < 32) {
					//select block
					int index = (p.y/32) + scroll;
					if(index < 0) return;
					if(index > blockList.length) return;
					setBlock(index);
					Toolkit.getDefaultToolkit().beep();
					onBlockSelect.accept(index);
				}else if(p.x < 64) {
					//select tool
					int index = p.y/32;
					if(index < toolCache.length) {
						setTool(index);
						Toolkit.getDefaultToolkit().beep();
					}
				}else{
					//select world
					effectiveMousePos = new Point(p.x - 64, p.y);
					ToolEvent e = createToolEvent();
					e.mouse = new UIMouseEvent(arg0);
					if(currTool != null) currTool.mousePress(e, arg0.getButton());
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				Point p = arg0.getPoint();
				effectiveMousePos = new Point(p.x - 64, p.y);
			}
			@Override
			public void mouseDragged(MouseEvent arg0) {
				Point p = arg0.getPoint();
				if(p.x < 64) return;
				effectiveMousePos = new Point(p.x - 64, p.y);
				ToolEvent e = createToolEvent();
				e.mouse = new UIMouseEvent(arg0);
				if(currTool != null) currTool.mouseDrag(e);
			}
		});
		addKeyListener(this);
		setFocusable(true);
		tick = new Timer();
		tick.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				tick();
			}
		}, new Date(), 20);
	}
	
	public ToolEvent createToolEvent() {
		ToolEvent e = new ToolEvent();
		e.screenPosition = effectiveMousePos;
		e.proxy = proxy;
		e.blockPosition = blockByPoint(effectiveMousePos);
		e.worldPosition = worldPosByPoint(effectiveMousePos);
		e.tproxy = tproxy;
		e.selectedBlock = currBlock.block;
		e.selectedBlockIcon = currBlock;
		return e;
	}
	private void tick() {
		paintImmediately();
	}
	
	public void paintImmediately() {
		paintImmediately(0, 0, getWidth(), getHeight());
	}
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	/*
	 * leftmost 64 pixels:
	 * left half: blocks
	 * right half: tools
	 */
	@Override
	public void paint(Graphics g) {
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
		
		//filter blocks
		List<BlockIcon> blockListCandidates = new ArrayList<BlockIcon>(blockCache.length);
		for(int i = 0; i < blockCache.length; i++) {
			if(pdata.creative || pdata.contains(blockCache[i])) blockListCandidates.add(pdata.newIcon(blockCache[i]));
		}
		
		blockList = blockListCandidates.toArray(new BlockIcon[blockListCandidates.size()]);
		//draw blocks
		int startY = scroll * -32;
		for(int i = 0; i < blockList.length; i++) {
			blockList[i].draw(0, (i*32)+startY, blockBar);
		}
		
		//update
		int x1 = map.blocks.startX;
		int y1 = map.blocks.startY;
		int x2 = x1 + map.blocks.sizeX;
		int y2 = y1 + map.blocks.sizeY;
		for(int x = x1; x < x2; x++) {
			lp:
			for(int y = y1; y < y2; y++) {
				MapEntry xx = map.get(x, y);
				if(xx == null) continue lp;
				Block b = xx.getType();
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
	@Override
	public void keyPressed(KeyEvent arg0) {
		int ch = arg0.getKeyCode();
		switch(ch) {
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
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
