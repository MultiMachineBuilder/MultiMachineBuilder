/**
 * 
 */
package mmb.MENU.toolkit.interfaces;

import java.awt.Point;

/**
 * @author oskar
 *
 */
@SuppressWarnings("javadoc")
public interface Positioned {
	/**
	 * Get the bound size
	 */
	
	public Point getPos();
	
	default public void setPos(int x, int y) {
		setX(x);
		setY(y);
	}
	/**
	 * Set position, but keep input disconnected
	 */
	default public void setPos(Point pos) {
		setX(pos.x);
		setY(pos.y);
	}
	/**
	 * Create the new position which is bound to the original object, but not input position
	 * @param pos source position
	 */
	default public Point createBoundPos(Point pos) {
		Point result = (Point) pos.clone();
		bindPos(result);
		return result;
	}
	
	/**
	 * Create a bound position using coordinate
	 * @param x @param y coordinates
	 */
	default public Point createBoundPos(int x, int y) {
		Point result = new Point(x, y);
		bindPos(result);
		return result;
	}
	
	default public void setX(int x) {
		getPos().x = x;
	}
	default public void setY(int y) {
		getPos().y = y;
	}
	
	/**
	 * Bind given position to the component
	 */
	public void bindPos(Point pos);
	
	default public int getX() {
		return getPos().y;
	}
	default public int getY() {
		return getPos().y;
	}
	default public Point getUnboundPos() {
		return (Point) getPos().clone();
	}
}
