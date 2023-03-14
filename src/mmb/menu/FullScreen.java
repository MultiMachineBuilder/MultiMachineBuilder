/**
 * 
 */
package mmb.menu;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import mmb.NN;
import mmb.data.variables.ListenableBoolean;
import mmb.engine.debug.Debugger;

/**
 * A set of full screen utilities
 * @author oskar
 */
public class FullScreen {
	private static Debugger debug = new Debugger("FullScreen");
	
	/** Is the game full screen? */
	@NN public static final ListenableBoolean isFullScreen = new ListenableBoolean();
	private static MMBFrame fullScreenFrame;
	static GraphicsDevice device = GraphicsEnvironment
	        .getLocalGraphicsEnvironment().getScreenDevices()[0];
	
	private static boolean isInitialized = false;
	/** Initialized full screen capabilities */
	public static void initialize() {
		if(isInitialized) return;
		debug.printl("Setting up FullScreen");
		isFullScreen.add(b -> {
			debug.printl("Setting fullscreen mode to: "+b);
			setFullScreen(b);
		});
		isInitialized = true;
	}
	
	private FullScreen() {}
	/**
	 * Sets the current full-screen window
	 * @param win window to set
	 */
	public static void setWindow(MMBFrame win) {
		if(fullScreenFrame != null) {
			fullScreenFrame.undergoingScreenTransform = true;
			fullScreenFrame.dispose();
			fullScreenFrame.undergoingScreenTransform = false;
		}
		fullScreenFrame = win;
		setFullScreen(isFullScreen.getValue());
	}
	private static void setFullScreen(boolean fullScreen) {
		if(fullScreenFrame == null) return;
		fullScreenFrame.undergoingScreenTransform = true;
		//windowed
		if(fullScreen) {
			fullScreenFrame.dispose();
			fullScreenFrame.setUndecorated(true);
			device.setFullScreenWindow(fullScreenFrame);
			fullScreenFrame.setVisible(true);
		}else{
			device.setFullScreenWindow(null);
			fullScreenFrame.dispose();
			fullScreenFrame.setUndecorated(false);
			fullScreenFrame.setVisible(true);
		}
		fullScreenFrame.undergoingScreenTransform = false;
	}
}
