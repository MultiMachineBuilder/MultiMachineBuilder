/**
 * 
 */
package mmbtest;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.PropertyExtension;
import mmb.annotations.Nil;
import mmb.engine.block.Block;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.recipe.SimpleItemList;
import mmb.engine.texture.BlockDrawer;

/**
 *
 * @author oskar
 *
 */
public class StandardTestReferences {
	public static final BlockDrawer drawer = new BlockDrawer() {
		@Override
		public void draw(@Nil BlockEntry ent, int x, int y, Graphics g, int w, int h) {
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
	public static final Block block = createBlock();
	private static Block createBlock() {
		Block blk = new Block("THIS IS A TEST BLOCK DONT SAVE IT");
		blk.setTexture(drawer);
		blk.setLeaveBehind(blk);
		return blk;
	}
	public static final BlockType bet = new BlockType("THIS IS A TEST BLOCKENT DONT SAVE IT",
		PropertyExtension.setLeaveBehind(block),
		PropertyExtension.setBlockFactory(json -> block)
	);
	public static final SimpleItemList ilist = new SimpleItemList(bet, block);
}
