/**
 * 
 */
package mmb.engine.java2d;

import java.awt.Color;

import mmb.NN;
import mmb.Nil;

/**
 * @author oskar
 *
 */
public class ColorMulRGBA extends ColorMul {
	
	@SuppressWarnings("null")
	@NN private Color mutiplier = Color.GRAY;
	/**
	 */
	protected ColorMulRGBA() {
		super(0, 4);
	}

	@Override
	public int[] lookupPixel(@SuppressWarnings("null") int[] src, int @Nil [] dest) {
		int[] dest0 = dest;
		if(dest0 == null) dest0 = new int[4];
		int r = src[0] * mutiplier.getRed() / 128;
		int g = src[1] * mutiplier.getGreen() / 128;
		int b = src[2] * mutiplier.getBlue() / 128;
		int a = src[3];
		int rr = r;
		int rg = g;
		int rb = b;
		int ra = a;
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
		dest0[3] = ra;
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
