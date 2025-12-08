/**
 * 
 */
package mmb.engine.texture;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Icon;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.block.BlockEntry;

/**
 * A single color block drawer
 * @author oskar
 */
public class ColorDrawer implements BlockDrawer {
	
	/** The color of this block drawer */
	@NN public final Color c;
	@NN private final ConstSolidIcon icon;
	public final float r, g, b, a;
	
	
	/**
	 * Creates a single color block drawer
	 * @param c color
	 */
	public ColorDrawer(Color c) {
		this.c = c;
		icon = new ConstSolidIcon(32, 32, c);
		r = c.getRed() / 255.0f;
		g = c.getGreen() / 255.0f;
		b = c.getBlue() / 255.0f;
		a = c.getAlpha() / 255.0f;
	}

	@Override
	public void draw(@Nil BlockEntry ent, int x, int y, Graphics gr, int w, int h) {
		Color old = gr.getColor();
		gr.setColor(c);
		gr.fillRect(x, y, w, h);
		gr.setColor(old);
	}

	@Override
	public Icon toIcon() {
		return icon;
	}

	@Override
	public int LOD() {
		return c.getRGB();
	}
}
