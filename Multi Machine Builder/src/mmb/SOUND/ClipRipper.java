/**
 * 
 */
package mmb.SOUND;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

/**
 * @author oskar
 *
 */
public class ClipRipper {
	public void clip2sound(Clip c) throws LineUnavailableException {
		if(!c.isOpen()) c.open();
		//int
		float[][] result = new float[0][0];
		
		//c.getFormat().getEncoding().
	}
}
