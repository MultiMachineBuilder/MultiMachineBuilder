/**
 * 
 */
package mmb.GRAPHICS.awt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.LookupTable;

/**
 * @author oskar
 *
 */
public abstract class ColorMapper extends LookupTable {
	public ColorMapper(int arg0, int arg1) {
		super(arg0, arg1);
	}
	public abstract int[] getFrom();
	public abstract int[] getTo();
	public abstract Color getFromColor();
	public abstract Color getToColor();
	public abstract void setFrom(Color c);
	public abstract void setTo(Color c);
	
	/**
	 * Creates a compatible color mapper.
	 * If image is binary, the 'from' color is false color and 'to' color is true color
	 * @param type image type, in same form as from {@link BufferedImage}
	 * @param from source color
	 * @param to target color
	 * @return a {@link ColorMapper} of appropriate type
	 * @throws IllegalArgumentException when image type is
	 * {@code TYPE_CUSTOM}, {@code TYPE_BYTE_BINARY}, {@code TYPE_BYTE_GRAY}, {@code TYPE_USHORT_GRAY} or unknown 
	 */
	public static ColorMapper ofType(int type, Color from, Color to) {
		switch(type) {
		case BufferedImage.TYPE_USHORT_555_RGB:
		case BufferedImage.TYPE_USHORT_565_RGB:	
		case BufferedImage.TYPE_INT_RGB:
		case BufferedImage.TYPE_3BYTE_BGR:
		case BufferedImage.TYPE_INT_BGR:
			return new ColorMapperRGB(from, to);
		case BufferedImage.TYPE_INT_ARGB:
		case BufferedImage.TYPE_INT_ARGB_PRE:
		case BufferedImage.TYPE_4BYTE_ABGR:
		case BufferedImage.TYPE_4BYTE_ABGR_PRE:
			return new ColorMapperRGBA(from, to);
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
