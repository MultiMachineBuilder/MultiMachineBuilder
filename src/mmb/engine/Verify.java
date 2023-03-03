/**
 * 
 */
package mmb.engine;

/**
 * Verifies correctness of values
 * @author oskar
 */
public class Verify {
	private Verify() {}
	
	//Double tests - descending they become less stringent
	/**
	 * Requires that provided argument be a positive real number
	 * @param dbl value to test
	 * @throws IllegalArgumentException if value is <=0, infinite or NaN
	 */
	public static void requirePositive(double dbl) {
		if(Double.isNaN(dbl)) throw new IllegalArgumentException("NaN");
		if(!Double.isFinite(dbl)) throw new IllegalArgumentException("Infinity");
		if(dbl < 0) throw new IllegalArgumentException("<0");
		if(dbl == 0) throw new IllegalArgumentException("0");
	}
	/**
	 * Requires that provided argument be a nonnegative real number
	 * @param dbl value to test
	 * @throws IllegalArgumentException if value is <0, infinite or NaN
	 */
	public static void requireNonNegative(double dbl) {
		if(Double.isNaN(dbl)) throw new IllegalArgumentException("NaN");
		if(!Double.isFinite(dbl)) throw new IllegalArgumentException("Infinity");
		if(dbl < 0) throw new IllegalArgumentException("<0");
	}
	/**
	 * Requires that provided argument be a finite real number
	 * @param dbl value to test
	 * @throws IllegalArgumentException if value is infinite or NaN
	 */
	public static void requireFinite(double dbl) {
		if(Double.isNaN(dbl)) throw new IllegalArgumentException("NaN");
		if(!Double.isFinite(dbl)) throw new IllegalArgumentException("Infinity");
	}
	/**
	 * Requires that provided argument be real number
	 * @param dbl value to test
	 * @throws IllegalArgumentException if value is NaN
	 */
	public static void requireValid(double dbl) {
		if(Double.isNaN(dbl)) throw new IllegalArgumentException("NaN");
	}
}
