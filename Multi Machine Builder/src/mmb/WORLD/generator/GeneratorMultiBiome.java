/**
 * 
 */
package mmb.WORLD.generator;

import mmb.RANDOM.GeometricRandom;
import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public class GeneratorMultiBiome implements Generator {
	private final Debugger debug = new Debugger("WORLD GEN MULTIBIOME");
	@Override
	public Grid<BlockEntry> genChunk(World map, int minX, int minY, int w, int h) {
		long biomen = random.getLong(minY + ((minX * 991) ^ 0x12433653), minY << 8, 2);
		biomen = biomen % biomes.length;
		if(biomen < 0) biomen += biomes.length;
		Biome biome = biomes[(int) biomen];
		Grid<BlockEntry> result = new FixedGrid<>(w, h);
		debug.printl(w+", "+h);
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				long randomized = random.getLong((i+minX) << 16, j+minY, j + ((i * 65537) ^ w));
				result.set(i, j, biome.randomize(randomized));
			}
		}
		return result;
	}
	
	
	protected enum Biome{
		QUARRY {
			@Override
			public Block randomize(long genseed) {
				int subseed = (int) (genseed % 10);
				switch(subseed) {
				case 0:
					return ContentsBlocks.copper_ore;
				case 1:
					return ContentsBlocks.silicon_ore;
				case 2:
					return ContentsBlocks.iron_ore;
				default:
					return ContentsBlocks.stone;
				}
			}
		},
		WOOD {
			@Override
			public Block randomize(long genseed) {
				if(genseed % 20 > 7) return ContentsBlocks.plank;
				return ContentsBlocks.logs;
			}
		},
		PLAINS {
			@Override
			public Block randomize(long genseed) {
				return ContentsBlocks.grass;
			}
		};
		
		public abstract Block randomize(long genseed);
	}
	private final Biome[] biomes = Biome.values();

	private GeometricRandom random = new GeometricRandom(0);
	@Override
	public void setSeed(long seed) {
		random = new GeometricRandom(seed);
	}
	@Override
	public String title() {
		return "Multi-Biome";
	}

}
