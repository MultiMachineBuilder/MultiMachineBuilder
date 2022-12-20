/**
 * 
 */
package mmb.engine.java2d;

import java.awt.Color;
import java.util.Arrays;

import mmb.Nil;

/**
 * @author oskar
 *
 */
public class ColorMapperRGB extends ColorMapper {
	private final int[] from;
    private final int[] to;

    /**
     * @param from source color
     * @param to target color
     */
    public ColorMapperRGB(Color from, Color to) {
        super(0, 3);

        this.from = new int[] {
            from.getRed(),
            from.getGreen(),
            from.getBlue(),
        };
        this.to = new int[] {
            to.getRed(),
            to.getGreen(),
            to.getBlue(),
        };
    }

    @Override
    public int[] lookupPixel(@SuppressWarnings("null") int[] src,
                             int @Nil [] dest) {
    	int[] dest0;
        if (dest == null) dest0 = new int[src.length];
        else dest0 = dest;

        int[] newColor = (Arrays.equals(src, from) ? to : src);
        System.arraycopy(newColor, 0, dest0, 0, newColor.length);

        return dest0;
    }

	/**
	 * @return source color, bound to the color mapper
	 */
	@Override
	public int[] getFrom() {
		return from;
	}

	/**
	 * @return target color, bound to the color mapper
	 */
	@Override
	public int[] getTo() {
		return to;
	}
    
	/**
	 * Sets the source color
	 * @param c color to be replaced
	 */
	@Override
	public void setFrom(Color c) {
		from[0] = c.getRed();
		from[1] = c.getGreen();
		from[2] = c.getBlue();
	}
    
	/**
	 * Sets the destination color
	 * @param c replacement color
	 */
	@Override
	public void setTo(Color c) {
		to[0] = c.getRed();
		to[1] = c.getGreen();
		to[2] = c.getBlue();
	}

	/**
	 * @return source color, in form of copied {@link Color} object from source array
	 */
	@Override
	public Color getFromColor() {
		return new Color(from[0], from[1], from[2]);
	}
	/**
	 * @return destination color, in form of copied {@link Color} object from destination array
	 */
	@Override
	public Color getToColor() {
		return new Color(to[0], to[1], to[2]);
	}
}
