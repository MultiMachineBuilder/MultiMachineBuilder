/**
 * 
 */
package mmb.engine.generator;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import mmb.engine.block.BlockEntry;
import mmb.engine.debug.Debugger;
import mmb.engine.worlds.world.World;
import mmbbase.beans.Titled;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public interface Generator extends Titled{
	@Nonnull public Grid<BlockEntry> genChunk(World map, int minX, int minY, int w, int h);
	public void setSeed(long seed);
	default void generate(World map, int chunkSize) {
		Debugger debug = new Debugger("WORLD GEN");
		int chunkSize0 = chunkSize;
		if(chunkSize0 > map.sizeX) chunkSize0 = map.sizeX;
		if(chunkSize0 > map.sizeY) chunkSize0 = map.sizeY;
		
		IntList chnkXs = new IntArrayList();
		int x = map.startX;
		while(x < map.endX) {
			chnkXs.add(x);
			x += chunkSize0;
		}
		debug.printl(chnkXs.toString());
		
		IntList chnkYs = new IntArrayList();
		int y = map.startY;
		while(y < map.endY) {
			chnkYs.add(y);
			y += chunkSize0;
		}
		debug.printl(chnkYs.toString());
		
		Grid<@Nonnull BlockEntry> grid = map.toGrid();
		int maxx = chnkXs.size()-1;
		int maxy = chnkYs.size()-1;
		int left = chnkXs.getInt(0);
		for(int i = 0; i < maxx; i++) {
			int right = chnkXs.getInt(i+1);
			int up = chnkYs.getInt(0);
			for(int j = 0; j < maxy; j++) {
				int down = chnkYs.getInt(j+1);
				int w = right-left;
				int h = down-up;
				debug.printl("["+left+","+up+"] ... ["+right+","+down+"]");
				Grid<BlockEntry> chunk = genChunk(map, left, up, w, h);
				Grid.copy(0, 0, chunk, left-map.startX, up-map.startY, grid, w, h);
				up = down;
			}
			left = right;
		}
	}
	
}
