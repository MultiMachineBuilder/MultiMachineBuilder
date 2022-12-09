/**
 * 
 */
package mmb.engine.texture;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Icon;

import org.joml.Vector2fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import mmb.engine.block.BlockEntry;
import mmb.engine.gl.GLHelper;
import mmb.engine.gl.RenderCtx;

/**
 * @author oskar
 *
 */
public class ColorDrawer implements BlockDrawer {
	public final Color c;
	private final ConstSolidIcon icon;
	public final float r, g, b, a;
	/**
	 * 
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
	public void draw(BlockEntry ent, int x, int y, Graphics g, int w, int h) {
		Color old = g.getColor();
		g.setColor(c);
		g.fillRect(x, y, w, h);
		g.setColor(old);
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
