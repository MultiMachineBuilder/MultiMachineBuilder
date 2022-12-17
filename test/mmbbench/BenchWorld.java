/**
 * 
 */
package mmbbench;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import mmb.StandardTestReferences;
import mmb.engine.block.BlockEntry;
import mmb.engine.worlds.world.World;

/**
 * Benchmarks for world
 * @author oskar
 */
class BenchWorld {
	
	public static World world;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		world = new World(new BlockEntry[][]{{StandardTestReferences.block}}, 0, 0);
		
		//warm-up
		for(int i = 0; i < 1_000_000; i++) world.place(StandardTestReferences.bet, 0, 0);
		for(int i = 0; i < 1_000_000; i++) world.get(0, 0);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		world.destroy();
	}

	// 10M block entities set benchmark
	@Test
	void benchSetBlockEntity() {
		for(int i = 0; i < 10_000_000; i++) world.place(StandardTestReferences.bet, 0, 0);
	}
	
	// 10M blocks set benchmark
	@Test
	void benchSetBlock() {
		for(int i = 0; i < 10_000_000; i++) world.place(StandardTestReferences.block, 0, 0);
	}
	
	// 10M blocks get benchmark
	@Test
	void benchGet() {
		for(int i = 0; i < 10_000_000; i++) world.get(0, 0);
	}

}
