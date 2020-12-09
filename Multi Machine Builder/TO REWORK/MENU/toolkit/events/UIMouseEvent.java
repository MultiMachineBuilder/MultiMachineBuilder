/**
 * 
 */
package mmb.MENU.toolkit.events;

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * @author oskar
 *
 */
public class UIMouseEvent {
	public Point p;
	public int button;
	public UIMouseEvent(Point p, int button) {
		super();
		this.p = p;
		this.button = button;
	}
	public UIMouseEvent(MouseEvent e) {
		p = e.getPoint();
		button = e.getButton();
	}

	public UIMouseEvent() {
		// TODO Auto-generated constructor stub
	}
	
	public UIMouseEvent(Point p) {
		this.p = p;
	}

}
