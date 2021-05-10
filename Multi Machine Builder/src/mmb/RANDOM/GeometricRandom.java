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
		int aa = (int) (seed & 0x00000000ffffffff);
		int bb = (int) ((seed & 0xffffffff00000000L)/ 0x100000000L);
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
		i1 = i1 ^ t;
		i2 = i2 ^ u;
		i3 = i3 ^ v;
		i4 = i4 ^ t;
		i5 = i5 ^ u;
		i6 = i6 ^ v;
		int lower = i1 ^ i3 ^ i5;
		int higher = i1 ^ i4 ^ i6;
		long result = 0x100000000L * higher;
		result += lower;
		return result;
	}

}
