/**
 * 
 */
package mmb.WORLD.generator;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import mmb.BEANS.Titled;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.worlds.world.BlockMap;
import mmb.debug.Debugger;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public interface Generator extends Titled{
	public Grid<BlockEntry> genChunk(BlockMap map, int minX, int minY, int w, int h);
	public void setSeed(long seed);
	default void generate(BlockMap map, int chunkSize) {
		Debugger debug = new Debugger("WORLD GEN");
		int chunkSize0 = chunkSize;
		if(chunkSize0 > map.sizeX) chunkSize0 = map.sizeX;
		if(chunkSize0 > map.sizeY) chunkSize0 = map.sizeY;
		IntList chnkXs = new IntArrayList();
		int x = map.startX;
		chnkXs.add(x);
		while(x < map.endX) {
			x += chunkSize0;
			if(x < map.endX) chnkXs.add(x);
		}
		debug.printl(chnkXs.toString());
		
		IntList chnkYs = new IntArrayList();
		int y = map.startY;
		chnkYs.add(y);
		while(y < map.endY) {
			y += chunkSize0;
			if(y < map.endY) chnkYs.add(y);
		}
		debug.printl(chnkYs.toString());
		
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
				Grid<BlockEntry> chunk = genChunk(map, left, up, w, h);
				Grid.copy(0, 0, chunk, left, up, map, w, h);
				up = down;
			}
			left = right;
		}
	}

}
