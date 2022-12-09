/**
 * 
 */
package mmb.testing;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.junit.jupiter.api.Test;

import mmb.engine.block.Block;
import mmb.engine.block.BlockEntityType;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.block.Blocks;
import mmb.engine.item.Items;
import mmb.engine.texture.BlockDrawer;

/**
 * Test block reistration
 * @author oskar
 */
class TestBlocksItems {
	//Create a texture, so ExceptionInInitializerError is not thrown in Blocks initializer
	@Nonnull private static final BlockDrawer fake = new BlockDrawer() {

		@Override
		public void draw(@Nullable BlockEntry ent, int x, int y, Graphics g, int w, int h) {
			//dummy
		}

		@Override
		public Icon toIcon() {
			//dummy
			return new ImageIcon();
		}

		@Override
		public int LOD() {
			// dummy
			return 0;
		}
	};
	
	@Test
	void testRegisterNull() {
		assertThrows(NullPointerException.class, () -> Items.register(null));
	}
	
	@Test
	void testRegisterNoID() {
		BlockEntityType nullID = new BlockEntityType();
		nullID.setLeaveBehind(nullID);
		nullID.setTexture(fake);
		assertThrows(NullPointerException.class, () -> Items.register(nullID), "Blocks with null ID should not be registerable");
	}
	
	@Test
	void testRegisterCorrect() {
		BlockEntityType correct = new BlockEntityType();
		correct.setLeaveBehind(correct);
		correct.setTexture(fake);
		correct.register("test");
		assertEquals(correct, Items.getExpectType("test", BlockType.class), "Block was not properly added");
	}
	
	@Nonnull private static final Block blk = new Block();
	
	@Test
	void testWontTakeNullTitle() {
		assertThrows(NullPointerException.class, () -> blk.title(null));
	}
	
	@Test
	void testWontTakeNullDescription() {
		assertThrows(NullPointerException.class, () -> blk.describe(null));
	}
	
	@Test
	void testWontTakeNullTextureImage() {
		assertThrows(NullPointerException.class, () -> blk.texture((BufferedImage)null));
	}
	@Test
	void testWontTakeNullTextureColor() {
		assertThrows(NullPointerException.class, () -> blk.texture((Color)null));
	}
	@Test
	void testWontTakeNullTextureDrawer() {
		assertThrows(NullPointerException.class, () -> blk.texture((BlockDrawer)null));
	}
	@Test
	void testWontTakeNullTextureString() {
		assertThrows(NullPointerException.class, () -> blk.texture((String)null));
	}
	@Test
	void testWontTakeNullDrop() {
		assertThrows(NullPointerException.class, () -> blk.drop(null));
	}
	@Test
	void testWontTakeNullLeave() {
		assertThrows(NullPointerException.class, () -> blk.leaveBehind(null));
	}
	@Test
	void testBlockCopy() {
		assertSame(blk, blk.blockCopy());
	}
	
	//Invalid args test
	@Test
	void testVolumeNaN() {
		assertThrows(IllegalArgumentException.class, () -> blk.volumed(Double.NaN));
	}
	@Test
	void testVolumePInf() {
		assertThrows(IllegalArgumentException.class, () -> blk.volumed(Double.POSITIVE_INFINITY));
	}
	@Test
	void testVolumeMInf() {
		assertThrows(IllegalArgumentException.class, () -> blk.volumed(Double.NEGATIVE_INFINITY));
	}
	@Test
	void testVolumeNegative() {
		assertThrows(IllegalArgumentException.class, () -> blk.volumed(-1));
	}
	@Test
	void testVolumeZero() {
		assertThrows(IllegalArgumentException.class, () -> blk.volumed(0));
	}
	@Test
	void testVolumePositive() {
		assertDoesNotThrow(() -> blk.volumed(1));
	}
	
	//Block entity tests
	@Test
	void testIsBlockEntity() {
		assertFalse(blk.isBlockEntity());
	}
	@Test
	void testAsBlockEntity() {
		assertThrows(IllegalStateException.class, () -> blk.asBlockEntity());
	}
	@Test
	void testNASBlockEntity() {
		assertNull(blk.nasBlockEntity());
	}
	@Test
	void testAsBlockEntityType() {
		assertThrows(IllegalStateException.class, () -> blk.asBlockEntityType());
	}
	@Test
	void testNASBlockEntityType() {
		assertNull(blk.nasBlockEntityType());
	}
	
	//Collision config tests
	@Test
	void testSetCollidable() {
		assertDoesNotThrow(() -> blk.setCollidable(false));
		assertDoesNotThrow(() -> blk.setCollidable(true));
	}
	
	@Test
	void testSetSrf() {
		assertDoesNotThrow(() -> blk.setSurface(false));
		assertDoesNotThrow(() -> blk.setSurface(true));
	}
	
}
