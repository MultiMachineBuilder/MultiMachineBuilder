/**
 * 
 */
package mmb.tileworld;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JPanel;

import org.joml.Vector2d;

import mmb.debug.Debugger;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

/**
 * @author oskar
 *
 */
public class TileGUI extends JPanel {
	public Vector2d offset;
	public TileMap map;
	private Debugger debug = new Debugger("TILES");
	public Point mousePos = new Point(0 ,0);
	private TileGUI that = this;
	/**
	 * Create the panel.
	 */
	public TileGUI() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Point p = arg0.getPoint();
				if(arg0.getButton() == 1) {
					Point world = blockByPoint(p);
					if(map.check(world)) {
						debug.printl(world.toString());
						int i = map.indexAtPos(world);
						if(map.data[i] == 0) {
							map.data[i] = 1;
						}else {
							map.data[i] = 0;
						}
						paintImmediately();
					}
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				mousePos = arg0.getPoint();
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				debug.printl("key:"+arg0.getKeyCode());
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
				
				debug.printl(offset.toString());
				paintImmediately();
			}
		});
		setFocusable(true);
	}
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		double endX = (this.getWidth() / 32) + offset.x;
		double endY = (this.getHeight() / 32) + offset.y;
		int ex1 = (int) Math.floor(offset.x);
		int ey1 = (int) Math.floor(offset.y);
		int ex2 = (int) Math.ceil(endX);
		int ey2 = (int) Math.ceil(endY);
		//debug.printl(ex1+","+ey1);
		for(int x = ex1; x <=ex2; x++) {
			for(int y = ey1; y <=ey2; y++) {
				paintTile(x, y, g);
			}
		}
	}
	
	private void paintTile(int x, int y, Graphics g) {
		int result = -1;
		if(map.check(x, y)) {
			result = map.getAtPos(x, y);
		}
		Color c;
		if(result == 0) {
			c = Color.CYAN;
		}else if(result == 1) {
			c = Color.GREEN;
		}else if(result == -1) {
			c = Color.darkGray;
		}else {
			c = Color.MAGENTA;
		}
		
		g.setColor(c);
		g.fillRect((int)(x-offset.x)*32, (int)(y-offset.y)*32, 32, 32);
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
	public void paintImmediately() {
		that.paintImmediately(0, 0, getWidth(), getHeight());
	}

}
