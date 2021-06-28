/**
 * 
 */
package mmb.WORLD.generator;

import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.world.BlockMap;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public class GeneratorPlain implements Generator {

	@Override
	public void generate(BlockMap map, int chunkSize) {
		map.fill(map.startX, map.startY, map.sizeX, map.sizeY, ContentsBlocks.grass);
	}

	@Override
	public String title() {
		return "Plain";
	}

	@Override
	public Grid<BlockEntry> genChunk(BlockMap map, int minX, int minY, int w, int h) {
		Grid<BlockEntry> result = new FixedGrid<>(w, h);
		result.fill(ContentsBlocks.grass);
		return result;
	}

	@Override
	public void setSeed(long seed) {
		//unused, no randomness
	}
}
