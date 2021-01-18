/**
 * 
 */
package mmb.GRAPHICS;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;

/**
 * @author oskar
 *
 */
public class BufferedImages {
	public static DataBufferInt getDataBuffer(BufferedImage img) {
		return (DataBufferInt)img.getRaster().getDataBuffer();
	}
	public static int[] getDataArray(BufferedImage img) {
		return ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
	}
	public static IntBuffer getIntBuffer(BufferedImage img) {
		return IntBuffer.wrap(((DataBufferInt)img.getRaster().getDataBuffer()).getData());
	}
}
