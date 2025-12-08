/**
 * 
 */
package mmb.engine.generator;

import static mmb.engine.settings.GlobalSettings.$res;

import mmb.annotations.NN;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.Blocks;
import mmb.engine.worlds.world.World;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * A standard world generator for creative mode. It only generates grass
 * @author oskar
 */
public class GeneratorPlain implements Generator {
	@NN private final String name = $res("ngui-plain");
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
