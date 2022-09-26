/**
 * 
 */
package mmb.GRAPHICS;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

/**
 * @author oskar
 *
 */
public class BufferedImages {
	public static DataBufferByte getDataBuffer(BufferedImage img) {
		return (DataBufferByte)img.getRaster().getDataBuffer();
	}
	public static byte[] getDataArray(BufferedImage img) {
		return getDataBuffer(img).getData();
	}
	public static ByteBuffer getIntBuffer(BufferedImage img) {
		return ByteBuffer.wrap(getDataArray(img));
	}
}
