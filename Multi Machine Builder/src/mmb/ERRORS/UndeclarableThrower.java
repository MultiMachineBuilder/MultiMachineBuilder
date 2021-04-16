/**
 * 
 */
package mmb.ERRORS;

/**
 * @author oskar
 *
 */
public class UndeclarableThrower {
	/**
	 * Throws given exception without a declaration
	 * @param t the exeption to throw
	 */
	public static void shoot(Throwable t) {
		UndeclarableThrower.<RuntimeException>shoot0(t);
	}
	@SuppressWarnings("unchecked")
	private static <T extends Throwable> void shoot0(Throwable e) throws T {
		throw (T) e;
	}
}