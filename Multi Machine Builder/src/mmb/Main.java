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
		Loading.main(args);
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
}
