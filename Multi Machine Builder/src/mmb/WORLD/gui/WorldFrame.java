/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import javax.swing.JComponent;

import org.joml.Vector2d;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.machine.Machine;
import mmb.WORLD.worlds.map.BlockMap;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import java.awt.Color;

/**
 * @author oskar
 *
 */
public class WorldFrame extends JComponent implements MouseListener, KeyListener, MouseMotionListener, MouseWheelListener{
	private static final long serialVersionUID = 7346245653768692732L;

	private Vector2d pos = new Vector2d();
	
	/**
	 * The perspective is a snapped camera position
	 */
	public final Vector2d perspective = new Vector2d();
	
	private transient Debugger debug = new Debugger("WORLD - anonymous");
	private static Debugger sdebug = new Debugger("WORLDS");
	//SECTION WORLD REFERENCES
	private String subWorldName;
	private transient World world;
	
	private String altMessage = "Loading";
	/**
	 * @return the altMessage
	 */
	public String getAltMessage() {
		return altMessage;
	}
	/**
	 * @param altMessage the altMessage to set
	 */
	public void setAltMessage(String altMessage) {
		this.altMessage = altMessage;
	}
	
	private String title = "none";
	
	private transient BlockMap map;
	
	//LISTENERS
	private transient List<Consumer<String>> titleListeners = new ArrayList<>();


	/** Add a title listener 
	 * @param arg0 title listener to be added
	 */
	public void addTitleListener(Consumer<String> arg0) {
		titleListeners.add(arg0);
	}

