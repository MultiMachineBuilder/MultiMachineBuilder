/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import javax.swing.JComponent;

import org.joml.Vector2d;

import mmb.WORLD.BlockDrawer;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.worlds.map.BlockMap;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import javax.swing.JPopupMenu;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;

/**
 * @author oskar
 *
 */
public class WorldFrame extends JComponent implements MouseListener, KeyListener, MouseMotionListener, MouseWheelListener{
	
	/**
	 * World position of upper left corner
	 */
	public final Vector2d pos = new Vector2d();
	private Debugger debug = new Debugger("WORLD - anonymous");
	private static Debugger sdebug = new Debugger("WORLDS");
	//XXX WORLD REFERENCES
	private String subWorldName;
	private World world;
	
	public String altMessage = "Loading";
	private String title = "none";
	
	private BlockMap map;
	
	//LISTENERS
	private List<Consumer<String>> titleListeners = new ArrayList<Consumer<String>>();


	/** Add a title listener */
	public boolean addTitleListener(Consumer<String> arg0) {
		return titleListeners.add(arg0);
	}

	/** Remove a title listener */
	public boolean removeTitleListener(Object arg0) {
		return titleListeners.remove(arg0);
	}
	
	private void fireTitleListeners() {
		for(Consumer<String> listener: titleListeners) {
			listener.accept(title);
		}
	}

