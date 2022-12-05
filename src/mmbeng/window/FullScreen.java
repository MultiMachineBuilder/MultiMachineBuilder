/**
 * 
 */
package mmbeng.window;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.annotation.Nonnull;

import mmb.data.variables.ListenerBooleanVariable;
import mmbeng.debug.Debugger;

/**
 * @author oskar
 *
 */
public class FullScreen {
	@Nonnull public static final ListenerBooleanVariable isFullScreen = new ListenerBooleanVariable();
	private static MMBFrame fullScreenFrame;
	
	private static Debugger debug = new Debugger("FullScreen");
	
	static GraphicsDevice device = GraphicsEnvironment
	        .getLocalGraphicsEnvironment().getScreenDevices()[0];
	
	private static boolean isInitialized = false;
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
	public static void setWindow(MMBFrame win) { //Stuck
		if(fullScreenFrame != null) {
			fullScreenFrame.undergoingScreenTransform = true;
			fullScreenFrame.dispose(); //Dispose of the old frame GETS STUCK
			fullScreenFrame.undergoingScreenTransform = false;
		}
		fullScreenFrame = win;
		setFullScreen(isFullScreen.getValue()); //Create the new frame STUCK
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
			fullScreenFrame.dispose(); //STUCK
			fullScreenFrame.setUndecorated(false);
			fullScreenFrame.setVisible(true);
		}
		fullScreenFrame.undergoingScreenTransform = false;
	}
}
