/**
 * 
 */
package mmb;

import java.awt.EventQueue;

import mmb.ERRORS.ExceptionHandler;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Main {
	public static void main(String[] args) {
		running = true;
		debug = new Debugger("MAIN");
		//count RAM
		debug.printl("RAM avaliable: "+Runtime.getRuntime().maxMemory());
		Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler.INSTANCE);
		try {
			String jversion = System.getProperty("java.version");
			debug.printl("Java version is: "+jversion);
			debug.printl("Changing scale because Java is >= 9");
			System.setProperty("sun.java2d.uiScale", "1.0");
			System.setProperty("prism.allowhidpi", "false");
			System.setProperty("sun.java2d.uiScale.enabled", "false");
			System.setProperty("sun.java2d.win.uiScaleX", "1.0");
			System.setProperty("sun.java2d.win.uiScaleY", "1.0");
			//UI initialized here
			loader = new Loading();
			loader.setVisible(true);
			if(loader.shouldWork) EventQueue.invokeLater(loader::continueLoading);
		// deepcode ignore DontCatch: log the game crash
		} catch (Throwable e) {
			Main.crash(e);
		}
	}
	private static Debugger debug;
	/**
	 * Crashes the game to the desktop
	 * @param e throwable, which caused the crash
	 * @throws SecurityException if run by the mod
	 */
	public static void crash(Throwable e) {
		debug.pstm(e, "GAME HAS CRASHED");
		System.exit(1);
	}
	static Loading loader;
	public static void state1(String str) {
		loader.st1.setText(str);
		debug.printl("State: "+str);
	}
	public static void state2(String str) {
		loader.st2.setText(str);
	}
	
	private static boolean running;
	/**
	 * Checks if the game is run as executable. Used to suppress debugger initialization when designing GUIs
	 * @return is the game executed?
	 */
	public static boolean isRunning() {
		return running;
	}
}
