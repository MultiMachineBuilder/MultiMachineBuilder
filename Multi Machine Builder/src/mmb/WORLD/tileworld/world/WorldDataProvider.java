/**
 * 
 */
package mmb.WORLD.tileworld.world;

import java.awt.Point;

import mmb.WORLD.tileworld.block.MapEntry;
import mmb.WORLD.tileworld.world.BlockProxy.BlockPos;

/**
 * @author oskar
 *
 */
public interface WorldDataProvider{
	MapEntry get(int x, int y);
	default MapEntry get(Point p) {return get(p.x, p.y);}
	void set(int x, int y, MapEntry me);
	default void set(Point p, MapEntry me) {set(p.x, p.y, me);}
	/**
	 * @param change
	 */
	default void set(BlockPos change) {set(change.p, change.b);}
}
