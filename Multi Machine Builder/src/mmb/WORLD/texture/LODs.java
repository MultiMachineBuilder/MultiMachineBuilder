/**
 * 
 */
package mmb.WORLD.texture;

import java.awt.image.BufferedImage;

import org.joml.Vector3i;
import org.joml.Vector4i;

/**
 * @author oskar
 *
 */
public class LODs {
	private LODs() {}
	/**
	 * Calulates a weighted color average for use in LODs
	 * @param img input image
	 * @return calculated LOD color in sRGB
	 */
	public static int calcLOD(BufferedImage img) {
		int r=0;
		int g=0;
		int b=0;
		int w=0;
		if(img.isAlphaPremultiplied()) {
			//with alpha
			for(int i = 0; i < img.getWidth(); i++) {
				for(int j=0; j < img.getHeight(); j++) {
					int data = img.getRGB(i, j);
					int aa = (data & 0xff000000) >> 24;
					int rr = (data & 0x00ff0000) >> 16;
					int gg = (data & 0x0000ff00) >> 8;
					int bb = (data & 0x000000ff);
					r += aa*rr;
					g += aa*gg;
					b += aa*bb;
					w += aa;
				}
			}
		}else {
			//without alpha
			for(int i = 0; i < img.getWidth(); i++) {
				for(int j=0; j < img.getHeight(); j++) {
					int data = img.getRGB(i, j);
					r += (data & 0x00ff0000) >> 16;
					g += (data & 0x0000ff00) >> 8;
					b += (data & 0x000000ff);
				}
			}
			w=img.getWidth()*img.getHeight();
		}
		if(w == 0) return 0;
		int r1 = r/w;
		int g1 = g/w;
		int b1 = b/w;
		return r1*65536 + g1*256 + b1;
	}
	
	/**
	 * Decodes sRGB data
	 * @param rgb
	 * @param out
	 */
	public void decodeRGB(int rgb, Vector3i out) {
		out.x = (rgb & 0x00ff0000) >> 16;
		out.y = (rgb & 0x0000ff00) >> 8;
		out.z = (rgb & 0x000000ff);
	}
	
	/**
	 * @param rgba
	 * @param out vector to write to
	 */
	public void decodeRGBA(int rgba, Vector4i out) {
		out.x = (rgba & 0xff000000) >> 24;
		out.y = (rgba & 0x00ff0000) >> 16;
		out.z = (rgba & 0x0000ff00) >> 8;
		out.w = (rgba & 0x000000ff);
	}
}
