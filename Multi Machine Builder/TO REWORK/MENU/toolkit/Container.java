/**
 * 
 */
package mmb.MENU.toolkit;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import mmb.MENU.toolkit.auxiliary.Pane;
import mmb.MENU.toolkit.events.ContainerCEH;
import mmb.MENU.toolkit.events.ContainerSEH;
import mmb.MENU.toolkit.events.SharedEventHandler;

/**
 * @author oskar
 *
 */
public abstract class Container extends UIComponent{
	public UIComponent mouseover;
	@Override
	public SharedEventHandler getCommonEventHandler() {
		return new ContainerSEH(this);
	}

	@Override
	public ComponentEventHandler getHandler() {
		return new ContainerCEH(this);
	}

	protected Pane[] comppos;

	public Rectangle[] getContentBoxes() {
		Rectangle[] boxes = new Rectangle[comppos.length];
		for(int i = 0; i < comppos.length; i++) {
			boxes[i] = new Rectangle(comppos[i].pos, comppos[i].getSize());
		}
		return boxes;
	}

	/*@Override
	public void prepare(Graphics g) {
		for(int i = 0; i < comppos.length; i++) {
			Pane compos = comppos[i];
			Graphics sub = g.create(compos.getX(), compos.getY(), compos.getWidth(), compos.getHeight());
			compos.comp.prepare(sub);
		}
	}*/

	@Override
	public void destroy() {
		UIComponent[] comps = getComponents();
		for(int i = 0; i < comps.length; i++) {
			comps[i].destroy();
		}
	}

	public UIComponent[] getComponents() {
		UIComponent[] comps = new UIComponent[comppos.length];
		for(int i = 0; i < comppos.length; i++) {
			comps[i] = comppos[i].comp;
		}
		return comps;
	}

	@Override
	public void render(Graphics g) {
		for(int i = 0; i < comppos.length; i++) {
			Pane compos = comppos[i];
			Graphics sub = g.create(compos.getX(), compos.getY(), compos.getWidth(), compos.getHeight());
			compos.comp.render(sub);
		}
	}
	
	/**
	 * Get bounding boxes of contained components
	 */
	@SuppressWarnings("javadoc")
	public int getComponentIndexByPoint(Point p) {
		Rectangle[] boxes = getContentBoxes();
		for(int i = 0; i < boxes.length; i++) {
			if(boxes[i].contains(p)) return i;
		}
		return -1;
	}
	public static class ComponentPoint{
		public int index;
		public UIComponent component;
		public Point pos; //local position
		@Override
		public String toString() {
			return "ComponentPoint [index=" + index + ", component=" + component + ", pos=" + pos + "]";
		}
	}
	
	/**
	 * Gets the component, its index, and its local position.
	 * @param p source point
	 * @return bundled results
	 */
	public ComponentPoint getComponentAndPoint(Point p) {
		int index = -1;
		
		loop:
		for(int i = 0; i < comppos.length; i++) {
			if(comppos[i].contains(p)) {
				index = i;
				break loop;
			}
		}
		
		Point pos;
		UIComponent uic;
		if(index < 0) {
			pos = null;
			uic = null;
		}else {
			pos = new Point(p.x - comppos[index].getX(), p.y - comppos[index].getY());
			uic = comppos[index].comp;
		}
		
		ComponentPoint cp = new ComponentPoint();
		cp.component = uic;
		cp.index = index;
		cp.pos = pos;
		return cp;
	}
}
