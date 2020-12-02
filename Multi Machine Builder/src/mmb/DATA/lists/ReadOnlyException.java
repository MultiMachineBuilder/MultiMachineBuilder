/**
 * 
 */
package mmb.DATA.lists;

/**
 * @author oskar
 *
 */
public class ReadOnlyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8249445804908380442L;

	/**
	 * 
	 */
	public ReadOnlyException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ReadOnlyException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ReadOnlyException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ReadOnlyException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ReadOnlyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
