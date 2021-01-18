/**
 * 
 */
package mmb.WORLD_new.gui;

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

import mmb.WORLD.tileworld.BlockDrawer;
import mmb.WORLD_new.block.BlockEntry;
import mmb.WORLD_new.worlds.map.BlockMap;
import mmb.WORLD_new.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class WorldFrame extends JComponent implements MouseListener, KeyListener, MouseMotionListener, MouseWheelListener {
	
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
			map = w.main;
		}else {
			sdebug.printl("Opening a new world");
			world = w;
			map = w.main;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_A:
			pos.x -=1;
			break;
		case KeyEvent.VK_D:
			pos.x += 1;
			break;
		case KeyEvent.VK_W:
			pos.y -=1;
			break;
		case KeyEvent.VK_S:
			pos.y += 1;
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
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void paint(Graphics g) {
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
		Dimension mapDim = map.size();
		double endX = (getWidth()/32)+pos.x;
		double endY = (getHeight()/32)+pos.y;
		int minTileX = (int) pos.x;
		if(minTileX < 0) minTileX = 0;
		int minTileY = (int) pos.y;
		if(minTileY < 0) minTileY = 0;
		int maxTileX = (int) Math.ceil(endX);
		if(maxTileX >= mapDim.width) maxTileX = mapDim.width-1;
		int maxTileY = (int) Math.ceil(endY);
		if(maxTileY >= mapDim.height) maxTileY = mapDim.height-1;
		int sX = (int) (pos.x * -32);
		int sY = (int) (pos.y * -32);
		for(int i = minTileX, x = sX; i <= maxTileX; i++, x += 32) {
			for(int j = minTileY, y = sY; j <= maxTileY; j++, y += 32) {
				renderTile(x, y, g, i, j, map.get(i, j), i, j);
			}
		}
	}
	private void renderTile(int x, int y, Graphics g, int i, int j, BlockEntry be, int wx, int wy) {
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
		active = a;
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
	}
}
