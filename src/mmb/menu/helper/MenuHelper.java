/**
 * 
 */
package mmb.menu.helper;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.annotation.Nullable;
import javax.swing.JButton;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import mmb.GlobalSettings;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

/**
 * @author oskar
 *
 */
public class MenuHelper {
	private MenuHelper() {}
	/**
	 * @param strTrue string for true button
	 * @param strFalse string for false button
	 * @param tooltip
	 * @param message
	 * @param action listener which is run after the window is answered
	 * @return a dialog
	 */
	public static BooleanDialog askBoolean(String strTrue, String strFalse, String tooltip, String message, BooleanConsumer action) {
		BooleanDialog result = new BooleanDialog(strTrue, strFalse, tooltip, message, action);
		result.setVisible(true);
		return result;
	}
	
	/**
	 * Creates a button
	 * @param resource string resource
	 * @param color background color
	 * @param listener action listener
	 * @return a button
	 */
	public static JButton newButton(String resource, @Nullable Color color, @Nullable ActionListener listener) {
		JButton button = new JButton(GlobalSettings.$str1(resource));
		button.setBackground(color);
		button.addActionListener(listener);
		return button;
	}
	
	public static ActionListener closeGUI(WorldWindow window, GUITab tab) {
		return e -> window.closeWindow(tab);
	}
}
