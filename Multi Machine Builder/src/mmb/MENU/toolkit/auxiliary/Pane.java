/**
 * 
 */
package mmb.MENU.toolkit.auxiliary;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import mmb.MENU.toolkit.UIComponent;
import mmb.MENU.toolkit.interfaces.Rectangular;

/**
 * @author oskar
 * A pane is a component with position.
 */
public class Pane implements Rectangular{
	public Point pos;
	public UIComponent comp;
	/**
	 * 
	 * @see mmb.MENU.toolkit.UIComponent#fillVertical()
	 */
	public void fillVertical() {
		comp.fillVertical();
	}
	/**
	 * 
	 * @see mmb.MENU.toolkit.UIComponent#fillHorizontal()
	 */
	public void fillHorizontal() {
		comp.fillHorizontal();
	}
	/**
	 * 
	 * @see mmb.MENU.toolkit.UIComponent#fill()
	 */
	public void fill() {
		comp.fill();
	}
	/**
	 * @param g
	 * @see mmb.MENU.toolkit.UIComponent#prepare(java.awt.Graphics)
	 */
	public void prepare(Graphics g) {
		comp.prepare(g.create(pos.x, pos.y, comp.getWidth(), comp.getHeight()));
	}
	/**
	 * @return
	 * @see mmb.MENU.toolkit.UIComponent#getComponent()
	 */
	public UIComponent getComponent() {
		return comp.getComponent();
	}
	public Pane(Point pos, UIComponent comp) {
		super();
		this.pos = pos;
		this.comp = comp;
	}
	public Pane(int x, int y, UIComponent comp) {
		super();
		this.pos = new Point(x, y);
		this.comp = comp;
	}
	
	public void render(Graphics g) {
		comp.render(g.create(pos.x, pos.y, comp.getWidth(), comp.getHeight()));
	}
	
	@Override
	public int getX() {
		return pos.x;
	}
	@Override
	public int getY() {
		return pos.y;
	}

	/**
	 * @see mmb.MENU.toolkit.UIComponent#getSize()
	 */
	@Override
	public Dimension getSize() {
		return comp.getSize();
	}

	/**
	 * @param size
	 * @see mmb.MENU.toolkit.UIComponent#bindSize(java.awt.Dimension)
	 */
	@Override
	public void bindSize(Dimension size) {
		comp.bindSize(size);
	}

	@Override
	public Point getPos() {
		return pos;
	}

	@Override
	public void bindPos(Point pos) {
		this.pos = pos;
	}
}
