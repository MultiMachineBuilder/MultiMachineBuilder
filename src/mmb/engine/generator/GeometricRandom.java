/**
 * 
 */
package mmb.engine.generator;

/**
 * A simple, deterministic hash-based random number generator
 * @author oskar
 */
public class GeometricRandom {
	// Nothing-up-my-sleeve numbers
	private static final int a = 0x54138957, b = 0xa35d6768, c = 0x65476846, d = 0x54546466;
	// Seeded numbers
	private final int t, u, v;

	/**
	 * Generates a geometric random generator
	 * @param seed
	 */
	public GeometricRandom(long seed) {
		int aa = (int) ( seed & 0x0000_0000_ffff_ffffL);
		int bb = (int) ((seed & 0xffff_ffff_0000_0000L)/ 0x1_0000_0000L);
		t = (a & aa) | (a & bb);
		u = (a | aa) & (b | bb);
		v = (t & c) ^ (u | d);
	}
	
	/**
	 * Generates a 64-bit random number
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 * @return a random 64-bit number
	 */
	public long getLong(int x, int y, int z) {
		int i1 = x ^ (y & z) + a;
		int i2 = y ^ (x & z) + b;
		int i3 = z ^ (x & y) + c;
		int i4 = x ^ (y | z) + d;
		int i5 = y ^ (x | z) + a;
		int i6 = z ^ (x | y) + b;
		i1 = i1 ^ (t + x) + c;
		i2 = i2 ^ (u * y) + d;
		i3 = i3 ^ (v - z) + a;
		i4 = i4 ^ (t << 8) + b;
		i5 = i5 ^ (u << 16) + c;
		i6 = i6 ^ (v << 24) + d;
		int lower = i1 ^ i3 ^ i5;
		int higher = i2 ^ i4 ^ i6;
		long result = 0x100000000L * higher;
		result += lower;
		return result*3L;
	}

}
