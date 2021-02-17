/**
 * 
 */
package mmb.WORLD.worlds;

import mmb.WORLD.Side;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public class SignalUtils {
	/**
	 * Return 4-bit 0000UDLR vector of incoming boolean signals
	 * @return vectorized incoming signals
	 */
	public static byte incomingSignals(int x, int y, BlockMap map) {
		byte result = 0;
		if(map.get(x+1, y).provideSignal(Side.L)) result += 1;
		if(map.get(x-1, y).provideSignal(Side.R)) result += 2;
		if(map.get(x, y+1).provideSignal(Side.U)) result += 4;
		if(map.get(x, y-1).provideSignal(Side.D)) result += 8;
		return result;
	}
	/**
	 * Count incoming signals. Used in WireWorld
	 * @param x X coordinate of block
	 * @param y Y coordinate of block
	 * @param map block map, from which to get signals
	 * @return number of 'true' signals
	 */
	public static int allIncomingSignals(int x, int y, BlockMap map) {
		int result = 0;
		if(map.get(x+1, y).provideSignal(Side.L)) result++;
		if(map.get(x+1, y+1).provideSignal(Side.UL)) result++;
		if(map.get(x, y+1).provideSignal(Side.U)) result++;
		if(map.get(x-1, y+1).provideSignal(Side.UR)) result++;
		if(map.get(x-1, y).provideSignal(Side.R)) result++;
		if(map.get(x-1, y-1).provideSignal(Side.DR)) result++;
		if(map.get(x, y-1).provideSignal(Side.D)) result++;
		if(map.get(x+1, y-1).provideSignal(Side.DL)) result++;
		return result;
	}
	/**
	 * @param x X coordiante
	 * @param y Y coordinate
	 * @param map the block map
	 * @return
	 */
	public static boolean hasIncomingSignal(int x, int y, BlockMap map) {
		if(map.get(x+1, y).provideSignal(Side.L)) return true;
		if(map.get(x-1, y).provideSignal(Side.R)) return true;
		if(map.get(x, y+1).provideSignal(Side.U)) return true;
		if(map.get(x, y-1).provideSignal(Side.D)) return true;
		return false;
	}
}
