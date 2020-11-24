/**
 * 
 */
package mmb.WORLD.inventory.InvGUI;

import javax.swing.JPanel;

import mmb.WORLD.inventory.Item;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.inventory.items.Items;
import mmb.WORLD.player.DataLayerPlayer;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

/**
 * @author oskar
 *
 */
public class InvGUI extends JPanel {
	public int scrollInv = 0;
	public int scrollCreative = 0;
	public DataLayerPlayer associated;
	private Point mousePos;
	private Item[] itemBuffer = new Item[0];
	
	/**
	 * @return the invMousePos
	 */
	public Point getInvMousePos() {
		return invMousePos;
	}
	/**
	 * @return the mousePos
	 */
	public Point getMousePos() {
		return mousePos;
	}
	/**
	 * @param mousePos the mousePos to set
	 */
	public void setMousePos(Point pos) {
		mousePos = pos;
		if(associated.creative) {
			invMousePos = (Point) mousePos.clone();
			invMousePos.x -= 32;
		}else {
			invMousePos = mousePos;
		}
	}

	private Point invMousePos = new Point();
	/**
	 * Create the panel.
	 * @param pdata 
	 */
	public InvGUI(DataLayerPlayer pdata) {
		itemBuffer = Items.getItems();
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if(invMousePos.x < 0) {
					scrollCreative += arg0.getWheelRotation();
				}else {
					scrollInv += arg0.getWheelRotation();
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				setMousePos(arg0.getPoint());
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				setMousePos(arg0.getPoint());
			}
		});
		associated = pdata;
		setFocusable(true);
		requestFocusInWindow();
	}
	
	
	public void paintImmediately() {
		paintImmediately(0, 0, getWidth(), getHeight());
	}

	@Override
	public void paint(Graphics g) {
		if(associated == null) return;
		Graphics inv;
		Graphics creative = null;
		if(associated.creative) {
			inv = g.create(0, 0, getWidth()-32, getHeight());
			creative = g.create(0, 0, 32, getHeight());
		}else {
			inv = g.create();
		}
		
		//draw blocks
		if(!associated.creative) return;
		int startY = scrollCreative * -32;
		for(int i = 0; i < itemBuffer.length; i++) {
			itemBuffer[i].getTexture().draw(0, (i*32)+startY, creative);
		}
		
		startY = scrollInv * -32;
		int columns = inv.getClipBounds().width / 32;
		ItemStack[] items = associated.inv.getContents();
		int x = 0;
		loop:
		for(int i = 0; i < itemBuffer.length; i++) {
			for(int j = 0; j < columns; j++) {
				x++;
				if(x == itemBuffer.length) break loop;
				items[i].getTexture().draw((j*32), (i*32)+startY, inv);
			}
		}
	}

}
