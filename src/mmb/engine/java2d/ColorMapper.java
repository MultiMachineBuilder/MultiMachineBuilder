/**
 * 
 */
package mmb.engine.java2d;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.LookupTable;

/**
 * Maps a single color from origin to destination
 * @author oskar
 */
public abstract class ColorMapper extends LookupTable {
	/**
	 * Creates a color mapper
	 * @param offset array offset of component array
	 * @param numComponents number of color components
	 */
	protected ColorMapper(int offset, int numComponents) {
		super(offset, numComponents);
	}
	/** @return source color array */
	public abstract int[] getFrom();
	/** @return destination color array */
	public abstract int[] getTo();
	/** @return source color*/
	public abstract Color getFromColor();
	/** @return destination color*/
	public abstract Color getToColor();
	/**
	 * Sets the source color
	 * @param c new source color
	 */
	public abstract void setFrom(Color c);
	/**
	 * Sets the destination color
	 * @param c new source color
	 */
	public abstract void setTo(Color c);
	
	/**
	 * Creates a compatible color mapper.
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
