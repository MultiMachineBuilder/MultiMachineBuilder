/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * @author oskar
 *
 */
public class ScrollablePlacementList extends JComponent implements MouseListener, MouseWheelListener{
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(128, getParent().getHeight());
	}

	private static final long serialVersionUID = -208562764791915412L;
	/**
	 * A list of all placers that player can choose
	 */
	public final List<Placer> placers = new ArrayList<>();
	private int placerIndex = -1;
	private transient Placer placer;
	/**
	 * @return the placerIndex
	 */
	public int getPlacerIndex() {
		return placerIndex;
	}

	/**
	 * @param placerIndex the placerIndex to set
	 */
	public void setPlacerIndex(int placerIndex) {
		if(placerIndex < 0) placerIndex = 0;
		if(placerIndex > placers.size()) placerIndex = placers.size();
		placer = placers.get(placerIndex);
		this.placerIndex = placerIndex;
	}

	/**
	 * @return the placer
	 */
	public Placer getPlacer() {
		return placer;
	}

	/**
	 * @param placer the placer to set
	 */
	public void setPlacer(Placer placer) {
		if(placer == null) return;
		int i = 0;
		for(Placer p: placers) {
			if(p == placer) setPlacerIndex(i);
			i++;
		}
	}

	/**
	 * How much is this placement list scrolled, in pixels
	 */
	public int scroll;
	public ScrollablePlacementList() {
		setFocusable(false);
		addMouseWheelListener(this);
		addMouseListener(this);
	}

	@Override
	public void paint(Graphics g) {
		int offset = -scroll;
		int i = 0;
		for(Placer p: placers){
			if(i == placerIndex) {
				//Selected
				g.setColor(Color.CYAN);
				g.fillRect(0, offset, getWidth(), 40);
			}
			//Paint a placer
			g.setColor(Color.BLACK);
			g.drawImage(p.getIcon(), 4, offset+4, null);
			g.drawString(p.getTitle(), 40, offset+20);
			offset += 40;
			i++;
		}
	}
	/**
	 * @param arg0
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(Placer arg0) {
		return placers.add(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object arg0) {
		return placers.remove(arg0);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int listSize() {
		return placers.size();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//unused
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//unused
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//unused
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//Select a placer
		int x = (e.getY() + scroll)/40;
		setPlacerIndex(x);
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//unused
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		scroll += arg0.getPreciseWheelRotation() * 20;
		repaint();
	}
}
