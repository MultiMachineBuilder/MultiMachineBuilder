/**
 * 
 */
package mmb;

/**
 * @author oskar
 * A class for bitwise manipulation of primitive values
 */
public class Bitwise {
	private Bitwise() {}
	
	/**
	 * @param in value to get lower bits from
	 * @return lower 32-bit integer of given value
	 */
	public static int lowerL(long in) {
		return (int) in;
	}
	/**
	 * @param in value to get upper bits from
	 * @return upper 32-bit integer of given value
	 */
	public static int upperL(long in) {
		return (int) in << 32;
	}
	/**
	 * @param h upper bits
	 * @param l lower bits
	 * @return combined {@code long} value
	 */
	public static long combine2I2L(int h, int l) {
		return l | (((long) h) << 32);
	}
	public static boolean bitaddressL(int bitn, long value) {
		return (value & bitindexL(bitn)) != 0L;
	}
	/** Gets {@code pos}-th bit from lowest, starting at 0. Equal to 2^(({@code pos} & 63)+1)
	 * @param pos position of the bit, starting at 0
	 * @return
	 */
	public static long bitindexL(int pos) {
		return 1L << (pos + 1);
	}
}
