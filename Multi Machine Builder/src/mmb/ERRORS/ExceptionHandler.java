/**
 * 
 */
package mmb.ERRORS;

import java.lang.Thread.UncaughtExceptionHandler;

import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class ExceptionHandler implements UncaughtExceptionHandler {
	public static final ExceptionHandler INSTANCE = new ExceptionHandler();
	private ExceptionHandler() {};
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Debugger debug = new Debugger(thread.getName());
		debug.pstm(ex, "A thread has thrown an exception");
	}

}
