/**
 * 
 */
package mmb.engine.generator;

import static mmb.engine.settings.GlobalSettings.$res;

import mmb.engine.block.BlockEntry;
import mmb.engine.block.Blocks;
import mmb.engine.worlds.world.World;
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
		map.toGrid().fill(0, 0, map.sizeX, map.sizeY, Blocks.grass);
	}
	@Override
	public String title() {
		return name;
	}
	@Override
	public Grid<BlockEntry> genChunk(World map, int minX, int minY, int w, int h) {
		Grid<BlockEntry> result = new FixedGrid<>(w, h);
		result.fill(Blocks.grass);
		return result;
	}
	@Override
	public void setSeed(long seed) {
		//unused, no randomness
	}
}
