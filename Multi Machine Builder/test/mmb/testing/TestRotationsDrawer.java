/**
 * 
 */
package mmb.testing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author oskar
 *
 */
public class TestRotationsDrawer extends JPanel {
	public BufferedImage[] images;
	@Override
	public void paint(Graphics g) {
		int x = 0;
		for(BufferedImage image: images) {
			g.drawImage(image, x, 0, null);
			x += 32;
		}
	}

	/**
	 * Create the panel.
	 */
	public TestRotationsDrawer() {
		
	}

}
