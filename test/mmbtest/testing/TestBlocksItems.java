/**
 * 
 */
package mmbtest.testing;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

import mmb.engine.block.Block;
import mmb.engine.block.BlockType;
import mmb.engine.blockdrawer.BlockDrawer;
import mmb.engine.block.BlockType;
import mmb.engine.item.Items;
import mmbtest.StandardTestReferences;

/**
 * Test block reistration
 * @author oskar
 */
class TestBlocksItems {
	@Test
	void testRegisterNull() {
		assertThrows(NullPointerException.class, () -> Items.register(null));
	}
	
	@Test
	void testRegisterNoID() {
		assertThrows(NullPointerException.class, () -> new Block(null), "Blocks with null ID should not be registerable");
	}
	
	@Test
	void testRegisterCorrect() {
		assertDoesNotThrow(() -> new Block("test"), "Block was not properly added");
	}
	
	private static final Block blk = new Block("block");
	
	@Test
	void testWontTakeNullTitle() {
		assertThrows(NullPointerException.class, () -> blk.setTitle(null));
	}
	
	@Test
	void testWontTakeNullDescription() {
		assertThrows(NullPointerException.class, () -> blk.setDescription(null));
	}
	@Test
	void testWontTakeNullTextureDrawer() {
		assertThrows(NullPointerException.class, () -> blk.setTexture((BlockDrawer)null));
	}
	@Test
	void testWontTakeNullTextureString() {
		assertThrows(NullPointerException.class, () -> blk.setTextureAsset((String)null));
	}
	@Test
	void testWontTakeNullDrop() {
		assertThrows(NullPointerException.class, () -> blk.setDrop(null));
	}
	@Test
	void testWontTakeNullLeave() {
		assertThrows(NullPointerException.class, () -> blk.setLeaveBehind(null));
	}
	@Test
	void testBlockCopy() {
		assertSame(blk, blk.blockCopy());
	}
	
	//Invalid args test
	@Test
	void testVolumeNaN() {
		assertThrows(IllegalArgumentException.class, () -> blk.setStorageVolume(Double.NaN));
	}
	@Test
	void testVolumePInf() {
		assertThrows(IllegalArgumentException.class, () -> blk.setStorageVolume(Double.POSITIVE_INFINITY));
	}
	@Test
	void testVolumeMInf() {
		assertThrows(IllegalArgumentException.class, () -> blk.setStorageVolume(Double.NEGATIVE_INFINITY));
	}
	@Test
	void testVolumeNegative() {
		assertThrows(IllegalArgumentException.class, () -> blk.setStorageVolume(-1));
	}
	@Test
	void testVolumeZero() {
		assertThrows(IllegalArgumentException.class, () -> blk.setStorageVolume(0));
	}
	@Test
	void testVolumePositive() {
		assertDoesNotThrow(() -> blk.setStorageVolume(1));
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
