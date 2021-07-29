/**
 * 
 */
package mmb.WORLD.texture;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Icon;

import mmb.WORLD.block.BlockEntry;

/**
 * @author oskar
 *
 */
public class ColorDrawer implements BlockDrawer {
	public final Color c;
	private final ConstSolidIcon icon;
	/**
	 * 
	 */
	public ColorDrawer(Color c) {
		this.c = c;
		icon = new ConstSolidIcon(32, 32, c);
	}

	@Override
	public void draw(BlockEntry ent, int x, int y, Graphics g, int side) {
		Color old = g.getColor();
		g.setColor(c);
		g.fillRect(x, y, side, side);
		g.setColor(old);
	}

	@Override
	public Icon toIcon() {
		return icon;
	}
}
