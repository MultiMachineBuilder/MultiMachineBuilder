package mmb.engine.blockdrawer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.block.BlockEntry;

/**
 * @author Monniasza
 * Transforms a {@link BlockDrawer} around a block's center using an affine transform
 */
public class TransformDrawer implements BlockDrawer {
	private final BlockDrawer inner;
	private final AffineTransform transform = new AffineTransform();
	/**
	 * Creates an affine-transformed block drawer
	 * @param inner block drawer to transform
	 * @param transform transformation applied to the {@code inner} around a center of a block
	 */
	public TransformDrawer(BlockDrawer inner, AffineTransform transform) {
		super();
		this.inner = inner;
		this.transform.setTransform(transform);
	}

	@Override
	public void draw(@Nil BlockEntry ent, int x, int y, Graphics g, int w, int h) {
		int corX = x + (w/2);
		int corY = y + (h/2);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform oldTransform = g2d.getTransform();
		
		//to origin, original transform, back to place
		g2d.translate(-corX, -corY);
		g2d.transform(transform);
		g2d.translate(corX, corY);
		
		//apply the texture
		inner.draw(ent, x, y, g, w, h);
		
		//revert the previous transform
		g2d.setTransform(oldTransform);
	}

	@Override
	public @NN Icon toIcon() {
		return inner.toIcon();
	}

	@Override
	public int LOD() {
		return inner.LOD();
	}

}
