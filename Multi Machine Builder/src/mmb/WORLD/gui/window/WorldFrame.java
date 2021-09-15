/**
 * 
 */
package mmb.WORLD.gui.window;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Objects;

import javax.swing.Timer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;

import org.joml.Vector2d;
import org.joml.Vector2i;

import com.pploder.events.CatchingEvent;
import com.pploder.events.Event;

import mmb.Vector2iconst;
import mmb.DATA.variables.ListenerBooleanVariable;
import mmb.MENU.StringRenderer;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.gui.FPSCounter;
import mmb.WORLD.gui.Placer;
import mmb.WORLD.gui.window.WorldWindow.ScrollablePlacementList;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.machine.Machine;
import mmb.WORLD.player.Player;
import mmb.WORLD.tool.WindowTool;
import mmb.WORLD.worlds.universe.Universe;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import java.awt.Color;

/**
 * @author oskar
 * The WorldFrame represents interface, which user can interact with.
 */
public class WorldFrame extends JComponent {
	//Serialization
	private static final long serialVersionUID = 7346245653768692732L;
	
	//Debugging
	private transient Debugger debug = new Debugger("WORLD - anonymous");
	private static Debugger sdebug = new Debugger("WORLDS");
	/** The global boolean variable controlling debug display */
	public static final ListenerBooleanVariable DEBUG_DISPLAY = new ListenerBooleanVariable();
	
	//Frames Per Second
	/** This variable holds current framerate */
	public final FPSCounter fps = new FPSCounter();
	
	/** Create a new WorldFrame 
	 * @param window the window, which contains the frame
	 */
	public WorldFrame(WorldWindow window) {
		this.window = window;
		Listener listener = new Listener();
		addMouseListener(listener);
		addMouseWheelListener(listener);
		addMouseMotionListener(listener);
		addKeyListener(listener);
		setFocusable(true);
		timer.setRepeats(true);
		timer.start();
	}

	//Loading message
	private String altMessage = "Loading";
	/** @return current loading message */
	public String getAltMessage() {
		return altMessage;
	}
	/**
	 * Sets the loading message
	 * @param altMessage new loading message
	 */
	public void setAltMessage(String altMessage) {
		this.altMessage = altMessage;
	}

	//Title
	private String title = "none";
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	//Events
	/** Runs when title changes */
	public final Event<String> titleChange = new CatchingEvent<>(debug, "Failed to run a title listener");
	/** Runs on each frame */
	public final Event<Graphics> redraw = new CatchingEvent<>(debug, "Failed to run a renderer");
	