	/** Remove a title listener 
	 * @param arg0 title listener to be removed*/
	public void removeTitleListener(Object arg0) {
		titleListeners.remove(arg0);
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
	 * Set the map, from the same world, with given name. Provide null to switch to main map.
	 * @param name map name
	 */
	public void setMap(String name) {
		setMap(world.getMap(name));
	}
	
	/**
	 * @param w new world
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
			map = w.getMain();
		}else {
			sdebug.printl("Opening a new world");
			world = w;
			map = w.getMain();
			w.activate();
		}
	}

	/**
	 * Change the world map
	 * @param newMap new world map
	 */
	public void setMap(BlockMap newMap) {
		if(newMap.getOwner() == null) {
			debug.printl("The given map is a singleton map");
			world.destroy();
			world = null;
		}
	}

	/**
	 * Create a new WorldFrame
	 */
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
		placer.mouseWheelMoved(arg0);
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
			perspective.x +=1;
			break;
		case KeyEvent.VK_D:
			perspective.x -= 1;
			break;
		case KeyEvent.VK_W:
			perspective.y +=1;
			break;
		case KeyEvent.VK_S:
			perspective.y -= 1;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		//unused
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		//unused
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//unused
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		setMousePosition(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//unused
	}

	@Override
	public void mousePressed(MouseEvent e) {
		setMousePosition(e);
		switch(e.getButton()) {
		case 0:
			break;
		case 1: //LMB
			if(map.inBounds(mouseoverBlockX, mouseoverBlockY))
				placer.getPlacer().place(mouseoverBlockX, mouseoverBlockY, map);
			break;
		case 3: //RMB
			showPopup(e);
			break;
		case 2: //MMB
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		/*unused*/
	} 
	
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
				if(map.getName() == null) title = "main "+world.getName();
				else title = "["+map.getName()+"] "+world.getName();
				
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
		//Dimensions in tiles
		double tilesW = (getWidth()/ 32);
		double tilesH = (getHeight()/ 32);
		//Persepctive => offset
		pos.set(perspective);
		pos.add(tilesW/2, tilesH/2);
		//Check validity
		if(map == null) return;	
		if(!map.isValid()) return;
		//Calculate DR corner
		double endX = tilesW - pos.x;
		double endY = tilesH - pos.y;
		//fill with background
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
		//Render tiles
		int startX = (int) ((ex1+pos.x)*32);
		int startY = (int) ((ey1+pos.y)*32);
		for(int i = ex1, x = startX; i < ex2; i++, x += 32) {
			for(int j = ey1, y = startY; j < ey2; j++, y += 32) {
				renderTile(x, y, g, map.get(i, j), i, j);
			}
		}
		
		//Draw machines
		for(Machine m: map.machines) {
			int x = (int)((m.posX()+pos.x)*32);
			int y = (int)((m.posY()+pos.y)*32);
			int w = m.sizeX();
			int h = m.sizeY();
			Graphics g2 = g.create(x, y, w*32, h*32);
			m.render(g2);
		}
		
		//Draw pointer
		int x = (int)((mouseoverBlockX+pos.x)*32);
		int y = (int)((mouseoverBlockY+pos.y)*32);
		
		//Preview
		if(placer.getPlacer() != null) placer.getPlacer().preview(g, new Point(x, y), map, new Point(mouseoverBlockX, mouseoverBlockY));
		g.setColor(Color.BLACK);
		g.drawRect(x, y, 32, 32);
		g.drawRect(x+2, y+2, 28, 28);
		g.setColor(Color.RED);
		g.drawRect(x+1, y+1, 30, 30);
		
		//Debug use only
		if(DEBUG_DISPLAY) {
			g.setColor(Color.CYAN);
			g.fillRect(0, 0, 150, 34);
			g.setColor(Color.BLACK);
			g.drawString("Offset: "+pos.x+","+pos.y, 2, 11);
			g.drawString("Selected block: "+mouseoverBlockX+","+mouseoverBlockY, 2, 28);
			
		}
	}
	private static final boolean DEBUG_DISPLAY = false;
	private void renderTile(int x, int y, Graphics g, BlockEntry be, int wx, int wy) {
		if(be == null) return;
		Point bp = be.getPosition();
		if(bp.x!=wx || bp.y!=wy) {
			debug.printl("The tile at ["+wx+","+wy+"] has uncompliant position["+bp.x+","+bp.y+"]");
		}
		BlockDrawer bd = be.getTexture();
		if(bd != null) bd.draw(x, y, g);
	}

	private transient Timer timer = new Timer();
	private boolean active = false;
	/**
	 * @return is timer active?
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * Set the activity of timer.
	 * If timer is active, the frame will be actively rendered
	 * @param a should timer be active?
	 */
	public void setActive(boolean a) {	
		if(active && !a) {
			timer.cancel();
		}else if(!active && a) {
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override public void run() {repaint();}
			}, 0, 20);
		}
		active = a;
	}
	private Point mousePosition = new Point();

	private WorldWindow window;
	/**
	 * Set mouse position from pair of coordinates
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public void setMousePosition(int x, int y) {
		mousePosition.x = x;
		mousePosition.y = y;
	}
	/**
	 * Set mouseover position from the event
	 * @param e mouse event
	 */
	public void setMousePosition(MouseEvent e) {
		setMousePosition(e.getX(), e.getY());
	}
	private void showPopup(MouseEvent e) {
		if(map.inBounds(mouseoverBlockX, mouseoverBlockY)) {
			WorldMenu menu = new WorldMenu(map.get(getMouseoverBlock()), e, this, window);
			menu.show(this, e.getX(), e.getY());
		}
	}
	
	/**
	 * @return the window
	 */
	public WorldWindow getWindow() {
		return window;
	}
	/**
	 * @param window the window to set
	 */
	public void setWindow(WorldWindow window) {
		this.window = window;
	}

	private int mouseoverBlockX;
	private int mouseoverBlockY;
	
	private void resetMouseoverBlock() {
		mouseoverBlockX = (int)Math.floor((mousePosition.x / 32.0)-pos.x);
		mouseoverBlockY = (int)Math.floor((mousePosition.y / 32.0)-pos.y);
	}
	/**
	 * @return block position over which mouse is over
	 */
	public Point getMouseoverBlock() {
		return new Point(mouseoverBlockX, mouseoverBlockY);
	}

	private ScrollablePlacementList placer;

	/**
	 * @return the placer
	 */
	public ScrollablePlacementList getPlacer() {
		return placer;
	}
	/**
	 * @param placer the placer to set
	 */
	public void setPlacer(ScrollablePlacementList placer) {
		this.placer = placer;
	}
}
