/**
 * 
 */
package mmb.menu.world.window;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.Timer;
import javax.swing.JComponent;

import org.joml.Vector2d;

import com.github.davidmoten.rtree2.geometry.Geometries;
import com.github.davidmoten.rtree2.geometry.Geometry;
import com.pploder.events.Event;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import mmb.NN;
import mmb.Nil;
import mmb.content.ppipe.Direction;
import mmb.content.ppipe.PipeTunnelEntry;
import mmb.data.variables.ListenableBoolean;
import mmb.engine.CatchingEvent;
import mmb.engine.Vector2iconst;
import mmb.engine.block.BlockEntry;
import mmb.engine.debug.Debugger;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;
import mmb.engine.item.Items;
import mmb.engine.java2d.StringRenderer;
import mmb.engine.mbmachine.Machine;
import mmb.engine.rotate.Side;
import mmb.engine.texture.Textures;
import mmb.engine.visuals.Visual;
import mmb.engine.worlds.universe.Universe;
import mmb.engine.worlds.world.Player;
import mmb.engine.worlds.world.World;
import mmb.menu.world.FPSCounter;
import mmb.menu.world.window.WorldWindow.ScrollablePlacementList;
import mmb.menu.wtool.WindowTool;

import java.awt.Color;

/**
 * The WorldFrame represents interface, which user can interact with.
 * @author oskar
 */
public class WorldFrame extends JComponent {
	//Serialization
	private static final long serialVersionUID = 7346245653768692732L;
	
	//Debugging
	@NN private transient Debugger debug = new Debugger("WORLD - anonymous");
	@NN private static Debugger sdebug = new Debugger("WORLDS");
	/** The global boolean variable controlling debug display */
	@NN public static final ListenableBoolean DEBUG_DISPLAY = new ListenableBoolean();
	
	//Frames Per Second
	/** This variable holds current framerate */
	@NN public final FPSCounter fps = new FPSCounter();
	