	/**
	 * @return the subWorldName
	 */
	public String getSubWorldName() {
		return subWorldName;
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * @return the map
	 */
	public BlockMap getMap() {
		return map;
	}
	

	/**
	 * Set the map. Provide null to switch to main map.
	 */
	public void setMap(String name) {
		setMap(world.getMap(name));
	}
	
	/**
	*/
	public void enterWorld(World w) {
		if(w == null) {
			sdebug.printl("The given world is null, exiting");
			if(world != null) world.destroy();
		}else if(world != null) {
			sdebug.printl("A world is already running.");
			world.destroy();
			world = w;
			w.activate();
			map = w.main;
		}else {
			sdebug.printl("Opening a new world");
			world = w;
			map = w.main;
			w.activate();
		}
	}

	/**
	 * Change the world map
	 */
	public void setMap(BlockMap newMap) {
		if(newMap.owner == null) {
			debug.printl("The given map is a singleton map");
			world.destroy();
			world = null;
		}
	}

	public WorldFrame() {
		initialize();
	}
	private void initialize() {
		addMouseListener(this);
		addMouseWheelListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		setActive(true);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		setMousePosition(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setMousePosition(e);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_A:
			pos.x +=1;
			break;
		case KeyEvent.VK_D:
			pos.x -= 1;
			break;
		case KeyEvent.VK_W:
			pos.y +=1;
			break;
		case KeyEvent.VK_S:
			pos.y -= 1;
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

	@Override
	public void mouseClicked(MouseEvent e) {} //unused

	@Override
	public void mouseEntered(MouseEvent e) {
		setMousePosition(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {} //unused

	@Override
	public void mousePressed(MouseEvent e) {
		setMousePosition(e);
		switch(e.getButton()) {
		case 0:
			break;
		case 1: //LMB
			map.place(mouseoverBlockX, mouseoverBlockY, block);
			break;
		case 3: //RMB
			showPopup(e);
		case 2: //MMB
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {} //unused
	
	@Override
	public void paint(Graphics g) {
		resetMouseoverBlock();
		if(g == null) return;
		if(map == null) {
			if(world == null) {
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(getForeground());
				g.drawString(altMessage, getWidth()/2, getHeight()/2);
				debug.id = "WORLD - IDLE";
				title = "none";
			}else{
				setMap((String) null);
				debug.id = "WORLD - main";
				title = "main";
				render(g);
			}
		}else {
			if(world == null) {
				title = "anonymous ["+map.getName()+"]";
			}else {
				title = "["+map.getName()+"] "+world.getName();
			}
			render(g);
			debug.id = "WORLD - "+title;
		}
		fireTitleListeners();
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	private void render(Graphics g) {
		if(map == null) return;	
		if(!map.isValid()) return;
		double endX = (getWidth()/ 32) - pos.x;
		double endY = (getHeight()/ 32) - pos.y;
		
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//Viewport block range
		int ex1 = (int) Math.floor(-pos.x);
		int ey1 = (int) Math.floor(-pos.y);
		int ex2 = (int) Math.ceil(endX);
		int ey2 = (int) Math.ceil(endY);
		//Viewport range clipping
		if(ex1 < map.startX) ex1 = map.startX;
		if(ey1 < map.startX) ey1 = map.startY;
		if(ex2 > map.startX) ex2 = map.endX;
		if(ey2 > map.startX) ey2 = map.endY;
		
		int startX, startY;
		startX = (int) ((ex1+pos.x)*32);
		startY = (int) ((ey1+pos.y)*32);
		//startX = (int) pos.x*-32
		//startY (int) pos.y*-32
		for(int i = ex1, x = startX; i < ex2; i++, x += 32) {
			for(int j = ey1, y = startY; j < ey2; j++, y += 32) {
				renderTile(x, y, g, map.get(i, j), i, j);
			}
		}
		
		//pointer
		int X = (int)((mouseoverBlockX+pos.x)*32);
		int Y = (int)((mouseoverBlockY+pos.y)*32);
		
		g.setColor(Color.BLACK);
		g.drawRect(X, Y, 32, 32);
		g.drawRect(X+2, Y+2, 28, 28);
		g.setColor(Color.RED);
		g.drawRect(X+1, Y+1, 30, 30);
		
		//Debug use only
		if(debugDisplay) {
			g.setColor(Color.CYAN);
			g.fillRect(0, 0, 150, 34);
			g.setColor(Color.BLACK);
			g.drawString("Offset: "+pos.x+","+pos.y, 2, 11);
			g.drawString("Selected block: "+mouseoverBlockX+","+mouseoverBlockY, 2, 28);
			
		}
	}
	private static final boolean debugDisplay = false;
	private void renderTile(int x, int y, Graphics g, BlockEntry be, int wx, int wy) {
		if(be == null) return;
		Point bp = be.getPosition();
		if(bp.x!=wx || bp.y!=wy) {
			debug.printl("The tile at ["+wx+","+wy+"] has uncompliant position["+bp.x+","+bp.y+"]");
		}
		BlockDrawer bd = be.getTexture();
		if(bd != null) bd.draw(x, y, g);
	}
	public void paintImmediately() {
		paintImmediately(getBounds());
	}
	private Timer timer = new Timer();
	private boolean active = false;
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean a) {	
		if(active && !a) {
			timer.cancel();
		}else if(!active && a) {
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					paintImmediately();
				}
				
			}, 0, 20);
		}
		active = a;
	}
	private Point mousePosition = new Point();
	public void setMousePosition(int x, int y) {
		mousePosition.x = x;
		mousePosition.y = y;
	}
	public void setMousePosition(MouseEvent e) {
		setMousePosition(e.getX(), e.getY());
	}
	private void showPopup(MouseEvent e) {
		if(map.inBounds(mouseoverBlockX, mouseoverBlockY)) {
			WorldMenu menu = new WorldMenu(map.get(getMouseoverBlock()), e, this);
			menu.show(this, e.getX(), e.getY());
		}
	}
	
	/**
	 * @return
	 */
	private int mouseoverBlockX, mouseoverBlockY;
	
	private void resetMouseoverBlock() {
		mouseoverBlockX = (int)Math.floor((mousePosition.x / 32.0)-pos.x);
		mouseoverBlockY = (int)Math.floor((mousePosition.y / 32.0)-pos.y);
	}
	public Point getMouseoverBlock() {
		return new Point(mouseoverBlockX, mouseoverBlockY);
	}

	private java.awt.List selList;
	private BlockType[] blockBuffer;
	private BlockType block;
	
	/**
	 * @param list
	 * @param blocks
	 */
	public void setBlockSelector(java.awt.List list, BlockType[] blocks) {
		selList = list;
		blockBuffer = blocks;
		list.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				block = blockBuffer[list.getSelectedIndex()];
				debug.printl("User selected block: "+block.title);
			}
		});
	}
	public void selectBlock(BlockType typ) {
		int index = -1;
		for(int i = 0; i < blockBuffer.length; i++) {
			if(blockBuffer[i] == typ) {
				index = i;
				break;
			}
		}
		if(index < 0) {
			debug.printl("Could not find requested block");
			return;
		}
		debug.printl("Block "+typ.title+" was selected");
		selList.select(index);
		block = typ;
	}

	/**
	 * @return currently selected block type
	 */
	public BlockType getBlock() {
		return block;
	}
}
