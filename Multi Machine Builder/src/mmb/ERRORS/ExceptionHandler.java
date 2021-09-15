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
	/** The singleton exception handler */
	public static final ExceptionHandler INSTANCE = new ExceptionHandler();
	private ExceptionHandler() {}
	@Override
	public void uncaughtException(@SuppressWarnings("null") Thread thread, @SuppressWarnings("null") Throwable ex) {
		@SuppressWarnings("null")
		Debugger debug = new Debugger(thread.getName());
		debug.pstm(ex, "A thread has thrown an exception");
	}

}
