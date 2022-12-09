/**
 * 
 */
package mmb.engine.texture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.joml.Vector4f;
import org.joml.Vector4fc;

import mmb.engine.block.BlockEntry;
import mmb.engine.gl.RenderCtx;
import mmb.engine.texture.Textures.Texture;

/**
 * @author oskar
 * Draws a block using teture
 */
public class TextureDrawer implements BlockDrawer {
	/**
	 * The image which is drawn
	 */
	public final BufferedImage img;
	private int LOD = -1;
	@Nonnull private final Icon icon;
	/**
	 * @param img the image to be drawn
	 */
	public TextureDrawer(BufferedImage img2) {
		this.img = img2;
		icon = new ImageIcon(img);
	}
	@Override
	public void draw(BlockEntry ent, int x, int y, Graphics g, int w, int h) {
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
