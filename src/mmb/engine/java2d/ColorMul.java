/**
 * 
 */
package mmb.engine.java2d;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.LookupTable;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public abstract class ColorMul extends LookupTable {
	protected ColorMul(int offset, int numComponents) {
		super(offset, numComponents);
		// TODO Auto-generated constructor stub
	}
	/** @return the current color */
	@Nonnull public abstract Color color();
	/** @param c new color */
	public abstract void setColor(Color c);
	
	/**
	 * Creates a compatible color mapper.
	 * @param type image type, in same form as from {@link BufferedImage}
	 * @return a {@link ColorMapper} of appropriate type
	 * @throws IllegalArgumentException when image type is
	 * {@code TYPE_CUSTOM}, {@code TYPE_BYTE_BINARY}, {@code TYPE_BYTE_GRAY}, {@code TYPE_USHORT_GRAY} or unknown 
	 */
	public static ColorMul ofType(int type) {
		switch(type) {
		case BufferedImage.TYPE_USHORT_555_RGB:
		case BufferedImage.TYPE_USHORT_565_RGB:	
		case BufferedImage.TYPE_INT_RGB:
		case BufferedImage.TYPE_3BYTE_BGR:
		case BufferedImage.TYPE_INT_BGR:
			return new ColorMulRGB();
		case BufferedImage.TYPE_INT_ARGB:
		case BufferedImage.TYPE_INT_ARGB_PRE:
		case BufferedImage.TYPE_4BYTE_ABGR:
		case BufferedImage.TYPE_4BYTE_ABGR_PRE:
			return new ColorMulRGBA();
		case BufferedImage.TYPE_CUSTOM:
			throw new IllegalArgumentException("Custom image type not supported");
		case BufferedImage.TYPE_BYTE_BINARY:
			throw new IllegalArgumentException("Black and white image type not supported");
		case BufferedImage.TYPE_BYTE_GRAY:
		case BufferedImage.TYPE_USHORT_GRAY:
			throw new IllegalArgumentException("Grayscale image type not supported");
		default:
			throw new IllegalArgumentException("Unknown image type: "+type);
		}
	}
}
