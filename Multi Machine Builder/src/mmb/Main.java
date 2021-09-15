/**
 * 
 */
package mmb;

import mmb.ERRORS.ExceptionHandler;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Main {
	public static void main(String[] args) {
		//count RAM
		debug.printl("RAM avaliable: "+Runtime.getRuntime().maxMemory());
		Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler.INSTANCE);
		try {
			String jversion = System.getProperty("java.version");
			boolean j8ol = jversion.charAt(0) == '1';
			debug.printl("Java version is: "+jversion.toString());
			if(!j8ol) {
				debug.printl("Changing scale because Java is >= 9");
				System.setProperty("sun.java2d.uiScale", "1.0");
				System.setProperty("prism.allowhidpi", "false");
				System.setProperty("sun.java2d.uiScale.enabled", "false");
				System.setProperty("sun.java2d.win.uiScaleX", "1.0");
				System.setProperty("sun.java2d.win.uiScaleY", "1.0");
			}
			
			//UI initialized here
			loader = new Loading();
			loader.setVisible(true);
			loader.continueLoading();
		// deepcode ignore DontCatch: log the game crash
		} catch (Throwable e) {
			Main.crash(e);
		}
	}
	private static final Debugger debug= new Debugger("MAIN");
	/**
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
	}
	public static void state2(String str) {
		loader.st2.setText(str);
	}
}
