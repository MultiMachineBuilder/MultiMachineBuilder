/**
 * 
 */
package mmb.tileworld;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author oskar
 *
 */
public class DrawerPlainColor implements BlockDrawer {
	public Color color;
	/**
	 * 
	 */
	public DrawerPlainColor(Color c) {
		color = c;
	}

	/* (non-Javadoc)
	 * @see mmb.tileworld.BlockDrawer#draw(int, int, java.awt.Graphics)
	 */
	@Override
	public void draw(int x, int y, Graphics g) {
		g.setColor(color);
		g.drawRect(x, y, 32, 32);
	}

}
