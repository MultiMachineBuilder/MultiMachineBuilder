/**
 * 
 */
package mmb.MENU;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author oskar
 *
 */
public class ShowTextureAtlas extends JFrame {
	public ShowTextureAtlas(Image img) {
		add(new JLabel(new ImageIcon(img)));
	}
}
