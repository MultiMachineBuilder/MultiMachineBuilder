/**
 * 
 */
package mmb.MENU;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.annotation.Nonnull;

import mmb.DATA.Settings;
import mmb.DATA.variables.ListenerBooleanVariable;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class FullScreen {
	@Nonnull public static final ListenerBooleanVariable isFullScreen = new ListenerBooleanVariable();
	private static MMBFrame fullScreenFrame;
	
	private static boolean isInitialized = false;
	private static Debugger debug = new Debugger("FullScreen");
	
	private static boolean setFullScreenWindowMethodRunning = false;
	
	static GraphicsDevice device = GraphicsEnvironment
	        .getLocalGraphicsEnvironment().getScreenDevices()[0];
	
	public static void initialize() {
		if(isInitialized) return;
		debug.printl("Setting up FullScreen");
		isFullScreen.add(b -> {
			debug.printl("Setting fullscreen mode to: "+b);
			Settings.set("fullscreen", Boolean.toString(b));
			if(!setFullScreenWindowMethodRunning) setFullScreen(b);
		});
		isInitialized = true;
	}
	
	private FullScreen() {}
	public static void setWindow(MMBFrame win) {
		if(fullScreenFrame != null) {
			fullScreenFrame.undergoingScreenTransform = true;
			fullScreenFrame.dispose(); //Dispose of the old frame
			fullScreenFrame.undergoingScreenTransform = false;
		}
		fullScreenFrame = win;
		setFullScreen(isFullScreen.getValue()); //Create the new frame
	}
	private static void setFullScreen(boolean fullScreen) { //internal update method
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
