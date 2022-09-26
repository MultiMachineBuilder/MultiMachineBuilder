/**
 * 
 */
package mmb.WORLD.generator;

import static mmb.GlobalSettings.$res;

import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.world.World;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public class GeneratorPlain implements Generator {
	private final String name = $res("ngui-plain");
	@Override
	public void generate(World map, int chunkSize) {
		map.toGrid().fill(0, 0, map.sizeX, map.sizeY, ContentsBlocks.grass);
	}
	@Override
	public String title() {
		return name;
	}
	@Override
	public Grid<BlockEntry> genChunk(World map, int minX, int minY, int w, int h) {
		Grid<BlockEntry> result = new FixedGrid<>(w, h);
		result.fill(ContentsBlocks.grass);
		return result;
	}
	@Override
	public void setSeed(long seed) {
		//unused, no randomness
	}
}
