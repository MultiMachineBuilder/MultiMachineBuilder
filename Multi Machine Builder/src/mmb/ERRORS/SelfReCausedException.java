/**
 * 
 */
package mmb.ERRORS;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import mmb.debug.Debugger;

/**
 * @author oskar
 * An exception which has a cause loop. Printing this exception will cause {@link StackOverflowError}
 */
public class SelfReCausedException extends RuntimeException {

	private static final long serialVersionUID = -8966574404236639957L;

	/**
	 * 
	 */
	public SelfReCausedException() {
		super();
	}

	/**
	 * @param arg0
	 */
	public SelfReCausedException(String arg0) {
		super(arg0);
	}
	
	@Override
	public synchronized Throwable getCause() {
		return this;
	}
	
	private static Debugger debug = new Debugger("SELF RECAUSED EXCEPTION");
	public static void main(String[] args) {
		test();
	}
	@Test static void test() {
		debug.printl("Testing");
		Assertions.assertThrows(StackOverflowError.class, () -> tryPrintStackTrace());
	}
	
	static private void tryPrintStackTrace() {
		Exception ex = new SelfReCausedException("This is a test");
		try {
			debug.pstm(ex, "Testing SelfReCausedException");
		}catch(StackOverflowError e) {
			debug.pstm(e, "SOE thrown");
			throw e;
		}
	}
}
