/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.Timer;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;

import org.joml.Vector2d;

import mmb.BEANS.BlockActivateListener;
import mmb.DATA.variables.ListenerBooleanVariable;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.machine.Machine;
import mmb.WORLD.worlds.universe.Universe;
import mmb.WORLD.worlds.world.World;
import mmb.WORLD.worlds.world.World.BlockMap;
import mmb.debug.Debugger;
import java.awt.Color;

/**
 * @author oskar
 *
 */
public class WorldFrame extends JComponent {

	private static final long serialVersionUID = 7346245653768692732L;
	public final FPSCounter fps = new FPSCounter();
	
	/**
	 * Create a new WorldFrame
	 */
	public WorldFrame() {
		Listener listener = new Listener();
		addMouseListener(listener);
		addMouseWheelListener(listener);
		addMouseMotionListener(listener);
		addKeyListener(listener);
		setFocusable(true);
		timer.setRepeats(true);
		timer.start();
	}

	private transient Debugger debug = new Debugger("WORLD - anonymous");
	private static Debugger sdebug = new Debugger("WORLDS");
	
	//[start] alt messages
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
	//[end]
	//[start] title
	private String title = "none";
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	//[end]
	//[start] listeners
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
	//[end]
	//[start] get/set maps
	private String subWorldName;
	private transient Universe world;
	private transient World map;
	/**
	 * @return the subWorldName
	 */
	public String getSubWorldName() {
		return subWorldName;
	}
	/**
	 * @return the world
	 */
	public Universe getWorld() {
		return world;
	}
	/**
	 * @return the map
	 */
	public World getMap() {
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
	public void enterWorld(@Nullable Universe w) {
		setNoMap();
		world = w;
		if(w == null) {
			sdebug.printl("The given world is null, exiting");
		}else{
			sdebug.printl("Opening a new world");
			map = w.getMain();
			w.setActive(true);
		}
	}
	/**
	 * Change the world map
	 * @param newMap new world map
	 */
	public void setMap(@Nullable World newMap) {
		setNoMap();
		map = newMap;
		if(map == null) {
			sdebug.printl("The given world is null, exiting");
		} else {
			world = map.getUniverse();
			world.setActive(true);
		}
	}
	/** Quits any open maps and universes */
	public void setNoMap() {
		debug.print("Exiting");
		if(world != null) world.destroy();
		else if(map != null) map.destroy();
		map = null;
		world = null;
	}
	//[end]
	private class Listener implements MouseListener, KeyListener, MouseMotionListener, MouseWheelListener{
		@Override
		public void mouseWheelMoved(@Nullable MouseWheelEvent e) {
			Objects.requireNonNull(e, "event is null");
			placer.mouseWheelMoved(e);
		}
	
		@Override
		public void mouseDragged(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
		}
	
		@Override
		public void mouseMoved(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
		}
	
		@Override
		public void keyPressed(@Nullable KeyEvent event) {
			Objects.requireNonNull(event, "event is null");
			switch(event.getKeyCode()) {
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
		public void keyReleased(@Nullable KeyEvent arg0) {
			//unused
		}
	
		@Override
		public void keyTyped(@Nullable KeyEvent arg0) {
			//unused
		}
	
		@Override
		public void mouseClicked(@Nullable MouseEvent e) {
			//unused
		}
	
		@Override
		public void mouseEntered(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
		}
	
		@Override
		public void mouseExited(@Nullable MouseEvent e) {
			//unused
		}
	
		@Override
		public void mousePressed(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
			switch(e.getButton()) {
			case 0:
				break;
			case 1: //LMB
				
				//If the block has ActionListener, run the listener
				if(map.inBounds(mouseoverBlockX, mouseoverBlockY)) {
					BlockEntry ent = map.get(mouseoverBlockX, mouseoverBlockY);
					if(ent instanceof BlockActivateListener) {
						((BlockActivateListener) ent).run(mouseoverBlockX, mouseoverBlockY, map);
						debug.printl("Running BlockActivateListener for: ["+mouseoverBlockX+","+mouseoverBlockY+"]");
					}else {
						if(placer.getPlacer() == null) return;
						placer.getPlacer().place(mouseoverBlockX, mouseoverBlockY, map);
					}
				}
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
		public void mouseReleased(@Nullable MouseEvent e) {
			/*unused*/
		} 
	}
	//[start] graphics
	@Override
	public void paint(@Nullable Graphics g) {
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
				setNoMap();
				debug.id = "WORLD - main";
				title = "main";
				render(g);
			}
		}else{
			if(world == null) title = "anonymous ["+map.getName()+"]";
			else if(map.getName() == null) title = "main "+world.getName();
			else title = "["+map.getName()+"] "+world.getName();
			render(g);
			debug.id = "WORLD - "+title;
		}
		fireTitleListeners();
	}	
	private void render(Graphics g) {
		//Count frames
		fps.count();
		
		//Check validity
		if(map == null) return;
		BlockMap m = map.getMap();
		if(m == null) return;
		
		//Dimensions in tiles
		double tilesW = (getWidth()/ 32);
		double tilesH = (getHeight()/ 32);
		
		//Perspective => offset
		pos.set(perspective);
		pos.add(tilesW/2, tilesH/2);
		
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
		if(ex1 < m.startX) ex1 = m.startX;
		if(ey1 < m.startX) ey1 = m.startY;
		if(ex2 > m.startX) ex2 = m.endX;
		if(ey2 > m.startX) ey2 = m.endY;
		
		//Render tiles
		int startX = (int) ((ex1+pos.x)*32);
		int startY = (int) ((ey1+pos.y)*32);
		for(int i = ex1, x = startX; i < ex2; i++, x += 32) {
			for(int j = ey1, y = startY; j < ey2; j++, y += 32) {
				renderTile(x, y, g, m.get(i, j));
			}
		}
		
		//Draw machines
		for(Machine mc: map.machines) {
			int x = (int)((mc.posX()+pos.x)*32);
			int y = (int)((mc.posY()+pos.y)*32);
			int w = mc.sizeX();
			int h = mc.sizeY();
			@SuppressWarnings("null")
			@Nonnull Graphics g2 = g.create(x, y, w*32, h*32);
			mc.render(g2);
		}
		
		//Draw pointer
		int x = (int)((mouseoverBlockX+pos.x)*32);
		int y = (int)((mouseoverBlockY+pos.y)*32);
		
		//Preview
		if(placer.getPlacer() != null)
			placer.getPlacer().preview(g, new Point(x, y), m, new Point(mouseoverBlockX, mouseoverBlockY));
		g.setColor(Color.BLACK);
		g.drawRect(x, y, 32, 32);
		g.drawRect(x+2, y+2, 28, 28);
		g.setColor(Color.RED);
		g.drawRect(x+1, y+1, 30, 30);
		
		//Debug use only
		if(DEBUG_DISPLAY.getValue()) {
			g.setColor(Color.CYAN);
			g.fillRect(0, 0, 150, 85);
			g.setColor(Color.BLACK);
			g.drawString("Offset: "+pos.x+","+pos.y, 2, 11);
			g.drawString("Selected block: "+mouseoverBlockX+","+mouseoverBlockY, 2, 28);
			g.drawString("BlockEntities: " + m.getBlockEntities().size(), 2, 45);
			g.drawString("FPS: "+fps.get(), 2, 62);
			g.drawString("TPS: "+map.tps.get(), 2, 79);
		}
	}
	public static final ListenerBooleanVariable DEBUG_DISPLAY = new ListenerBooleanVariable();
	private static void renderTile(int x, int y, Graphics g, @Nullable BlockEntry blockEntry) {
		if(blockEntry != null) blockEntry.render(x, y, g);
	}
	private Vector2d pos = new Vector2d();
	/**
	 * The perspective is a snapped camera position
	 */
	public final Vector2d perspective = new Vector2d();
	
	//[end]
	//[start] Activity
	private transient Timer timer = new Timer(20, e -> repaint());
	/**
	 * @return is timer active?
	 */
	public boolean isActive() {
		return timer.isRunning();
	}
	/**
	 * Set the activity of timer.
	 * If timer is active, the frame will be actively rendered
	 * @param a should timer be active?
	 */
	public void setActive(boolean a) {	
		if(timer.isRunning() && !a) {
			timer.setRepeats(true);
			timer.restart();
		}else if(!timer.isRunning() && a) {
			timer.setRepeats(false);
			timer.stop();
		}
	}
	//[end]
	//[start] Mouse position
	private Point mousePosition = new Point();
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
	//[end]
	private void showPopup(MouseEvent e) {
		if(map.inBounds(mouseoverBlockX, mouseoverBlockY)) {
			WorldMenu menu = new WorldMenu(map.get(mouseoverBlockX, mouseoverBlockY), this, window);
			menu.show(this, e.getX(), e.getY());
		}
	}
	//[start] Window reference
	private WorldWindow window;
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
	//[end]
	//[start] Mouseover block
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
	/**
	 * @return the mouseoverBlockX
	 */
	public int getMouseoverBlockX() {
		return mouseoverBlockX;
	}
	/**
	 * @return the mouseoverBlockY
	 */
	public int getMouseoverBlockY() {
		return mouseoverBlockY;
	}
	//[end]
	//[start] Scrollable Placement List
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
	//[end]
}