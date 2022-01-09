/**
 * 
 */
package mmb.testing;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics;

import javax.swing.Icon;

import org.junit.jupiter.api.Test;

import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.Blocks;
import mmb.WORLD.texture.BlockDrawer;

/**
 * @author oskar
 *
 */
class TestBlocks {
	
	@Test
	void testRegister() {
		//Create a texture, so ExceptionInInitializerError is not thrown in Blocks initializer
		BlockDrawer fake = new BlockDrawer() {

			@Override
			public void draw(BlockEntry ent, int x, int y, Graphics g, int side) {
				// TODO Auto-generated method stub
			}

			@Override
			public Icon toIcon() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		assertThrows(NullPointerException.class, () -> Blocks.register(null), "Null blocks should not be registerable");
		BlockEntityType nullID = new BlockEntityType();
		nullID.setLeaveBehind(nullID);
		nullID.setTexture(fake);
		assertThrows(NullPointerException.class, () -> Blocks.register(nullID), "Blocks with null ID should not be registerable");
		BlockEntityType correct = new BlockEntityType();
		correct.setLeaveBehind(correct);
		correct.setTexture(fake);
		correct.register("test");
		assertEquals(correct, Blocks.get("test"), "Block was not properly added");
	}

}
