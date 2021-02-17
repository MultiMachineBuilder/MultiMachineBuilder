/**
 * 
 */
package mmb.testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.block.Blocks;

/**
 * @author oskar
 *
 */
class TestBlocks {
	
	@Test
	void testRegister() {
		assertThrows(NullPointerException.class, () -> Blocks.register(null), "Null blocks should not be registerable");
		BlockEntityType nullID = new BlockEntityType();
		assertThrows(NullPointerException.class, () -> Blocks.register(nullID), "Blocks with null ID should not be registerable");
		BlockEntityType correct = new BlockEntityType();
		correct.register("test");
		assertEquals(correct, Blocks.get("test"), "Block was not properly added");
		Blocks.remove(correct);
		assertNull(Blocks.get("test"), "Block was not properly removed");
		correct.register();
		Blocks.remove("test");
		assertNull(Blocks.get("test"), "Block was not properly removed by title");
	}

}
