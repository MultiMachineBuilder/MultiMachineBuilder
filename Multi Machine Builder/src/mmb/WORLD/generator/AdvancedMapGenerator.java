/**
 * 
 */
package mmb.WORLD.generator;

import mmb.RANDOM.GeometricRandom;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.worlds.world.World;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public class AdvancedMapGenerator implements Generator{

	@Override
	public String title() {
		return "Advanced Multi-Biome";
	}

	@Override
	public Grid<BlockEntry> genChunk(World map, int minX, int minY, int w, int h) {
		int maxX = minX+w;
		int maxY = minY+h;
		long ul = random.getLong(minX, minY, 0);
		long ur = random.getLong(maxX, minY, 0);
		long dl = random.getLong(minX, maxY, 0);
		long dr = random.getLong(maxX, maxY, 0);
		
		return null;
	}

	@Override
	public void setSeed(long seed) {
		random = new GeometricRandom(seed);
	}
	private GeometricRandom random = new GeometricRandom(0);
}