	/** Create a new WorldFrame 
	 * @param window the window, which contains the frame
	 */
	public WorldFrame(WorldWindow window) {
		//Dump everything for testing
		StringBuilder list = new StringBuilder();
		boolean i = false;
		list.append('[');
		for(ItemType item: Items.items) {
			if(i) list.append("\n,");
			i = true;
			list.append('"');
			list.append(item.id().replace("\\", "\\\\").replace("\"", "\\\""));
			list.append('"');
		}
		list.append(']');
		
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
	@NN public final transient Event<String> titleChange = new CatchingEvent<>(debug, "Failed to run a title listener");
	/** Runs on each frame */
	@NN public final transient Event<Graphics> redraw = new CatchingEvent<>(debug, "Failed to run a renderer");
	
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
	public void enterWorld(@Nil Universe w) {
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
	public void setMap(@Nil World newMap) {
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
		debug.printl("Exiting");
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
	boolean u, d, l, r;
	private class Listener implements MouseListener, KeyListener, MouseMotionListener, MouseWheelListener{
		@Override public void mouseWheelMoved(@Nil MouseWheelEvent e) {
			Objects.requireNonNull(e, "event is null");
			window.scrollScrollist(e.getWheelRotation()*32);
			WindowTool tool = window.toolModel.getTool();
			setZoom(zoomsel - e.getWheelRotation());
			if(tool != null) tool.mouseWheelMoved(e);
		}
		@Override public void mouseDragged(@Nil MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseDragged(e);
		}
		@Override public void mouseMoved(@Nil MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseMoved(e);
		}
	
		@Override public void keyPressed(@Nil KeyEvent e) {
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
			case KeyEvent.VK_Q:
				map.player.speed.x = 0;
				map.player.speed.y = 0;
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
			case KeyEvent.VK_DOWN:
				d = true;
				break;
			case KeyEvent.VK_UP:
				u = true;
				break;
			case KeyEvent.VK_LEFT:
				l = true;
				break;
			case KeyEvent.VK_RIGHT:
				r = true;
				break;
			default:
				break;
			}
			map.player.setControls(u, d, l, r);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.keyPressed(e);
		}
		@Override public void keyReleased(@Nil KeyEvent e) {
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
			case KeyEvent.VK_DOWN:
				d = false;
				break;
			case KeyEvent.VK_UP:
				u = false;
				break;
			case KeyEvent.VK_LEFT:
				l = false;
				break;
			case KeyEvent.VK_RIGHT:
				r = false;
				break;
			default:
				break;
			}
			map.player.setControls(u, d, l, r);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.keyReleased(e);
		}
		@Override public void keyTyped(@Nil KeyEvent e) {
			Objects.requireNonNull(e, "event is null");
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.keyTyped(e);
		}
	
		@Override public void mouseClicked(@Nil MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseClicked(e);
		}
		@Override public void mouseEntered(@Nil MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseEntered(e);
		}
		@Override public void mouseExited(@Nil MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseExited(e);
		}
		@Override public void mousePressed(@Nil MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			setMousePosition(e);
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mousePressed(e);
		}
		@Override public void mouseReleased(@Nil MouseEvent e) {
			Objects.requireNonNull(e, "event is null");
			WindowTool tool = window.toolModel.getTool();
			if(tool != null) tool.mouseReleased(e);
		} 
	}
	
	//Graphics
	@NN public final transient Event<StringBuilder> informators =
			new CatchingEvent<>(debug, "Failed to process info task");
	@Override
	public void paint(@Nil Graphics g) {
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
		
		//Bind camera to player
		if(window.getCheckBindCameraPlayer().isSelected()) {
			perspective.set(map.player.pos);
			perspective.negate();
		}
				
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
		int l = Math.min(Math.max(ul.x, map.startX), map.endX-1);
		int r = Math.min(Math.max(dr.x, map.startX), map.endX-1);
		int u = Math.min(Math.max(ul.y, map.startY), map.endY-1);
		int d = Math.min(Math.max(dr.y, map.startY), map.endY-1);
		
		//Count Blocks Per Frame
		int bpf = 0;

		//Render tiles
		if(blockScale < 4) {
			//Render LOD
			Point c1 = blockPositionOnScreen(map.startX, map.startY);
			Point c2 = blockPositionOnScreen(map.endX+1, map.endY+1);
			g.drawImage(map.LODs, c1.x, c1.y, c2.x-c1.x, c2.y-c1.y, null);
		}else {
			//Render in full
			int rstartX = (int)Math.ceil((l+pos.x)*blockScale);
			int rstartY = (int)Math.ceil((u+pos.y)*blockScale);
			for(int i = l, x = rstartX; i <= r; i++, x += blockScale) {
				for(int j = u, y = rstartY; j <= d; j++, y += blockScale) {
					renderTile(x, y, g, map.get(i, j));
					bpf++;
				}
			}
		}
		
		int div2x = (int)Math.ceil(blockScale/2);
		int div4x = (int)Math.ceil(blockScale/4);
		int div2y = (int)Math.ceil(blockScale/2);
		int div4y = (int)Math.ceil(blockScale/4);
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
			if(entry.getValue() != null)
				entry.getValue().render(g, pt.x+div4x, pt.y+div4y, div2x, div2y);
		}
		
		//Draw machines
		for(Machine mc: map.machines) {
			int x = (int)((mc.posX()+pos.x)*blockScale);
			int y = (int)((mc.posY()+pos.y)*blockScale);
			int w = mc.sizeX();
			int h = mc.sizeY();
			@NN Graphics g2 = g.create(x, y, (int)Math.ceil(w*blockScale), (int)Math.ceil(h*blockScale));
			mc.render(g2);
		}
		
		//Render visual objects
		Iterable<com.github.davidmoten.rtree2.Entry<Visual, Geometry>> visuals = map.visuals().search(Geometries.rectangle(l, u, r, d));
		AtomicInteger nvisuals = new AtomicInteger();
		AtomicBoolean success = new AtomicBoolean();
		for(com.github.davidmoten.rtree2.Entry<Visual, Geometry> node: visuals) {
			node.value().render(g, WorldFrame.this);
			nvisuals.incrementAndGet();
		}
		
		
		//Render player
		Point pul = worldPositionOnScreen(map.player.pos.x - 0.3, map.player.pos.y - 0.3);
		Point pdr = worldPositionOnScreen(map.player.pos.x + 0.3, map.player.pos.y + 0.3);
		Image img = playerface0;
		switch(map.player.getBlink()) {
		case 1:
		case 2:
		case 5:
		case 6:
			img = playerface1;
			break;
		case 3:
		case 4:
			img = playerface2;
			break;
		case 0:
			break;
		default:
			throw new IllegalStateException("Invalid player animation state: "+map.player.getBlink());
		}
		g.drawImage(img, pul.x, pul.y, pdr.x-pul.x, pdr.y-pul.y, null);
		
		//Pointer
		int x = (int)((mouseover.x+pos.x)*blockScale);
		int y = (int)((mouseover.y+pos.y)*blockScale);
		int framesize = (int)Math.ceil(blockScale)-1;
		thickframe(x, y, framesize, framesize, Color.RED, g);
		
		//Preview
		WindowTool tool = window.toolModel.getTool();
		Objects.requireNonNull(tool, "tool is null");
		tool.preview(x, y, blockScale, g);
			
		//Debug use only
		if(DEBUG_DISPLAY.getValue()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Camera position: ").append(perspective).append("\r\n");
			sb.append("Player position: ").append(map.player.pos).append("\r\n");
			sb.append("Block position: ").append(mouseover.x).append(',').append(mouseover.y).append("\r\n");
			//Get selected block
			if(map.inBounds(mouseover)) {
				BlockEntry block = map.get(mouseover);
				sb.append("Selected block: ").append(block.type()).append(" ,is surface: ").append(block.isSurface()).append("\r\n");
				sb.append("Pipe connections: ^");
				printPipeTunnel(sb, block.getPipeTunnel(Side.U));
				sb.append(" v");
				printPipeTunnel(sb, block.getPipeTunnel(Side.D));
				sb.append(" <");
				printPipeTunnel(sb, block.getPipeTunnel(Side.L));
				sb.append(" >");
				printPipeTunnel(sb, block.getPipeTunnel(Side.R));
				sb.append("\r\n");
				sb.append("Chirotation: ").append(block.getChirotation()).append("\r\n");
			}else {
				sb.append("Out of bounds").append("\r\n");
			}
			sb.append("BlockEntities: ").append(map.blockents.size()).append("\r\n");
			sb.append("FPS: ").append(fps.get()).append("\r\n");
			sb.append("TPS: ").append(map.tps.get()).append("\r\n");
			sb.append("BPF: ").append(bpf).append("\r\n");
			sb.append("Dumped items: ").append(map.drops.size()).append("\r\n");
			sb.append("Visuals on map: ").append(map.visuals().size()).append("\r\n");
			sb.append("Visuals visible: ").append(nvisuals.get()).append("\r\n");
			sb.append("Visuals succeeded: ").append(success.get()).append("\r\n");
			sb.append("Alcohol to be digested: ").append(getPlayer().getDigestibleAlcohol()).append("\r\n");
			sb.append("Alcohol content: ").append(getPlayer().getBAC()).append("\r\n");
			//Information for mouseover block
			BlockEntry ent = getMouseoverBlockEntry();
			if(ent != null) ent.debug(sb);
			informators.trigger(sb);
			//Display mouseover block info
			StringRenderer.renderStringBounded(Color.BLACK, Color.CYAN, Color.BLACK, sb.toString(), 0, 0, 5, 5, g);
		}
	}
	private static void printPipeTunnel(StringBuilder sb, @Nil PipeTunnelEntry ent) {
		if(ent == null) sb.append('X');
		else if(ent.dir == Direction.BWD) sb.append('B');
		else sb.append('F');
	}
	private void renderTile(int x, int y, Graphics g, @Nil BlockEntry blockEntry) {
		if(blockEntry == null) return;
		try {
			blockEntry.render(x, y, g, (int) Math.ceil(blockScale));
		} catch (Exception e) {
			debug.stacktraceError(e, "Failed to render a "+blockEntry.type().title());
		}
	}

