/**
 * 
 */
package mmb.ERRORS;

/**
 * @author oskar
 * Thrown when GameContainer is given an unsupported GameObject.
 * Read more at all add() methods of given GameObject
 */
public class UnsupportedObjectException extends RuntimeException {
	public UnsupportedObjectException() {
		super();
	}

	public UnsupportedObjectException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UnsupportedObjectException(String arg0) {
		super(arg0);
	}

	public UnsupportedObjectException(Throwable arg0) {
		super(arg0);
	}

	private static final long serialVersionUID = 1543184504019405822L;
}
