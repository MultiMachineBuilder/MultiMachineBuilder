/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * @author oskar
 * The block rotation is expressed with 3-bit integer.
 * 
 * Step 1: apply transposition
 * Step 2: apply mirrors
 * 
 * Bitmask: XYT
 */
public class Rotations {

	public static final int NORMAL = 0;
	public static final int VERTICAL_MIRROR = 1;
	public static final int HORIZONTAL_MIRROR = 2;
	public static final int FULL_ROTATION = 3;
	public static final int BACKSLASH_FLIP = 4;
	public static final int COUNTER_CLOCKWISE = 5;
	public static final int CLOCKWISE = 6;
	public static final int SLASH_FLIP = 7;
	
	/**
	 * @return the flipv
	 */
	public static int[] getFlipv() {
		return flipV;
	}
	/**
	 * @return the fliph
	 */
	public static int[] getFliph() {
		return flipH;
	}
	/**
	 * @return the flipbs
	 */
	public static int[] getFlipbs() {
		return flipBS;
	}
	/**
	 * @return the flips
	 */
	public static int[] getFlips() {
		return flipS;
	}
	/**
	 * @return the cw
	 */
	public static int[] getCw() {
		return cw;
	}
	/**
	 * @return the ccw
	 */
	public static int[] getCcw() {
		return ccw;
	}

	//Array transforms
	protected static final int[] flipV = new int[] {1, 0, 3, 2, 5, 4, 7, 6};
	protected static final int[] flipH = new int[] {2, 3, 0, 1, 6, 7, 4, 5};
	protected static final int[] flipBS = new int[] {4, 6, 5, 7, 0, 2, 1, 3};
	protected static final int[] flipS = new int[] {7, 6, 5, 4, 3, 2, 1, 0};
	protected static final int[] cw = new int[] {6, 4, 7, 5, 2, 0, 3, 1};
	protected static final int[] ccw = new int[] {5, 7, 4, 6, 2, 3, 0, 1};
	
	/**
	 * Transform a point using given transform
	 * @param rotation
	 * @param src source coordinates
	 * @param range coordinate range
	 * @return
	 */
	public static Point apply(int rotation, Point src, int range) {
		return apply(rotation, src.x, src.y, range);
	}
	/**
	 * Transform a point using given transform
	 * @param rotation
	 * @param srcX source X coordinate
	 * @param srcY source Y coordinate
	 * @param range coordinate range
	 * @return transformed point
	 */
	public static Point apply(int rotation, int srcX, int srcY, int range) {
		if(rotation > 3) { //4, 5, 6, 7 => 0, 1, 2, 3
			int tmp = srcX;
			srcX = srcY;
			srcY = tmp;
			rotation -= 4;
		}
		if(rotation > 1) { //2, 3 => 0, 1
			srcY = -srcY;
			rotation -=2;
		}
		if(rotation == 1) srcX = -srcX;
		srcX += range;
		srcY += range;
		return new Point(srcX,srcY);
	}
	
	/**
	 * Return all 8 transformed versions of given image.
	 * @param src
	 * @return
	 */
	public static BufferedImage[] mirroredVersions(BufferedImage src) {
		BufferedImage[] firstRound = new BufferedImage[2];
		firstRound[0] = src;
		//Transform: transpose
		BufferedImage transform = new BufferedImage(src.getHeight(), src.getWidth(), src.getType());
		firstRound[1] = transform;
		for(int i = 0; i < src.getWidth(); i++) {
			for(int j = 0; j < src.getHeight(); j++) {
				transform.setRGB(j, i, src.getRGB(i, j));
			}
		}
		BufferedImage[] result = new BufferedImage[8];
		for(int i = 0; i < 2; i++) {
			int w = firstRound[i].getWidth();
			int h = firstRound[i].getHeight();
			int j = i*4;
			Graphics g;
			BufferedImage hFlip = new BufferedImage(w, h, src.getType());
			g = hFlip.getGraphics();
			g.drawImage(firstRound[i], 0, h, w, -h, null);
			g.dispose();
			BufferedImage vFlip = new BufferedImage(w, h, src.getType());
			g = vFlip.getGraphics();
			g.drawImage(firstRound[i], w, 0, -w, h, null);
			g.dispose();
			BufferedImage dFlip = new BufferedImage(w, h, src.getType());
			g = dFlip.getGraphics();
			g.drawImage(firstRound[i], h, h, -w, -h, null);
			g.dispose();
			result[j] = firstRound[i];
			result[j+1] = vFlip;
			result[j+2] = hFlip;
			result[j+3] = dFlip;
		}
		return result;
	}
}
