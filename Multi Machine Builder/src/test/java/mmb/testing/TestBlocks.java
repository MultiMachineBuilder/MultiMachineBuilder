/**
 * 
 */
package mmb.testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mmb.WORLD_new.block.BlockType;
import mmb.WORLD_new.block.Blocks;

/**
 * @author oskar
 *
 */
class TestBlocks {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}
	
	@Test
	void testRegister() {
		assertThrows(NullPointerException.class, () -> Blocks.register(null), "Null blocks should not be registerable");
		BlockType nullID = new BlockType();
		assertThrows(NullPointerException.class, () -> Blocks.register(nullID), "Blocks with null ID should not be registerable");
		BlockType correct = new BlockType();
		correct.id = "test";
		assertEquals(correct , Blocks.register(correct), "Block was not properly added");
	}

}
