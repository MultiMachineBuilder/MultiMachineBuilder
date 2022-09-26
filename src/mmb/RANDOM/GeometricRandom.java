/**
 * 
 */
package mmb.RANDOM;

/**
 * @author oskar
 *
 */
public class GeometricRandom {
	private static final int a = 0x54138957, b = 0xa35d6768, c = 0x65476846, d = 0x54546466;
	private final int t, u, v;

	/**
	 * @param seed
	 */
	public GeometricRandom(long seed) {
		int aa = (int) ( seed & 0x0000_0000_ffff_ffffL);
		int bb = (int) ((seed & 0xffff_ffff_0000_0000L)/ 0x1_0000_0000L);
		t = (a & aa) | (a & bb);
		u = (a | aa) & (b | bb);
		v = (t & c) ^ (u | d);
	}
	
	public long getLong(int x, int y, int z) {
		int i1 = x ^ (y & z);
		int i2 = y ^ (x & z);
		int i3 = z ^ (x & y);
		int i4 = x ^ (y | z);
		int i5 = y ^ (x | z);
		int i6 = z ^ (x | y);
		i1 = i1 ^ (t + x);
		i2 = i2 ^ (u * y);
		i3 = i3 ^ (v - z);
		i4 = i4 ^ (t << 8);
		i5 = i5 ^ (u << 16);
		i6 = i6 ^ (v << 24);
		int lower = i1 ^ i3 ^ i5;
		int higher = i1 ^ i4 ^ i6;
		long result = 0x100000000L * higher;
		result += lower;
		return result*3L;
	}

}
