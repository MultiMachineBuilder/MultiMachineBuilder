/**
 * 
 */
package mmb.WORLD.gui.window;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import java.util.Objects;

import javax.swing.Timer;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;

import org.joml.Vector2d;

import com.pploder.events.CatchingEvent;
import com.pploder.events.Event;

import mmb.DATA.variables.ListenerBooleanVariable;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.gui.FPSCounter;
import mmb.WORLD.gui.Placer;
import mmb.WORLD.gui.window.WorldWindow.ScrollablePlacementList;
import mmb.WORLD.machine.Machine;
import mmb.WORLD.player.Player;
import mmb.WORLD.tool.WindowTool;
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
	public final Event<String> titleChange = new CatchingEvent<>(debug, "Failed to run a title listener");
	/** Add a title listener 
	 * @param arg0 title listener to be added
	 */
	@Deprecated
	public void addTitleListener(Consumer<String> arg0) {
		titleChange.addListener(arg0);
	}
	/** Remove a title listener 
	 * @param arg0 title listener to be removed*/
	@Deprecated
	public void removeTitleListener(Consumer<String> arg0) {
		titleChange.removeListener(arg0);
	}
	//[end]
	//[start] world & map access
	private transient Universe world;
	private transient World map;
	
	/**
	 * @return the subWorldName
	 */
	public String getSubWorldName() {
		return map.getName();
	}
	/**
	 * @return the world
	 */
	public Universe getWorld() {
		return world;
	}
	/**
	 * Enter given universe
	 * @param w new universe
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
	
	/**
	 * A convienience method to get player
	 * @return
	 */
	public Player getPlayer() {
		if(map != null) return map.player;
		return null;
	}
	//[end]
	//Mouse and key listener
	private class Listener implements MouseListener, KeyListener, MouseMotionListener, MouseWheelListener{
		@Override
		public void mouseWheelMoved(@Nullable MouseWheelEvent e) {
			Objects.requireNonNull(e, "event is null");
			window.scrollScrollist(e.getWheelRotation()*32);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseWheelMoved(e);
		}
	
		@Override
		public void mouseDragged(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseDragged(e);
		}
	
		@Override
		public void mouseMoved(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseMoved(e);
		}
	
		@Override
		public void keyPressed(@Nullable KeyEvent e) {
			Objects.requireNonNull(e, "event is null");
			switch(e.getKeyCode()) {
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
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.keyPressed(e);
		}
	
		@Override
		public void keyReleased(@Nullable KeyEvent e) {
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.keyReleased(e);
		}
	
		@Override
		public void keyTyped(@Nullable KeyEvent e) {
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.keyTyped(e);
		}
	
		@Override
		public void mouseClicked(@Nullable MouseEvent e) {
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseClicked(e);
		}
	
		@Override
		public void mouseEntered(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseEntered(e);
		}
	
		@Override
		public void mouseExited(@Nullable MouseEvent e) {
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseExited(e);
		}
	
		@Override
		public void mousePressed(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mousePressed(e);
		}
	
		@Override
		public void mouseReleased(@Nullable MouseEvent e) {
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseReleased(e);
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
		titleChange.trigger(title);
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
		
		//fill with background
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//Using corner approach instead
		Point ul = blockAt(0, 0);
		Point dr = blockAt(getWidth(), getHeight());
		
		//The four coordinates of targeted rendered blocks
		int l = Math.max(ul.x, m.startX);
		int r = Math.min(dr.x, m.endX-1);
		int u = Math.max(ul.y, m.startY);
		int d = Math.min(dr.y, m.endY-1);
		
		//Count Blocks Per Frame
		int bpf = 0;

		//Render tiles
		int rstartX = (int) ((l+pos.x)*32);
		int rstartY = (int) ((u+pos.y)*32);
		for(int i = l, x = rstartX; i <= r; i++, x += 32) {
			for(int j = u, y = rstartY; j <= d; j++, y += 32) {
				renderTile(x, y, g, m.get(i, j));
				bpf++;
			}
		}
		
		//Draw machines
		for(Machine mc: map.getMap().machines) {
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
		Placer placer0 = placer.getSelectedValue();
		if(placer0 != null)
			placer0.preview(g, new Point(x, y), m, new Point(mouseoverBlockX, mouseoverBlockY));
		g.setColor(Color.BLACK);
		g.drawRect(x, y, 32, 32);
		g.drawRect(x+2, y+2, 28, 28);
		g.setColor(Color.RED);
		g.drawRect(x+1, y+1, 30, 30);
		
		//Debug use only
		if(DEBUG_DISPLAY.getValue()) {
			g.setColor(Color.CYAN);
			g.fillRect(0, 0, 150, 102);
			g.setColor(Color.BLACK);
			g.drawString("Offset: "+pos.x+","+pos.y, 2, 11);
			g.drawString("Selected block: "+mouseoverBlockX+","+mouseoverBlockY, 2, 28);
			g.drawString("BlockEntities: " + m.getBlockEntities().size(), 2, 45);
			g.drawString("FPS: "+fps.get(), 2, 62);
			g.drawString("TPS: "+map.tps.get(), 2, 79);
			g.drawString("BPF: "+bpf, 2, 96);
		}
	}
	private void renderTile(int x, int y, Graphics g, @Nullable BlockEntry blockEntry) {
		if(blockEntry == null) return;
		try {
			blockEntry.render(x, y, g);
		} catch (Exception e) {
			debug.pstm(e, "Failed to render a "+blockEntry.type().title());
		}
	}

	/**
	 * The global boolean variable controlling debug display
	 */
	public static final ListenerBooleanVariable DEBUG_DISPLAY = new ListenerBooleanVariable();
	
	private Vector2d pos = new Vector2d();
	/**
	 * The perspective is a snapped camera position
	 */
	public final Vector2d perspective = new Vector2d();
	
	//[end]
	//[start] Activity
	private transient Timer timer = new Timer(20, e -> {
		repaint();
		requestFocusInWindow();
	});
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
	public void showPopup(MouseEvent e) {
		if(map == null) return;
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
	 * Get the block at given screen position
	 * @param x on-frame X coordinate
	 * @param y on-frame Y coordinate
	 * @return the new point with block coordinates
	 */
	public Point blockAt(int x, int y) {
		int xx = (int)Math.floor((x / 32.0)-pos.x);
		int yy = (int)Math.floor((y / 32.0)-pos.y);
		return new Point(xx, yy);
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
	 * @return the associated Scrollable Placement List
	 */
	public ScrollablePlacementList getPlacer() {
		return placer;
	}
	/**
	 * @param placer the new Scrollable Placement List
	 */
	public void setPlacer(ScrollablePlacementList placer) {
		this.placer = placer;
	}
	//[end]

}