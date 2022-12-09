/**
 * 
 */
package mmb.engine.java2d;

import java.awt.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author oskar
 *
 */
public class ColorMulRGB extends ColorMul {
	
	@SuppressWarnings("null")
	@Nonnull private Color mutiplier = Color.GRAY;
	/**
	 * @param offset
	 * @param numComponents
	 */
	protected ColorMulRGB() {
		super(0, 3);
	}

	@Override
	public int[] lookupPixel(@SuppressWarnings("null") int[] src, @Nullable int[] dest) {
		int[] dest0 = dest;
		if(dest0 == null) dest0 = new int[3];
		int r = src[0] * mutiplier.getRed() / 128;
		int g = src[1] * mutiplier.getGreen() / 128;
		int b = src[2] * mutiplier.getBlue() / 128;
		int rr = r;
		int rg = g;
		int rb = b;
		if(r > 255) {
			int x = r - 255;
			rg += x;
			rb += x;
			r = 255;
		}
		if(g > 255) {
			int x = g - 255;
			rr += x;
			rb += x;
			g = 255;
		}
		if(b > 255) {
			int x = b - 255;
			rr += x;
			rg += x;
			b = 255;
		}
		if(rr > 255) rr = 255;
		if(rg > 255) rg = 255;
		if(rb > 255) rb = 255;
		dest0[0] = rr;
		dest0[1] = rg;
		dest0[2] = rb;
		return dest0;
	}

	@Override
	public Color color() {
		return mutiplier;
	}

	@Override
	public void setColor(Color c) {
		mutiplier = c;
	}

}
