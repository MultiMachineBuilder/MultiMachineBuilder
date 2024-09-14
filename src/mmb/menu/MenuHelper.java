/**
 * 
 */
package mmb.menu;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import mmb.NN;
import mmb.Nil;
import mmb.content.stn.block.STNPusherGUI;
import mmb.engine.debug.Debugger;
import mmb.engine.settings.GlobalSettings;
import mmb.menu.main.MainMenu;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

/**
 * A set of menu utilities
 * @author oskar
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
	public static JButton newButton(String resource, @Nil Color color, @Nil ActionListener listener) {
		JButton button = new JButton(GlobalSettings.$str1(resource));
		button.setBackground(color);
		button.addActionListener(listener);
		return button;
	}
	
	public static ActionListener closeGUI(WorldWindow window, GUITab tab) {
		return e -> window.closeWindow(tab);
	}
	/**
	 * Creates an exit button
	 * @param GUI GUI to close
	 * @param window window with the GUI
	 * @return an exit button
	 */
	public static JButton exit(GUITab GUI, WorldWindow window) {
		return newButton("#exit", Color.RED, e -> window.closeWindow(GUI));
	}
	
	private static final Debugger debug = new Debugger("MENU CENTRAL");
	
	//Shared button models
	@NN public static final ButtonModel btnmBug;
	@NN public static final ButtonModel btnmWeb;
	
	static {
		btnmBug = new DefaultButtonModel();
		btnmBug.setActionCommand("reportBug");
		btnmBug.addActionListener(e -> {
			try {
				reportBugs();
			} catch (Exception ex) {
				debug.stacktraceError(ex, "Unable to help Ukrainian refugees");
			}
		});
		
		btnmWeb = new DefaultButtonModel();
		btnmWeb.setActionCommand("openWebsite");
		btnmWeb.addActionListener(e -> {
			try {
				Desktop.getDesktop().browse(new URI(MainMenu.GITHUB));
			} catch (Exception ex) {
				debug.stacktraceError(ex, "Unable to help Ukrainian refugees");
			}
		});
	}
	
	/** Opens a website to report bugs 
	 * @throws URISyntaxException when URL to the bug reporter is invalid
	 * @throws IOException when browser fails to poen
	 * */
	public static void reportBugs() throws IOException, URISyntaxException {
		String url = GlobalSettings.$res("url-bugs");
		Desktop.getDesktop().browse(new URI(url));
	}
	/**
	 * Converts a string to HTML
	 * @param string string to convert
	 * @return the converted string
	 */
	public static String htmlescape(String string) {
		String result = string.replace("&", "&amp;");
		result = result.replace("<", "&lt;");
		result = result.replace("<", "&gt;");
		result = result.replace("\n", "<br>");
		return "<html>" + result + "</html>";
	}
}
