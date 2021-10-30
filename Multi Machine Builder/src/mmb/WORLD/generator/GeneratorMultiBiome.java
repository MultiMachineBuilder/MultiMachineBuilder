/**
 * 
 */
package mmb.WORLD.generator;

import javax.annotation.Nonnull;

import mmb.RANDOM.GeometricRandom;
import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;
import java.awt.Rectangle;

/**
 * @author oskar
 *
 */
public class GeneratorMultiBiome implements Generator {
	private final Debugger debug = new Debugger("WORLD GEN MULTIBIOME");
	private static final Rectangle rect = new Rectangle(-1, -1, 2, 2);
	private Rectangle chunk = new Rectangle();
	@Override
	public Grid<BlockEntry> genChunk(World map, int minX, int minY, int w, int h) {
		long biomen = random.getLong(minY + ((minX * 991) ^ 0x12433653), minY << 8, 2);
		biomen = biomen % biomes.length;
		if(biomen < 0) biomen += biomes.length;
		Biome biome = biomes[(int) biomen];
		//If this chunk touches a [(-1, -1), (0, 0)] rectangle, set it to plains
		chunk.x = minX;
		chunk.y = minY;
		chunk.width = w;
		chunk.height = h;
		if(rect.intersects(chunk)) biome = Biome.PLAINS;
		//Generate blocks
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
				int subseed = (int) (genseed % 100);
				switch(subseed) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
					return ContentsBlocks.copper_ore;
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
					return ContentsBlocks.silicon_ore;
				case 20:
				case 21:
				case 22:
				case 23:
				case 24:
				case 25:
				case 26:
				case 27:
				case 28:
				case 29:
					return ContentsBlocks.iron_ore;
				case 30:
				case 31:
				case 32:
				case 33:
				case 34:
				case 35:
				case 36:
				case 37:
				case 38:
				case 39:
				case 40:
				case 41:
				case 42:
				case 43:
				case 44:
				case 45:
				case 46:
				case 47:
				case 48:
				case 49:
					return ContentsBlocks.coal_ore;
				case 50:
				case 51:
				case 52:
				case 53:
				case 54:
				case 55:
					return ContentsBlocks.gold_ore;
				default:
					return ContentsBlocks.stone;
				}
			}
		},
		WOOD {
			@Override
			public Block randomize(long genseed) {
				int subseed = (int) (genseed % 20);
				switch(subseed) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
					return ContentsBlocks.plank;
				case 8:
				case 9:
				case 10:
					return ContentsBlocks.leaves;
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
				default:
					return ContentsBlocks.logs;
				}	
			}
		},
		PLAINS {
			@Override
			public Block randomize(long genseed) {
				return ContentsBlocks.grass;
			}
		},
		SAVANNA {
			@Override
			public Block randomize(long genseed) {
				if((genseed & 7) == 0) {
					//First 3 bits arefavorable
					return WOOD.randomize(genseed >> 3);
				}
				return ContentsBlocks.grass;
			}
		},
		DESERT {
			@Override
			public Block randomize(long genseed) {
				return ContentsBlocks.sand;
			}
		};
		
		@Nonnull public abstract Block randomize(long genseed);
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
