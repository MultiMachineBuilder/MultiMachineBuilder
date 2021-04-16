/**
 * 
 */
package mmb.WORLD.tool;

import java.awt.event.MouseListener;

import javax.swing.Icon;

/**
 * @author oskar
 *
 */
public interface Tool extends MouseListener{
	public Icon getIcon();
	public void openGUI();
	public void closeGUI();
}
