/**
 * 
 */
package mmb.WORLD;

import javax.swing.JDialog;
import javax.swing.JTextArea;

/**
 * @author oskar
 *
 */
public class CopyWindow extends JDialog {
	public CopyWindow(String s) {
		add(new JTextArea(s));
	}
}
