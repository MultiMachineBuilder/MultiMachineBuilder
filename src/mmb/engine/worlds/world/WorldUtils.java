/**
 * 
 */
package mmbeng.worlds.world;

import javax.annotation.Nullable;

import mmbeng.block.BlockEntity;
import mmbeng.block.BlockEntry;
import mmbeng.rotate.Side;

/**
 * @author oskar
 *
 */
public class WorldUtils {
	private WorldUtils() {}
	
	/**
	 * Return 4-bit 0000UDLR vector of incoming boolean signals
	 * @param x X coordinate of block
	 * @param y Y coordinate of block
	 * @param map block map, from which to get signals
	 * @return vectorized incoming signals
	 */
	public static byte incomingSignals(int x, int y, World map) {
		byte result = 0;
		if(provideSignal(map.get(x+1, y), Side.L)) result += 1;
		if(provideSignal(map.get(x-1, y), Side.R)) result += 2;
		if(provideSignal(map.get(x, y+1), Side.U)) result += 4;
		if(provideSignal(map.get(x, y-1), Side.D)) result += 8;
		return result;
	}
	/**
	 * Count incoming signals. Used in WireWorld
	 * @param x X coordinate of block
	 * @param y Y coordinate of block
	 * @param world block map, from which to get signals
	 * @return number of 'true' signals
	 */
	public static int allIncomingSignals(int x, int y, World world) {
		int result = 0;
		if(provideSignal(world.get(x+1, y), Side.L)) result++;
		if(provideSignal(world.get(x+1, y+1), Side.UL)) result++;
		if(provideSignal(world.get(x, y+1), Side.U)) result++;
		if(provideSignal(world.get(x-1, y+1), Side.UR)) result++;
		if(provideSignal(world.get(x-1, y), Side.R)) result++;
		if(provideSignal(world.get(x-1, y-1), Side.DR)) result++;
		if(provideSignal(world.get(x, y-1), Side.D)) result++;
		if(provideSignal(world.get(x+1, y-1), Side.DL)) result++;
		return result;
	}
	/**
	 * @param x X coordiante
	 * @param y Y coordinate
	 * @param world the block map
	 * @return
	 */
	public static boolean hasIncomingSignal(int x, int y, World world) {
		if(provideSignal(world.get(x+1, y), Side.L)) return true;
		if(provideSignal(world.get(x-1, y), Side.R)) return true;
		if(provideSignal(world.get(x, y+1), Side.U)) return true;
		if(provideSignal(world.get(x, y-1), Side.D)) return true;
		return false;
	}
	public static boolean hasIncomingSignal(BlockEntity ent) {
		return hasIncomingSignal(ent.posX(), ent.posY(), ent.owner());
	}
	/**
	 * Check if given block emits signal at given side
	 * @param ent block to check
	 * @param s side
	 * @return does block provide signal?
	 */
	public static boolean provideSignal(@Nullable BlockEntry ent, Side s) {
		if(ent == null) return false;
		return ent.provideSignal(s);
	}
}
