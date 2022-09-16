/**
 * 
 */
package mmb;

import javax.swing.JDialog;
import javax.swing.JTextArea;

/**
 * @author oskar
 *
 */
public class CopyWindow extends JDialog {
	private static final long serialVersionUID = 1265791357617321139L;

	public CopyWindow(String s) {
		add(new JTextArea(s));
	}
}
