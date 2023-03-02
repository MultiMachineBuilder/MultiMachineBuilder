/**
 * 
 */
package mmb.engine.texture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.NN;
import mmb.Nil;
import mmb.engine.block.BlockEntry;

/**
 * A basic textured block drawer
 * @author oskar
 */
public class TextureDrawer implements BlockDrawer {
	/** The image which is drawn */
	@NN public final BufferedImage img;
	/** The LOD color */
	private int LOD = -1;
	@NN private final Icon icon;
	/**
	 * Creates a texture drawer
	 * @param img2 the image to be drawn
	 */
	public TextureDrawer(BufferedImage img2) {
		this.img = img2;
		icon = new ImageIcon(img);
	}
	@Override
	public void draw(@Nil BlockEntry ent, int x, int y, Graphics g, int w, int h) {
		g.drawImage(img, x, y, w, h, null);
	}
	@Override
	public Icon toIcon() {
		return icon;
	}
	@Override
	public int LOD() {
		if(LOD < 0) 
			LOD = LODs.calcLOD(img);
		return LOD;
	}
}