	//Positioning
	private Vector2d pos = new Vector2d();
	/**
	 * The perspective is a snapped camera position
	 */
	public final Vector2d perspective = new Vector2d();
	
	//Activity
	@NN private transient Timer timer = new Timer(20, e -> {
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
	
	/**
	 * Shows a pop-up menu
	 * @param e mouse event for the pop-up
	 */
	public void showPopup(MouseEvent e) {
		if(map == null) return;
		if(map.inBounds(mouseover)) {
			WorldPopup menu = new WorldPopup(map.get(mouseover), this);
			menu.show(this, e.getX(), e.getY());
		}
	}
	
	//Window reference
	/** The reference to the world window */
	@NN public final WorldWindow window;
	
	//Mouse position
	@NN private Point mousePosition = new Point();
	private void setMousePosition(MouseEvent e) {
		mousePosition.setLocation(e.getX(), e.getY());
	}
	/** @return X coordinate of the mouse pointer*/
	public int mouseX() {
		return mousePosition.x;
	}
	/** @return Y coordinate of the mouse pointer*/
	public int mouseY() {
		return mousePosition.x;
	}
	/**
	 * Sets the point to the mouse location
	 * @param pt location to write to
	 * @return input
	 */
	public Point mouse(Point pt) {
		pt.setLocation(mousePosition);
		return pt;
	}

	//Mouseover block
	@NN private final Point mouseover = new Point();
	/** @return block position over which mouse is over*/
	@NN public Point getMouseoverBlock() {
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
	
	//Block positioning
	
	//Block position mapper
	/**
	 * Get the block at given screen position
	 * @param x on-frame X coordinate
	 * @param y on-frame Y coordinate
	 * @return the new point with block coordinates
	 */
	@NN public Point blockAt(int x, int y) {
		return blockAt(x, y, new Point());
	}
	/**
	 * @param x on-frame X coordinate
	 * @param y on-frame Y coordinate
	 * @param tgt where to write data?
	 * @return target point with written position
	 */
	@NN public Point blockAt(int x, int y, Point tgt) {
		tgt.x = (int)Math.floor((x / blockScale)-pos.x);
		tgt.y = (int)Math.floor((y / blockScale)-pos.y);
		return tgt;
	}
	/**
	 * @param p on-frame position
	 * @return the new point with block coordinates
	 */
	@NN public Point blockAt(Point p) {
		return blockAt(p.x, p.y);
	}
	/**
	 * @param p on-frame position
	 * @param tgt where to write data?
	 * @return target point with written position
	 */
	@NN public Point blockAt(Point p, Point tgt) {
		return blockAt(p.x, p.y, tgt);
	}
	
	/**
	 * @param x on-frame X coordinate
	 * @param y on-frame Y coordinate
	 * @param tgt where to write data?
	 * @return target point with written position
	 */
	@NN public Vector2d worldAt(double x, double y, Vector2d tgt) {
		tgt.x = (x / blockScale)-pos.x;
		tgt.y = (y / blockScale)-pos.y;
		return tgt;
	}
	
	/**
	 * @param x X coordinate of the screen
	 * @param y Y coordinate of the screen
	 * @return point with on-screen block position of UL corner
	 */
	@NN public Point blockPositionOnScreen(int x, int y) {
		int X = (int) ((x+pos.x)*blockScale);
		int Y = (int) ((y+pos.y)*blockScale);
		return new Point(X, Y);
		
	}
	
	@NN public Point blockPositionOnScreen(int x, int y, Point tgt) {
		tgt.x = (int) ((x+pos.x)*blockScale);
		tgt.y = (int) ((y+pos.y)*blockScale);
		return tgt;
		
	}
	
	@NN public Point worldPositionOnScreen(double x, double y) {
		int X = (int) ((x+pos.x)*blockScale);
		int Y = (int) ((y+pos.y)*blockScale);
		return new Point(X, Y);
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
	private double blockScale = 32;
	/**
	 * @return the blockScale
	 */
	public double getBlockScale() {
		return blockScale;
	}
	/**
	 * @param blockScale the blockScale to set
	 */
	public void setBlockScale(double blockScale) {
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
		if(x1 > x2) renderBlockRange(x2, y1, x1, y2, c, g); //NOSONAR swapping x1 and x2
		else if(y1 > y2) renderBlockRange(x1, y2, x2, y1, c, g); //NOSONAR swapping y1 and y2
		else {
			Point c1 = blockPositionOnScreen(x1, y1);
			Point c2 = blockPositionOnScreen(x2+1, y2+1);
			c2.x -= 1;
			c2.y -= 1;
			thickframe(c1.x, c1.y, c2.x-c1.x, c2.y-c1.y, c, g);
		}
	}
	
	/**
	 * @return current zoom level index
	 */
	public int getZoom() {
		return zoomsel;
	}
	/**
	 * @param zoomsel new zoom level index
	 */
	public void setZoom(int zoomsel) {
		this.zoomsel = zoomsel;
		if(zoomsel < 0 ) this.zoomsel = 0;
		if(zoomsel >= zoomlevels.size()) this.zoomsel = zoomlevels.size() - 1;
		blockScale = zoomlevels.getDouble(this.zoomsel);
	}

	/**
	 * Zoom levels selectable with scroll wheel
	 */
	@NN public static final DoubleList zoomlevels =
		DoubleList.of(   0.012, 0.016,  0.024,  0.032,  0.048,  0.064,  0.080, 0.096,
				         0.120, 0.160,  0.240,  0.320,  0.480,  0.640,  0.800, 0.96,
				         1.200, 1.600,  2.400,  3.200,  4.800,  6.400,  8.000, 9.6,
				        12,     16,     24,     32,     48,     64,     80,    96,
				       120,    160,    240,    320,    480,    640,    800,   960,
				      1200,   1600,   2400,   3200,   4800,   6400,   8000,  9600);
	private int zoomsel = 27;
	
	//Player icon
	@NN private static final BufferedImage playerface0 = Textures.get("player/pchar.png");
	@NN private static final BufferedImage playerface1 = Textures.get("player/pchar1.png");
	@NN private static final BufferedImage playerface2 = Textures.get("player/pchar2.png");
}