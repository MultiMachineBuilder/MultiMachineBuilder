/**
 * 
 */
package mmb.menu;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import mmb.NN;
import mmb.engine.debug.Debugger;
import mmb.engine.settings.GlobalSettings;

/**
 *
 * @author oskar
 *
 */
public class MenuUtils {
	private MenuUtils() {}
	private static final Debugger debug = new Debugger("MENU CENTRAL");
	
	@NN public static final ButtonModel btnmBug;
	
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
	}
	
	/** Opens a website to report bugs 
	 * @throws URISyntaxException when URL to the bug reporter is invalid
	 * @throws IOException when browser fails to poen
	 * */
	public static void reportBugs() throws IOException, URISyntaxException {
		String url = GlobalSettings.$res("url-bugs");
		Desktop.getDesktop().browse(new URI(url));
	}
}
