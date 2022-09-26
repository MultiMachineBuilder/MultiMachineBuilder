/**
 * 
 */
package mmb.GRAPHICS.awt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.texture.BlockDrawer;

/**
 * @author oskar
 * A drawer which can have colors remapped;
 */
public class MappedColorTexture implements BlockDrawer {

	private BufferedImage result;
	private final ColorMapper mapper;
	private int LOD = 0;
	/**
	 * @param c
	 * @see mmb.GRAPHICS.awt.ColorMapperRGBA#setFrom(java.awt.Color)
	 */
	public void setFrom(Color c) {
		synchronized(drawLock) {
			mapper.setFrom(c);
			resetColors();
		}
	}
	/**
	 * 
	 */
	private void resetColors() {
		op.filter(src, result);
	}
	/**
	 * Sets target color of this mapped texture
	 * @param c
	 * @see mmb.GRAPHICS.awt.ColorMapperRGBA#setTo(java.awt.Color)
	 */
	public void setTo(Color c) {
		synchronized(drawLock) {
			mapper.setTo(c);
			LOD = c.getRGB();
			resetColors();
		}
	}

	private final BufferedImageOp op;
	private Object drawLock = new Object();
	private final BufferedImage src;
	/**
	 * Creates a new color-mapped texture
	 * @param from source color
	 * @param to destination color
	 * @param src source image
	 */
	public MappedColorTexture(Color from, Color to, BufferedImage src) {
		mapper = ColorMapper.ofType(src.getType(), from, to);
		op = new LookupOp(mapper, null);
		this.src = src;
		result = op.createCompatibleDestImage(new BufferedImage(src.getWidth(), src.getHeight(), src.getType()), null);
		icon = new ImageIcon(result);
		resetColors();
	}
	@Override
	public void draw(@Nullable BlockEntry ent, int x, int y, Graphics g, int w, int h) {
		synchronized(drawLock) {
			g.drawImage(result, x, y, w, h, null);
		}
	}

	@Nonnull private final ImageIcon icon;
	@Override
	public Icon toIcon() {
		return icon;
	}
	/**
	 * @return the result
	 */
	public BufferedImage getResult() {
		return result;
	}
	@Override
	public int LOD() {
		return LOD;
	}	
}
