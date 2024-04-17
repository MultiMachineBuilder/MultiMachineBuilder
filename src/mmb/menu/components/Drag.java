package mmb.menu.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import org.joml.Vector2ic;

import com.pploder.events.Event;
import com.pploder.events.SimpleEvent;

import mmb.NN;
import mmb.engine.Vector2iconst;
import mmb.engine.texture.Textures;

/**
 * A component which sends events when dragged.
 */
public class Drag extends JComponent {
	
	private static final long serialVersionUID = -2395142506211941851L;
	private static final BufferedImage dragimage = Textures.get("move.png");
	
	private int lastx;
	private int lasty;
	
	/** Invoked when this component is dragged */
	@NN public final Event<Vector2ic> dragged = new SimpleEvent<>();
	
	/** Creates a dragger component */
	public Drag() {
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setBackground(Color.BLUE);
		Listener listener = new Listener();
		addMouseMotionListener(listener);
		addMouseListener(listener);
	}
	
	private class Listener implements MouseMotionListener, MouseListener{
		@Override
		public void mouseDragged(MouseEvent e) {
			int dragx = e.getX() - lastx;
			int dragy = e.getY() - lasty;
			lastx = e.getX();
			lasty = e.getY();
			dragged.trigger(new Vector2iconst(dragx, dragy));
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			lastx = e.getX();
			lasty = e.getY();
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	protected void paintComponent(@SuppressWarnings("null") Graphics g) {
		g.drawImage(dragimage, 0, 0, getWidth(), getHeight(), null);
	}
	
}
