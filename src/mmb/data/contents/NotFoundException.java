/**
 * 
 */
package mmb.data.contents;

/**
 * @author oskar
 *
 */
public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 7047254397578982800L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NotFoundException(String arg0) {
		super(arg0);
	}

	public NotFoundException(Throwable arg0) {
		super(arg0);
	}

}
