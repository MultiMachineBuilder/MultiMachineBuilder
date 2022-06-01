/**
 * 
 */
package mmb.WORLD.generator;

import javax.annotation.Nonnull;

import mmb.RANDOM.GeometricRandom;
import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.contentgen.Materials;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

import static mmb.GlobalSettings.$res;

import java.awt.Rectangle;
import java.util.Random;

/**
 * @author oskar
 *
 */
public class GeneratorMultiBiome implements Generator {
	private final String name = $res("ngui-biomes");
	private final Debugger debug = new Debugger("WORLD GEN MULTIBIOME");
	private static final Rectangle rect = new Rectangle(-1, -1, 2, 2);
	private Rectangle chunk = new Rectangle();
	@Override
	public Grid<BlockEntry> genChunk(World map, int minX, int minY, int w, int h) {
		long n = random.getLong(minY + ((minX * 991) ^ 0x12433653), minY << 8, 2);
		long biomen = n;
		biomen = biomen % biomes.length;
		if(biomen < 0) biomen += biomes.length;
		Biome biome = biomes[(int) biomen];
		//If this chunk touches a [(-1, -1), (0, 0)] rectangle, set it to plains
		chunk.x = minX;
		chunk.y = minY;
		chunk.width = w;
		chunk.height = h;
		if(rect.intersects(chunk)) biome = Biome.PLAINS;
		Random rnd = new Random(n);
		//Generate blocks
		Grid<BlockEntry> result = new FixedGrid<>(w, h);
		debug.printl(w+", "+h);
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				long randomized = /*random.getLong((i+minX) << 16, j+minY, j + ((i * 65537) ^ w))*/rnd.nextLong();
				result.set(i, j, biome.randomize(randomized));
			}
		}
		return result;
	}
	
	protected enum Biome{
		QUARRY {
			@Override
			public Block randomize(long genseed) {
				int subseed = (int) (genseed % 250);
				switch(subseed) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
					return Materials.lead.ore;
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
					return Materials.copper.ore;
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
					return Materials.zinc.ore;
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
					return Materials.silicon.ore;
				case 20:
				case 21:
				case 22:
				case 23:
				case 24:
					return Materials.tin.ore;
				case 25:
				case 26:
				case 27:
				case 28:
				case 29:
					return Materials.iron.ore;
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
					return Materials.rudimentary.ore;
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
					return Materials.coal.ore;
				case 50:
					return Materials.diamond.ore;
				case 51:
				case 52:
					return Materials.platinum.ore;
				case 53:
				case 54:
					return Materials.tungsten.ore;
				case 55:
				case 56:
					return Materials.gold.ore;
				case 57:
				case 58:
					return Materials.iridium.ore;
				case 59:
				case 60:
				case 61:
				case 62:
				case 63:
					return Materials.nickel.ore;
				case 64:
				case 65:
				case 66:
					return Materials.silver.ore;
				case 67:
				case 68:
				case 69:
					return Materials.cobalt.ore;
				case 70:
				case 71:
				case 72:
				case 73:
					return Materials.uranium.ore;
				case 80:
				case 81:
				case 82:
				case 83:
				case 84:
				case 85:
				case 86:
				case 87:
				case 88:
				case 89:
					return Materials.alu.ore;
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
		return name;
	}

}
