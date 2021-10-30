/**
 * 
 */
package mmb.MENU;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.joml.Vector2d;

import mmb.WORLD.block.BlockEntry;
import mmb.debug.Debugger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * @author oskar
 *
 */
public class TestCollision extends JFrame {
	static final Debugger debug = new Debugger("TEST COLLISIONS");
	public TestCollision() {
		add(new TestCollisionModule());
	}
	private static final long serialVersionUID = -6745749211945372942L;
	
	private class TestCollisionModule extends JComponent{
		private static final long serialVersionUID = 5472170491185104730L;
		private double posX, posY;
		public TestCollisionModule() {
			addMouseMotionListener(new MouseMotionListener() {
				@Override
				public void mouseDragged(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseMoved(MouseEvent arg0) {
					//Calculate the collision
					posX = arg0.getX() / 64.0;
					posY = arg0.getY() / 64.0;
					Vector2d vec = new Vector2d();
					double l = posX-0.25;
					double r = posX+0.25;
					double u = posY-0.25;
					double d = posY+0.25;
					BlockEntry.displace(1, 1, 2, 2, l, u, r, d, vec);
					posX += vec.x;
					posY += vec.y;
					repaint();
				}
			});
		}
		@Override
		public void paint(@SuppressWarnings("null") Graphics g) {
			g.setColor(Color.RED);
			g.fillRect(64, 64, 64, 64);
			g.setColor(Color.BLUE);
			g.fillRect((int)((posX-0.25)*64), (int)((posY-0.25)*64), 32, 32);
		}
	}

}
