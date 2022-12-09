/**
 * 
 */
package mmbeng.mods;

/**
 * @author oskar
 *
 */
public class PrematureHaltException extends RuntimeException {
	public PrematureHaltException() {
		super();
	}

	public  PrematureHaltException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PrematureHaltException(String arg0) {
		super(arg0);
	}

	public PrematureHaltException(Throwable arg0) {
		super(arg0);
	}

	private static final long serialVersionUID = -6911205129366213081L;

}