	//Universe
	private transient Universe world;
	/** @return currently active universe */
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
			w.start();
		}
	}
	
	//World
	private transient World map;
	/** @return current map name, or null if it is main */
	public String getSubWorldName() {
		return map.getName();
	}
	/** @return currently active map */
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
			world.start();
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
	
	//Player
	/**
	 * A convenience method to get player
	 * @return the player belonging to the world
	 */
	public Player getPlayer() {
		if(map != null) return map.player;
		return null;
	}
	
	//Modifier keys
	private boolean alt;
	/** @return is [Alt] pressed? */
	public boolean altPressed() {
		return alt;
	}
	private boolean ctrl;
	/** @return is [Ctrl] pressed? */
	public boolean ctrlPressed() {
		return ctrl;
	}
	private boolean shift;
	/** @return is [Shift] pressed? */
	public boolean shiftPressed() {
		return shift;
	}
	
	//Mouse and key listener
	private class Listener implements MouseListener, KeyListener, MouseMotionListener, MouseWheelListener{
		@Override public void mouseWheelMoved(@Nullable MouseWheelEvent e) {
			Objects.requireNonNull(e, "event is null");
			window.scrollScrollist(e.getWheelRotation()*32);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseWheelMoved(e);
		}
		@Override public void mouseDragged(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseDragged(e);
		}
		@Override public void mouseMoved(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseMoved(e);
		}
	
		@Override public void keyPressed(@Nullable KeyEvent e) {
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
			case KeyEvent.VK_ALT:
				alt = true;
				break;
			case KeyEvent.VK_CONTROL:
				ctrl = true;
				break;
			case KeyEvent.VK_SHIFT:
				shift = true;
				break;
			default:
				break;
			}
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.keyPressed(e);
		}
		@Override public void keyReleased(@Nullable KeyEvent e) {
			Objects.requireNonNull(e, "event is null");
			switch(e.getKeyCode()) {
			case KeyEvent.VK_ALT:
				alt = false;
				break;
			case KeyEvent.VK_CONTROL:
				ctrl = false;
				break;
			case KeyEvent.VK_SHIFT:
				shift = false;
				break;
			default:
				break;
			}
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.keyReleased(e);
		}
		@Override public void keyTyped(@Nullable KeyEvent e) {
			Objects.requireNonNull(e, "event is null");
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.keyTyped(e);
		}
	
		@Override public void mouseClicked(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseClicked(e);
		}
		@Override public void mouseEntered(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseEntered(e);
		}
		@Override public void mouseExited(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseExited(e);
		}
		@Override public void mousePressed(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mousePressed(e);
		}
		@Override public void mouseReleased(@Nullable MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseReleased(e);
		} 
	}
	
	//Graphics
	public final Event<StringBuilder> informators =
			new CatchingEvent<StringBuilder>(debug, "Failed to process render task");
	@Override
	public void paint(@Nullable Graphics g) {
		resetMouseoverBlock();
		redraw.trigger(g);
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
			}
		}else{
			if(world == null) title = "anonymous ["+map.getName()+"]";
			else if(map.getName() == null) title = "main "+world.getName();
			else title = "["+map.getName()+"] "+world.getName();
			debug.id = "WORLD - "+title;
		}
		render(g);
		titleChange.trigger(title);
	}
	private void render(Graphics g) {
		//Count frames
		fps.count();
		
		//Check validity
		if(map == null || !map.isValid()) return;
		//Dimensions in tiles
		double tilesW = (getWidth()/ blockScale);
		double tilesH = (getHeight()/ blockScale);
		
		//Perspective => offset
		pos.set(perspective);
		pos.add(tilesW/2, tilesH/2);
		
		//fill with background
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//Get in-world bounds
		Point ul = blockAt(0, 0);
		Point dr = blockAt(getWidth(), getHeight());
		
		//The four coordinates of targeted rendered blocks
		int l = Math.max(ul.x, map.startX);
		int r = Math.min(dr.x, map.endX-1);
		int u = Math.max(ul.y, map.startY);
		int d = Math.min(dr.y, map.endY-1);
		
		//Count Blocks Per Frame
		int bpf = 0;

		//Render tiles
		int rstartX = (int) ((l+pos.x)*blockScale);
		int rstartY = (int) ((u+pos.y)*blockScale);
		for(int i = l, x = rstartX; i <= r; i++, x += blockScale) {
			for(int j = u, y = rstartY; j <= d; j++, y += blockScale) {
				renderTile(x, y, g, map.get(i, j));
				bpf++;
			}
		}
		
		int div2 = blockScale/2;
		int div4 = blockScale/4;
		Point pt = new Point();
		//Render dropped items
		for(Entry<Vector2iconst, ItemEntry> entry: map.drops.entries()) {
			//Check if the vector is in bounds
			int x = entry.getKey().x;
			int y = entry.getKey().y;
			if(x < l) continue;
			if(x > r) continue;
			if(y < u) continue;
			if(y > d) continue;
			blockPositionOnScreen(x, y, pt);
			if(entry.getValue() == null) continue;
			entry.getValue().render(g, pt.x+div4, pt.y+div4, div2);
		}
		
		//Draw machines
		for(Machine mc: map.machines) {
			int x = (int)((mc.posX()+pos.x)*blockScale);
			int y = (int)((mc.posY()+pos.y)*blockScale);
			int w = mc.sizeX();
			int h = mc.sizeY();
			@SuppressWarnings("null")
			@Nonnull Graphics g2 = g.create(x, y, w*blockScale, h*blockScale);
			mc.render(g2);
		}
		
		//Draw pointer
		int x = (int)((mouseover.x+pos.x)*blockScale);
		int y = (int)((mouseover.y+pos.y)*blockScale);
		
		//Pointer
		thickframe(x, y, blockScale-1, blockScale-1, Color.RED, g);
		
		//Preview
		WindowTool tool = window.toolModel.getTool();
		Objects.requireNonNull(tool, "tool is null");
		tool.preview(x, y, blockScale, g);
			
		//Debug use only
		if(DEBUG_DISPLAY.getValue()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Position: ").append(perspective).append("\r\n");
			sb.append("Selected block: ").append(mouseover.x).append(',').append(mouseover.y).append("\r\n");
			sb.append("BlockEntities: ").append(map.blockents.size()).append("\r\n");
			sb.append("FPS: ").append(fps.get()).append("\r\n");
			sb.append("TPS: ").append(map.tps.get()).append("\r\n");
			sb.append("BPF: ").append(bpf).append("\r\n");
			sb.append("Dumped items: ").append(map.drops.size()).append("\r\n");
			//Information for mouseover block
			BlockEntry ent = getMouseoverBlockEntry();
			if(ent != null) ent.debug(sb);
			informators.trigger(sb);
			//Display mouseover block info
			StringRenderer.renderStringBounded(Color.BLACK, Color.CYAN, Color.BLACK, sb.toString(), 0, 0, 5, 5, g);
		}
	}
	private void renderTile(int x, int y, Graphics g, @Nullable BlockEntry blockEntry) {
		if(blockEntry == null) return;
		try {
			blockEntry.render(x, y, g, blockScale);
		} catch (Exception e) {
			debug.pstm(e, "Failed to render a "+blockEntry.type().title());
		}
	}

	//Positioning
	private Vector2d pos = new Vector2d();
	/**
	 * The perspective is a snapped camera position
	 */
	public final Vector2d perspective = new Vector2d();
	
	//Activity
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
	
	public void showPopup(MouseEvent e) {
		if(map == null) return;
		if(map.inBounds(mouseover)) {
			WorldMenu menu = new WorldMenu(map.get(mouseover), this);
			menu.show(this, e.getX(), e.getY());
		}
	}
	
	//Window reference
	/** The reference to the world window */
	public final WorldWindow window;
	
	//Mouse position
	private Point mousePosition = new Point();
	private void setMousePosition(MouseEvent e) {
		mousePosition.setLocation(e.getX(), e.getY());
	}

	//Mouseover block
	@Nonnull private final Point mouseover = new Point();
	/** @return block position over which mouse is over*/
	public Point getMouseoverBlock() {
		return new Point(mouseover);
	}
	/** @return the block which is currently selected with the mouse */
	public BlockEntry getMouseoverBlockEntry() {
		if(map.inBounds(mouseover)) return map.get(mouseover);
		return null;
	}
	/** @return X coordinate of mouseover block */
	public int getMouseoverBlockX() {
		return mouseover.x;
	}
	/** @return Y coordinate of mouseover block */
	public int getMouseoverBlockY() {
		return mouseover.y;
	}
	private void resetMouseoverBlock() {
		blockAt(mousePosition, mouseover);
	}
	
	//Block position mapper
	/**
	 * Get the block at given screen position
	 * @param x on-frame X coordinate
	 * @param y on-frame Y coordinate
	 * @return the new point with block coordinates
	 */
	public Point blockAt(int x, int y) {
		return blockAt(x, y, new Point());
	}
	/**
	 * @param x on-frame X coordinate
	 * @param y on-frame Y coordinate
	 * @param tgt where to write data?
	 * @return target point with written position
	 */
	public Point blockAt(int x, int y, Point tgt) {
		tgt.x = (int)Math.floor(((double)x / blockScale)-pos.x);
		tgt.y = (int)Math.floor(((double)y / blockScale)-pos.y);
		return tgt;
	}
	/**
	 * @param p on-frame position
	 * @return the new point with block coordinates
	 */
	public Point blockAt(Point p) {
		return blockAt(p.x, p.y);
	}
	/**
	 * @param p on-frame position
	 * @param tgt where to write data?
	 * @return target point with written position
	 */
	public Point blockAt(Point p, Point tgt) {
		return blockAt(p.x, p.y, tgt);
	}
	
	public Point blockPositionOnScreen(int x, int y) {
		int X = (int) ((x+pos.x)*blockScale);
		int Y = (int) ((y+pos.y)*blockScale);
		return new Point(X, Y);
		
	}
	
	public Point blockPositionOnScreen(int x, int y, Point tgt) {
		tgt.x = (int) ((x+pos.x)*blockScale);
		tgt.y = (int) ((y+pos.y)*blockScale);
		return tgt;
		
	}
	
	//Scrollable Placement List
	private ScrollablePlacementList placer;
	/** @return the associated Scrollable Placement List */
	public ScrollablePlacementList getPlacer() {
		return placer;
	}
	/** @param placer the new Scrollable Placement List */
	public void setPlacer(ScrollablePlacementList placer) {
		this.placer = placer;
	}
	
	//Block scaling
	private int blockScale = 32;
	/**
	 * @return the blockScale
	 */
	public int getBlockScale() {
		return blockScale;
	}
	/**
	 * @param blockScale the blockScale to set
	 */
	public void setBlockScale(int blockScale) {
		this.blockScale = blockScale;
	}

	public static void thickframe(int x, int y, int w, int h, Color c, Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(x-1, y-1, w+2, h+2);
		g.drawRect(x+1, y+1, w-2, h-2);
		g.setColor(c);
		g.drawRect(x, y, w, h);
	}
	
	public void renderBlockRange(int x1, int y1, int x2, int y2, Color c, Graphics g) {
		if(x1 > x2) renderBlockRange(x2, y1, x1, y2, c, g);
		else if(y1 > y2) renderBlockRange(x1, y2, x2, y1, c, g);
		else {
			Point c1 = blockPositionOnScreen(x1, y1);
			Point c2 = blockPositionOnScreen(x2+1, y2+1);
			c2.x -= 1;
			c2.y -= 1;
			thickframe(c1.x, c1.y, c2.x-c1.x, c2.y-c1.y, c, g);
		}
	}
}